package org.cotrix.application.changelog;

import static java.util.Collections.*;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.application.changelog.CodeChange.Deleted;
import org.cotrix.application.changelog.CodeChange.Modified;
import org.cotrix.application.changelog.CodeChange.New;
import org.cotrix.domain.codelist.Codelist;

public class DefaultChangelog implements Changelog.Private {
	
	private final Codelist list;
	
	private final List<CodeChange.New> added = new ArrayList<>();
	private final List<CodeChange.Deleted> deleted = new ArrayList<>();
	private final List<CodeChange.Modified> modified = new ArrayList<>();
	
	public DefaultChangelog(Codelist list) {
		this.list=list;
	}
	
	public Codelist codelist() {
		return list;
	}
	
	@Override
	public List<CodeChange.New> added() {
		
		return unmodifiableList(added);
	}

	@Override
	public List<CodeChange.Deleted> deleted() {
		
		return unmodifiableList(deleted);
	}
	
	@Override
	public List<Modified> modified() {
		return unmodifiableList(modified);
	}
	
	@Override
	public Private add(Deleted change) {
		deleted.add(change);
		return this;
	}
	
	@Override
	public Private add(Modified change) {
		modified.add(change);
		return this;
	}
	
	@Override
	public Private add(New change) {
		added.add(change);
		return this;
	}
	
}
