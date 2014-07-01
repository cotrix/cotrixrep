package org.acme;

import static org.cotrix.common.CommonUtils.*;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.bind.annotation.XmlRootElement;

import org.cotrix.configuration.ConfigurationBean;

@XmlRootElement(name = "config")
public class SampleConfig implements ConfigurationBean {

	static String instance = "<cotrix><config/></cotrix>";

	static File asFile() {
		try {
			File tmp = File.createTempFile("pre", "tmp");
			FileOutputStream fos = new FileOutputStream(tmp);
			fos.write(instance.getBytes());
			fos.close();
			tmp.deleteOnExit();
			return tmp;
		} catch (Exception e) {
			throw unchecked(e);
		}
	}

}
