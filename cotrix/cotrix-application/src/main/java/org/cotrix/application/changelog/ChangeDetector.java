package org.cotrix.application.changelog;

import static org.cotrix.domain.managed.ManagedCode.*;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.managed.ManagedCode;

@Singleton
public class ChangeDetector {

	
	public List<CodeChange> changesBetween(Code before, Code after) {
		
		return changesBetween(before,manage(after));
	}
	
	public List<CodeChange> changesBetween(Code before, ManagedCode after) {

		//TODO
		List<CodeChange> changes = new ArrayList<>();
		
		return changes;
	}
}
