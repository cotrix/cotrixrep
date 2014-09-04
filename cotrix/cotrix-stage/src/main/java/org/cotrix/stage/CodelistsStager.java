package org.cotrix.stage;

import static org.cotrix.domain.dsl.Data.*;
import static org.cotrix.domain.utils.DomainConstants.*;
import static org.cotrix.stage.data.SomeCodelists.*;
import static org.cotrix.stage.data.SomeUsers.*;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.cotrix.application.VersioningService;
import org.cotrix.common.Outcome;
import org.cotrix.common.events.ApplicationLifecycleEvents.FirstTime;
import org.cotrix.common.events.ApplicationLifecycleEvents.Ready;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.links.Link;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.domain.user.User;
import org.cotrix.io.MapService;
import org.cotrix.io.ParseService;
import org.cotrix.io.sdmx.map.Sdmx2CodelistDirectives;
import org.cotrix.io.sdmx.parse.Stream2SdmxDirectives;
import org.cotrix.io.tabular.csv.parse.Csv2TableDirectives;
import org.cotrix.io.tabular.map.ColumnDirectives;
import org.cotrix.io.tabular.map.Table2CodelistDirectives;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.security.SignupService;
import org.cotrix.stage.data.SomeCodelists.Info;
import org.cotrix.stage.data.SyntheticCodelists;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.tabular.Column;
import org.virtualrepository.tabular.Table;

public class CodelistsStager {

	@Inject
	ParseService parser;

	@Inject
	MapService mapper;

	@Inject
	CodelistIngester ingester;
	
	@Inject
	CodelistRepository codelists;
	
	@Inject
	SignupService service;
	
	@Inject
	VersioningService versioning;

	static private final Logger log = LoggerFactory.getLogger(CodelistsStager.class);

	
	public void stage(@Observes @FirstTime Ready event) {
		
		for (User u : users)
			service.signup(u,u.name());
		
		Map<Info,Codelist> staged = new HashMap<>();
		
		for (Info info : CSV_CODELISTS)
			staged.put(info,stageCSV(info));

		for (Info info : SDMX_CODELISTS)
			stageSDMX(info);

		addVersioned(staged);
		linkSamples(staged);
	}
	
	private void addVersioned(Map<Info,Codelist> staged) {
		
		Codelist asfis = staged.get(asfis2011);
		
		Codelist newer = versioning.bump(asfis).to("2012");
		
		codelists.add(newer);
	}
	
