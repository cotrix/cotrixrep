package org.cotrix;

import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.cotrix.domain.Codelist;
import org.cotrix.io.Channels;
import org.cotrix.io.PublicationService;
import org.cotrix.io.map.MapService;
import org.cotrix.io.map.Outcome;
import org.cotrix.io.sdmx.SdmxMapDirectives;
import org.cotrix.io.sdmx.SdmxPublishDirectives;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.Asset;
import org.virtualrepository.VirtualRepository;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class UseCases {

	@Inject
	MapService mapper;
	
	@Inject
	VirtualRepository repository;

	@Inject
	PublicationService publicationService;
	
	
	@Test
	public void importFromSdmxRegistryAndPublishToSR() {

		//discover codelists 'in the hood'
		repository.discover(Channels.importTypes);

		//pick first codelist simulating choice from Cotrix user (we have no idea where this comes from)
		Asset codelist = repository.iterator().next();

		CodelistBean bean = repository.retrieve(codelist, CodelistBean.class);
		
		//import codelist with Cotrix import service (no mapping customisations, we do not have a Cotrix user)
		Outcome outcome = mapper.map(bean, SdmxMapDirectives.DEFAULT);

		Codelist importedCodelist = outcome.result();
		
		//choose SR simulating publication channel choice from Cotrix user;
		QName channelName = new QName("semantic-repository");
		
		//publish imported codelist (no mapping customisations, we do not have a Cotrix user)
		publicationService.publish(importedCodelist,SdmxPublishDirectives.DEFAULT,channelName);

	}

}