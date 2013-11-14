/**
 * 
 */
package org.cotrix.web.share.server.util;

import java.util.Collection;
import java.util.Iterator;

import org.virtualrepository.AssetType;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AssetTypes {
	
	
	public static String toString(Collection<AssetType> types)
	{
		if (types.isEmpty()) return "";
		StringBuilder builder = new StringBuilder();
		Iterator<AssetType> iterator = types.iterator();
		
		while(iterator.hasNext()) {
			builder.append(String.valueOf(iterator.next()));
			if (iterator.hasNext()) builder.append(", ");
		}
		return builder.toString();
	}

}
