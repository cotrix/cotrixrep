package org.acme;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.cotrix.application.MailService;
import org.cotrix.application.impl.DefaultMailService;
import org.cotrix.application.impl.DefaultMailService.MailServiceConfiguration;
import org.cotrix.configuration.ConfigurationProvider;
import org.junit.Before;
import org.junit.Test;
import org.jvnet.mock_javamail.Mailbox;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DefaultMailServiceTest {
	
	protected static final String ADDRESS = "cotrix.noreply@gmail.com";
	
	protected MailService mailService;	

	@Before
	public void setUp() throws Exception {
		
		MailServiceConfiguration configuration = new MailServiceConfiguration();
		configuration.setSenderEmail(ADDRESS);
		
		configuration.setUsername("cotrix.noreply@gmail.com");
		configuration.setPassword("fakePassowrd");
		
		configuration.setAuth(true);
		configuration.setStarttls(true);
		configuration.setHost("localhost");
		configuration.setPort(587);
		
		Mailbox.clearAll();
		
		mailService = new DefaultMailService(new ConfigurationProvider<MailServiceConfiguration>(configuration));
	}

	@Test
	public void test() throws MessagingException, IOException {
		String address = "test@test.com";
		String subject = "This is a test subject";
		String body = "this is a body";
		mailService.sendMessage(Collections.singletonList(address), subject, body);
		
		List<Message> inbox = Mailbox.get(address);
		assertEquals(inbox.size(),1);
		Message message = inbox.get(0);
		assertEquals(1, message.getFrom().length);
		assertEquals(ADDRESS, message.getFrom()[0].toString());
		assertEquals(subject, message.getSubject());
		assertEquals(body, message.getContent());
	}

}
