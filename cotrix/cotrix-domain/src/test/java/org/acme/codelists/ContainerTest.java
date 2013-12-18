package org.acme.codelists;

import static java.util.Arrays.*;
import static org.junit.Assert.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.trait.Status.*;

import java.util.Iterator;

import org.cotrix.domain.common.Container;
import org.cotrix.domain.common.NamedContainer;
import org.cotrix.domain.common.StateContainer;
import org.cotrix.domain.memory.NamedMS;
import org.cotrix.domain.trait.EntityProvider;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.trait.Status;
import org.junit.Test;

public class ContainerTest {

	@Test
	public void beansMustBeValid() {
		
		try {
			new Container.Private<MyEntity,MyBean>(null);
		}
		catch(IllegalArgumentException e){}
	}
	
	@Test
	public void beansArePreserved() {
		
		MyBean bean1 = new MyBean();
		MyBean bean2 = new MyBean();
		
		StateContainer<MyBean> beans = beans(bean1,bean2);
		Container.Private<MyEntity,MyBean> c = container(beans);
		

		assertEquals(2,c.size());
		assertEquals(beans,c.state());
		
	}
	
	@Test
	public void beansAreWrapped() {
		
		MyBean bean1 = new MyBean();
		MyBean bean2 = new MyBean();
		
		Container<MyEntity> c = container(bean1,bean2);
		
		assertTrue(c.contains(bean1.entity()));
		assertTrue(c.contains(bean2.entity()));

		
		Iterator<MyEntity> it = c.iterator();
		assertEquals(bean1.entity(), it.next());
		assertEquals(bean2.entity(), it.next());
		
	}
	
	
	@Test
	public void namedEntitiesAreHandled() {
		
		MyBean bean1 = new MyBean();
		bean1.name(q("a"));
		MyBean bean2 = new MyBean();
		bean2.name(q("b"));
		MyBean bean3 = new MyBean();
		bean3.name(q("b"));
		
		NamedContainer<MyEntity> c = namedContainer(bean1,bean2,bean3);
		
		assertTrue(c.contains(q("a")));
		assertFalse(c.contains(q("c")));
		
		assertEquals(asList(bean1.entity()),c.getAll(q("a")));
		assertEquals(bean1.entity(),c.lookup(q("a")));
		assertEquals(asList(bean2.entity(),bean3.entity()),c.getAll(q("b")));
		
		try {
			c.lookup(q("b"));
		}
		catch(IllegalStateException e) {}

		
		
	}
	
	@Test
	public void entitiesCanBeAdded() {
		
		Container.Private<MyEntity,MyBean> c = container();
		
		MyBean bean = new MyBean();
		
		Container.Private<MyEntity,MyBean> changeset = container(bean);
		
		c.update(changeset);
		
		assertEquals(1,c.size());
		assertTrue(c.contains(bean.entity()));
	}
	
	@Test
	public void entitiesCanBeRemoved() {
		
		MyBean bean = new MyBean();
		
		Container.Private<MyEntity,MyBean> c = container(bean);
		
		MyBean deleted = new MyBean(bean.id(),DELETED);
		
		Container.Private<MyEntity,MyBean> changeset = container(deleted);
		
		c.update(changeset);
		
		assertEquals(0,c.size());
		assertFalse(c.contains(bean.entity()));
	}
	
	
	@Test
	public void entitiesCanBeModified() {
		
		MyBean bean = new MyBean();
		bean.name(q("n"));
		
		Container.Private<MyEntity,MyBean> c = container(bean);
		
		MyBean modified = new MyBean(bean.id(),MODIFIED);
		modified.name(q("m"));
		
		Container.Private<MyEntity,MyBean> changeset = container(modified);
		
		c.update(changeset);
		
		assertEquals(1,c.size());
		assertEquals(modified.name(),bean.name());
	}
	
	
	static class MyBean extends NamedMS implements Identified.State, Named.State, EntityProvider<MyEntity> {
		
		MyBean(String id, Status status) {
			super(id,status);
		}
		
		MyBean() {}
		
		@Override
		public MyEntity entity() {
			return new MyEntity(this);
		}
	}
	
	static class MyEntity extends Named.Abstract<MyEntity,MyBean> {
		
		MyEntity(MyBean state) {
			super(state);
		}
	};
}
