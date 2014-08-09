package org.acme.domain;

import org.acme.codelists.LinkTest;
import org.acme.codelists.LinkDefTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({LinkDefTest.class, LinkTest.class })
public class LinkRefTest {

}
