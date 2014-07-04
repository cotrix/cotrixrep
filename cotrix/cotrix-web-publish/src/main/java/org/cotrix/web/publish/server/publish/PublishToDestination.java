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
import org.cotrix.common.BeanSession;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.io.CloudService;
import org.cotrix.io.SerialisationService;
import org.cotrix.io.SerialisationService.SerialisationDirectives;
import org.cotrix.lifecycle.Lifecycle;
import org.cotrix.lifecycle.LifecycleService;
import org.cotrix.lifecycle.State;
import org.cotrix.lifecycle.impl.DefaultLifecycleStates;
import org.cotrix.web.common.server.util.TmpFileManager;
import org.cotrix.web.common.server.util.ValueUtils;
import org.cotrix.web.common.shared.exception.Exceptions;
import org.cotrix.web.common.shared.exception.ServiceErrorException;
import org.cotrix.web.publish.shared.PublishDirectives;
import org.fao.fi.comet.mapping.model.MappingData;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.tabular.Table;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface PublishToDestination {

	public <T> void publish(Codelist original, T mapped, SerialisationDirectives<T> serializationDirectives, PublishDirectives publishDirectives, PublishStatus publishStatus, BeanSession session) throws Exception;

	public class DesktopDestination implements PublishToDestination {
		
		@Inject
		private Event<CodelistActionEvents.Publish> events;

		@Inject
		private SerialisationService serialiser;
		
		@Inject
		private TmpFileManager tmpFileManager;

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public <T> void publish(Codelist source,T mapped, SerialisationDirectives<T> serializationDirectives, PublishDirectives publishDirectives, PublishStatus publishStatus, BeanSession session) throws Exception {
			File destination = tmpFileManager.createTmpFile();
			
			OutputStream os = new FileOutputStream(destination);
			serialiser.serialise(mapped, os, serializationDirectives);
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
		public <T> void publish(Codelist source, T mapped, SerialisationDirectives<T> serializationDirectives, PublishDirectives publishDirectives, PublishStatus publishStatus, BeanSession session) {
			
			QName repositoryId = ValueUtils.toQName(publishDirectives.getRepositoryId());
			
			try {
				if (mapped instanceof Table) cloud.publish(source,(Table) mapped, repositoryId);
				if (mapped instanceof CodelistBean) cloud.publish(source,(CodelistBean) mapped, repositoryId);
				if (mapped instanceof MappingData) cloud.publish(source,(MappingData) mapped, repositoryId);
			} catch(Exception e) {
				ServiceErrorException errorException = Exceptions.toServiceErrorException("Oops, there seems a communication problem with "+repositoryId.getLocalPart()+".", e);
				throw errorException;
			}
			
			Codelist publishedCodelist = publishStatus.getPublishedCodelist(); 
			Lifecycle lifecycle = lifecycleService.lifecycleOf(publishedCodelist.id());
			State state = lifecycle.state();
			if (state!=DefaultLifecycleStates.draft) events.fire(new CodelistActionEvents.Publish(publishedCodelist.id(), publishedCodelist.qname(), publishedCodelist.version(), repositoryId, session));
			
			publishStatus.setPublishResult(null);
		}

	}

}
