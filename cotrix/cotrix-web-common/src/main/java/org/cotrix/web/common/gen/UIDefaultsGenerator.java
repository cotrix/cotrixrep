/**
 * 
 */
package org.cotrix.web.common.gen;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.common.Range;
import org.cotrix.domain.utils.DomainConstants;
import org.cotrix.web.common.client.factory.UIDefaults;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIRange;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UIDefaultsGenerator extends Generator {

	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
		
		try {
			JClassType classType = context.getTypeOracle().getType(typeName);
			String packageName = classType.getPackage().getName();
			String simpleName = classType.getSimpleSourceName() + "Generated";
			PrintWriter printWriter = context.tryCreate(logger, packageName, simpleName);
			String name = typeName + "Generated";
			
			if (printWriter == null) return name;
			
			ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(packageName, simpleName);
			composer.addImplementedInterface(classType.getName());

			// Need to add whatever imports your generated class needs. 
			composer.addImport(UIDefaults.class.getName());
			composer.addImport(UIQName.class.getName());
			composer.addImport(UIRange.class.getName());
			composer.addImport(List.class.getName());
			composer.addImport(ArrayList.class.getName());
			
			SourceWriter src = composer.createSourceWriter(context, printWriter);
			
			//DEFAULT TYPE
			src.println("public UIQName defaultType() {");
			QName defaultType = DomainConstants.defaultType;
			src.indent();
			src.println("return new UIQName(\""+defaultType.getNamespaceURI()+"\",\""+defaultType.getLocalPart()+"\");");
			src.outdent();
			src.println("}");
			
			//DEFAULT TYPES
			src.println("public List<UIQName> defaultTypes() {");
			QName[] defaultTypes = DomainConstants.DEFAULT_TYPES;
			src.indent();
			
			src.println("List<UIQName> types = new ArrayList<UIQName>();");
			for (QName type:defaultTypes)	src.println("types.add(new UIQName(\""+type.getNamespaceURI()+"\",\""+type.getLocalPart()+"\"));");
			src.println("return types;");
			
			src.outdent();
			src.println("}");
			
			//DEFAULT RANGE
			src.println("public UIRange defaultRange() {");
			Range defaultRange = DomainConstants.defaultRange;
			src.indent();
			src.println("return new UIRange("+defaultRange.min()+","+defaultRange.max()+");");
			src.outdent();
			src.println("}");
			
			//DEFAULT VALUE
			src.println("public String defaultValue() {");
			String defaultValue = DomainConstants.defaultValueType.defaultValue();
			src.indent();
			if (defaultValue == null) src.println("return null;");
			else src.println("return \""+defaultValue+"\";");
			src.outdent();
			src.println("}");
			
			//DEFAULT NAMESPACE
			src.println("public String defaultNameSpace() {");
			String defaultNameSpace = DomainConstants.NS;
			src.indent();
			src.println("return \""+defaultNameSpace+"\";");
			src.outdent();
			src.println("}");
			
			//public UIQName systemType();
			
			//SYSTEM TYPE
			src.println("public UIQName systemType() {");
			QName systemType = DomainConstants.SYSTEM_TYPE;
			src.indent();
			src.println("return new UIQName(\""+systemType.getNamespaceURI()+"\",\""+systemType.getLocalPart()+"\");");
			src.outdent();
			src.println("}");

			src.commit(logger);
			return typeName + "Generated";

		} catch (NotFoundException e) {
			e.printStackTrace();
			throw new UnableToCompleteException();
		}
	}

}
