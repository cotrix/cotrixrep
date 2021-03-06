package org.cotrix.io.comet.serialise;

import java.io.OutputStream;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.cotrix.common.events.Current;
import org.cotrix.io.impl.SerialisationTask;
import org.fao.fi.comet.mapping.model.MappingData;
import org.sdmxsource.sdmx.api.manager.output.StructureWriterManager;

public class Comet2Xml implements SerialisationTask<MappingData,Comet2XmlDirectives> {

	@Inject @Current
	StructureWriterManager manager;
	
	@Override
	public Class<Comet2XmlDirectives> directedBy() {
		return Comet2XmlDirectives.class;
	}
	
	@Override
	public void serialise(MappingData mapping, OutputStream stream, Comet2XmlDirectives directives) throws Exception {
		
		//TODO temp solution for quick prototyping: hoping in external facility
		Marshaller marshaller = JAXBContext.newInstance(MappingData.class).createMarshaller();
		marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);		
		marshaller.marshal(mapping,stream);
		
	}
	
	@Override
	public String toString() {
		return "comet-2-xml";
	}
}
