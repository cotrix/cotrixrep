package org.cotrix.io.sdmx;

import java.io.InputStream;
import java.util.Set;

import javax.inject.Inject;

import org.cotrix.domain.Codelist;
import org.cotrix.io.ingest.ImportTask;
import org.sdmxsource.sdmx.api.manager.parse.StructureParsingManager;
import org.sdmxsource.sdmx.api.model.StructureWorkspace;
import org.sdmxsource.sdmx.api.model.beans.SdmxBeans;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.util.io.ReadableDataLocationTmp;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.sdmx.SdmxCodelist;

public class SdmxImportTask implements ImportTask<SdmxCodelist,SdmxImportDirectives> {

	private final VirtualRepository repository;
	private final StructureParsingManager parser;
	
	@Inject
	public SdmxImportTask(VirtualRepository repository, StructureParsingManager parser) {
		this.repository=repository;
		this.parser=parser;
	}
	
	@Override
	public Class<SdmxImportDirectives> directedBy() {
		return SdmxImportDirectives.class;
	}
	
	@Override
	public Codelist parse(InputStream data, SdmxImportDirectives directives) throws Exception {
		
		StructureWorkspace ws = parser.parseStructures(new ReadableDataLocationTmp(data));
		
		SdmxBeans beans = ws.getStructureBeans(false);
		
		Set<CodelistBean> listBeans =  beans.getCodelists();
		
		if (listBeans.isEmpty() || listBeans.size()>1)
			throw new IllegalArgumentException("stream includes no codelists or is ambiguous, i.e. contains multiple codelists");
		
		CodelistBean listBean = listBeans.iterator().next();
		
		return transform(listBean, directives);
	}
	
	@Override
	public Codelist retrieve(SdmxCodelist asset, SdmxImportDirectives directives) throws Exception {
		
		CodelistBean listBean = repository.retrieve(asset, CodelistBean.class);
		
		return transform(listBean,directives);
	}
	
	public Codelist transform(CodelistBean listBean, SdmxImportDirectives directives) throws Exception {
		
		Sdmx2Codelist transform = new Sdmx2Codelist();
		
		return transform.apply(listBean,directives);
	}
	
	@Override
	public String toString() {
		return "sdmx-import";
	}
}
