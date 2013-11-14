/**
 * 
 */
package org.cotrix.web.publish.server.publish;

import java.io.File;

import javax.inject.Inject;

import org.cotrix.domain.Codelist;
import org.cotrix.web.publish.shared.DestinationType;
import org.cotrix.web.publish.shared.FormatType;
import org.cotrix.web.publish.shared.PublishDirectives;
import org.virtualrepository.tabular.Table;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PublisherBuilder<M,O> {
	
	@Inject
	public static PublishMapper.CsvMapper csvMapper;
	
	@Inject
	public static SerializationDirectivesProducer.TableDesktopProducer tableDesktopProducer;
	
	@Inject
	public static PublishDestination.DesktopDestination desktopDestination;
	
	
	public static class FormatBuilder {
		protected Codelist codelist;
		
		protected FormatBuilder(Codelist codelist) {
			this.codelist = codelist;
		}
		
		public DestinationBuilder<?> as(FormatType type) {
			
			switch (type) {
				case CSV: return new DestinationBuilder<Table>(codelist, type, csvMapper);
				default:
					break;
			}
			return null;
		}
	}
	
	public static class DestinationBuilder<M> {
		
		protected Codelist codelist;
		protected FormatType type;
		protected PublishMapper<M> mapper;
		
		protected DestinationBuilder(Codelist codelist, FormatType type, PublishMapper<M> mapper) {
			this.codelist = codelist;
			this.type = type;
			this.mapper = mapper;
		}
		
		 public PublisherBuilder<M,?> to(DestinationType destination) {
			 switch (destination) {
				case FILE: return new PublisherBuilder<M, File>(codelist, mapper, desktopDestination);
				default:
					break;
			}
			 return null;
		 }
		 
		 
		
		
		
	}
	
	public static FormatBuilder publish(Codelist codelist) {
		return new FormatBuilder(codelist);
	}
	
	protected Codelist codelist;
	protected PublishMapper<M> mapper;
	protected SerializationDirectivesProducer<M> serializationProducer;
	protected PublishDestination<O> destination;
	
	protected PublishDirectives publishDirectives;
	
	protected PublisherBuilder(Codelist codelist, PublishMapper<M> mapper, PublishDestination<O> destination) {
		
		this.codelist = codelist;
		this.mapper = mapper;
		this.destination = destination;
	}
	
	public PublisherBuilder<M,O> withDirectives(PublishDirectives publishDirectives) {
		this.publishDirectives = publishDirectives;
		return this;
	}
		

}
