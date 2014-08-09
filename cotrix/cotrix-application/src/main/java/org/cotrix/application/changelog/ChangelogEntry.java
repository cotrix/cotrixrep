package org.cotrix.application.changelog;

import static java.lang.String.*;
import static org.cotrix.application.shared.EditorialEvent.Type.*;

import org.cotrix.application.shared.DefaultEditorialEvent;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Link;

public class ChangelogEntry extends DefaultEditorialEvent {

	public static ChangelogEntry entry(final Code c) {
		
		String id = c.id();
		String name = c.qname().toString();
		
		return new ChangelogEntry(CHANGE, c.qname().getLocalPart(),id,name);
		
	}
	
	public static ChangelogEntry entry(Code c, Link l) {
		
		return entry(CHANGE,c,l);
	}
	
	public static ChangelogEntry entry(Type type,Code c, Link l) {
	
		String id = format(compound,c.id(),l.definition().id());
		String name = format(compound,c.qname(),l.qname());
		
		ChangelogEntry e = new ChangelogEntry(type,l.qname().getLocalPart(),id,name);
		
		return e;
	}
	
	
	public static ChangelogEntry entry(Code c, Attribute a) {
		return entry(CHANGE,c,a);
	}
	
	public static ChangelogEntry entry(Type type,Code c, Attribute a) {
		
		String id = format(compound,c.id(),a.definition().id());
		String name = format(compound,c.qname(),a.qname());
		
		ChangelogEntry e = new ChangelogEntry(type,a.qname().getLocalPart(),id,name);
		
		e.subtitle(a.language()==null? a.type().getLocalPart():a.type().getLocalPart()+","+a.language());
		
		return e;
	}
	
	private static final String compound = "%s:%s";
	
	
	private final String id;
	private final String name;
	private String from;
	private String to;
	
	
	public ChangelogEntry(Type type,String title,String id,String name) {
		super(type,title);
		this.id = id;
		this.name=name;
	}
	
	public String id() {
		return id;
	}
	
	public String name() {
		return name;
	}
	
	public String from() {
		return from;
	}
	
	public String to() {
		return to;
	}
	
	ChangelogEntry from(Object val) {
		this.from=val.toString();
		return this;
	}
	
	ChangelogEntry to(Object val) {
		this.to=val.toString();
		return this;
	}
	
}
