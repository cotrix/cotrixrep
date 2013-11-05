/**
 * 
 */
package org.cotrix.web.importwizard.server.climport;

import java.io.InputStream;

import org.cotrix.io.ParseService;
import org.cotrix.io.ParseService.ParseDirectives;
import org.virtualrepository.Asset;
import org.virtualrepository.VirtualRepository;

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


		protected VirtualRepository repository;
		protected Asset asset;
		protected Class<T> type;

		/**
		 * @param repository
		 * @param asset
		 * @param type
		 */
		public RetrieveSource(VirtualRepository repository, Asset asset,
				Class<T> type) {
			this.repository = repository;
			this.asset = asset;
			this.type = type;
		}



		/** 
		 * {@inheritDoc}
		 */
		@Override
		public T getCodelist() {
			return repository.retrieve(asset, type);
		}

	}

}
