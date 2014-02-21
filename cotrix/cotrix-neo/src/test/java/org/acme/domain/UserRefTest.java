package org.acme.domain;

import org.acme.users.PermissionTest;
import org.acme.users.RoleTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({PermissionTest.class, RoleTest.class})
public class UserRefTest {

}
