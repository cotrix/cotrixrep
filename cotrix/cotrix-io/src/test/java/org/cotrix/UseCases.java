package org.cotrix;

import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.cotrix.domain.Codelist;
import org.cotrix.io.ImportService;
import org.cotrix.io.PublicationService;
import org.cotrix.io.ingest.Outcome;
import org.cotrix.io.sdmx.SdmxImportDirectives;
import org.cotrix.io.sdmx.SdmxPublishDirectives;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.virtualrepository.Asset;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class UseCases {

	@Inject
	ImportService importService;

	@Inject
	PublicationService publicationService;

	@Test
	public void importFromSdmxRegistryAndPublishToSR() {

		//discover codelists 'in the hood'
		importService.discoverRemoteCodelists();

		//pick first codelist simulating choice from Cotrix user (we have no idea where this comes from)
		Asset remoteCodelist = importService.remoteCodelists().iterator().next();

		//import codelist with Cotrix import service (no mapping customisations, we do not have a Cotrix user)
		Outcome<Codelist> outcome = importService.importCodelist(remoteCodelist.id(), SdmxImportDirectives.DEFAULT);

		Codelist importedCodelist = outcome.result();
		
		//choose SR simulating publication channel choice from Cotrix user;
		QName channelName = new QName("semantic-repository");
		
		//publish imported codelist (no mapping customisations, we do not have a Cotrix user)
		publicationService.publish(importedCodelist,SdmxPublishDirectives.DEFAULT,channelName);

	}

}