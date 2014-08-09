package org.acme;

import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.common.Ranges.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.managed.ManagedCode.*;
import static org.cotrix.domain.validation.Validators.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.cotrix.application.ValidationService;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.managed.ManagedCode;
import org.cotrix.domain.values.ValueType;
import org.cotrix.repository.CodelistActions;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.test.ApplicationTest;
import org.junit.Before;
import org.junit.Test;

public class ValidationTest extends ApplicationTest {

	
	ValueType type = valueType().with(max_length.instance(2));
	AttributeDefinition def = attrdef().name("def").occurs(once).valueIs(type).build();
	Attribute a = attribute().instanceOf(def).value("ss").build();
	Code code = code().name("c").attributes(a).build();
	Codelist list = codelist().name("l").definitions(def).with(code).build();
	
	@Inject
	CodelistRepository codelists;
	
	@Inject
	ValidationService validator;
	
	@Before
	public void versionList() {
		
		codelists.add(list);
		
	}
	
	@Test
	public void attributesAreValidatedOnUpdate() {
		
		Attribute onetoomany = attribute().instanceOf(def).build();
		Attribute moda = modify(a).value("toolong").build();

		Code modified = modify(code).attributes(onetoomany,moda).build();
		
		codelists.update(modify(list).with(modified).build());
		
		ManagedCode managed = manage(list.codes().lookup(modified.id()));
		
		//System.out.println(managed.attribute(INVALID).value());
		
		assertNotNull(managed.attribute(INVALID));
		
		modified = modify(code).attributes(delete(onetoomany),modify(a).value("ss").build()).build();
		
		codelists.update(modify(list).with(modified).build());
		
		assertNull(managed.attribute(INVALID));
	}
	
	@Test
	public void missingAAttributesAreDetectedOnUpdate() {
		
		Code modified = modify(code).attributes(delete(a)).build();
		
		codelists.update(modify(list).with(modified).build());
		
		ManagedCode managed = manage(list.codes().lookup(modified.id()));
		
		assertNotNull(managed.attribute(INVALID));
		
		//System.out.println(managed.attribute(INVALID).value());
		
	}
	
	@Test
	public void attributesAreValidatedWhenDefinitionsChange() {
		
		AttributeDefinition moddef = modify(def).valueIs(valueType().with(number.instance())).build();
		
		codelists.update(modify(list).definitions(moddef).build());
		
		validator.validate(list);
		
		ManagedCode managed = manage(list.codes().getFirst(code));
		
		assertNotNull(managed.attribute(INVALID));
		
		//System.out.println(managed.attribute(INVALID).value());
	}
	
	@Test
	public void attributesAreValidatedWhenDefinitionsAreRemoved() {
		
		//invalidate first
		
		Attribute onetoomany = attribute().instanceOf(def).build();
		
		Attribute moda = modify(a).value("toolong").build();

		Code modified = modify(code).attributes(onetoomany,moda).build();
		
		codelists.update(modify(list).with(modified).build());
		
		validator.validate(list);
		
		ManagedCode managed = manage(list.codes().getFirst(code));
		
		assertNotNull(managed.attribute(INVALID));

		//delete def and remove issue

		codelists.update(list.id(),CodelistActions.deleteAttrdef(def.id()));
			
		validator.validate(list);
		
		assertNull(managed.attribute(INVALID));

	}
	
	
	
	
}
