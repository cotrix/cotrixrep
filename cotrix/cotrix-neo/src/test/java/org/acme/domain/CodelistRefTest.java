package org.acme.domain;

import org.acme.codelists.AttributeTest;
import org.acme.codelists.AttributedTest;
import org.acme.codelists.CodeTest;
import org.acme.codelists.CodelistTest;
import org.acme.codelists.ContainerTest;
import org.acme.codelists.IdentifiedTest;
import org.acme.codelists.NamedTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({IdentifiedTest.class, AttributedTest.class, NamedTest.class, ContainerTest.class, AttributeTest.class, CodeTest.class, CodelistTest.class})
public class CodelistRefTest {

}