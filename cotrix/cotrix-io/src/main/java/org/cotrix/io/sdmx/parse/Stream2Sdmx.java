package org.cotrix.io.sdmx.parse;

import static org.cotrix.common.CommonUtils.*;

import java.io.InputStream;
import java.util.Set;

import javax.inject.Inject;

import org.cotrix.common.events.Current;
import org.cotrix.io.impl.ParseTask;
import org.sdmxsource.sdmx.api.manager.parse.StructureParsingManager;
import org.sdmxsource.sdmx.api.model.StructureWorkspace;
import org.sdmxsource.sdmx.api.model.beans.SdmxBeans;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.util.io.ReadableDataLocationTmp;

/**
 * A {@link ParseTask} for SDMX-ML streams.
 * 
 * @author Fabio Simeoni
 *
 */
public class Stream2Sdmx implements ParseTask<CodelistBean, Stream2SdmxDirectives> {

	private final StructureParsingManager parser;

	@Inject
	public Stream2Sdmx(@Current StructureParsingManager parser) {

		notNull("sdmx parser",parser);
		
		this.parser = parser;
	}

	@Override
	public CodelistBean parse(InputStream stream, Stream2SdmxDirectives directives) throws Exception {

		StructureWorkspace ws = parser.parseStructures(new ReadableDataLocationTmp(stream));

		SdmxBeans beans = ws.getStructureBeans(false);

		Set<CodelistBean> listBeans = beans.getCodelists();

		if (listBeans.isEmpty() || listBeans.size() > 1)
			throw new IllegalArgumentException(
					"stream includes no codelists or is ambiguous, i.e. contains multiple codelists");

		return listBeans.iterator().next();

	}

	@Override
	public Class<Stream2SdmxDirectives> directedBy() {
		return Stream2SdmxDirectives.class;
	}
	
	@Override
	public String toString() {
		return "sdmx-parser";
	}
}
