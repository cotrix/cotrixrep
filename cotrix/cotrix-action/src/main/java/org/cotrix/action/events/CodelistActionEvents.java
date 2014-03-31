package org.cotrix.action.events;

import static org.cotrix.common.Utils.*;

import javax.xml.namespace.QName;

import org.cotrix.common.cdi.BeanSession;

public class CodelistActionEvents {

	public abstract static class CodelistEvent {
		
		public final String id;
		public final QName name;
		public final String version;
		
		public final BeanSession session;
		
		public CodelistEvent(String id, QName name, String version, BeanSession session) {
			
			valid("id", id);
			valid("name", name);
			valid("version", version);
			
			this.id = id;
			this.name = name;
			this.version = version;
			this.session = session;
		}
		
	}
	
	public static class Create extends CodelistEvent {
		
		public Create(String id, QName name, String version, BeanSession session) {
			super(id,name,version,session);
		}
		
	}
	
	public static class Import extends CodelistEvent {
		
		public Import(String id, QName name, String version, BeanSession session) {
			super(id,name,version,session);
		}
		
	}
	
	public static class Version extends CodelistEvent {
		public Version(String id, QName name, String version, BeanSession session) {
			super(id,name,version,session);
		}
	}
	
	public static class Publish extends CodelistEvent {
		
		public final QName repository;
		
		public Publish(String id, QName name, String version, QName repository, BeanSession session) {
			
			super(id,name,version,session);
			
			valid("repository", repository);
			
			this.repository=repository;
		}
	}
	
}
