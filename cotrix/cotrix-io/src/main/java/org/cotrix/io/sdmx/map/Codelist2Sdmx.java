package org.cotrix.io.sdmx.map;

import static org.cotrix.common.Report.*;
import static org.sdmxsource.sdmx.api.constants.TERTIARY_BOOL.*;

import java.text.ParseException;
import java.util.Calendar;

import org.cotrix.common.Report;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Named;
import org.cotrix.io.impl.MapTask;
import org.cotrix.io.sdmx.SdmxElement;
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

		report().log("mapping codelist "+codelist.name()+"("+codelist.id()+") to SDMX");
		report().log(Calendar.getInstance().getTime().toString());
		
		String name = directives.name()==null?codelist.name().getLocalPart():directives.name();
		
		CodelistMutableBean codelistbean = new CodelistMutableBeanImpl();
		
		codelistbean.setAgencyId(directives.agency());
		codelistbean.setId(name);
		codelistbean.setVersion(directives.version()==null?codelist.version():directives.version());
		codelistbean.setFinalStructure(directives.isFinal()==null?UNSET:directives.isFinal()==true?TRUE:FALSE);
		
		
		mapCodelistAttributes(codelist,codelistbean,directives);
		 
		for (Code code : codelist.codes()) {
			
			CodeMutableBean codebean = new CodeMutableBeanImpl();
			codebean.setId(code.name().getLocalPart());
			
			mapAttributes(code,codebean,directives);
			
			codelistbean.addItem(codebean);
		}
		
		report().log("mapped codelist "+codelist.name()+"("+codelist.id()+") to SDMX in "+(System.currentTimeMillis()-time)/1000);

		return codelistbean.getImmutableInstance();
	}
	
	//helpers
	
	
	private void mapCodelistAttributes(Codelist list, CodelistMutableBean bean, Codelist2SdmxDirectives directives) {
		
		//name-based pass
		for (Attribute a : list.attributes()) {
			
			String val = a.value();
			SdmxElement element = directives.get(a);
			
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
					
					default:
				}
		}

		mapAttributes(list, bean, directives);
		
	}
	
	private <T extends Attributed & Named> void mapAttributes(T attributed, NameableMutableBean bean, Codelist2SdmxDirectives directives) {
		
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
						annotation.setTitle(a.name().getLocalPart());
						annotation.addText(lang, val);
						bean.addAnnotation(annotation);		
						break;
						
					default:
						
				}
		}
		
		if (!hasName)
			bean.addName("en",attributed.name().getLocalPart());
			
	}
	
	@Override
	public String toString() {
		return "codelist-2-sdmx";
	}
}
