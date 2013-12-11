package org.cotrix.application;

import java.util.List;

public interface MailService {

	public abstract void sendMessage(List<String> recipients, String subject,
			String messageBody);

}