/**
 * 
 */
package org.cotrix.web.publish.server.publish;

import java.nio.charset.Charset;

import org.cotrix.io.SerialisationService.SerialisationDirectives;
import org.cotrix.io.comet.serialise.Comet2XmlDirectives;
import org.cotrix.io.sdmx.serialise.Sdmx2XmlDirectives;
import org.cotrix.io.tabular.csv.serialise.Table2CsvDirectives;
import org.cotrix.web.common.shared.CsvConfiguration;
import org.cotrix.web.publish.shared.PublishDirectives;
import org.fao.fi.comet.mapping.model.MappingData;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.tabular.Table;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface SerializationDirectivesProducer<T> {
	
	public SerialisationDirectives<T> produce(PublishDirectives directives);
	
	public class Table2CsvSerializationDirectivesProducer implements SerializationDirectivesProducer<Table> {

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
	
	public class Sdmx2XmlSerializationDirectivesProducer implements SerializationDirectivesProducer<CodelistBean> {

		@Override
		public SerialisationDirectives<CodelistBean> produce(PublishDirectives publishDirectives) {
			return Sdmx2XmlDirectives.DEFAULT;
		}
	}
	
	public class Comet2XmlSerializationDirectivesProducer implements SerializationDirectivesProducer<MappingData> {

		@Override
		public SerialisationDirectives<MappingData> produce(PublishDirectives publishDirectives) {
			return Comet2XmlDirectives.DEFAULT;
		}
	}

}
