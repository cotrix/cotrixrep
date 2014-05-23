package org.acme.domain;

import org.acme.codelists.AttributeTest;
import org.acme.codelists.CodelistTest;
import org.acme.codelists.DefinitionTest;
import org.acme.codelists.AttributedTest;
import org.acme.codelists.CodeTest;
import org.acme.codelists.ContainerTest;
import org.acme.codelists.IdentifiedTest;
import org.acme.codelists.NamedTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({CodelistTest.class, IdentifiedTest.class, AttributedTest.class, AttributeTest.class, DefinitionTest.class, NamedTest.class, ContainerTest.class, CodeTest.class})
public class CodelistRefTest {

}
