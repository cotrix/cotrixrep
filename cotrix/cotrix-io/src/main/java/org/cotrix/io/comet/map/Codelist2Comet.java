package org.cotrix.io.comet.map;

import static org.cotrix.common.Log.*;
import static org.cotrix.common.Report.*;
import static org.cotrix.common.Report.Item.Type.*;
import static org.cotrix.domain.utils.Constants.*;
import static org.fao.fi.comet.mapping.dsl.DataProviderDSL.*;
import static org.fao.fi.comet.mapping.dsl.MappingDataDSL.*;
import static org.fao.fi.comet.mapping.dsl.MappingDetailDSL.*;
import static org.fao.fi.comet.mapping.dsl.MappingElementDSL.*;
import static org.fao.fi.comet.mapping.dsl.MappingElementIdentifierDSL.*;
import static org.fao.fi.comet.mapping.model.MappingScoreType.*;

import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.NamedContainer;
import org.cotrix.io.impl.MapTask;
import org.fao.fi.comet.mapping.dsl.MappingDSL;
import org.fao.fi.comet.mapping.model.DataProvider;
import org.fao.fi.comet.mapping.model.MappingData;
import org.fao.fi.comet.mapping.model.MappingElement;
import org.fao.fi.comet.mapping.model.data.Property;
import org.fao.fi.comet.mapping.model.data.PropertyList;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;

/**
 * A transformation from {@link Codelist} to {@link CodelistBean}.
 * 
 * @author Fabio Simeoni
 *
 */
public class Codelist2Comet implements MapTask<Codelist,MappingData,Codelist2CometDirectives> {

	
	@Override
	public Class<Codelist2CometDirectives> directedBy() {
		return Codelist2CometDirectives.class;
	}
	
	/**
	 * Applies the transformation to a given {@link Codelist} with given directives.
	 * @param codelist the codelist
	 * @params directives the directives
	 * @return the result of the transformation
	 * @throws Exception if the given codelist cannot be transformed
	 */
	public MappingData map(Codelist codelist, Codelist2CometDirectives directives) throws Exception {
		
		double time = System.currentTimeMillis();

		report().log(item("mapping codelist "+codelist.name()+"("+codelist.id()+") to Comet")).as(INFO)
				.log(item(Calendar.getInstance().getTime().toString())).as(INFO);
		
		NamedContainer<? extends Attribute> attributes = codelist.attributes();
		
		DataProvider source = provider(NS, NS+"/codelist", NS+"/codelist/"+encode(codelist.name().toString()), codelist.version());
		
		String previous = attributes.contains(PREVIOUS_VERSION) ? attributes.lookup(PREVIOUS_VERSION).value():null;
		
		DataProvider target = provider(NS, NS+"/codelist", NS+"/codelist/"+encode(codelist.name().toString()), previous);
		
		MappingData data = new MappingData()
				.id(uri(codelist.name()+":"+codelist.version()+(previous==null?"":":"+previous)))
				.version(codelist.version())
				.producedBy(NS)
				.linking(source)
				.to(target)
				.on(new Date())
				.with(minimumWeightedScore(1.0), maximumCandidates(1));
		
		if (previous!=null)
			data.setDescription(String.format("A Mapping between codelist v.%s and v.%s of codelist %s", codelist.version(), previous, codelist.name()));
		
		for (Code c : codelist.codes()) {
			
			NamedContainer<? extends Attribute> cAttributes = c.attributes();
			MappingElement element = wrap(properties(c.attributes()));
			
			if (cAttributes.contains(PREVIOUS_VERSION_NAME)) {
				String previousName = cAttributes.lookup(PREVIOUS_VERSION_NAME).value();
				data.including(
						MappingDSL.map(element.with(identifierFor(uri(c.name().toString())))).
							to(
							  target(element.with(identifierFor(uri(previousName)))).withMappingScore(1.0,AUTHORITATIVE)
							)
				);
			}
			else
				data.including(
						MappingDSL.map(element.with(identifierFor(uri(c.name().toString())))));
		}
		
		String msg = "transformed codelist "+codelist.name()+"("+codelist.id()+") to Comet in "+(System.currentTimeMillis()-time)/1000;
		
		report().log(item(msg)).as(INFO);

		return data;

	}
	
	
	private URI uri(String id) throws Exception {
		return new URI(encode(id));
	}
	
	private String encode(String id) throws Exception {
		return URLEncoder.encode(id,"UTF-8");
	}
	
	
	private PropertyList properties(NamedContainer<? extends Attribute> attributes) {
		
		List<Property> properties = new ArrayList<>();
		
		for (Attribute a : attributes)
			if (a.type()!=SYSTEM_TYPE)
				properties.add(propertyOf(a));
		
		return new PropertyList(properties);
	}

	private Property propertyOf(Attribute a) {

		return new Property(a.name().toString(), a.type().toString(), a.value()==null?"":a.value());
	}
	
	@Override
	public String toString() {
		return "codelist-2-comet";
	}
}
