/**
 * 
 */
package org.cotrix.web.publish.server.publish;

import java.nio.charset.Charset;

import org.cotrix.io.SerialisationService.SerialisationDirectives;
import org.cotrix.io.tabular.csv.serialise.Table2CsvDirectives;
import org.cotrix.web.publish.shared.PublishDirectives;
import org.cotrix.web.share.shared.CsvConfiguration;
import org.virtualrepository.tabular.Table;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface SerializationDirectivesProducer<T> {
	
	public SerialisationDirectives<T> produce(PublishDirectives directives);
	
	public class TableDesktopProducer implements SerializationDirectivesProducer<Table> {

		@Override
		public SerialisationDirectives<Table> produce(PublishDirectives publishDirectives) {
			assert publishDirectives.getCsvConfiguration()!=null;
			
			CsvConfiguration csvConfiguration = publishDirectives.getCsvConfiguration();
			
			Table2CsvDirectives directives = new Table2CsvDirectives();
			directives.options().hasHeader(csvConfiguration.isHasHeader());
			directives.options().setEncoding(Charset.forName(csvConfiguration.getCharset()));
			directives.options().setQuote(csvConfiguration.getQuote());
			directives.options().setDelimiter(csvConfiguration.getFieldSeparator());
			return directives;
		}
		
	}

}