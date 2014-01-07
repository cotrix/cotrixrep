package org.acme.repository;

import org.acme.UserRepositoryCrudTest;
import org.acme.UserRepositoryQueryTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({UserRepositoryCrudTest.class, UserRepositoryQueryTest.class})
public class UserRepositoryRefTest {

}
