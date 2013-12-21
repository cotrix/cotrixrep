/**
 * 
 */
package org.cotrix.application.mail;

import static org.cotrix.common.Constants.*;
import static org.cotrix.common.Utils.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.cotrix.application.MailService;
import org.cotrix.configuration.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 * 
 */
@Alternative
@Priority(RUNTIME)
@ApplicationScoped
public class DefaultMailService implements MailService {

	private static Logger log = LoggerFactory.getLogger(DefaultMailService.class);

	@Inject
	protected Provider<MailConfiguration> provider;
	
	
	@PostConstruct
	public void init() {
		
		if (!provider.get().isEnabled())
			log.info("mail service is disabled");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendMessage(Collection<String> recipients, String subject, String body) {
		
		valid("recipients",recipients);
		valid("subject",subject);
		valid("body",body);

		final MailConfiguration config = provider.get();

		if (!config.isEnabled())
			return;

		log.trace("mailing {} about: {}", recipients, subject);

		

		Properties props = new Properties();
		props.put("mail.smtp.auth", config.isAuth());
		props.put("mail.smtp.starttls.enable", config.isStartTls());
		props.put("mail.smtp.host", config.host());
		props.put("mail.smtp.port", config.port());

		log.trace("mail service configfuration: {}", props);

		Authenticator auth =  new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(config.username(), config.pwd());
			}
		};
		
		Session session = Session.getInstance(props, auth);

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(config.senderEmail()));

			StringBuilder addressList = new StringBuilder();
			Iterator<String> recipientIterator = recipients.iterator();
			while (recipientIterator.hasNext()) {
				addressList.append(recipientIterator.next());
				if (recipientIterator.hasNext())
					addressList.append(',');
			}

			message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(addressList.toString()));
			message.setSubject(subject);
			message.setText(body);

			Transport.send(message);

		} catch (Exception e) {
			
			log.error("cannot send email {} to {}",subject,recipients);
		
		}

	}

}
