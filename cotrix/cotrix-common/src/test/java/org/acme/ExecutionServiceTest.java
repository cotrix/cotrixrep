package org.acme;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.cotrix.common.async.ExecutionService;
import org.cotrix.common.async.ReportingFuture;
import org.cotrix.common.async.TaskContext;
import org.cotrix.common.async.TaskObserver;
import org.cotrix.common.async.TaskUpdate;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class ExecutionServiceTest {

	@Inject
	AsyncApi asyncApi;

	@Inject
	ExecutionService service;

	static class Api {

		@Inject
		TaskContext ctx;

		void api() throws Exception {

			for (int i = 1; i < 101; i++) {

				Thread.sleep(20);

				if (i % 10 == 0)
					ctx.save(new TaskUpdate(i / 100f, "done " + i + "%"));
			}

		}

	}

	static class AsyncApi {

		@Inject
		Api syncApi;

		@Inject
		ExecutionService service;

		ReportingFuture<?> api() throws Exception {

			Callable<Void> task = new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					syncApi.api();
					return null;
				}
			};

			return service.execute(task);

		}

	}

	@Test
	public void test() throws Exception {

		ReportingFuture<?> future = asyncApi.api();

		while (!future.isDone()) {

			TaskUpdate update = future.get(TaskUpdate.class);
			if (update != null)
				System.out.println(update.progress());

			Thread.sleep(100);
		}
	}

	@Test
	public void testNotif() throws Exception {

		ReportingFuture<?> future = asyncApi.api();

		future.register(new TaskObserver.Abstract<TaskUpdate>() {

			@Override
			public void on(TaskUpdate update) {
				System.out.println(update.progress());
			}
		}).forType(TaskUpdate.class);

		future.get();

	}

}
