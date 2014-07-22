package org.cotrix.application.changelog;

import java.util.List;

public interface Changelog {
	
	//api

	List<CodelistChange.NewCode> added();

	List<CodelistChange.DeletedCode> deleted();
	
	List<CodelistChange.ModifiedCode> modified();

	//spi
	
	interface Private extends Changelog {
	
		Changelog.Private add(CodelistChange.NewCode change);
		
		Changelog.Private add(CodelistChange.DeletedCode change);
		
		Changelog.Private add(CodelistChange.ModifiedCode change);
		
	}
}