/**
 * 
 */
package org.cotrix.web.publish.server.publish;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.cotrix.action.events.CodelistActionEvents;
import org.cotrix.domain.Codelist;
import org.cotrix.io.CloudService;
import org.cotrix.io.SerialisationService;
import org.cotrix.io.SerialisationService.SerialisationDirectives;
import org.cotrix.web.publish.shared.PublishDirectives;
import org.cotrix.web.share.server.util.ValueUtils;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.tabular.Table;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface PublishToDestination {

	public <T> void publish(T codelist, SerialisationDirectives<T> serializationDirectives, PublishDirectives publishDirectives, PublishStatus publishStatus) throws Exception;

	public class DesktopDestination implements PublishToDestination {

		@Inject
		protected SerialisationService serialiser;

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public <T> void publish(T codelist, SerialisationDirectives<T> serializationDirectives, PublishDirectives publishDirectives, PublishStatus publishStatus) throws Exception {
			File destination = File.createTempFile("publish", ".tmp");
			OutputStream os = new FileOutputStream(destination);
			serialiser.serialise(codelist, os, serializationDirectives);
			publishStatus.setPublishResult(destination);
		}

	}

	public class CloudDestination implements PublishToDestination {

		@Inject
		protected CloudService cloud;
		
		@Inject
		protected Event<CodelistActionEvents.CodelistEvent> events;

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public <T> void publish(T codelist, SerialisationDirectives<T> serializationDirectives, PublishDirectives publishDirectives, PublishStatus publishStatus) {
			
			QName repositoryId = ValueUtils.toQName(publishDirectives.getRepositoryId());
			if (codelist instanceof Table) cloud.publish((Table) codelist, repositoryId);
			if (codelist instanceof CodelistBean) cloud.publish((CodelistBean) codelist, repositoryId);
			
			Codelist publishedCodelist = publishStatus.getPublishedCodelist();
			events.fire(new CodelistActionEvents.Publish(publishedCodelist.id(), publishedCodelist.name(), publishedCodelist.version(), repositoryId));
			
			publishStatus.setPublishResult(null);
		}

	}

}
