package org.cotrix.io.sdmx;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.utils.Constants;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.sdmx.api.model.mutable.base.AnnotationMutableBean;
import org.sdmxsource.sdmx.api.model.mutable.base.NameableMutableBean;
import org.sdmxsource.sdmx.api.model.mutable.codelist.CodeMutableBean;
import org.sdmxsource.sdmx.api.model.mutable.codelist.CodelistMutableBean;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.base.AnnotationMutableBeanImpl;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.codelist.CodeMutableBeanImpl;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.codelist.CodelistMutableBeanImpl;
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
	 * Applies the transformation to a given {@link Codelist}
	 * @param codelist the codelist
	 * @return the result of the transformation
	 * @throws Exception if the given codelist cannot be transformed
	 */
	public CodelistBean apply(Codelist codelist) throws Exception {
		
		log.trace("transforming codelist "+codelist.id()+" to sdmx");
		
		CodelistMutableBean codelistbean = new CodelistMutableBeanImpl();
		
		codelistbean.setAgencyId("SDMX");
		codelistbean.setId(codelist.name().getLocalPart());
		codelistbean.setVersion(codelist.version());
		 
		mapAttributes(codelist,codelistbean);
		 
		for (Code code : codelist.codes()) {
			
			CodeMutableBean codebean = new CodeMutableBeanImpl();
			codebean.setId(code.name().getLocalPart());
			
			mapAttributes(code,codebean);
			
			codelistbean.addItem(codebean);
		}
		
		return codelistbean.getImmutableInstance();
	}
	
	//helpers
	
	private <T extends Attributed & Named> void mapAttributes(T attributed, NameableMutableBean bean) {
		
		boolean hasName = false;
		
		for (Attribute attribute : attributed.attributes()) {
			boolean language = attribute.language()!=null;
			if (attribute.type()==Constants.NAME) {
				bean.addName(language?attribute.language():"en", attribute.value());
				hasName = true;
			}
			else
				if (language)
					bean.addDescription(attribute.language(),attribute.value());	
				else { 
					AnnotationMutableBean annotation = new AnnotationMutableBeanImpl();
					annotation.setTitle(attribute.name().getLocalPart());
					annotation.addText("en", attribute.value());
					bean.addAnnotation(annotation);					
				}
			}
		
		if (!hasName)
			bean.addName("en",attributed.name().getLocalPart());
			
	}
}
