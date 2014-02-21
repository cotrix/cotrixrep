package org.acme;

import static java.util.Arrays.*;

import javax.inject.Inject;

import org.cotrix.application.MailService;
import org.cotrix.test.ApplicationTest;
import org.junit.Test;

public class MailServiceTest extends ApplicationTest {

	@Inject
	MailService service;
	
	@Test
	public void canBeDisabled() {
		
		service.sendMessage(asList("some@me.com"),"new event","something has happened");
		
	}
}
