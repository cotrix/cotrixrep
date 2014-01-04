package org.acme.utils;

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

@SuppressWarnings("all")
public class TestNeoStore implements GraphDatabaseService, GraphDatabaseAPI {
	
	private GraphDatabaseService inner;

	public void setInner(GraphDatabaseService inner) {
		this.inner = inner;
	}
	
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