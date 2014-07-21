/**
 * 
 */
package org.cotrix.web.manage.gen;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.cotrix.domain.validation.Validator;
import org.cotrix.domain.validation.Validators;
import org.cotrix.web.manage.client.codelist.metadata.attributedefinition.constraint.MetaConstraint;

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
public class MetaConstraintProviderGenerator extends Generator {

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
			composer.addImport(MetaConstraint.class.getName());
			composer.addImport(List.class.getName());
			composer.addImport(ArrayList.class.getName());
			composer.addImport(Arrays.class.getName());
			composer.addImport(Collections.class.getName());
			
			SourceWriter src = composer.createSourceWriter(context, printWriter);
			
			List<MetaConstraint> metaConstraints = getMetaConstraints();
			src.println("public List<MetaConstraint> getMetaConstraints() {");
			src.indent();
			if (metaConstraints.isEmpty()) src.println("return Collections.emptyList();");
			else {
				src.println("List<MetaConstraint> metaConstraints = new ArrayList<MetaConstraint>("+metaConstraints.size()+");");
				for (MetaConstraint metaConstraint:metaConstraints) {
					src.print("metaConstraints.add(new MetaConstraint(\""+metaConstraint.getName()+"\", Arrays.<String>asList(new String[]{");
					Iterator<String> argIterator = metaConstraint.getArguments().iterator();
					while(argIterator.hasNext()) {
						src.print("\""+argIterator.next()+"\"");
						if (argIterator.hasNext()) src.print(", ");
					}
					src.println("})));");
				}
				src.println("return metaConstraints;");

			}
			
			Arrays.<String>asList("");
			src.outdent();
			src.println("}");

			src.commit(logger);
			return typeName + "Generated";

		} catch (NotFoundException e) {
			e.printStackTrace();
			throw new UnableToCompleteException();
		}
	}

	private List<MetaConstraint> getMetaConstraints() {
		List<MetaConstraint> metaConstraints = new ArrayList<MetaConstraint>();
		for (Validator validator:Validators.values()) {
			metaConstraints.add(new MetaConstraint(validator.name(), validator.parameterNames()));
		}
		return metaConstraints;
	}

}
