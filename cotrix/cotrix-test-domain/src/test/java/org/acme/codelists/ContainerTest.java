package org.acme.codelists;

import static java.util.Arrays.*;
import static org.acme.codelists.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.junit.Assert.*;

import java.util.Iterator;

import org.acme.DomainTest;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.common.BeanContainer;
import org.cotrix.domain.common.Container;
import org.junit.Test;

@SuppressWarnings({"rawtypes","unchecked"})
public class ContainerTest extends DomainTest {

	@Test
	public void beansMustBeValid() {
		
		try {
			new Container.Private<>((BeanContainer)null);
		}
		catch(IllegalArgumentException e){}
	}
	
	@Test
	public void beansArePreserved() {
		
		Attribute a1 = attribute().name(name).build();
		Attribute a2 = attribute().name(name2).build();
		
		BeanContainer<Attribute.Bean> beans = likes(a1,a2);
		
		Container.Private c = new Container.Private<>(beans);
		
		assertEquals(2,c.size());
		
	}
	
	@Test
	public void beansAreWrapped() {
		
		Attribute a1 = attribute().name(name).build();
		Attribute a2 = attribute().name(name2).build();
		
		Container c = new Container.Private<>(likes(a1,a2));
		
		assertTrue(c.contains(a1));
		assertTrue(c.contains(a2));

		
		Iterator it = c.iterator();
		
		assertEquals(a1, it.next());
		assertEquals(a2, it.next());
		
	}
	
	
	@Test
	public void namedEntitiesAreHandled() {
		
		Attribute a1 = attribute().name("a").build();
		Attribute a2 = attribute().name("b").build();
		Attribute a3 = attribute().name("b").build();
		
		Container c = new Container.Private<>(likes(a1,a2,a3));
		
		assertTrue(c.contains(a1));
		assertFalse(c.contains(q("c")));
		
		assertEquals(asList(a1),c.get(a1));
		assertEquals(a1,c.getFirst(a1));
		assertEquals(asList(a2,a3),c.get(a3));
		
		try {
			c.getFirst(a3);
		}
		catch(IllegalStateException e) {}

		
		
	}
	
	@Test
	public void entitiesCanBeLookedup() {
		
		
		Attribute a = attribute().name(name).build();
		
		Container.Private c = new Container.Private<>(likes(a));
		
		assertTrue(c.contains(a));
		
		assertEquals(a,c.lookup(a.id()));
		
		assertNull(c.lookup("bad"));
	}
	
	@Test
	public void entitiesCanBeAdded() {
		
		Container.Private c = new Container.Private<>();
		
		Attribute a = attribute().name(name).build();
		
		Container.Private changeset = new Container.Private<>(likes(a));
		
		c.update(changeset);
		
		assertEquals(1,c.size());
		assertTrue(c.contains(a));
	}
	
	@Test
	public void entitiesCanBeRemoved() {
		
		Attribute a = attribute().name(name).build();
		
		Container.Private c = new Container.Private<>(likes(a));
		
		Attribute deleted = deleteAttribute(a.id());
		
		Container.Private changeset = new Container.Private<>(reveal(deleted));
		
		c.update(changeset);
		
		assertEquals(0,c.size());
		assertFalse(c.contains(a));
	}
	
	
	@Test
	public void entitiesCanBeModified() {
		
		Attribute a = attribute().name(name).build();
		
		BeanContainer sc = likes(a);
		
		Container.Private c = new Container.Private<>(sc);
		
		Attribute modified = modifyAttribute(a.id()).name(name2).build();

		Container.Private changeset = new Container.Private<>(reveal(modified));
		
		c.update(changeset);
		
		assertEquals(1,c.size());
		assertTrue(sc.contains(modified.qname()));
	}
}
