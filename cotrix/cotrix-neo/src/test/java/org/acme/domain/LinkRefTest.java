package org.acme.domain;

import org.acme.codelists.CodelinkTest;
import org.acme.codelists.LinkDefinitionTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({LinkDefinitionTest.class, CodelinkTest.class })
public class LinkRefTest {

}
