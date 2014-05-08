/**
 * 
 */
package org.cotrix.web.publish.server.publish;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import org.cotrix.common.cdi.BeanSession;
import org.cotrix.common.cdi.Current;
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
	protected PublishMapper.CometMapper cometMapper;

	@Inject
	protected SerializationDirectivesProducer.Table2CsvSerializationDirectivesProducer csvSerializationDirectivesProducer;
	
	@Inject
	protected SerializationDirectivesProducer.Sdmx2XmlSerializationDirectivesProducer sdmx2XmlSerializationDirectivesProducer;
	
	@Inject
	protected SerializationDirectivesProducer.Comet2XmlSerializationDirectivesProducer comet2XmlSerializationDirectivesProducer;
	
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
	
	@Inject @Current
	protected BeanSession session;
	
	
	protected ExecutorService executorService = Executors.newFixedThreadPool(10);
	
	public void createPublisher(PublishDirectives publishDirectives, PublishStatus publishStatus) {

		switch (publishDirectives.getFormat()) {
			case CSV: createPublisher(csvMapper, csvSerializationDirectivesProducer, publishDirectives, publishStatus); break;
			case SDMX: createPublisher(sdmxMapper, sdmx2XmlSerializationDirectivesProducer, publishDirectives, publishStatus); break;
			case COMET: createPublisher(cometMapper, comet2XmlSerializationDirectivesProducer, publishDirectives, publishStatus); break;
		}
	}
	
	protected <T> void createPublisher(final PublishMapper<T> mapper, final SerializationDirectivesProducer<T> serDir, final PublishDirectives publishDirectives, final PublishStatus publishStatus) {
		final BeanSession unscopedSession = this.session.copy();
		switch (publishDirectives.getDestination()) {
			case FILE: {
				executorService.execute(new Runnable() {
					
					@Override
					public void run() {
						publisher.publish(publishDirectives, mapper, serDir, desktopDestination, publishStatus, unscopedSession);
					}
				});
			} break;
			case CHANNEL: {
				executorService.execute(new Runnable() {
					
					@Override
					public void run() {
						publisher.publish(publishDirectives, mapper, Publishers.<T>getEmptySerializationDirectivesProducer(), cloudDestination, publishStatus, unscopedSession);
					}
				});
			} break;
		}
	}

}
