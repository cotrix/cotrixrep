package org.cotrix.stage.data;

import static java.util.Arrays.*;
import static org.cotrix.domain.dsl.Roles.*;
import static org.cotrix.domain.dsl.Users.*;

import java.util.Collection;

import org.cotrix.domain.user.User;

public class SomeUsers {
	
	public static final User me = user().name("me").fullName("me").email("me@me.me").is(ROOT).build();

	public static final User federico = user().name("federico").fullName("Federico De Faveri").email("federico.defaveri@fao.org").is(ROOT).build();
	public static final User fabio = user().name("fabio").fullName("Fabio Simeoni").email("fabio.simeoni@fao.org").is(ROOT).build();
	
	public static final User anton = user().name("anton").fullName("Anton Ellenbroek").email("anton.ellenbroek@invented.com").is(MANAGER).build();
	public static final User aureliano = user().name("aureliano").fullName("Aureliano Gentile").email("gentile.aureliano@invented.com").is(MANAGER).build();
	public static final User claudio = user().name("claudio").fullName("Claudio Baldassarre").email("claudio.baldassarre@invented.com").is(MANAGER).build();
	public static final User henry = user().name("henry").fullName("Henry Burgsteden").email("henry.burgsteden@invented.com").is(MANAGER).build();
	
	public static final User erik = user().name("erik").fullName("Erik VanIngen").email("erik.vaningen@invented.com").is(USER).build();
	public static final User fabiof = user().name("fiorellato").fullName("Fabio Fiorellato").email("fabio.fiorellato@invented.com").is(USER).build();
	
	
	
	public static final Collection<User> users = asList(me,federico,fabio,anton,aureliano,erik,fabiof,claudio,henry);
	
	public static final Collection<User> owners = asList(me,anton,aureliano,claudio,henry);
	
}
