package org.cotrix.io.sdmx.map;

import static org.cotrix.common.Log.*;
import static org.cotrix.common.Report.*;
import static org.cotrix.common.Report.Item.Type.*;
import static org.sdmxsource.sdmx.api.constants.TERTIARY_BOOL.*;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import org.cotrix.common.Report;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.dsl.Codes;
import org.cotrix.domain.links.AttributeLink;
import org.cotrix.domain.links.LinkValueType;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.utils.AttributeTemplate;
import org.cotrix.io.impl.MapTask;
import org.cotrix.io.sdmx.SdmxElement;
import org.cotrix.io.sdmx.map.Codelist2SdmxDirectives.GetClause;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.sdmx.api.model.mutable.base.AnnotationMutableBean;
import org.sdmxsource.sdmx.api.model.mutable.base.NameableMutableBean;
import org.sdmxsource.sdmx.api.model.mutable.codelist.CodeMutableBean;
import org.sdmxsource.sdmx.api.model.mutable.codelist.CodelistMutableBean;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.base.AnnotationMutableBeanImpl;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.codelist.CodeMutableBeanImpl;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.codelist.CodelistMutableBeanImpl;
import org.sdmxsource.sdmx.util.date.DateUtil;

/**
 * A transformation from {@link Codelist} to {@link CodelistBean}.
 * 
 * @author Fabio Simeoni
 *
 */
public class Codelist2Sdmx implements MapTask<Codelist,CodelistBean,Codelist2SdmxDirectives> {

	@Override
	public Class<Codelist2SdmxDirectives> directedBy() {
		return Codelist2SdmxDirectives.class;
	}
	
	/**
	 * Applies the transformation to a given {@link Codelist} with given directives.
	 * @param codelist the codelist
	 * @params directives the directives
	 * @return the result of the transformation
	 * @throws Exception if the given codelist cannot be transformed
	 */
	public CodelistBean map(Codelist codelist, Codelist2SdmxDirectives directives) throws Exception {
		
		double time = System.currentTimeMillis();

		report().log(item("transforming codelist "+codelist.qname()+"("+codelist.id()+") to SDMX")).as(INFO)
				.log(item(Calendar.getInstance().getTime().toString())).as(INFO);
		
		String name = directives.name()==null?codelist.qname().getLocalPart():directives.name();
		
		CodelistMutableBean codelistbean = new CodelistMutableBeanImpl();
		
		codelistbean.setAgencyId(directives.agency());
		codelistbean.setId(name);
		codelistbean.setVersion(directives.version()==null?codelist.version():directives.version());
		codelistbean.setFinalStructure(directives.isFinal()==null?UNSET:directives.isFinal()==true?TRUE:FALSE);
		
		
		mapCodelistAttributes(codelist,codelistbean,directives);
		 
		for (Code code : codelist.codes()) {
			
			CodeMutableBean codebean = new CodeMutableBeanImpl();
			
			if (code.qname()==null) {
				report().log(item(code.id()+" has no name ")).as(ERROR);
				continue;
			}
			
			codebean.setId(code.qname().getLocalPart());
			
			mapAttributes(code,codebean,directives.forCodes());
			
			mapCodelinks(code, codebean, directives.forCodes());
			
			codelistbean.addItem(codebean);
		}
		
		String msg = "transformed codelist "+codelist.qname()+"("+codelist.id()+") to SDMX in "+(System.currentTimeMillis()-time)/1000;
		
		report().log(item(msg)).as(INFO);

		return codelistbean.getImmutableInstance();

	}
	
	//helpers
	
	
	private void mapCodelistAttributes(Codelist list, CodelistMutableBean bean, Codelist2SdmxDirectives directives) {
		
		//name-based pass
		for (Attribute a : list.attributes()) {
			
			String val = a.value();
			SdmxElement element = directives.forCodelist().get(a);
			
			if (element!=null)
				switch(element) {
				
					case VALID_FROM:
						try {
							bean.setStartDate(DateUtil.getDateTimeFormat().parse(val));
						}
						catch(ParseException e) {
							Report.report().log(item("unparseable start date: "+a.value())).as(WARN);
						}
						break;
					case VALID_TO:
						try {
							bean.setEndDate(DateUtil.getDateTimeFormat().parse(val));
						}
						catch(ParseException e) {
							Report.report().log(item("unparseable end date: "+a.value())).as(WARN);
						}
						break;
					
					default:
				}
		}

		mapAttributes(list, bean, directives.forCodelist());
		
	}
	
	private <T extends Attributed & Named> void mapAttributes(T attributed, NameableMutableBean bean, GetClause directives) {
		
		boolean hasName = false;
		
		for (Attribute a : attributed.attributes()) {
			
			String val = a.value();
			String lang = a.language()==null?"en":a.language();
			
			SdmxElement element = directives.get(a);
			
			if (element!=null)
				switch(element) {
				
					case NAME:
						bean.addName(lang,val);
						hasName = true;
						break;
					case DESCRIPTION:
						bean.addDescription(lang,val);
						break;
					case ANNOTATION:
						AnnotationMutableBean annotation = new AnnotationMutableBeanImpl();
						annotation.setTitle(a.qname().getLocalPart());
						annotation.addText(lang, val);
						bean.addAnnotation(annotation);		
						break;
						
					default:
						
				}
			
			
		}
		
		if (!hasName)
			bean.addName("en",attributed.qname().getLocalPart());
			
	}
	
	private void mapCodelinks(Code code, NameableMutableBean bean, GetClause directives) {
		
		boolean hasName = false;
		
		for (Codelink link : code.links()) {
			
			LinkValueType type = link.definition().valueType();
			
			if (!(type instanceof AttributeLink))
				continue;
			
			AttributeTemplate template = AttributeLink.class.cast(type).template();
			
			Attribute a = Codes.attribute().name(link.qname()).ofType(template.type()).in(template.language()).build();

			String val = valueOf(link);
			String lang = a.language()==null?"en":a.language();
			
			SdmxElement element = directives.get(a);
			
			if (element!=null)
				switch(element) {
				
					case NAME:
						bean.addName(lang,val);
						hasName = true;
						break;
					case DESCRIPTION:
						bean.addDescription(lang,val);
						break;
					case ANNOTATION:
						AnnotationMutableBean annotation = new AnnotationMutableBeanImpl();
						annotation.setTitle(a.qname().getLocalPart());
						annotation.addText(lang, val);
						bean.addAnnotation(annotation);		
						break;
						
					default:
						
				}
			
			
		}
		
		if (!hasName)
			bean.addName("en",code.qname().getLocalPart());
			
	}
	
	private String valueOf(Codelink l) {
		
		List<Object> linkval = l.value();
		return linkval.isEmpty()? null:
							 	linkval.size()==1? linkval.get(0).toString() :
							 					   linkval.toString();
	}
	
	@Override
	public String toString() {
		return "codelist-2-sdmx";
	}
}