	private void linkSamples(Map<Info,Codelist> staged) {
		
		Codelist countries = staged.get(sample_countries);
		Codelist continents = staged.get(sample_continents);
		
		List<Code> cs = new ArrayList<>();
		
		for (Code continent : continents.codes())
			cs.add(continent);
	
		LinkDefinition link = linkdef().name("Continent").target(continents).anchorTo(attribute().name("Name").build()).build();

		List<Code> codes = new ArrayList<>();
		
		Random random = new Random();
				
		for (Code code : countries.codes()) {
			
			int r = random.nextInt(cs.size()-1)+1;
			
			Code codeChangeset = modify(code).links(link().instanceOf(link).target(cs.get(r)).build()).build(); 
			
			codes.add(codeChangeset);
		}
	

		Codelist changeset = modify(countries).links(link).with(codes).build();
		
		codelists.update(changeset);
	}
	
	
	@SuppressWarnings("unused")
	private void buildSampleLinked() {
		
		//one linked codelist
		
		Codelist target = SyntheticCodelists.demo();
		
		target = ingester.ingest(target);
		
		LinkDefinition nameLink = linkdef().name("name-link").target(target).build();
		
		Collection<LinkDefinition> links = attributeLinks(target);
		
		//add name link
		links.add(nameLink);
		
		Codelist source = codelist().name("SAMPLE LINKED").links(links).build();
				
		source = ingester.ingest(source);
		
		Map<QName,LinkDefinition> linkmap = new HashMap<>();

		QName nameLinkname = nameLink.qname();
		
		for (LinkDefinition link : source.linkDefinitions())
			if (link.qname().equals(nameLinkname))
				nameLink = link;
			else
				linkmap.put(link.qname(),link);
		
		Code[] codes = codes(target,nameLink, linkmap);
		
		Codelist withLinks = modifyCodelist(source.id()).with(codes).build();
		
		codelists.update(withLinks);

	}

	
	private Code[] codes(Codelist target,LinkDefinition nameLink, Map<QName,LinkDefinition> links) {
		
		int i =1;
		
		Collection<Code> codes = new ArrayList<>();

		for (Code code : target.codes()) {
			
			Collection<Link> codelinks = new ArrayList<>();
			
			codelinks.add(link().instanceOf(nameLink).target(code).build());
			
			for (Attribute a : code.attributes()) {
				
				if (a.is(SYSTEM_TYPE))
					continue;
				
				QName name = new QName(a.qname()+"-link");
				
				LinkDefinition attributeLink = links.get(name);
				
				Link link = link().instanceOf(attributeLink).target(code).build();
				
				codelinks.add(link);
			
			}
			
			codes.add(
					code().name("code="+i).links(codelinks.toArray(new Link[0])).build()
			);
		}	
		
	
		return codes.toArray(new Code[0]);
				
		
	}
	public static Collection<LinkDefinition> attributeLinks(Codelist target) {
		
		Collection<LinkDefinition> links = new HashSet<>();
		
		for (Code code : target.codes()) 
			
			for (Attribute a : code.attributes()) {
				
				if (a.is(SYSTEM_TYPE))
					continue;
				
				QName name = new QName(a.qname()+"-link");
				
				Attribute template = attribute().name(a.qname()).ofType(a.type()).in(a.language()).build();
			
				LinkDefinition link = linkdef().name(name).target(target).anchorTo(template).build();		
				
				links.add(link);
				
			}
	
		return links;
		
	}
	
	Codelist stageCSV(Info info) {

		log.info("staging {}", info.name);

		InputStream inputStream = CodelistsStager.class.getResourceAsStream(info.resource);

		Csv2TableDirectives parseDirectives = new Csv2TableDirectives();
		parseDirectives.options().hasHeader(true);
		parseDirectives.options().setDelimiter('\t');
		parseDirectives.options().setQuote('"');
		parseDirectives.options().setEncoding(Charset.forName("UTF-8"));

		Table table = parser.parse(inputStream, parseDirectives);

		List<ColumnDirectives> directives = new ArrayList<ColumnDirectives>();
		Column codeColumn = null;
		
		for (Column column : table.columns())
			if (column.name().toString().equals(info.code))
				codeColumn = column;
			else
				directives.add(new ColumnDirectives(column));

		if (codeColumn == null)
			throw new AssertionError("bad fixture: no column with name " + info.code);

		Table2CodelistDirectives mappingDirectives = new Table2CodelistDirectives(codeColumn);
		mappingDirectives.name(info.name);
		mappingDirectives.version(info.version);

		for (ColumnDirectives directive : directives)
			mappingDirectives.add(directive);

		Attribute att1 = attribute().name("filename").value(info.resource).in("en").build();
		Attribute att2 = attribute().name("format").value("CSV").in("en").build();

		mappingDirectives.attributes(att1, att2);

		Outcome<Codelist> outcome = mapper.map(table, mappingDirectives);

		return ingester.ingest(outcome.result());
	}

	void stageSDMX(Info info) {

		log.info("staging {}({})", info.name, info.version);

		InputStream inputStream = CodelistsStager.class.getResourceAsStream(info.resource);

		CodelistBean bean = parser.parse(inputStream, Stream2SdmxDirectives.DEFAULT);

		Sdmx2CodelistDirectives directives = new Sdmx2CodelistDirectives();
		
		directives.name(info.name);
		
		Outcome<Codelist> outcome = mapper.map(bean, directives);

		ingester.ingest(outcome.result());

	}


}
