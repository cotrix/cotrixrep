/**
 * 
 */
package org.cotrix.web.common.server.util;

import static org.virtualrepository.impl.Type.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.cotrix.web.common.shared.Format;
import org.cotrix.web.common.shared.codelist.Property;
import org.cotrix.web.common.shared.codelist.RepositoryDetails;
import org.virtualrepository.AssetType;
import org.virtualrepository.Properties;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.comet.CometGenericType;
import org.virtualrepository.csv.CsvCodelistType;
import org.virtualrepository.csv.CsvGenericType;
import org.virtualrepository.sdmx.SdmxCodelistType;
import org.virtualrepository.sdmx.SdmxGenericType;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Repositories {
	
	public static RepositoryDetails convert(RepositoryService service) {
		RepositoryDetails details = new RepositoryDetails();
		details.setId(ValueUtils.safeValue(service.name()));
		details.setName(ValueUtils.safeValue(service.name()));
		details.setProperties(convert(service.properties()));
		details.setPublishedTypes(toString(service.publishedTypes()));
		details.setReturnedTypes(toString(service.returnedTypes()));
		return details;
	}

	public static List<Property> convert(Properties properties)
	{
		List<Property> props = new ArrayList<Property>();
		for (org.virtualrepository.Property property:properties) {
			if (property.isDisplay()) props.add(convert(property));
		}
		
		return props;
	}
	
	private static Property convert(org.virtualrepository.Property property)
	{
		return new Property(property.name(), String.valueOf(property.value()), property.description());
	}
	
	private static String toString(List<Format> formats)
	{
		if (formats.isEmpty()) return "";
		StringBuilder builder = new StringBuilder();
		Iterator<Format> iterator = formats.iterator();
		
		while(iterator.hasNext()) {
			Format format = iterator.next();
			builder.append(String.valueOf(format));
			if (iterator.hasNext()) builder.append(", ");
		}
		return builder.toString();
	}
	
	public static String toString(AssetType type) {
		Format format = convert(type);
		return format.toString();
	}
	
	public static String toString(Collection<AssetType> types) {
		List<Format> formats = convertTypes(types);
		return toString(formats);
	}
	
	public static List<Format> convertTypes(Collection<AssetType> types) {
		List<Format> formats = new ArrayList<>();
		for (AssetType type:types) formats.add(convert(type));
		return formats;
	}
	
	private static Format convert(AssetType type) {
		if (type instanceof CsvGenericType || type instanceof CsvCodelistType || type==any) return Format.CSV;
		if (type instanceof SdmxGenericType || type instanceof SdmxCodelistType) return Format.SDMX;
		if (type instanceof CometGenericType) return Format.COMET;
		throw new IllegalArgumentException("Unknown asset type "+type);
	}

}
