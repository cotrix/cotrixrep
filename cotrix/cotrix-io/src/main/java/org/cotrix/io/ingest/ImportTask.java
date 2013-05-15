package org.cotrix.io.ingest;

import java.io.InputStream;

import org.cotrix.domain.Codelist;
import org.cotrix.io.Task;
import org.virtualrepository.Asset;

/**
 * A configurable {@link Task} to import {@link Codelist}s from data streams or {@link Asset}s available through remote import channels.
 * 
 * @author Fabio Simeoni
 *
 * @param <D> the type of directives that direct the task
 */
public interface ImportTask<A extends Asset,D extends ImportDirectives> extends Task<D>  {

	/**
	 * Derives a {@link Codelist} from an {@link Asset} available through some import channel, using given directives.
	 * @param asset the asset
	 * @param directives the directives
	 * @return the codelist
	 * @throws Exception if the codelist cannot be derived
	 */
	Codelist retrieve(A asset, D directives) throws Exception;
	
	/**
	 * Derives a {@link Codelist} from a data stream, using given directives.
	 * @param stream the stream
	 * @param directives the directives
	 * @return the codelist
	 * @throws Exception if the codelist cannot be derived
	 */
	Codelist parse(InputStream stream, D directives) throws Exception;
}
