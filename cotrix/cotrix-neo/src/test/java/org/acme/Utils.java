package org.acme;

import static org.cotrix.common.Constants.*;

import javax.annotation.Priority;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.cotrix.common.cdi.ApplicationEvents.NewStore;
import org.cotrix.common.cdi.ApplicationEvents.Startup;
import org.cotrix.neo.NeoLifecycle.NewNeoStore;
import org.neo4j.graphdb.DependencyResolver;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.event.KernelEventHandler;
import org.neo4j.graphdb.event.TransactionEventHandler;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.graphdb.schema.Schema;
import org.neo4j.graphdb.traversal.BidirectionalTraversalDescription;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.GraphDatabaseAPI;
import org.neo4j.kernel.TransactionBuilder;
import org.neo4j.kernel.impl.nioneo.store.StoreId;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Priority(TEST)
@SuppressWarnings("all")
public class Utils {

	private static Logger log = LoggerFactory.getLogger("test");
	
	//we want a fresh db across tests, but the same within one test.
	//we use a singleton delegate and stuff it with a fresh db at each test.
	
	static TestDatabaseService store = new TestDatabaseService();
	
	
	public static void init(@Observes Startup event, Event<NewStore> events) {
		
		log.info("creating Neo store in memory for testing");
		
		store.inner =  new TestGraphDatabaseFactory().newImpermanentDatabase();
		
		events.fire(new NewNeoStore(store));
	}
			
	@Produces @Alternative @Singleton
	public static GraphDatabaseService testDatabase() {
		
		return store;
	}
	
	@SuppressWarnings("all")
	static class TestDatabaseService implements GraphDatabaseService, GraphDatabaseAPI {
		
		GraphDatabaseService inner;

		public Node createNode() {
			return inner.createNode();
		}

		public Node createNode(Label... labels) {
			return inner.createNode(labels);
		}

		public Node getNodeById(long id) {
			return inner.getNodeById(id);
		}

		public Relationship getRelationshipById(long id) {
			return inner.getRelationshipById(id);
		}

		public Iterable<Node> getAllNodes() {
			return inner.getAllNodes();
		}

		public ResourceIterable<Node> findNodesByLabelAndProperty(Label label, String key, Object value) {
			return inner.findNodesByLabelAndProperty(label, key, value);
		}

		public Iterable<RelationshipType> getRelationshipTypes() {
			return inner.getRelationshipTypes();
		}

		public boolean isAvailable(long timeout) {
			return inner.isAvailable(timeout);
		}

		public void shutdown() {
			inner.shutdown();
		}

		public Transaction beginTx() {
			return inner.beginTx();
		}

		public <T> TransactionEventHandler<T> registerTransactionEventHandler(TransactionEventHandler<T> handler) {
			return inner.registerTransactionEventHandler(handler);
		}

		public <T> TransactionEventHandler<T> unregisterTransactionEventHandler(TransactionEventHandler<T> handler) {
			return inner.unregisterTransactionEventHandler(handler);
		}

		public KernelEventHandler registerKernelEventHandler(KernelEventHandler handler) {
			return inner.registerKernelEventHandler(handler);
		}

		public KernelEventHandler unregisterKernelEventHandler(KernelEventHandler handler) {
			return inner.unregisterKernelEventHandler(handler);
		}

		public Schema schema() {
			return inner.schema();
		}

		public IndexManager index() {
			return inner.index();
		}

		public TraversalDescription traversalDescription() {
			return inner.traversalDescription();
		}

		public BidirectionalTraversalDescription bidirectionalTraversalDescription() {
			return inner.bidirectionalTraversalDescription();
		}

		@Override
		public DependencyResolver getDependencyResolver() {
			return ((GraphDatabaseAPI) inner).getDependencyResolver();
		}

		@Override
		public StoreId storeId() {
			return ((GraphDatabaseAPI) inner).storeId();
		}

		@Override
		public TransactionBuilder tx() {
			return ((GraphDatabaseAPI) inner).tx();
		}

		@Override
		@Deprecated
		public String getStoreDir() {
			return ((GraphDatabaseAPI) inner).getStoreDir();
		}
	}
}
