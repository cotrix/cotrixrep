package org.acme.repository;

import org.acme.CodelistRepositoryCrudTest;
import org.acme.CodelistRepositoryQueryTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({CodelistRepositoryCrudTest.class, CodelistRepositoryQueryTest.class})
public class CodelistRepositoryRefTest {

}
