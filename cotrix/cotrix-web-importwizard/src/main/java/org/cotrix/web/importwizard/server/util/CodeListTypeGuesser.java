/**
 * 
 */
package org.cotrix.web.importwizard.server.util;

import java.io.InputStream;

import org.cotrix.web.importwizard.shared.CodeListType;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CodeListTypeGuesser {
	
	public CodeListType guess(String fileName, InputStream is);

}
