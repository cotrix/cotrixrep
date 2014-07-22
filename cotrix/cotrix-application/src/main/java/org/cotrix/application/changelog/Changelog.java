package org.cotrix.application.changelog;

import java.util.List;

public interface Changelog {
	
	//api

	List<CodeChange.New> added();

	List<CodeChange.Deleted> deleted();
	
	List<CodeChange.Modified> modified();

	//spi
	
	interface Private extends Changelog {
	
		Changelog.Private add(CodeChange.New change);
		
		Changelog.Private add(CodeChange.Deleted change);
		
		Changelog.Private add(CodeChange.Modified change);
		
	}
}