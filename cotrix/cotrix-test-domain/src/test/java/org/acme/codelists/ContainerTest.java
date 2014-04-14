package org.acme.codelists;

import static java.util.Arrays.*;
import static org.acme.codelists.Fixture.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.junit.Assert.*;

import java.util.Iterator;

import org.acme.DomainTest;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.common.Container;
import org.cotrix.domain.common.NamedContainer;
import org.cotrix.domain.common.NamedStateContainer;
import org.cotrix.domain.common.StateContainer;
import org.junit.Test;

@SuppressWarnings({"rawtypes","unchecked"})
public class ContainerTest extends DomainTest {

	@Test
	public void beansMustBeValid() {
		
		try {
			new Container.Private<>(null);
		}
		catch(IllegalArgumentException e){}
	}
	
	@Test
	public void beansArePreserved() {
		
		Attribute a1 = attribute().name(name).build();
		Attribute a2 = attribute().name(name2).build();
		
		StateContainer<Attribute.State> beans = likes(a1,a2);
		
		Container.Private c = container(beans);
		

		assertEquals(2,c.size());
		assertEquals(beans,c.state());
		
	}
	
	@Test
	public void beansAreWrapped() {
		
		Attribute a1 = attribute().name(name).build();
		Attribute a2 = attribute().name(name2).build();
		
		Container.Private c = container(likes(a1,a2));
		
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
		
		NamedContainer c = namedContainer(likes(a1,a2,a3));
		
		assertTrue(c.contains(q("a")));
		assertFalse(c.contains(q("c")));
		
		assertEquals(asList(a1),c.getAll(q("a")));
		assertEquals(a1,c.lookup(q("a")));
		assertEquals(asList(a2,a3),c.getAll(q("b")));
		
		try {
			c.lookup(q("b"));
		}
		catch(IllegalStateException e) {}

		
		
	}
	
	@Test(expected=IllegalStateException.class)
	public void entitiesCanBeLookedup() {
		
		
		Attribute a = attribute().name(name).build();
		
		Container.Private c = container(likes(a));
		
		assertEquals(a,c.lookup(a.id()));
		
		c.lookup("bad");
	}
	
	@Test
	public void entitiesCanBeAdded() {
		
		Container.Private c = container();
		
		Attribute a = attribute().name(name).build();
		
		Container.Private changeset = container(likes(a));
		
		c.update(changeset);
		
		assertEquals(1,c.size());
		assertTrue(c.contains(a));
	}
	
	@Test
	public void entitiesCanBeRemoved() {
		
		Attribute a = attribute().name(name).build();
		
		Container.Private c = container(likes(a));
		
		Attribute deleted = deleteAttribute(a.id());
		
		Container.Private changeset = container(reveal(deleted,Attribute.Private.class).state());
		
		c.update(changeset);
		
		assertEquals(0,c.size());
		assertFalse(c.contains(a.name()));
	}
	
	
	@Test
	public void entitiesCanBeModified() {
		
		Attribute a = attribute().name(name).build();
		
		NamedStateContainer sc = likes(a);
		
		Container.Private c = container(sc);
		
		Attribute modified = modifyAttribute(a.id()).name(name2).build();

		Container.Private changeset = container(stateOf(modified));
		
		c.update(changeset);
		
		assertEquals(1,c.size());
		assertTrue(sc.contains(modified.name()));
	}
	
	private Attribute.State stateOf(Attribute a) {
		return reveal(a,Attribute.Private.class).state();
	}
}
