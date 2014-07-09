package org.cotrix.common.async;

import static org.cotrix.common.CommonUtils.*;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cotrix.common.async.TaskManagerProvider.TaskManager;
import org.cotrix.common.tx.Transaction;
import org.cotrix.common.tx.Transactions;

@ApplicationScoped
public class DefaultExecutionService implements ExecutionService {

	private static ExecutorService service = Executors.newCachedThreadPool();

	@Inject
	private TaskContext context;

	@Inject
	private TaskManagerProvider managers;

	@Inject
	Transactions txs;

	private static class Closure {	
		Task t; 
	}


	@Override
	public <T> ReportingFuture<T> execute(final Callable<T> task) throws RejectedExecutionException {

		notNull("task", task);

		try {
			final CountDownLatch started = new CountDownLatch(1);

			final Closure closure = new Closure();

			final TaskManager manager = managers.get();

			Callable<T> wrap = new Callable<T>() {


				@Override
				public T call() throws Exception {

					manager.started();

					try {

						closure.t = context.thisTask();

						started.countDown();

						try(Transaction tx = txs.open()) {

							T result = task.call();

							tx.commit();

							return result;
						}

					}

					catch(Exception e) {

						context.thisTask().failed(e);

						throw e;
					}

					finally {

						context.reset();

						manager.finished();
					}
				}
			};

			Future<T> future = service.submit(wrap);

			started.await();

			return new DefaultReportingFuture<T>(future,closure.t);

		}
		catch(Exception e) {
			throw unchecked(e);
		}
	}
}
