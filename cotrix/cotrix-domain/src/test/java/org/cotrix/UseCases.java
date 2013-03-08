package org.cotrix;

import static org.cotrix.domain.dsl.Codes.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.Code;
import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.CodelistLink;
import org.junit.Test;

public class UseCases {

	//not automated tests as such: we test only the expressiveness and use of the model against real-world list samples
	//for convenience, we still use JUnit as a structuring framework.
	
	@Test
	public void modelFF_IRD_ICES_STOCKS() {
		
		QName desc = new QName("http://invented.org","description");
		String en = "en";
		
		Code code1 = code().name("anb-7b-k8a-b").attributes(
				attr().name(desc).value("Anglerfish (Lophius budegassa) in Divisions VIIb–k and VIIIa and b").in(en).build()).build();
				
		Code code2 = code().name("anb-8c9a").attributes(
				attr().name(desc).value("Anglerfish (Lophius budegassa) in Divisions VIIIc and IXa").in(en).build()).build();
		
		Code code3 = code().name("ane-bisc").attributes(
				attr().name(desc).value("Anchovy in Subarea VIII (Bay of Biscay)").in(en).build()).build();
		
		Code code4 = code().name("ane-pore").attributes(
				attr().name(desc).value("Anchovy in Division IXa").in(en).build()).build();
		
		codelist().name("IFF-IRD-ICES-STOCS").with(code1,code2,code3,code4).build();
		
	}
	
	@Test
	public void modelLinkedAsfis() {
		
		//create taxo and assume it has been imported with given identifiers
		
		Code taxocode = code("taxo1").name("1020100101").build();
		Codelist taxo = codelist("taxo").name("taxocodes").with(taxocode).build();
		
		//create 3-alpha and link it to taxo
		
		//create list link first, so that we can refer to it in code links
		CodelistLink alpha2taxo = listLink().name("alpha-2-taxo")
											.target(taxo)
											.attributes(
											 //we could add attributes to the link
											)
											.build();
		
		//then create an alpha code
		Code a3code = code().name("LAU")
						.links(
								codeLink().instanceOf(alpha2taxo) //points to its definition
										  .target(taxocode)
										  .attributes(
											//we can add attributes to instance level if we want
										   )
										  .build()
						)
						.attributes(
							attr().name("scientific_name").value("Petromyzon marinus").build()
						)
						.build();
		
		//finally create list with links and
		Codelist alpha3 = codelist().name("3-alpha")
									.with(a3code)
									.links(alpha2taxo)
									.attributes(
									   //we could add attributes (list metadata)
									 )
									.build();
	
	

		
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
