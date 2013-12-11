/**
 * 
 */
package org.cotrix.configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;

import org.cotrix.common.Configuration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class TestUtil {
	
	protected static ConfigurationReader READER = new ConfigurationReader(Collections.<Class<? extends Configuration>>singletonList(MyConfiguration.class).iterator());
	public static MyConfiguration SAMPLE_CONFIGURATION = new MyConfiguration("This is the first parameter", 11);
	
	
	public static String getSampleConfigurationXML() {
		String xml = READER.write(Collections.<Configuration>singletonList(SAMPLE_CONFIGURATION));
		return xml;
	}
	
	public static File getSampleConfigurationTmpFile() throws IOException {
		String xml = TestUtil.getSampleConfigurationXML();
		File tmp = File.createTempFile("TestUtil", "tmp");
		FileOutputStream fos = new FileOutputStream(tmp);
		fos.write(xml.getBytes());
		fos.close();
		tmp.deleteOnExit();
		return tmp;
	}
}
