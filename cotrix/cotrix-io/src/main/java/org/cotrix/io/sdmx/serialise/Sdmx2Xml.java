package org.cotrix.io.sdmx.serialise;

import java.io.OutputStream;

import javax.inject.Inject;

import org.cotrix.common.events.Current;
import org.cotrix.io.impl.SerialisationTask;
import org.sdmxsource.sdmx.api.constants.STRUCTURE_OUTPUT_FORMAT;
import org.sdmxsource.sdmx.api.manager.output.StructureWriterManager;
import org.sdmxsource.sdmx.api.model.beans.SdmxBeans;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.sdmx.sdmxbeans.model.SdmxStructureFormat;
import org.sdmxsource.sdmx.util.beans.container.SdmxBeansImpl;

public class Sdmx2Xml implements SerialisationTask<CodelistBean,Sdmx2XmlDirectives> {

	@Inject @Current
	StructureWriterManager manager;
	
	@Override
	public Class<Sdmx2XmlDirectives> directedBy() {
		return Sdmx2XmlDirectives.class;
	}
	
	@Override
	public void serialise(CodelistBean list, OutputStream stream, Sdmx2XmlDirectives directives) throws Exception {

		SdmxBeans beans = new SdmxBeansImpl(list);
		
		STRUCTURE_OUTPUT_FORMAT format = STRUCTURE_OUTPUT_FORMAT.SDMX_V21_STRUCTURE_DOCUMENT;
		
		manager.writeStructures(beans,new SdmxStructureFormat(format), stream);
		
	}
	
	@Override
	public String toString() {
		return "sdmx-2-xml";
	}
}
