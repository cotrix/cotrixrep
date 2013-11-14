/**
 * 
 */
package org.cotrix.web.share.server.util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Encodings {
	
	public static List<String> getEncodings()
	{
		List<String> charsets = new ArrayList<String>();
		for (Charset charset:Charset.availableCharsets().values()) {
			charsets.add(charset.displayName());
		}
		return charsets;
	}

}
