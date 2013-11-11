/**
 * 
 */
package org.cotrix.web.importwizard.server.climport;

import java.io.InputStream;

import org.cotrix.io.CloudService;
import org.cotrix.io.ParseService;
import org.cotrix.io.ParseService.ParseDirectives;
import org.virtualrepository.Asset;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface ImporterSource<T> {

	public T getCodelist();

	public class ParserSource<T> implements ImporterSource<T> {

		protected ParseService parser;
		protected ParseDirectives<T> directives;
		protected InputStream is;

		/**
		 * @param parser
		 * @param directives
		 * @param is
		 */
		public ParserSource(ParseService parser, ParseDirectives<T> directives,
				InputStream is) {
			this.parser = parser;
			this.directives = directives;
			this.is = is;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public T getCodelist() {
			return parser.parse(is, directives);
		}

	}

	public class RetrieveSource<T> implements ImporterSource<T> {


		protected CloudService cloud;
		protected Asset asset;
		protected Class<T> type;

		/**
		 * @param repository
		 * @param asset
		 * @param type
		 */
		public RetrieveSource(CloudService repository, Asset asset,
				Class<T> type) {
			this.cloud = repository;
			this.asset = asset;
			this.type = type;
		}



		/** 
		 * {@inheritDoc}
		 */
		@Override
		public T getCodelist() {
			return cloud.retrieve(asset.id(), type);
		}

	}

}
