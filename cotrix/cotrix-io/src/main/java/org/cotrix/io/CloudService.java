package org.cotrix.io;

import javax.xml.namespace.QName;

import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.Asset;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.tabular.Table;

/**
 * Interfaces a set of remote repositories for discovery, retrieval, and publication of codelists.
 * 
 * 
 * 
 * @author Fabio Simeoni
 * 
 */
public interface CloudService extends Iterable<Asset> {

	/**
	 * Polls the underlying repositories for available codelists, using a default timeout.
	 * 
	 * @return the number of newly discovered codelists
	 */
	int discover();

	/**
	 * Polls the underlying repositories for available codelists, using a given timeout.
	 * 
	 * @param timeout the timeout
	 * @return the number of newly discovered codelists
	 */
	int discover(int timeout);

	/**
	 * Retrieves a codelist in tabular format from its repository.
	 * 
	 * @param id the codelist identifier.
	 * @return the codelist
	 * 
	 * @throws IllegalStateException if the codelist cannot be retrieved in tabular format
	 */
	<T> T retrieve(String id, Class<T> type);
	
	/**
	 * Retrieves a codelist in SDMX format from its repository.
	 * 
	 * @param id the codelist identifier.
	 * @return the codelist
	 * 
	 * @throws IllegalStateException if the codelist cannot be retrieved in SDMX format
	 */
	CodelistBean retrieveAsSdmx(String id);

	/**
	 * Retrieves a codelist in tabular format from its repository.
	 * 
	 * @param id the codelist identifier.
	 * @return the codelist
	 * 
	 * @throws IllegalStateException if the codelist cannot be retrieved in tabular format
	 */
	Table retrieveAsTable(String id);

	/**
	 * Publishes a codelist in SDMX in a given repository.
	 * @param list the codelist
	 * @param repository the name of the repository
	 * 
	 * @throws IllegalStateException if the codelist cannot be published in SDMX in the given repository
	 */
	void publish(CodelistBean list, QName repository);

	/**
	 * Publishes a codelist in tabular format in a given repository.
	 * @param list the codelist
	 * @param repository the name of the repository
	 * 
	 * @throws IllegalStateException if the codelist cannot be published in tabular format in the given repository.
	 */
	void publish(Table list, QName repository);

	/**
	 * Returns the publication channels.
	 * 
	 * @return the publication channels
	 */
	Iterable<RepositoryService> repositories();

}