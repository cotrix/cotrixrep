package org.acme;

import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.Constants.*;
import static org.junit.Assert.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Attribute;
import org.junit.Test;

//we test state implementations of domain objects 
//by persisting and retrieving the objects

public class PersistentCodelistTest extends DomainApplicationTest {
	
	QName name = q("name");
	QName type = q("type");
	String value = "val";
	String lang = "lang";

	QName name2 = q("name2");
	QName type2 = q("type2");
	String value2 = "val2";
	String lang2 = "lang2";


	@Test
	public void changeCode() {

		Codelist list = like(codelist().name(name).build());

		Codelist changeset = modifyCodelist(list.id()).name("newname").build();

		reveal(list).update(reveal(changeset));

		assertEquals(q("newname"), list.name());

	}

	@Test
	public void addCode() {

		Codelist list = like(codelist().name(name).build());

		Code added = code().name(name).build();

		assertFalse(list.codes().contains(added));

		Codelist changeset = modifyCodelist(list.id()).with(added).build();

		reveal(list).update(reveal(changeset));

		assertTrue(list.codes().contains(name));

	}

	@Test
	public void removeCode() {

		Code c = code().name(name).build();

		Codelist list = like(codelist().name(name).with(c).build());

		assertTrue(list.codes().contains(c));

		Code deleted = deleteCode(c.id());

		Codelist changeset = modifyCodelist(list.id()).with(deleted).build();

		reveal(list).update(reveal(changeset));

		assertFalse(list.codes().contains(c));

	}

	@Test
	public void changesAttributes() {

		Attribute a = attribute().name(name).value(value).ofType(type).in(lang).build();

		Attribute attrChangeset = modifyAttribute(a.id()).name(name2).value(value2).ofType(type2).in(lang2).build();

		Codelist list = like(codelist().name(name).attributes(a).build());

		Codelist changeset = modifyCodelist(list.id()).attributes(attrChangeset).build();

		reveal(list).update(reveal(changeset));

		a = list.attributes().lookup(name2);

		assertEquals(value2, a.value());
		assertEquals(type2, a.type());
		assertEquals(lang2, a.language());
	}

	@Test(expected = IllegalArgumentException.class)
	public void cannotErasetNameOfAttributes() {

		Attribute a = attribute().name(name).build();
		Attribute changeset = modifyAttribute(a.id()).name(NULL_QNAME).build();

		reveal(a).update(reveal(changeset));

	}

	@Test
	public void erasesValue() {

		Attribute a = like(attribute().name(name).value(value).ofType(type).in(lang).build());
		Attribute changeset = modifyAttribute(a.id()).value(NULL_STRING).build();

		reveal(a).update(reveal(changeset));

		assertEquals(name, a.name());
		assertNull(a.value());
		assertEquals(type, a.type());
		assertEquals(lang, a.language());
	}

	@Test
	public void erasesType() {

		Attribute a = attribute().name(name).value(value).ofType(type).in(lang).build();

		Attribute changeset = modifyAttribute(a.id()).ofType(NULL_QNAME).build();

		reveal(a).update(reveal(changeset));

		assertEquals(name, a.name());
		assertNull(a.type());
		assertEquals(name, a.name());
		assertEquals(lang, a.language());
	}

	@Test
	public void erasesLanguage() {

		Attribute a = attribute().name(name).value(value).ofType(type).in(lang).build();

		Attribute changeset = modifyAttribute(a.id()).in(NULL_STRING).build();

		reveal(a).update(reveal(changeset));

		assertEquals(name, a.name());
		assertNull(a.language());
		assertEquals(name, a.name());
		assertEquals(type, a.type());
	}

}
