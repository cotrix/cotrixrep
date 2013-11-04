package org.cotrix;

import javax.inject.Inject;

import org.cotrix.common.Outcome;
import org.cotrix.domain.Codelist;
import org.cotrix.io.Channels;
import org.cotrix.io.map.MapService;
import org.cotrix.io.sdmx.SdmxMapDirectives;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.Asset;
import org.virtualrepository.VirtualRepository;

import com.googlecode.jeeunit.JeeunitRunner;


@RunWith(JeeunitRunner.class)
public class RetrieveIntegrationTests {

	@Inject
	MapService mapper;
	
	@Inject
	VirtualRepository repository;
	
	@Test
	public void discoverCodelists() {
		
		repository.discover(Channels.importTypes);
		
		for (Asset codelist : repository)
			System.out.println(codelist);
	}
	
	@Test
	public void importSdmxCodelist() {
		
		repository.discover(Channels.importTypes);
		
		Asset codelist  = repository.iterator().next();
		
		CodelistBean bean = repository.retrieve(codelist, CodelistBean.class);
		
		Outcome<Codelist> outcome = mapper.map(bean, SdmxMapDirectives.DEFAULT); 
		
		System.out.println(outcome.result());
	}
	
//	@Test
//	public void discoverAndImportRemoteSdmxCodelistFromSR() {
//		
//		repository.discover(Channels.importTypes);
//		
//		Asset srCodelist = null;
//		for (Asset remoteCodelist : repository)
//			if (remoteCodelist.service().name().equals(RepositoryPlugin.name)) {
//				srCodelist=remoteCodelist;
//				break;
//			}
//		
//		if (srCodelist!=null) {
//			CodelistBean appbean = repository.retrieve(srCodelist, CodelistBean.class);
//			Outcome outcome = mapper.map(appbean, SdmxMapDirectives.DEFAULT); 
//			System.out.println(outcome.result());
//		}
//	}
	
}