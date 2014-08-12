package org.cotrix.stage.data;

import static org.cotrix.domain.dsl.Data.*;
import static org.cotrix.domain.utils.Constants.*;
import static org.cotrix.domain.validation.Validators.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.dsl.Data;

public class SyntheticCodelists {
	
	public static Collection<Codelist> synthetics = Arrays.asList(
			numbered(60), sparse(60), complex(60), demo()
	);
			
	
	private static Codelist numbered(int ncodes) {
	
		Code[] codes = new Code[ncodes];
		for (int i = 1; i <= codes.length; i++) {
			Attribute[] attributes = new Attribute[]{attribute().name("Row").value(String.valueOf(i)).in("en").build()};

			codes[i-1] = code().name("code "+i).attributes(attributes).build();
		}

		return Data.codelist().name("Numbered").with(codes).build();
	}

	private static Codelist sparse(int ncodes) {
		
		Attribute att = attribute().name("format").value("Sparse").in("en").build();

		Code[] codes = new Code[ncodes];
		for (int i = 0; i < codes.length; i++) {
			int numAttributes = i/10 + 1;
			Attribute[] attributes = new Attribute[numAttributes];
			for (int l = 0; l<attributes.length; l++) attributes[l] = attribute().name("attribute"+l).value("value "+i+"-"+l).in("en").build();

			codes[i] = code().name("code"+i).attributes(attributes).build();
		}

		return Data.codelist().name("Sparse").with(codes).attributes(att).build();
	}

	private static Codelist complex(int ncodes) {
		
		Attribute[] codelistAttributes = new Attribute[3];
		codelistAttributes[0] = attribute().name("format").value("complex").in("en").build();
		codelistAttributes[1] = attribute().name("Author").value("Federico").in("en").build();
		codelistAttributes[2] = attribute().name("Author").value("Fabio").in("en").build();

		String[] languages = new String[]{"en", "fr", "es"};

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
					Attribute attribute = attribute().name("attPos"+l).value("value "+i+"-"+l+".["+y+"]").in("en").build();
					attributes.add(attribute);
				}
			}

			codes[i] = code().name("code"+i).attributes(attributes).build();
		}

		return Data.codelist().name("Complex").with(codes).attributes(codelistAttributes).version("2.1").build();
	}
	
	public static Codelist demo() {
		
		return Data.codelist().name("SAMPLE CODELIST")
				.definitions(
						attrdef().name("demo one").is(DESCRIPTION_TYPE).valueIs(
							valueType().with(max_length.instance(5)).defaultsTo("someval")
						).in("en").build(),
						attrdef().name("demo two").is(NAME_TYPE).valueIs(
							valueType().with(min_length.instance(3)).defaultsTo("someotherval").required()
						).build(),
						attrdef().name("demo three").valueIs(
								valueType().with(min_length.instance(3), max_length.instance(10))
										   .defaultsTo("yetsomeotherval")
							).build()
				)
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
								in("en").build(),
								
								attribute().
								name("description").
								value("Elefante marino settentrionale").
								ofType("description").
								in("es").build(),
								
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
										in("en").build(),
										
										attribute().
										name("description").
										value("Elefante marino del Sud").
										ofType("description").
										in("es").build(),
										
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
														in("es").build(),
														
														attribute().
														name("author").
														value("Fabio").
														ofType("annotation").
														in("es").build(),
														
														attribute().
														name("author").
														value("Marco").
														ofType("description").
														in("es").build(),
														
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
}
