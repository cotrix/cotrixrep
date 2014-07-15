package org.cotrix.common.async;

import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionException;


public interface ExecutionService {
	
	<T> ReportingFuture<T> execute(Callable<T> task) throws RejectedExecutionException;
	
}
