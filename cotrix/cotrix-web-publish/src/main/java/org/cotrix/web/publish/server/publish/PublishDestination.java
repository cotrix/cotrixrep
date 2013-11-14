/**
 * 
 */
package org.cotrix.web.publish.server.publish;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.inject.Inject;

import org.cotrix.io.CloudService;
import org.cotrix.io.SerialisationService;
import org.cotrix.io.SerialisationService.SerialisationDirectives;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface PublishDestination<O> {

	public <T> O publish(T codelist, SerialisationDirectives<T> directives) throws Exception;

	public class DesktopDestination implements PublishDestination<File> {

		@Inject
		protected SerialisationService serialiser;

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public <T> File publish(T codelist, SerialisationDirectives<T> directives) throws Exception {
			File tmp = File.createTempFile("publish", ".tmp");
			OutputStream os = new FileOutputStream(tmp);
			serialiser.serialise(codelist, os, directives);
			return tmp;
		}

	}

	public class CloudDestination implements PublishDestination<Void> {

		@Inject
		protected CloudService cloud;


		/** 
		 * {@inheritDoc}
		 */
		@Override
		public <T> Void publish(T codelist, SerialisationDirectives<T> directives) {
			
			//return cloud.retrieve(asset.id(), type);
			return null;
		}

	}

}
