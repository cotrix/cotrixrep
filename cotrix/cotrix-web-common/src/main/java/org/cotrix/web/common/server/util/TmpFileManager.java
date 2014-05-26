/**
 * 
 */
package org.cotrix.web.common.server.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class TmpFileManager {

	private Logger logger = LoggerFactory.getLogger(TmpFileManager.class);

	private static long cleaningDelay = TimeUnit.MINUTES.toMillis(30);

	private final TimerTask cleaningTask = new TimerTask() {

		@Override
		public void run() {
			clean();
		}
	};

	private Map<File, Long> tmps;
	private Timer cleaningTimer;

	public TmpFileManager() {
		cleaningTimer = new Timer(true);
		cleaningTimer.schedule(cleaningTask, cleaningDelay, cleaningDelay);
		tmps = new HashMap<>();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				cleanAll();
			}
		});
	}

	public File createTmpFile() throws IOException {
		File tmp = File.createTempFile("cotrix", ".tmp");
		tmp.deleteOnExit();
		tmps.put(tmp, System.currentTimeMillis());
		return tmp;
	}
	
	public File createTmpFile(InputStream inputStream) throws IOException {
		File tmp = createTmpFile();
		Files.copy(inputStream, tmp.toPath(), StandardCopyOption.REPLACE_EXISTING);
		return tmp;
	}

	private void clean() {
		logger.trace("starting tmp file cleaning");
		long current = System.currentTimeMillis();
		List<File> toDelete = new ArrayList<>();
		for (Entry<File, Long> entry:tmps.entrySet()) {
			if (entry.getValue()<current-cleaningDelay) toDelete.add(entry.getKey());			
		}
		logger.trace("found "+toDelete.size()+" files to delete");
		for (File tmp:toDelete) {
			tmps.remove(tmp);
			if (tmp.exists()) tmp.delete();
		}
		logger.trace("tmp files removed");
	}

	private void cleanAll() {
		for (File file:tmps.keySet()) file.delete();
		tmps.clear();
	}
}
