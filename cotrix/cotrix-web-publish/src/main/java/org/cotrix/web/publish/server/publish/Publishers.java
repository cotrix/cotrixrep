/**
 * 
 */
package org.cotrix.web.publish.server.publish;

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
	public PublishMapper.CsvMapper csvMapper;
	
	@Inject
	public PublishMapper.SdmxMapper sdmxMapper;

	@Inject
	public SerializationDirectivesProducer.CSVSerializationDirectivesProducer csvDesktopProducer;
	
	@Inject
	public SerializationDirectivesProducer.XmlSerializationDirectivesProducer xmlDesktopProducer;
	
	@SuppressWarnings("unchecked")
	public static <T> SerializationDirectivesProducer<T> getEmptySerializationDirectivesProducer() {
		return (SerializationDirectivesProducer<T>)EMPTY_PRODUCER;
	}

	@Inject
	public PublishToDestination.DesktopDestination desktopDestination;
	
	@Inject
	public PublishToDestination.CloudDestination cloudDestination;
	
	public Publisher<?> createPublisher(PublishDirectives publishDirectives, PublishStatus publishStatus) {

		switch (publishDirectives.getFormat()) {
			case CSV: return createPublisher(csvMapper, csvDesktopProducer, publishDirectives, publishStatus);
			case SDMX: return createPublisher(sdmxMapper, xmlDesktopProducer, publishDirectives, publishStatus);
		}
		throw new IllegalArgumentException("Unknown format "+publishDirectives.getFormat());
	}
	
	protected <T> Publisher<T> createPublisher(PublishMapper<T> mapper, SerializationDirectivesProducer<T> serDir, PublishDirectives publishDirectives, PublishStatus publishStatus) {
		switch (publishDirectives.getDestination()) {
			case FILE: return new Publisher<T>(publishDirectives, mapper, serDir, desktopDestination, publishStatus);
			case CHANNEL: return new Publisher<T>(publishDirectives, mapper, Publishers.<T>getEmptySerializationDirectivesProducer(), cloudDestination, publishStatus);
		}
		throw new IllegalArgumentException("Unknown destination type "+publishDirectives.getDestination());
	}

}
