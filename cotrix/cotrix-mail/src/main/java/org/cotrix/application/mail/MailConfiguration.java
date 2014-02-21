package org.cotrix.application.mail;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.cotrix.configuration.ConfigurationBean;


@XmlRootElement(name="mail")
public class MailConfiguration implements ConfigurationBean {

		@XmlAttribute
		private boolean enabled = true;
		
		@XmlElement
		private String senderEmail;

		@XmlElement
		private String username;

		@XmlElement
		private String password;

		@XmlElement
		private boolean auth;

		@XmlElement
		private boolean starttls;

		@XmlElement
		private String host;

		@XmlElement
		private int port;

		
		public boolean isEnabled() {
			return enabled;
		}
		
		public String senderEmail() {
			return senderEmail;
		}

		public String username() {
			return username;
		}


		public String pwd() {
			return password;
		}

		public boolean isAuth() {
			return auth;
		}

		public boolean isStartTls() {
			return starttls;
		}

		public String host() {
			return host;
		}

		public int port() {
			return port;
		}
}
