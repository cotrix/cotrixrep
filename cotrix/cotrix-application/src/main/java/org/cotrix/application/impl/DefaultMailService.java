/**
 * 
 */
package org.cotrix.application.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.cotrix.application.MailService;
import org.cotrix.common.Configuration;
import org.cotrix.configuration.ConfigurationProvider;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DefaultMailService implements MailService {

	@Inject
	protected ConfigurationProvider<MailServiceConfiguration> configurationProvider;
	
	
	public DefaultMailService() {
	}
	
	public DefaultMailService(ConfigurationProvider<MailServiceConfiguration> configurationProvider) {
		this.configurationProvider = configurationProvider;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void sendMessage(List<String> recipients, String subject, String messageBody) {

		final MailServiceConfiguration configuration = configurationProvider.getConfiguration();
		
		System.out.println(configuration.getHost());

		Properties props = new Properties();
		props.put("mail.smtp.auth", configuration.isAuth());
		props.put("mail.smtp.starttls.enable", configuration.isStarttls());
		props.put("mail.smtp.host", configuration.getHost());
		props.put("mail.smtp.port", configuration.getPort());

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(configuration.getUsername(), configuration.getPassword());
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(configuration.getSenderEmail()));
			
			StringBuilder addressList = new StringBuilder();
			Iterator<String> recipientIterator = recipients.iterator();
			while (recipientIterator.hasNext()) {
				addressList.append(recipientIterator.next());
				if (recipientIterator.hasNext()) addressList.append(',');
			}
			
			message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(addressList.toString()));
			message.setSubject(subject);
			message.setText(messageBody);
			
			System.out.println(message.toString());

			Transport.send(message);

			System.out.println("Done");

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@XmlRootElement
	public static class MailServiceConfiguration implements Configuration {

		@XmlElement
		protected String senderEmail;

		@XmlElement
		protected String username;

		@XmlElement
		protected String password;

		@XmlElement
		protected boolean auth;

		@XmlElement
		protected boolean starttls;

		@XmlElement
		protected String host;

		@XmlElement
		protected int port;

		/**
		 * @return the senderEmail
		 */
		public String getSenderEmail() {
			return senderEmail;
		}

		/**
		 * @param senderEmail the senderEmail to set
		 */
		public void setSenderEmail(String senderEmail) {
			this.senderEmail = senderEmail;
		}

		/**
		 * @return the username
		 */
		public String getUsername() {
			return username;
		}

		/**
		 * @param username the username to set
		 */
		public void setUsername(String username) {
			this.username = username;
		}

		/**
		 * @return the password
		 */
		public String getPassword() {
			return password;
		}

		/**
		 * @param password the password to set
		 */
		public void setPassword(String password) {
			this.password = password;
		}

		/**
		 * @return the auth
		 */
		public boolean isAuth() {
			return auth;
		}

		/**
		 * @param auth the auth to set
		 */
		public void setAuth(boolean auth) {
			this.auth = auth;
		}

		/**
		 * @return the starttls
		 */
		public boolean isStarttls() {
			return starttls;
		}

		/**
		 * @param starttls the starttls to set
		 */
		public void setStarttls(boolean starttls) {
			this.starttls = starttls;
		}

		/**
		 * @return the host
		 */
		public String getHost() {
			return host;
		}

		/**
		 * @param host the host to set
		 */
		public void setHost(String host) {
			this.host = host;
		}

		/**
		 * @return the port
		 */
		public int getPort() {
			return port;
		}

		/**
		 * @param port the port to set
		 */
		public void setPort(int port) {
			this.port = port;
		}

	}

}
