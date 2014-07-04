package org.cotrix.neo.repository;

import static java.lang.String.*;

import java.util.Iterator;

import javax.inject.Inject;

import org.cotrix.common.CommonUtils;
import org.cotrix.neo.domain.Constants.NodeType;
import org.cotrix.neo.domain.NeoCodelist;
import org.cotrix.neo.domain.utils.NeoBeanFactory;
import org.cotrix.neo.domain.utils.NeoNodeIterator;
import org.cotrix.repository.CodelistCoordinates;
import org.cotrix.repository.Criterion;
import org.cotrix.repository.Query;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;

public abstract class NeoQueries {

	public static final String $node = "N";
	public static final String $result = "RESULT";

	@Inject
	protected NeoQueryEngine engine;
	
	public Query.Private<Object, Integer> repositorySize(final NodeType type) {

		return new Query.Private<Object, Integer>() {

			@Override
			public Integer execute() {

				String query = format("MATCH (%1$s:%2$s) RETURN COUNT(%1$s) as %3$s", 
									  $node, 
									  type.name(),
									  $result);

				ExecutionResult result = engine.execute(query);

				try (ResourceIterator<Long> it = result.columnAs($result);) {
					return it.next().intValue();
				}
			}
		};
	}
	
	
	protected NeoCriterion<?> reveal(Criterion<?> c) {
		return CommonUtils.reveal(c,NeoCriterion.class);
	}
	
	
	public <T> Criterion<T> descending(final Criterion<T> c) {

		return new NeoCriterion<T>() {
			
			@Override
			protected String process(NeoMultiQuery<?, ?> query) {
				query.setDescending();
				return reveal(c).process(query);
			}
		};
	}
	

	public <T> Criterion<T> all(final Criterion<T> c1, final Criterion<T> c2) {
		
		return new NeoCriterion<T>() {
			
			@Override
			protected String process(NeoMultiQuery<?, ?> query) {
				return format("%1$s,%2$s",reveal(c1).process(query),reveal(c2).process(query));
			}
		};
		
	}

	protected Iterator<CodelistCoordinates> coordinates(ResourceIterator<Node> it) {

		NeoBeanFactory<CodelistCoordinates> factory = new NeoBeanFactory<CodelistCoordinates>() {

			public CodelistCoordinates beanFrom(Node node) {

				NeoCodelist list = new NeoCodelist(node);

				return new CodelistCoordinates(list.id(), list.name(), list.version().value());

			};
		};

		return new NeoNodeIterator<>(it, factory);
	}
}
