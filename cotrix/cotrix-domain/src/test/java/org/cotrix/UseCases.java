package org.cotrix;

import static org.cotrix.domain.dsl.Codes.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.Code;
import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.junit.Test;

public class UseCases {

	//not automated tests as such: we test only the expressiveness and use of the model against real-world list samples
	//for convenience, we still use JUnit as a structuring framework.
	
	@Test
	public void modelFF_IRD_ICES_STOCKS() {
		
		QName desc = new QName("http://invented.org","description");
		String en = "en";
		
		Code code1 = code().name("anb-7b-k8a-b").and(
				attr().name(desc).value("Anglerfish (Lophius budegassa) in Divisions VIIb–k and VIIIa and b").in(en).build()).build();
				
		Code code2 = code().name("anb-8c9a").and(
				attr().name(desc).value("Anglerfish (Lophius budegassa) in Divisions VIIIc and IXa").in(en).build()).build();
		
		Code code3 = code().name("ane-bisc").and(
				attr().name(desc).value("Anchovy in Subarea VIII (Bay of Biscay)").in(en).build()).build();
		
		Code code4 = code().name("ane-pore").and(
				attr().name(desc).value("Anchovy in Division IXa").in(en).build()).build();
		
		codelist().name("IFF-IRD-ICES-STOCS").with(code1,code2,code3,code4).build();
		
	}
	
	@Test
	public void modelAsfis() {
		
		//25	1020100101	LAU	Petromyzon marinus	Sea lamprey	Lamproie marine	Lamprea marina			Минога морская	Linnaeus 1758	Petromyzontidae	PETROMYZONTIFORMES	TRUE
		//3-alpha
		
		Code a3code = code().name("LAU").and(
					attr().name("scientific_name").value("Petromyzon marinus").build()
					,attr().name("scientific_name").value("Petromyzon marinus").build()
		).build();
		
		Codelist alpha3 = codelist().name("3-alpha").with(a3code).build();
	
		//taxo
		
		Code taxocode = code().name("1020100101").build();
		
		Codelist taxo = codelist().name("taxocodes").with(taxocode).build();
		
		//isscaap
		
		Code isscode = code().name("25").build();
		
		Codelist isscaap = codelist().name("ISSCAAP").with(isscode).build();
		
		// asfis
		
		Codebag asfisCodebag = codebag().name("asfis").with(alpha3,taxo,isscaap).build();
		
		
		System.out.println(asfisCodebag);
				
				
	}
	
	@Test
	public void modelXXX() {
		//TODO
	}
}
