/**
 * 
 */
package org.cotrix.web.common.gen;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cotrix.web.common.client.ext.CotrixExtension;

import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.ConfigurationProperty;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.PropertyOracle;
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
public class CotrixExtensionProviderGenerator extends Generator {

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
			composer.addImport(CotrixExtension.class.getName());
			composer.addImport(List.class.getName());
			composer.addImport(ArrayList.class.getName());
			composer.addImport(Collections.class.getName());
			
			SourceWriter src = composer.createSourceWriter(context, printWriter);
			
			List<String> extensions = getExtensions(context.getPropertyOracle());
			src.println("public List<CotrixExtension> getExtensions() {");
			src.indent();
			if (extensions.isEmpty()) src.println("return Collections.emptyList();");
			else {
				src.println("List<CotrixExtension> extensions = new ArrayList<CotrixExtension>("+extensions.size()+");");
				for (String extension:extensions) {
					src.println("extensions.add(new "+extension+"());");
				}
				src.println("return extensions;");

			}
			src.outdent();
			src.println("}");

			src.commit(logger);
			return typeName + "Generated";

		} catch (NotFoundException e) {
			e.printStackTrace();
			throw new UnableToCompleteException();
		}
	}

	private List<String> getExtensions(PropertyOracle propertyOracle) {

		try {
			ConfigurationProperty propertyValue = propertyOracle.getConfigurationProperty("extensionModule");
			return propertyValue.getValues();
		} catch (BadPropertyValueException e) {
			//We silent it
			return Collections.emptyList();
		}
	}

}
