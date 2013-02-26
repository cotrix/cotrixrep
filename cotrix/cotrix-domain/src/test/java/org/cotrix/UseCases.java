package org.cotrix;

import static org.cotrix.domain.dsl.Codes.*;

import javax.xml.namespace.QName;

import org.junit.Test;

public class UseCases {

	//not automated tests as such: we test only the expressiveness and use of the model against real-world list samples
	//for convenience, we still use JUnit as a structuring framework.
	
	@Test
	public void modelFF_IRD_ICES_STOCKS() {
		
		QName desc = new QName("http://invented.org","description");
		String en = "en";
		
		codelist().name("IFF-IRD-ICES-STOCS").with(
						
						code().name("anb-7b-k8a-b").attributes(
								attr().name(desc).value("Anglerfish (Lophius budegassa) in Divisions VIIb–k and VIIIa and b").in(en).build()).build()
						,code().name("anb-8c9a").attributes(
								attr().name(desc).value("Anglerfish (Lophius budegassa) in Divisions VIIIc and IXa").in(en).build()).build()
						,code().name("ane-bisc").attributes(
								attr().name(desc).value("Anchovy in Subarea VIII (Bay of Biscay)").in(en).build()).build()
						,code().name("ane-pore").attributes(
								attr().name(desc).value("Anchovy in Division IXa").in(en).build()).build()
								
		).build();
	}
	
	@Test
	public void modelAsfis() {
		//TODO
	}
	
	@Test
	public void modelXXX() {
		//TODO
	}
}
