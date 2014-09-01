package org.cotrix.repository.spi;

import java.util.Collection;
import java.util.Date;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.domain.user.User;
import org.cotrix.repository.CodelistCoordinates;
import org.cotrix.repository.CodelistSummary;
import org.cotrix.repository.Criterion;
import org.cotrix.repository.MultiQuery;
import org.cotrix.repository.Query;

public interface CodelistQueryFactory {

	MultiQuery<Codelist,Codelist> allLists();

	MultiQuery<Codelist, CodelistCoordinates> allListCoordinates();

	MultiQuery<Codelist,Code> allCodes(String codelistId);

	MultiQuery<Codelist, CodelistCoordinates> codelistsFor(User u);
	
	MultiQuery<Codelist, Code> codes(Collection<String> ids);
	
	
	MultiQuery<Codelist, Code> codesWithAttributes(String id, Iterable<QName> names);
	
	MultiQuery<Codelist, Code> codesWithCommonAttributes(String id, Iterable<QName> names);
	
	
	MultiQuery<Codelist, Code> codesChangedSince(String id, Date date);
	
	Query<Codelist, Code> code(String id);
	
	Query<Codelist, CodelistSummary> summary(String codelistId);

	Criterion<Codelist> byCodelistName();

	Criterion<Code> byCodeName();
	
	Criterion<CodelistCoordinates> byCoordinateName();

	<T> Criterion<T> all(Criterion<T> c1, Criterion<T> c2);
	
	<T> Criterion<T> descending(Criterion<T> c);
	
	Criterion<Codelist> byVersion();

	Criterion<Code> byAttribute(final Attribute template,int position);
		
	Criterion<Code> byLink(final LinkDefinition template,int position);


}
