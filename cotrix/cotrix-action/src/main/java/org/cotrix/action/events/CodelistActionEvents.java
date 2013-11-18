package org.cotrix.action.events;

import static org.cotrix.common.Utils.*;

import javax.xml.namespace.QName;

public class CodelistActionEvents {

	public abstract static class CodelistEvent {
		
		public final String id;
		public final QName name;
		public final String version;
		
		public CodelistEvent(String id, QName name, String version) {
			
			valid("id", id);
			valid("name", name);
			valid("version", version);
			
			this.id = id;
			this.name = name;
			this.version = version;
		}
		
	}
	
	public static class Import extends CodelistEvent {
		
		public Import(String id, QName name, String version) {
			super(id,name,version);
		}
		
	}
	
	public static class Version extends CodelistEvent {
		public Version(String id, QName name, String version) {
			super(id,name,version);
		}
	}
	
	public static class Publish extends CodelistEvent {
		
		public final QName repository;
		
		public Publish(String id, QName name, String version, QName repository) {
			
			super(id,name,version);
			
			valid("repository", repository);
			
			this.repository=repository;
		}
	}
	
}
