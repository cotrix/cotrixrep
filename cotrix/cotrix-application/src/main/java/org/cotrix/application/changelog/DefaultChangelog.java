package org.cotrix.application.changelog;

import static java.util.Collections.*;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.application.changelog.CodelistChange.DeletedCode;
import org.cotrix.application.changelog.CodelistChange.ModifiedCode;
import org.cotrix.application.changelog.CodelistChange.NewCode;
import org.cotrix.domain.codelist.Codelist;

public class DefaultChangelog implements Changelog.Private {
	
	private final Codelist list;
	
	private final List<CodelistChange.NewCode> added = new ArrayList<>();
	private final List<CodelistChange.DeletedCode> deleted = new ArrayList<>();
	private final List<CodelistChange.ModifiedCode> modified = new ArrayList<>();
	
	public DefaultChangelog(Codelist list) {
		this.list=list;
	}
	
	public Codelist codelist() {
		return list;
	}
	
	@Override
	public List<CodelistChange.NewCode> added() {
		
		return unmodifiableList(added);
	}

	@Override
	public List<CodelistChange.DeletedCode> deleted() {
		
		return unmodifiableList(deleted);
	}
	
	@Override
	public List<ModifiedCode> modified() {
		return unmodifiableList(modified);
	}
	
	@Override
	public Private add(DeletedCode change) {
		deleted.add(change);
		return this;
	}
	
	@Override
	public Private add(ModifiedCode change) {
		modified.add(change);
		return this;
	}
	
	@Override
	public Private add(NewCode change) {
		added.add(change);
		return this;
	}
	
}
