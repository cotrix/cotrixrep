package org.cotrix.integration;

import javax.inject.Inject;

import org.cotrix.common.Outcome;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.io.CloudService;
import org.cotrix.io.MapService;
import org.cotrix.io.sdmx.map.Sdmx2CodelistDirectives;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.Asset;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.sdmx.SdmxCodelist;

import com.googlecode.jeeunit.JeeunitRunner;


@RunWith(JeeunitRunner.class)
public class RetrieveIntegrationTests {

	@Inject
	MapService mapper;
	
	@Inject
	CloudService cloud;
	
	@Inject
	VirtualRepository repository;
	
	@Test
	public void discoverCodelists() {
		
		cloud.discover();
		
		for (Asset codelist : cloud)
			System.out.println(codelist.id());
	}
	
	@Test
	public void retrieveSdmxCodelist() {
		
		cloud.discover();
		
		for (Asset codelist : cloud)
			
			if (codelist.type()==SdmxCodelist.type) {

				System.out.println(codelist);
				
				try {
					
					CodelistBean bean =  cloud.retrieveAsSdmx(codelist.id());
					
					Outcome<Codelist> outcome = mapper.map(bean,Sdmx2CodelistDirectives.DEFAULT); 
					
					System.out.println(outcome.report());
					System.out.println(outcome.result());
				
					
				}
				catch(Exception e) {
					e.printStackTrace();
				}

				break;

			}
		
	}
	
}