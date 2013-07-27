package org.cotrix.io.sdmx;

import static org.cotrix.domain.utils.Utils.*;

import java.io.InputStream;
import java.util.Set;

import javax.inject.Inject;

import org.cotrix.io.parse.ParseTask;
import org.sdmxsource.sdmx.api.manager.parse.StructureParsingManager;
import org.sdmxsource.sdmx.api.model.StructureWorkspace;
import org.sdmxsource.sdmx.api.model.beans.SdmxBeans;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.util.io.ReadableDataLocationTmp;

public class SdmxParseTask implements ParseTask<CodelistBean, SdmxParseDirectives> {

	private final StructureParsingManager parser;

	@Inject
	public SdmxParseTask(StructureParsingManager parser) {

		notNull("sdmx parser",parser);
		
		this.parser = parser;
	}

	@Override
	public CodelistBean parse(InputStream stream, SdmxParseDirectives directives) throws Exception {

		StructureWorkspace ws = parser.parseStructures(new ReadableDataLocationTmp(stream));

		SdmxBeans beans = ws.getStructureBeans(false);

		Set<CodelistBean> listBeans = beans.getCodelists();

		if (listBeans.isEmpty() || listBeans.size() > 1)
			throw new IllegalArgumentException(
					"stream includes no codelists or is ambiguous, i.e. contains multiple codelists");

		return listBeans.iterator().next();

	}

	@Override
	public Class<? extends SdmxParseDirectives> directedBy() {
		return SdmxParseDirectives.class;
	}
	
	@Override
	public String toString() {
		return "sdmx-parser";
	}
}
