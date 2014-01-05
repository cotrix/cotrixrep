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
import org.cotrix.neo.domain.Constants;
import org.cotrix.neo.domain.Constants.Relations;
import org.cotrix.neo.domain.NeoCode;
import org.cotrix.neo.domain.NeoCodelist;
import org.cotrix.neo.domain.utils.NeoBeanFactory;
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
	

	public Query.Private<Codelist, Integer> repositorySize() {

		return new Query.Private<Codelist, Integer>() {

			@Override
			public Integer execute() {

				String query = "MATCH (n:" + CODELIST.name() + ") RETURN COUNT(n) as SIZE";

				ExecutionResult result = engine.execute(query);

				try (ResourceIterator<Long> it = result.columnAs("SIZE");) {
					return it.next().intValue();
				}
			}
		};
	}

	@Override
	public MultiQuery<Codelist,Codelist> allLists() {

		return new NeoMultiQuery<Codelist,Codelist>(engine) {

			@Override
			public Iterator<Codelist> iterator() {

				String query = "match (n:" + CODELIST.name() + ") return n as NODE";

				ExecutionResult result = execute(query);

				ResourceIterator<Node> it = result.columnAs("NODE");

				return codelists(it);
			}

		};
	}

	@Override
	public MultiQuery<Codelist, CodelistCoordinates> allListCoordinates() {
		
		return new NeoMultiQuery<Codelist,CodelistCoordinates>(engine) {

			@Override
			public Iterator<CodelistCoordinates> iterator() {

				String query = "MATCH (n:" + CODELIST.name() + ") RETURN n as NODE";

				ExecutionResult result = execute(query);

				ResourceIterator<Node> it = result.columnAs("NODE");

				return coordinates(it);
			}

		};
	}

	@Override
	public MultiQuery<Codelist,Code> allCodes(final String codelistId) {
		
		return new NeoMultiQuery<Codelist,Code>(engine) {

			@Override
			public Iterator<Code> iterator() {

				String pattern = " {"+Constants.id_prop+":'"+codelistId+"'}";
				String query = "MATCH (n:" + CODELIST.name() + pattern+") -[:"+Relations.CODE.name()+"] -> (c) RETURN c AS CODE";

				ExecutionResult result = execute(query);

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

	Iterator<Codelist> codelists(ResourceIterator<Node> it) {
		return new IteratorAdapter<>(new NeoNodeIterator<>(it, NeoCodelist.factory));
	}
	
	Iterator<Code> codes(ResourceIterator<Node> it) {
		return new IteratorAdapter<>(new NeoNodeIterator<>(it, NeoCode.factory));
	}
	
	
	Iterator<CodelistCoordinates> coordinates(ResourceIterator<Node> it) {
		
		NeoBeanFactory<CodelistCoordinates> factory = new NeoBeanFactory<CodelistCoordinates>() {
			
			public CodelistCoordinates beanFrom(Node node) {
				
				NeoCodelist list = new NeoCodelist(node);
				
				return new CodelistCoordinates(list.id(),list.name(),list.version().value());
				
			};
		};
		
		return new NeoNodeIterator<>(it,factory);
	}

}
