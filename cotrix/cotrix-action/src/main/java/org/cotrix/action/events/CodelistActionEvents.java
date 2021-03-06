package org.cotrix.action.events;

import static org.cotrix.common.CommonUtils.*;

import javax.xml.namespace.QName;

import org.cotrix.common.BeanSession;

public class CodelistActionEvents {

	public abstract static class CodelistEvent {
		
		public final String codelistId;
		public final QName codelistName;
		public final String codelistVersion;
		
		public final BeanSession session;
		
		public CodelistEvent(String id, QName name, String version, BeanSession session) {
			
			valid("id", id);
			valid("name", name);
			valid("version", version);
			
			this.codelistId = id;
			this.codelistName = name;
			this.codelistVersion = version;
			this.session = session;
		}
		
	}
	
	public static class Create extends CodelistEvent {
		
		public Create(String id, QName name, String version, BeanSession session) {
			super(id,name,version,session);
		}
		
	}
	
	public static class Import extends CodelistEvent {
		
		public final String origin;
		
		public Import(String origin,String id, QName name, String version, BeanSession session) {
			super(id,name,version,session);
			this.origin=origin;
		}
		
	}
	
	public static class Version extends CodelistEvent {
		
		public final String oldId;
		
		public Version(String oldId, String id, QName name, String version, BeanSession session) {
			
			super(id,name,version,session);
			
			this.oldId=oldId;
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
