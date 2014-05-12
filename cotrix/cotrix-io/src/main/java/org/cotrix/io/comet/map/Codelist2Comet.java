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
import java.util.Calendar;
import java.util.Date;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.common.NamedContainer;
import org.cotrix.io.impl.MapTask;
import org.fao.fi.comet.mapping.dsl.MappingDSL;
import org.fao.fi.comet.mapping.model.DataProvider;
import org.fao.fi.comet.mapping.model.MappingData;
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
		
		DataProvider provider = provider(NS,"code");
		
		String previous = attributes.contains(PREVIOUS_VERSION) ? attributes.lookup(PREVIOUS_VERSION).value():null;
		
		MappingData data = new MappingData().
				id(uri(codelist.name()+":"+codelist.version()+(previous==null?"":":"+previous))).
				version(codelist.version()).
				producedBy(NS).
				on(new Date()).
				linking(provider).to(provider).
				with(minimumWeightedScore(1.0), maximumCandidates(1));
		
		if (previous!=null)
			data.setDescription(String.format("A Mapping between codelist v.%s and v.%s of codelist %s", codelist.version(), previous, codelist.name()));
		
		for (Code c : codelist.codes()) {
			NamedContainer<? extends Attribute> cAttributes = c.attributes();
			
			if (cAttributes.contains(PREVIOUS_VERSION_NAME)) {
				String previousName = cAttributes.lookup(PREVIOUS_VERSION_NAME).value();
				data.including(
						MappingDSL.map(nil().with(identifierFor(provider,uri(c.name().toString())))).
							to(
							  target(nil().with(identifierFor(provider,uri(previousName)))).withMappingScore(1.0,AUTHORITATIVE)
							)
				);
			}
			else
				data.including(
						MappingDSL.map(nil().with(identifierFor(provider, uri(c.name().toString())))));
		}
		
		String msg = "mapped codelist "+codelist.name()+"("+codelist.id()+") to Comet in "+(System.currentTimeMillis()-time)/1000;
		
		report().log(item(msg)).as(INFO);

		return data;

	}
	
	
	private URI uri(String id) throws Exception {
		return new URI(URLEncoder.encode(id,"UTF-8"));
	}
	
	
	@Override
	public String toString() {
		return "codelist-2-comet";
	}
}
