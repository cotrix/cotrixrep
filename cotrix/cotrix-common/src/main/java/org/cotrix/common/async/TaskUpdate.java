package org.cotrix.common.async;

public class TaskUpdate {

	private final float progress;
	private final String activity;
	
	public TaskUpdate(float progress,String activity) {
		this.progress=progress;
		this.activity=activity;
	}
	
	public static TaskUpdate update(float progress,String activity) {
		return new TaskUpdate(progress, activity);
	}

	public float progress() {
		return progress;
	}
	public String activity() {
		return activity;
	}
	
	
}
