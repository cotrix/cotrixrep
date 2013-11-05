package org.cotrix;

import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.cotrix.common.Outcome;
import org.cotrix.domain.Codelist;
import org.cotrix.io.Channels;
import org.cotrix.io.MapService;
import org.cotrix.io.SerialisationService;
import org.cotrix.io.sdmx.map.Codelist2SdmxDirectives;
import org.cotrix.io.sdmx.map.Sdmx2CodelistDirectives;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.Asset;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.sdmx.SdmxCodelist;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class UseCases {

	@Inject
	MapService mapper;
	
	@Inject
	VirtualRepository repository;

	@Inject
	SerialisationService serialiser;
	
	
	@Test
	public void importFromSdmxRegistryAndPublishToSR() {

		//discover codelists 'in the hood'
		repository.discover(Channels.importTypes);

		//pick first codelist simulating choice from Cotrix user (we have no idea where this comes from)
		Asset codelist = repository.iterator().next();

		CodelistBean bean = repository.retrieve(codelist, CodelistBean.class);
		
		//import codelist with Cotrix import serialiser (no mapping customisations, we do not have a Cotrix user)
		Outcome<Codelist> outcome = mapper.map(bean, Sdmx2CodelistDirectives.DEFAULT);

		Codelist importedCodelist = outcome.result();
		
		//choose SR simulating publication channel choice from Cotrix user;
		QName channelName = new QName("semantic-repository");
		
		//publish imported codelist (no mapping customisations, we do not have a Cotrix user)
		Outcome<CodelistBean> outcome2 = mapper.map(importedCodelist,Codelist2SdmxDirectives.DEFAULT);

		RepositoryService service = repository.services().lookup(channelName);
		
		repository.publish(new SdmxCodelist(codelist.name(),service), outcome2.result());
	}

}