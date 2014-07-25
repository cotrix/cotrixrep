package org.cotrix.io.sdmx.map;

import static org.cotrix.common.Report.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.io.sdmx.SdmxElement.*;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.utils.Constants;
import org.cotrix.io.impl.MapTask;
import org.cotrix.io.utils.SharedDefinitionPool;
import org.sdmxsource.sdmx.api.model.beans.base.AnnotationBean;
import org.sdmxsource.sdmx.api.model.beans.base.NameableBean;
import org.sdmxsource.sdmx.api.model.beans.base.TextTypeWrapper;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodeBean;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;

public class Sdmx2Codelist implements MapTask<CodelistBean, Codelist,Sdmx2CodelistDirectives>	 {

	@Override
	public Class<Sdmx2CodelistDirectives> directedBy() {
		return Sdmx2CodelistDirectives.class;
	}
	
	public Codelist map(CodelistBean listbean, Sdmx2CodelistDirectives directives) throws Exception {
		
		report().log("importing codelist '"+listbean.getId()+"'");
		report().log("==============================");
		
		//NODE: this cannot scale that much: it's all in memory.
		
		
		List<Code> codes = new ArrayList<Code>();
		
		SharedDefinitionPool pool = new SharedDefinitionPool();
		
		for (CodeBean codebean : listbean.getItems())
			codes.add(
					code().
					name(codebean.getId()).
					attributes(attributesOf(codebean,pool,directives)).
					build()
			);
		
		String version = directives.version()==null?listbean.getVersion():directives.version();
		
		Codelist codelist = codelist().
				name(directives.name()==null?new QName(listbean.getId()):directives.name()).
				with(codes).
				definitions(pool).
				attributes(from(listbean,directives)).
				version(version).
				build();
		
		report().log("==============================");
		report().log("terminated import of codelist '"+codelist.qname()+"' with "+codelist.codes().size()+" codes.");
		
		return codelist;
	}
	
	
	
	
	
	//helpers
	
	
	
	
	private Attribute attributeFor(QName name, QName type, TextTypeWrapper wrapper, SharedDefinitionPool pool) {
		
		return pool==null?
				attribute()
					.name(name)
					.ofType(type).in(wrapper.getLocale())
				    .value(wrapper.getValue()).
				    build() 
				:
				attribute().
					 instanceOf(pool.get(name,type,wrapper.getLocale()))
					 .value(wrapper.getValue())
					 .build();
	}
	
	
	private List<Attribute> from(CodelistBean list, Sdmx2CodelistDirectives directives) {
		
		List<Attribute> attributes = new ArrayList<Attribute>();
		
		if (directives.isIncluded(FINAL))
			attributes.add(attribute().
					        name(directives.get(FINAL)).
					        value(String.valueOf(list.isFinal().isTrue())).
					        build());
		
		if (directives.isIncluded(AGENCY))
			attributes.add(	attribute().
								name(directives.get(AGENCY)).
								value(list.getAgencyId()).
								build());
		
		if (directives.isIncluded(VALID_FROM) && list.getStartDate()!=null)
			attributes.add(attribute().
							name(directives.get(VALID_FROM)).
							value(list.getStartDate().getDateInSdmxFormat()).
							build());
	
		
		if (directives.isIncluded(VALID_TO) && list.getEndDate()!=null)
			attributes.add(attribute().
						name(directives.get(VALID_TO)).
						value(list.getEndDate().getDateInSdmxFormat())
						.build());
	   	
		if (directives.isIncluded(URI) && list.getUri()!=null)
			attributes.add(attribute().
					name(directives.get(URI)).
					value(list.getUri().toString())
					.build());
		
		attributes.addAll(attributesOf(list,directives));
		
		return attributes;
	}
	
	
	
	private List<Attribute> attributesOf(NameableBean bean, Sdmx2CodelistDirectives directives) {
		return attributesOf(bean,null, directives);
	}
	
	private List<Attribute> attributesOf(NameableBean bean, SharedDefinitionPool pool, Sdmx2CodelistDirectives directives) {
		
		List<Attribute> attributes = new ArrayList<>();
		
		//names: at least one, all with language
		for (TextTypeWrapper name : bean.getNames())
			attributes.add(attributeFor(directives.get(NAME), Constants.NAME_TYPE, name, pool));
		

		//descriptions: might be none, all with language
		for (TextTypeWrapper description :  bean.getDescriptions()) 
			attributes.add(attributeFor(directives.get(DESCRIPTION),Constants.DESCRIPTION_TYPE, description, pool));
		
		//annotations: there might be none, all with language
		for (AnnotationBean annotationBean : bean.getAnnotations()) {
			
			QName name = annotationBean.getId()==null?
					annotationBean.getTitle()==null?directives.get(NAME):q(annotationBean.getTitle()):
					q(annotationBean.getId());

			QName type = annotationBean.getType()==null?Constants.DEFAULT_TYPE:q(annotationBean.getType());
			
			for (TextTypeWrapper annotation :  annotationBean.getText()) // create an attribute for each annotation text piece
				attributes.add(attributeFor(name,type, annotation, pool));
			
		}
		
		return attributes;
		
	}
	
	
	@Override
	public String toString() {
		return "sdmx-2-codelist";
	}

}
