package org.cotrix.io.sdmx;

import static org.sdmxsource.sdmx.api.constants.TERTIARY_BOOL.*;

import java.text.ParseException;

import org.cotrix.common.Report;
import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Named;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.sdmx.api.model.mutable.base.AnnotationMutableBean;
import org.sdmxsource.sdmx.api.model.mutable.base.NameableMutableBean;
import org.sdmxsource.sdmx.api.model.mutable.codelist.CodeMutableBean;
import org.sdmxsource.sdmx.api.model.mutable.codelist.CodelistMutableBean;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.base.AnnotationMutableBeanImpl;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.codelist.CodeMutableBeanImpl;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.codelist.CodelistMutableBeanImpl;
import org.sdmxsource.sdmx.util.date.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A transformation from {@link Codelist} to {@link CodelistBean}.
 * 
 * @author Fabio Simeoni
 *
 */
public class Codelist2Sdmx {

	private static Logger log = LoggerFactory.getLogger(Codelist2Sdmx.class);
	
	/**
	 * Applies the transformation to a given {@link Codelist} with given directives.
	 * @param codelist the codelist
	 * @params directives the directives
	 * @return the result of the transformation
	 * @throws Exception if the given codelist cannot be transformed
	 */
	public CodelistBean apply(Codelist codelist, SdmxPublishDirectives directives) throws Exception {
		
		log.trace("transforming codelist "+codelist.id()+" to sdmx");
		
		CodelistMutableBean codelistbean = new CodelistMutableBeanImpl();
		
		codelistbean.setAgencyId(directives.agency());
		codelistbean.setId(directives.name()==null?codelist.name().getLocalPart():directives.name());
		codelistbean.setVersion(directives.version()==null?codelist.version():directives.version());
		codelistbean.setFinalStructure(directives.isFinal()==null?UNSET:directives.isFinal()==true?TRUE:FALSE);
		
		
		mapCodelistAttributes(codelist,codelistbean,directives);
		 
		for (Code code : codelist.codes()) {
			
			CodeMutableBean codebean = new CodeMutableBeanImpl();
			codebean.setId(code.name().getLocalPart());
			
			mapAttributes(code,codebean,directives);
			
			codelistbean.addItem(codebean);
		}
		
		return codelistbean.getImmutableInstance();
	}
	
	//helpers
	
	
	private void mapCodelistAttributes(Codelist list, CodelistMutableBean bean, SdmxPublishDirectives directives) {
		
		//name-based pass
		for (Attribute a : list.attributes()) {
			

			String val = a.value();
			SdmxElement element = directives.get(a.name());
			
			if (element!=null)
				switch(element) {
				
					case VALID_FROM:
						try {
							bean.setStartDate(DateUtil.getDateTimeFormat().parse(val));
						}
						catch(ParseException e) {
							Report.report().logWarning("unparseable start date: "+a.value());
						}
						break;
					case VALID_TO:
						try {
							bean.setEndDate(DateUtil.getDateTimeFormat().parse(val));
						}
						catch(ParseException e) {
							Report.report().logWarning("unparseable end date: "+a.value());
						}
						break;
				}
		}

		mapAttributes(list, bean, directives);
		
	}
	
	private <T extends Attributed & Named> void mapAttributes(T attributed, NameableMutableBean bean, SdmxPublishDirectives directives) {
		
		boolean hasName = false;
		
		for (Attribute a : attributed.attributes()) {
			

			String val = a.value();
			String lang = a.language()==null?"en":a.language();
			
			SdmxElement element = directives.get(a.type());
			
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
						annotation.setTitle(a.name().getLocalPart());
						annotation.addText(lang, val);
						bean.addAnnotation(annotation);		
						break;
						
				}
		}
		
		if (!hasName)
			bean.addName("en",attributed.name().getLocalPart());
			
	}
}
