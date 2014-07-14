/**
 * 
 */
package org.cotrix.web.common.client.util;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
import org.cotrix.web.common.client.widgets.ProgressBar;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ProgressAnimation {
	
	public interface AnimationListener {
		public void onComplete();
	}
	
	static final boolean demo = false;
	
	//time in between observations => speed of progress
	static final int monitor_rate = 5;
	
	//max progress
	static final int max_progress = 100;
	
	//amount of progress that is visibly recorded
	static final double bar_step = 1;	
	
	//max progress when waiting for a report (percent of what's left)
	static final double idle_bound= 11/100f;
			
	//rate of progress when waiting for a report (percent of what's left to idle_bound)
	static final double idle_rate= 1/500f;
	
	private double next_bar;
	
	//current progress
	private double progress; 
	
	//last reported evidence
	private double evidence;
	
	private ProgressBar progressBar;
	
	public ProgressAnimation(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}
	
	long startTime = 0;
	long lastUpdate = 0;
	long sum = 0;
	int count = 0;

	public void start() {
		
		startTime = System.currentTimeMillis();
		lastUpdate = System.currentTimeMillis();
		sum = 0;
		count = 0;
		
		next_bar=progress=evidence=0;
		next_bar = 1;
		progressBar.setProgress(0);

		RepeatingCommand command = new RepeatingCommand() {
			
			@Override
			public boolean execute() {
				progress();
				return next_bar<max_progress;
			}
		};
		
		
		Scheduler.get().scheduleFixedDelay(command , monitor_rate);

	}
	
	private void printstate() {
		long delay = (System.currentTimeMillis()-lastUpdate);
		sum += delay;
		count++;
		double avg = sum/count;	
		
		System.out.println("progress ="+progress+", step ="+step()+", evidence="+evidence + " time: "+(System.currentTimeMillis()-startTime)+" delay: "+delay+" avg delay: "+avg);

		lastUpdate = System.currentTimeMillis();
	}
	
	
	private void progress() {
		
		if (demo)
			printstate();
		
		progress = progress + step();
		
		if (progress >= next_bar) {

			updateBar();
			
			next_bar = next_bar+bar_step;

		}
	}
	
	private double step() {

		//catch up if there is new evidence
		if (evidence>progress)
			return evidence-progress;

		//or make an optimistic small step towards a max threshold (percent of what's left)
		double threshold = (max_progress-evidence)*idle_bound;
	
		return (evidence+threshold-progress)*idle_rate;
				
	}
	
	public void report(float r) {
		evidence = r;
	}
	
	private void updateBar() {
		progressBar.setProgress(progressBar.getProgress()+1);
	}

}
