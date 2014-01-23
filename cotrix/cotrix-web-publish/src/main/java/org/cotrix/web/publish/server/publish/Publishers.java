/**
 * 
 */
package org.cotrix.web.publish.server.publish;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import org.cotrix.io.SerialisationService.SerialisationDirectives;
import org.cotrix.web.publish.shared.PublishDirectives;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Publishers {
	
	protected final static SerializationDirectivesProducer<Object> EMPTY_PRODUCER = new SerializationDirectivesProducer<Object>() {

		@Override
		public SerialisationDirectives<Object> produce(
				PublishDirectives directives) {
			return null;
		}
	};
	
	@Inject
	protected PublishMapper.CsvMapper csvMapper;
	
	@Inject
	protected PublishMapper.SdmxMapper sdmxMapper;

	@Inject
	protected SerializationDirectivesProducer.CSVSerializationDirectivesProducer csvDesktopProducer;
	
	@Inject
	protected SerializationDirectivesProducer.XmlSerializationDirectivesProducer xmlDesktopProducer;
	
	@SuppressWarnings("unchecked")
	protected static <T> SerializationDirectivesProducer<T> getEmptySerializationDirectivesProducer() {
		return (SerializationDirectivesProducer<T>)EMPTY_PRODUCER;
	}

	@Inject
	protected PublishToDestination.DesktopDestination desktopDestination;
	
	@Inject
	protected PublishToDestination.CloudDestination cloudDestination;
	
	@Inject
	protected Publisher publisher;
	
	protected ExecutorService executorService = Executors.newFixedThreadPool(10);
	
	public void createPublisher(PublishDirectives publishDirectives, PublishStatus publishStatus) {

		switch (publishDirectives.getFormat()) {
			case CSV: createPublisher(csvMapper, csvDesktopProducer, publishDirectives, publishStatus); break;
			case SDMX: createPublisher(sdmxMapper, xmlDesktopProducer, publishDirectives, publishStatus); break;
		}
	}
	
	protected <T> void createPublisher(final PublishMapper<T> mapper, final SerializationDirectivesProducer<T> serDir, final PublishDirectives publishDirectives, final PublishStatus publishStatus) {
		switch (publishDirectives.getDestination()) {
			case FILE: {
				executorService.execute(new Runnable() {
					
					@Override
					public void run() {
						publisher.publish(publishDirectives, mapper, serDir, desktopDestination, publishStatus);
					}
				});
			} break;
			case CHANNEL: {
				executorService.execute(new Runnable() {
					
					@Override
					public void run() {
						publisher.publish(publishDirectives, mapper, Publishers.<T>getEmptySerializationDirectivesProducer(), cloudDestination, publishStatus);
					}
				});
			} break;
		}
	}

}
