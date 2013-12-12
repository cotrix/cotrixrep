package org.cotrix.application;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface MailService {

	public abstract void sendMessage(Iterable<String> recipients, String subject, String messageBody);

}