package org.cotrix.io.impl;

import java.util.Iterator;

import javax.inject.Inject;

import org.cotrix.io.CloudService;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.Asset;
import org.virtualrepository.AssetType;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.tabular.Table;

/**
 * A collection of import and publication channels. 
 * 
 * @author Fabio Simeoni
 *
 */
public class DefaultCloudService implements Iterable<Asset>, CloudService {

	/**
	 * The types supported for publication.
	 */
	public static final AssetType[] publicationTypes = {SdmxCodelist.type};
	
	/**
	 * The types supported for import.
	 */
	public static final AssetType[] importTypes = {SdmxCodelist.type, CsvCodelist.type};
	
	private final VirtualRepository repository;
	
	/**
	 * Creates an instance over a {@link VirtualRepository}.
	 * @param repository the repository
	 */
	@Inject
	public DefaultCloudService(VirtualRepository repository) {
		this.repository=repository;
	}
	
	@Override
	public int discover() {
		return repository.discover(DefaultCloudService.importTypes);
	}
	
	@Override
	public int discover(int timeout) {
		return repository.discover(timeout,DefaultCloudService.importTypes);
	}
	
	@Override
	public <T> T retrieve(String id, Class<T> type) {
		return repository.retrieve(repository.lookup(id),type);
	}
	
	@Override
	public CodelistBean retrieveAsSdmx(String id) {
		
		return repository.retrieve(repository.lookup(id),CodelistBean.class);
	}
	
	@Override
	public void publish(CodelistBean list, RepositoryService channel) {
		
		repository.publish(new SdmxCodelist(list.getId(),channel), list);
	}
	
	@Override
	public Table retrieveAsTable(String id) {
		
		return repository.retrieve(repository.lookup(id),Table.class);
	}

	@Override
	public void publish(Table list, RepositoryService channel) {
		
		repository.publish(new CsvCodelist("name",0,channel),list);
	}
	
	
	@Override
	public Iterable<RepositoryService> repositories() {
		return repository.services();
	}
	
	@Override
	public Iterator<Asset> iterator() {
		return repository.iterator();
	}
}
