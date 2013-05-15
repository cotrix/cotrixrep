package org.cotrix.io.publish;

import javax.xml.namespace.QName;

import org.cotrix.domain.Codelist;
import org.cotrix.io.Task;

/**
 * A publication {@link Task}.
 * 
 * @author Fabio Simeoni
 *
 * @param <D> the type of mockDirectives that govern the task
 */
public interface PublicationTask<D extends PublicationDirectives> extends Task<D> {
	
	void publish(QName target, Codelist list, D directives) throws Exception;
	
}
