package org.cotrix.io.sdmx;

import static org.cotrix.common.Report.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.io.sdmx.SdmxElement.*;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.utils.Constants;
import org.sdmxsource.sdmx.api.model.beans.base.AnnotationBean;
import org.sdmxsource.sdmx.api.model.beans.base.NameableBean;
import org.sdmxsource.sdmx.api.model.beans.base.TextTypeWrapper;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodeBean;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;

public class Sdmx2Codelist {

	public Codelist apply(CodelistBean listBean, SdmxMapDirectives directives) throws Exception {
		
		report().log("importing codelist '"+listBean.getId()+"'");
		report().log("==============================");
		
		List<Code> codes = new ArrayList<Code>();
		
		for (CodeBean codeBean : listBean.getItems())
			codes.add(code().
						name(codeBean.getId()).
						attributes(attributesOf(codeBean,directives)).
						build());
		
		Codelist codelist = codelist().
				name(directives.name()==null?new QName(listBean.getId()):directives.name()).
				with(codes).
				attributes(attributesOf(listBean,directives)).
				version(directives.version()==null?listBean.getVersion():directives.version()).
				build();
		
		report().log("==============================");
		report().log("terminated import of codelist '"+codelist.name()+"' with "+codelist.codes().size()+" codes.");
		
		return codelist;
	}
	
	
	
	
	
	//helpers
	
	
	private List<Attribute> attributesOf(CodelistBean list, SdmxMapDirectives directives) {
		
		List<Attribute> attributes = new ArrayList<Attribute>();
		
		if (directives.isIncluded(FINAL))
			attributes.add(attr().
					        name(directives.get(FINAL)).
					        value(String.valueOf(list.isFinal().isTrue())).
					        build());
		
		if (directives.isIncluded(AGENCY))
			attributes.add(	attr().
								name(directives.get(AGENCY)).
								value(list.getAgencyId()).
								build());
		
		if (directives.isIncluded(VALID_FROM) && list.getStartDate()!=null)
			attributes.add(attr().
							name(directives.get(VALID_FROM)).
							value(list.getStartDate().getDateInSdmxFormat()).
							build());
	
		
		if (directives.isIncluded(VALID_TO) && list.getEndDate()!=null)
			attributes.add(attr().
						name(directives.get(VALID_TO)).
						value(list.getEndDate().getDateInSdmxFormat())
						.build());
	   	
		if (directives.isIncluded(URI) && list.getUri()!=null)
			attributes.add(attr().
					name(directives.get(URI)).
					value(list.getUri().toString())
					.build());
		
		mapCommonAttributes(list,attributes,directives);
		
		return attributes;
	}
	
	private List<Attribute> attributesOf(CodeBean bean, SdmxMapDirectives directives) {
		
		List<Attribute> attributes = new ArrayList<Attribute>();
		
		mapCommonAttributes(bean, attributes,directives);
		
		return attributes;
	}
	
	private void mapCommonAttributes(NameableBean bean, List<Attribute> attributes,SdmxMapDirectives directives) {
		
		//names: at least one, all with language
		for (TextTypeWrapper name : bean.getNames())
			attributes.add(attr().
							 name(directives.get(NAME)).
							 value(name.getValue()).
							 ofType(Constants.NAME).
							 in(name.getLocale())
							 .build());
		
		//descriptions: might be none, all with language
		for (TextTypeWrapper description :  bean.getDescriptions()) 
			attributes.add(attr().
							name(directives.get(DESCRIPTION)).
							value(description.getValue()).
							ofType(Constants.DEFAULT_TYPE).
							in(description.getLocale()).
							build());
		
		//annotations: there might be none, all with language
		for (AnnotationBean annotationBean : bean.getAnnotations()) {
			
			QName name = annotationBean.getId()==null?
					annotationBean.getTitle()==null?directives.get(NAME):q(annotationBean.getTitle()):
					q(annotationBean.getId());

			QName type = annotationBean.getType()==null?Constants.DEFAULT_TYPE:q(annotationBean.getType());
			
			for (TextTypeWrapper annotation :  annotationBean.getText()) // create an attribute for each annotation text piece
				attributes.add(attr().
						        name(name).
						        value(annotation.getValue()).
						        ofType(type).
						        in(annotation.getLocale())
						        .build());
			
		}
				
	}

}
