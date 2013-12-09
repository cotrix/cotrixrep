/**
 * 
 */
package org.cotrix.web.importwizard.server.climport;

import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.io.CloudService;
import org.cotrix.io.ParseService;
import org.cotrix.io.ParseService.ParseDirectives;
import org.cotrix.io.sdmx.parse.Stream2SdmxDirectives;
import org.cotrix.web.importwizard.server.util.ParsingHelper;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.Asset;
import org.virtualrepository.tabular.Table;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ImporterSource {

		@Inject
		protected ParseService parser;
		

		@Inject
		protected CloudService cloud;

		/** 
		 * {@inheritDoc}
		 * @throws Exception 
		 */
		public <T> T getCodelist(ImportTaskSession parameters, SourceParameterProvider<T> parameterProvider) throws Exception {
			if (parameters.getInputStream()!=null) {
				InputStream is = parameters.getInputStream();
				return parser.parse(is, parameterProvider.getParseDirectives(parameters));
			}
			
			if (parameters.getAsset()!=null) {
				Asset asset = parameters.getAsset();
				return cloud.retrieve(asset.id(), parameterProvider.getTypeClass());
			}
			
			throw new IllegalArgumentException("No source found");
		}

	
	
	public interface SourceParameterProvider<T> {
		public ParseDirectives<T> getParseDirectives(ImportTaskSession parameters);
		public Class<T> getTypeClass();
		
		
		@Singleton
		public class TableDirectivesProvider implements SourceParameterProvider<Table> {
			
			@Inject
			protected ParsingHelper parsingHelper;

			@Override
			public ParseDirectives<Table> getParseDirectives(ImportTaskSession parameters) {
				return parsingHelper.getDirectives(parameters.getCsvParserConfiguration());
			}
			
			@Override
			public Class<Table> getTypeClass() {
				return Table.class;
			}
		}
		
		@Singleton
		public class CodelistBeanDirectivesProvider implements SourceParameterProvider<CodelistBean> {

			@Override
			public ParseDirectives<CodelistBean> getParseDirectives(ImportTaskSession parameters) {
				return Stream2SdmxDirectives.DEFAULT;
			}
			
			@Override
			public Class<CodelistBean> getTypeClass() {
				return CodelistBean.class;
			}
		}
	
	}
	
	

}
