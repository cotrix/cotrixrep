package org.cotrix.common.async;



public interface TaskEvents {
	
	public static class StartTask {
		

		public static interface InfoProvider {
			
			TaskInfo get();
		}
		
		public interface TaskInfo {}
		
		final TaskInfo info;
		
		public StartTask(TaskInfo info) {
			this.info=info;
		}
	}
	
	public static class EndTask{
		public static EndTask instance = new EndTask();
	}

	
}
