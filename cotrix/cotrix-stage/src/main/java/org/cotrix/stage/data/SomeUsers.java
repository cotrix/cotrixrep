package org.cotrix.stage.data;

import static java.util.Arrays.*;
import static org.cotrix.domain.dsl.Roles.*;
import static org.cotrix.domain.dsl.Users.*;

import java.util.Collection;

import org.cotrix.domain.user.User;

public class SomeUsers {

	public static final User federico = user().name("federico").email("federico.defaveri@fao.org").fullName("Federico De Faveri").is(ROOT).build();
	public static final User fabio = user().name("fabio").email("fabio.simeoni@fao.org").fullName("Fabio Simeoni").is(ROOT).build();
	
	public static final User anton = user().name("anton").email("anton.ellenbroek@invented.com").fullName("Anton Ellenbroek").is(MANAGER).build();
	public static final User aureliano = user().name("aureliano").email("gentile.aureliano@invented.com").fullName("Aureliano Gentile").is(MANAGER).build();
	
	public static final User erik = user().name("erik").email("erik.vaningen@invented.com").fullName("Erik VanIngen").is(USER).build();
	public static final User fabiof = user().name("fiorellato").email("fabio.fiorellato@invented.com").fullName("Fabio Fiorellato").is(USER).build();
	public static final User claudio = user().name("baldassarre").email("claudio.baldassarre@invented.com").fullName("Claudio Baldassarre").is(USER).build();
	
	
	public static final Collection<User> users = asList(federico,fabio,anton,aureliano,erik,fabiof,claudio);
	
	public static final Collection<User> owners = asList(anton,aureliano);
	
}
