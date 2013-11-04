package org.cotrix.io.sdmx;

import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.cotrix.domain.Codelist;
import org.cotrix.io.publish.PublicationTask;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.sdmx.SdmxCodelist;

public class SdmxPublishTask implements PublicationTask<SdmxPublishDirectives> {

	private final VirtualRepository repository;
	
	@Inject
	public SdmxPublishTask(VirtualRepository repository) {
		this.repository=repository;
	}
	
	@Override
	public Class<? extends SdmxPublishDirectives> directedBy() {
		return SdmxPublishDirectives.DEFAULT.getClass();
	}
	
	@Override
	public void publish(QName target,Codelist codelist, SdmxPublishDirectives directives) throws Exception {
		
		RepositoryService service = repository.services().lookup(target);
		
		SdmxCodelist asset = new SdmxCodelist(codelist.name().getLocalPart(),service);
		
		CodelistBean bean = new Codelist2Sdmx().apply(codelist,directives);
		
		repository.publish(asset,bean);
	}
	
	@Override
	public String toString() {
		return "sdmx default publisher";
	}
}
