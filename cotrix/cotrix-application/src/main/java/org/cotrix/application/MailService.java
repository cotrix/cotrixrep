package org.cotrix.application;

import java.util.Collection;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface MailService {

	public abstract void sendMessage(Collection<String> recipients, String subject, String messageBody);

}