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
import org.cotrix.common.cdi.BeanSession;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.io.CloudService;
import org.cotrix.io.SerialisationService;
import org.cotrix.io.SerialisationService.SerialisationDirectives;
import org.cotrix.lifecycle.Lifecycle;
import org.cotrix.lifecycle.LifecycleService;
import org.cotrix.lifecycle.State;
import org.cotrix.lifecycle.impl.DefaultLifecycleStates;
import org.cotrix.web.publish.shared.PublishDirectives;
import org.cotrix.web.share.server.util.ValueUtils;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.tabular.Table;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface PublishToDestination {

	public <T> void publish(T codelist, SerialisationDirectives<T> serializationDirectives, PublishDirectives publishDirectives, PublishStatus publishStatus, BeanSession session) throws Exception;

	public class DesktopDestination implements PublishToDestination {
		
		@Inject
		private Event<CodelistActionEvents.Publish> events;

		@Inject
		protected SerialisationService serialiser;

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public <T> void publish(T codelist, SerialisationDirectives<T> serializationDirectives, PublishDirectives publishDirectives, PublishStatus publishStatus, BeanSession session) throws Exception {
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
		
		@Inject
		private LifecycleService lifecycleService;

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public <T> void publish(T codelist, SerialisationDirectives<T> serializationDirectives, PublishDirectives publishDirectives, PublishStatus publishStatus, BeanSession session) {
			
			QName repositoryId = ValueUtils.toQName(publishDirectives.getRepositoryId());
			if (codelist instanceof Table) cloud.publish((Table) codelist, repositoryId);
			if (codelist instanceof CodelistBean) cloud.publish((CodelistBean) codelist, repositoryId);
			
			Codelist publishedCodelist = publishStatus.getPublishedCodelist(); 
			Lifecycle lifecycle = lifecycleService.lifecycleOf(publishedCodelist.id());
			State state = lifecycle.state();
			if (state!=DefaultLifecycleStates.draft) events.fire(new CodelistActionEvents.Publish(publishedCodelist.id(), publishedCodelist.name(), publishedCodelist.version(), repositoryId, session));
			
			publishStatus.setPublishResult(null);
		}

	}

}
