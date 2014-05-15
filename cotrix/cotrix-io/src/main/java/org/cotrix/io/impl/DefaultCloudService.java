package org.cotrix.io.impl;

import static java.util.Arrays.*;
import static org.cotrix.common.Utils.*;

import java.io.InputStream;
import java.util.Iterator;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.namespace.QName;

import org.cotrix.common.cdi.ApplicationEvents.EndRequest;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Attribute;
import org.cotrix.io.CloudService;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.Asset;
import org.virtualrepository.AssetType;
import org.virtualrepository.Context;
import org.virtualrepository.Property;
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
@Singleton
public class DefaultCloudService implements Iterable<Asset>, CloudService {

	
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
	public int discover(RepositoryService... repositories) {
		return repository.discover(asList(repositories), DefaultCloudService.importTypes);
	}
	
	@Override
	public int discover(int timeout) {
		return repository.discover(timeout,DefaultCloudService.importTypes);
	}
	
	@Override
	public int discover(int timeout, RepositoryService... repositories) {
		return repository.discover(timeout,asList(repositories),DefaultCloudService.importTypes);
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
	public Table retrieveAsTable(String id) {
		
		return repository.retrieve(repository.lookup(id),Table.class);
	}
	
	@Override
	public InputStream retrieveAsCsv(String id) {
		return repository.retrieve(repository.lookup(id), InputStream.class);
	}
	
	@Override
	public void publish(Codelist list, CodelistBean bean, QName name) {
		
		notNull("codelist",list);
		notNull("sdmx bean",bean);
		notNull("repository name",name);
		
		RepositoryService service = repository.services().lookup(name);
		
		SdmxCodelist asset = new SdmxCodelist(bean.getId(),service);
		
		repository.publish(describe(list,asset), bean);
	}
	
	@Override
	public void publish(Codelist list, Table table, QName name) {
		
		notNull("codelist",list);
		notNull("table",table);
		notNull("repository name",name);
		
		RepositoryService service = repository.services().lookup(name);
		
		Asset asset = new CsvCodelist("name",0,service);
		
		repository.publish(describe(list,asset),table);
	}
	
	
	private Asset describe(Codelist list, Asset asset) {
		
		for (Attribute attribute : list.attributes()) {
			asset.properties().add(new Property(attribute.name().toString(),attribute.value()));
		}
		
		return asset;
	}
	
	@Override
	public Iterable<RepositoryService> repositories() {
		return repository.services();
	}
	
	@Override
	public Iterator<Asset> iterator() {
		return repository.iterator();
	}
	
	
	void onStartRequest(@Observes EndRequest end){
		Context.reset();
	}
}
