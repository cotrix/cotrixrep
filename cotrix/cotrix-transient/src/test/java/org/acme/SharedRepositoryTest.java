package org.acme;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({CodelistRepositoryTest.class,
			   UserRepositoryTest.class})
public class SharedTest {

}
