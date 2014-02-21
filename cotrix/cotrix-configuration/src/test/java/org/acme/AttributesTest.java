package org.acme;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.cotrix.configuration.utils.Attributes;
import org.cotrix.test.ApplicationTest;
import org.junit.Test;

public class AttributesTest extends ApplicationTest {

	@XmlRootElement
	static class Sample {
		
		@XmlElement
		Attributes attrs = new Attributes();
	}
	
	@Test
	public void binds() throws Exception {
		
		String config = "<sample><attrs one='1' two='2' /></sample>";
		InputStream stream = new ByteArrayInputStream(config.getBytes());
		Sample sample = (Sample) JAXBContext.newInstance(Sample.class).createUnmarshaller().unmarshal(stream);

		Map<String,String> map = sample.attrs.asMap();
		
		assertEquals(map.get("one"), "1");
		assertEquals(map.get("two"), "2");
	}
}
