/**
 * 
 */
package org.cotrix.web.publish.server.publish;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.cotrix.io.CloudService;
import org.cotrix.io.SerialisationService;
import org.cotrix.io.SerialisationService.SerialisationDirectives;
import org.cotrix.web.publish.shared.PublishDirectives;
import org.cotrix.web.share.server.util.ValueUtils;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.tabular.Table;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface PublishToDestination<O> {

	public <T> O publish(T codelist, SerialisationDirectives<T> serializationDirectives, PublishDirectives publishDirectives) throws Exception;

	public class DesktopDestination implements PublishToDestination<File> {

		@Inject
		protected SerialisationService serialiser;

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public <T> File publish(T codelist, SerialisationDirectives<T> serializationDirectives, PublishDirectives publishDirectives) throws Exception {
			File destination = File.createTempFile("publish", ".tmp");
			OutputStream os = new FileOutputStream(destination);
			serialiser.serialise(codelist, os, serializationDirectives);
			return destination;
		}

	}

	public class CloudDestination implements PublishToDestination<Void> {

		@Inject
		protected CloudService cloud;

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public <T> Void publish(T codelist, SerialisationDirectives<T> serializationDirectives, PublishDirectives publishDirectives) {
			
			RepositoryService destination = null;
			QName repositoryId = ValueUtils.toQName(publishDirectives.getRepositoryId());
			for (RepositoryService repositoryService:cloud.repositories()) {
				if (repositoryService.name().equals(repositoryId)) {
					destination = repositoryService;
					break;
				}
			}
			
			if (destination == null) throw new IllegalArgumentException("No repository with id "+repositoryId+" found");
			
			if (codelist instanceof Table) cloud.publish((Table) codelist, destination);
			if (codelist instanceof CodelistBean) cloud.publish((CodelistBean) codelist, destination);
			
			return null;
		}

	}

}
