package org.cotrix.neo.repository;

import static org.cotrix.common.Constants.*;
import static org.cotrix.neo.domain.Constants.NodeType.*;

import java.util.Iterator;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.common.IteratorAdapter;
import org.cotrix.domain.user.User;
import org.cotrix.neo.domain.Constants.Relations;
import org.cotrix.neo.domain.NeoCode;
import org.cotrix.neo.domain.NeoCodelist;
import org.cotrix.neo.domain.utils.NeoNodeIterator;
import org.cotrix.repository.CodelistCoordinates;
import org.cotrix.repository.CodelistSummary;
import org.cotrix.repository.Criterion;
import org.cotrix.repository.MultiQuery;
import org.cotrix.repository.Query;
import org.cotrix.repository.spi.CodelistQueryFactory;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;

@Singleton @Alternative @Priority(RUNTIME)
public class NeoCodelistQueries implements CodelistQueryFactory {

	@Inject
	private NeoQueryEngine engine;
	

	public Query<Codelist.State, Integer> repositorySize() {

		return new Query<Codelist.State, Integer>() {

			@Override
			public Integer execute() {

				String query = "match (n:" + CODELIST.name() + ") return count(n) as SIZE";

				ExecutionResult result = engine.execute(query);

				try (ResourceIterator<Long> it = result.columnAs("SIZE");) {
					return it.next().intValue();
				}
			}
		};
	}

	@Override
	public MultiQuery<Codelist, ? extends Codelist> allLists() {

		return new NeoMultiQuery<Codelist, Codelist.Private>() {

			@Override
			public Iterator<Codelist.Private> iterator() {

				String query = "match (n:" + CODELIST.name() + ") return n as NODE";

				ExecutionResult result = engine.execute(query);

				ResourceIterator<Node> it = result.columnAs("NODE");

				return codelists(it);
			}

		};
	}

	@Override
	public MultiQuery<Codelist, CodelistCoordinates> allListCoordinates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MultiQuery<Codelist,  ? extends Code> allCodes(String codelistId) {
		
		return new NeoMultiQuery<Codelist, Code.Private>() {

			@Override
			public Iterator<Code.Private> iterator() {

				String query = "match (n:" + CODELIST.name() + ") -[:"+Relations.CODE.name()+"] -> (c) return c as CODE";

				ExecutionResult result = engine.execute(query);

				ResourceIterator<Node> it = result.columnAs("CODE");

				return codes(it);
			}

		};
	}

	@Override
	public MultiQuery<Codelist, CodelistCoordinates> codelistsFor(User u) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query<Codelist, CodelistSummary> summary(String codelistId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Criterion<Codelist> byCodelistName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Criterion<Code> byCodeName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Criterion<CodelistCoordinates> byCoordinateName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Criterion<T> all(Criterion<T> c1, Criterion<T> c2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Criterion<T> descending(Criterion<T> c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Criterion<Codelist> byVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Criterion<Code> byAttribute(Attribute attribute, int position) {
		// TODO Auto-generated method stub
		return null;
	}

	// helpers

	Iterator<Codelist.Private> codelists(ResourceIterator<Node> it) {
		return new IteratorAdapter<>(new NeoNodeIterator<>(it, NeoCodelist.factory));
	}
	
	Iterator<Code.Private> codes(ResourceIterator<Node> it) {
		return new IteratorAdapter<>(new NeoNodeIterator<>(it, NeoCode.factory));
	}

}
