package org.cotrix.io;

import java.io.InputStream;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Codelist;
import org.fao.fi.comet.mapping.model.MappingData;
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
	 * Polls a set of underlying repositories for available codelists.
	 * 
	 * @param repositories the target services
	 * @return the number of newly discovered codelists
	 */
	int discover(RepositoryService ... repositories);

	
	/**
	 * Polls the underlying repositories for available codelists, using a given timeout.
	 * 
	 * @param timeout the timeout
	 * @return the number of newly discovered codelists
	 */
	int discover(int timeout);
	
	
	/**
	 * Polls a set of underlying repositories for available codelists, using a given timeout.
	 * 
	 * @param timeout the timeout
	 * @param repositories the target services
	 * @return the number of newly discovered codelists
	 */
	int discover(int timeout, RepositoryService ... repositories);

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
	 * Retrieves a codelist as a stream of CSV data.
	 * 
	 * @param id the codelist identifier.
	 * @return the codelist
	 * 
	 * @throws IllegalStateException if the codelist cannot be retrieved as a CSV stream.
	 */	
	InputStream retrieveAsCsv(String id);

	/**
	 * Publishes a codelist in SDMX in a given repository.
	 * 
	 * @param codelist the original codelist
	 * @param bean the publication API
	 * @param repository the name of the repository
	 * 
	 * @throws IllegalStateException if the codelist cannot be published in SDMX in the given repository
	 */
	void publish(Codelist codelist,CodelistBean bean, QName repository);
	
	/**
	 * Publishes a codelist in tabular format in a given repository.
	 * @param codelist the original codelist
	 * @param table the publication api
	 * @param repository the name of the repository
	 * 
	 * @throws IllegalStateException if the codelist cannot be published in tabular format in the given repository.
	 */
	void publish(Codelist codelist,Table table, QName repository);
	
	
	/**
	 * Publishe the 'lineage mapping' of a codelist in a given repository.
	 * @param codelist the codelist
	 * @param mapping the mapping
	 * @param repository the name of the repository
	 * 
	 * @throws IllegalStateException if the codelist cannot be published in tabular format in the given repository.
	 */
	void publish(Codelist codelist,MappingData mapping, QName repository);

	/**
	 * Returns the publication channels.
	 * 
	 * @return the publication channels
	 */
	Iterable<RepositoryService> repositories();

}