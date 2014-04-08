package org.cotrix.stage.data;

import static org.cotrix.domain.dsl.Codes.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.dsl.Codes;
import org.cotrix.domain.links.NameLink;
import org.cotrix.domain.utils.Constants;

public class SyntheticCodelists {
	
	public static Collection<Codelist> synthetics = Arrays.asList(
			numbered(60), sparse(60), complex(60), demo()
	);
			
	
	private static Codelist numbered(int ncodes) {
	
		Code[] codes = new Code[ncodes];
		for (int i = 1; i <= codes.length; i++) {
			Attribute[] attributes = new Attribute[]{attribute().name("Row").value(String.valueOf(i)).in("English").build()};

			codes[i-1] = code().name("code "+i).attributes(attributes).build();
		}

		return Codes.codelist().name("Numbered").with(codes).build();
	}

	private static Codelist sparse(int ncodes) {
		
		Attribute att = attribute().name("format").value("Sparse").in("English").build();

		Code[] codes = new Code[ncodes];
		for (int i = 0; i < codes.length; i++) {
			int numAttributes = i/10 + 1;
			Attribute[] attributes = new Attribute[numAttributes];
			for (int l = 0; l<attributes.length; l++) attributes[l] = attribute().name("attribute"+l).value("value "+i+"-"+l).in("English").build();

			codes[i] = code().name("code"+i).attributes(attributes).build();
		}

		return Codes.codelist().name("Sparse").with(codes).attributes(att).build();
	}

	private static Codelist complex(int ncodes) {
		
		Attribute[] codelistAttributes = new Attribute[3];
		codelistAttributes[0] = attribute().name("format").value("complex").in("English").build();
		codelistAttributes[1] = attribute().name("Author").value("Federico").in("English").build();
		codelistAttributes[2] = attribute().name("Author").value("Fabio").in("English").build();

		String[] languages = new String[]{"En", "Fr", "It"};

		Code[] codes = new Code[ncodes];
		for (int i = 0; i < codes.length; i++) {
			int numAttributes = i/10 + 1;
			List<Attribute> attributes = new ArrayList<Attribute>();
			for (int l = 0; l<numAttributes; l++) {

				for (String language:languages){
					Attribute attribute = attribute().name("attLang"+l).value("value "+i+"-"+l+"."+language).in(language).build();
					attributes.add(attribute);
				}

				for (int y = 0; y < 3;y++){
					Attribute attribute = attribute().name("attPos"+l).value("value "+i+"-"+l+".["+y+"]").in("En").build();
					attributes.add(attribute);
				}
			}

			codes[i] = code().name("code"+i).attributes(attributes).build();
		}

		return Codes.codelist().name("Complex").with(codes).attributes(codelistAttributes).version("2.1").build();
	}
	
	public static Codelist demo() {
		
		return Codes.codelist().name("Demo Codelist")
				.with(
						code().
						name("4060300201").
						attributes(
								attribute().
								name("description").
								value("Northern elephant seal").
								ofType("description").
								in("en").build(),

								attribute().
								name("description").
								value("Éléphant de mer boréal").
								ofType("description").
								in("fr").build(),
								
								attribute().
								name("description").
								value("Foca elephante del norte").
								ofType("description").
								in("es").build(),
								
								attribute().
								name("description").
								value("Mirounga angustirostris").
								ofType("description").
								in("la").build(),
								
								attribute().
								name("description").
								value("Elefante marino settentrionale").
								ofType("description").
								in("it").build(),
								
								attribute().
								name("author").
								value("Gill 1876").
								ofType("annotation").build()
								).build(),
								
								code().
								name("4060300202").
								attributes(
										attribute().
										name("description").
										value("Southern elephant seal").
										ofType("description").
										in("en").build(),
										
										attribute().
										name("description").
										value("Éléphant de mer austral").
										ofType("description").
										in("fr").build(),
										
										attribute().
										name("description").
										value("Foca elephante del sur").
										ofType("description").
										in("es").build(),
										
										attribute().
										name("description").
										value("Mirounga leonina").
										ofType("description").
										in("la").build(),
										
										attribute().
										name("description").
										value("Elefante marino del Sud").
										ofType("description").
										in("it").build(),
										
										attribute().
										name("author").
										value("Linnaeus 1758").
										ofType("annotation").build()
										).build(),
										
										code().
										name("4060300203").
										attributes(
												attribute().
												name("description").
												value("Southern elephant").
												ofType("description").
												in("en").build(),
												
												attribute().
												name("description").
												value("South elephant").
												ofType("description").
												in("en").build(),
												
												attribute().
												name("description").
												value("Éléphant").
												ofType("description").
												in("fr").build(),
												
												attribute().
												name("description").
												value("Éléphant").
												ofType("annotation").
												in("fr").build(),
												
												attribute().
												name("author").
												value("Federico 2013").
												ofType("annotation").build()	
												,
												attribute().
												name("author").
												value("Fabio 2013").
												ofType("annotation").build()
												).build()).

												attributes(
														attribute().
														name("file").
														value("complex_codelist.txt").
														ofType("description").in("en").build(),
														
														attribute().
														name("encoding").
														value("UTF-8").
														ofType("description").build(),
														
														attribute().
														name("author").
														value("Federico").
														ofType("annotation").
														in("it").build(),
														
														attribute().
														name("author").
														value("Fabio").
														ofType("annotation").
														in("it").build(),
														
														attribute().
														name("author").
														value("Marco").
														ofType("description").
														in("it").build(),
														
														attribute().
														name("author").
														value("Frederick").
														ofType("annotation").
														in("en").build(),
														
														attribute().
														name("author").
														value("Mark").
														ofType("description").
														in("en").build()).
														version("2.2").build();
	}

	
	public static Codelist linked(Codelist list) {
		
		int i =1;
		
		Map<QName,CodelistLink> links = new HashMap<>();
		
		QName name = new QName("name-link");
		
		CodelistLink nameLink = listLink().name("name-link").target(list).build();
		
		links.put(name,nameLink);
		
		Collection<Code> codes = new ArrayList<>();
		
		
		for (Code code : list.codes()) {
			
			Collection<Codelink> codelinks = new ArrayList<>();
			
			codelinks.add(link().instanceOf(nameLink).target(code).build());
			
			for (Attribute a : code.attributes()) {
				
				if (a.type()==Constants.SYSTEM_TYPE)
					continue;
				
				name = new QName(a.name()+"-link");
				
				CodelistLink attributeLink = links.get(name);
				
				if (attributeLink==null)  {
					
					attributeLink = listLink().name(name).target(list).anchorTo(
														attribute().name(a.name()).build())
											.build();		
					
					links.put(name,attributeLink);
				}
				
				codelinks.add(link().instanceOf(attributeLink).target(code).build());
			
			}
			
			codes.add(
					code().name("code="+i).links(codelinks.toArray(new Codelink[0])).build()
			);
		}	
		
	
		return codelist().name("sample linked")
									.links(links.values().toArray(new CodelistLink[0]))
									.with(codes.toArray(new Code[0]))
									.build();
				
		
	}
}
