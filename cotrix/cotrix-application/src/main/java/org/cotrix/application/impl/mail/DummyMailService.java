package org.cotrix.application.impl.mail;

import java.util.Collection;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;

import org.cotrix.application.MailService;
import org.cotrix.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Alternative @Priority(Constants.DEFAULT)
public class DummyMailService implements MailService {

	Logger log = LoggerFactory.getLogger(MailService.class);

	@Override
	public void sendMessage(Collection<String> recipients, String subject,
			String messageBody) {

		log.info("notified " + recipients + " of " + subject);
	}
}
