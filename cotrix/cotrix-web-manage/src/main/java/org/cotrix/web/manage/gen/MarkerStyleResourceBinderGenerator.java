/**
 * 
 */
package org.cotrix.web.manage.gen;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.cotrix.web.manage.client.codelist.codes.marker.MarkerType;
import org.cotrix.web.manage.client.codelist.codes.marker.style.MarkerStyle;
import org.cotrix.web.manage.client.codelist.codes.marker.style.MarkerStyleResource.Style;
import org.cotrix.web.manage.client.codelist.codes.marker.style.MarkerStyleResource.StyleElement;
import org.cotrix.web.manage.client.codelist.codes.marker.style.MarkerStyleResourceBinder;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MarkerStyleResourceBinderGenerator extends Generator {

	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {

		try {
			JClassType classType = context.getTypeOracle().getType(typeName);
			JClassType targetType = getTargetType(classType, context.getTypeOracle());
			
			String packageName = classType.getPackage().getName();
			String simpleName = getSimpleGeneratedClassName(classType);
			PrintWriter printWriter = context.tryCreate(logger, packageName, simpleName);

			if (printWriter == null) return getFullyQualifiedGeneratedClassName(classType);

			ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(packageName, simpleName);
			composer.addImplementedInterface(classType.getName());

			// Need to add whatever imports your generated class needs. 
			composer.addImport(targetType.getQualifiedSourceName());
			composer.addImport(MarkerType.class.getName());
			composer.addImport(EnumMap.class.getName());
			composer.addImport(MarkerStyle.class.getName());

			SourceWriter src = composer.createSourceWriter(context, printWriter);

			Map<MarkerType, Map<StyleElement, JMethod>> markersElements = getResourceAnnotations(targetType);
			Map<StyleElement, JMethod> markerStyleElements = getElementStyleAnnotations(context.getTypeOracle().getType(MarkerStyle.class.getCanonicalName()));

			
			src.println("@Override");
			src.println("public EnumMap<MarkerType, MarkerStyle> bind(final "+targetType.getName()+" resource) {");
			src.indent();

			src.println("EnumMap<MarkerType, MarkerStyle> styles = new EnumMap<MarkerType, MarkerStyle>(MarkerType.class);");

			for (Entry<MarkerType, Map<StyleElement, JMethod>> entry:markersElements.entrySet()) {
				src.println("styles.put(MarkerType."+entry.getKey()+", new MarkerStyle() {");
				src.indent();
				Map<StyleElement, JMethod> markerElements = entry.getValue();
				for (Entry<StyleElement, JMethod> markerStyleElement:markerStyleElements.entrySet()) {
					JMethod toImplement = markerStyleElement.getValue();
					JMethod toCall = markerElements.get(markerStyleElement.getKey());
					if (toCall == null) throw new RuntimeException("The MarkerStyle interface declare method "+toImplement.getName()+" annotated with element "+markerStyleElement.getKey()+" but the element style is not present in the resource");
					src.println("public "+toImplement.getReturnType().getSimpleSourceName()+" "+toImplement.getName()+"() {");
					src.indent();
					src.println("return resource."+toCall.getName()+"();");
					src.outdent();
					src.println("}");
				}
				
				src.outdent();
				src.println("});");
			}

			src.println("return styles;");

			src.outdent();
			src.println("}");

			src.commit(logger);
			return getFullyQualifiedGeneratedClassName(classType);

		} catch (NotFoundException e) {
			e.printStackTrace();
			throw new UnableToCompleteException();
		}
	}

	private Map<MarkerType, Map<StyleElement, JMethod>> getResourceAnnotations(JClassType type) {

		Map<MarkerType, Map<StyleElement, JMethod>> elements = new EnumMap<>(MarkerType.class);
		for (JMethod method : type.getInheritableMethods()) {
			for (Annotation annotation:method.getAnnotations()) {
				if (annotation.annotationType().isAnnotationPresent(Style.class)) {
					MarkerType markerType = getMarkerType(annotation);
				
					Map<StyleElement, JMethod> markerElements = elements.get(markerType);
					if (markerElements == null) {
						markerElements = new HashMap<>();
						elements.put(markerType, markerElements);
					}

					JMethod oldMethod = markerElements.put(annotation.annotationType().getAnnotation(Style.class).value(), method);
					if (oldMethod!=null) throw new RuntimeException("Two annotation of type "+annotation.annotationType().getName()+" with MarkerType "+markerType);
				}
			}
		}
		return elements;
	}

	private MarkerType getMarkerType(Annotation a) {
		try {
			return MarkerType.class.cast(a.getClass().getMethods()[0].invoke(a));
		} catch (Exception e) {
			throw new RuntimeException("Failed getting annotation value", e);
		}
	}

	private Map<StyleElement, JMethod> getElementStyleAnnotations(JClassType type) {
		Map<StyleElement, JMethod> elements = new EnumMap<>(StyleElement.class);

		for (JMethod method : type.getInheritableMethods()) {
			Style style = method.getAnnotation(Style.class);
			if (style == null) throw new RuntimeException("The method "+method.getName()+" is missing the style annotation");
			JMethod oldMethod = elements.put(style.value(), method);
			if (oldMethod!=null) throw new RuntimeException("Two annotation for element "+style.value()+" found in MarkerStyle");
		}

		return elements;
	}

	private JClassType getTargetType(JClassType interfaceType, TypeOracle typeOracle) {
		JClassType[] superTypes = interfaceType.getImplementedInterfaces();
		JClassType markerStyleProviderType = typeOracle.findType(MarkerStyleResourceBinder.class.getCanonicalName());
		if (superTypes.length != 1
				|| !superTypes[0].isAssignableFrom(markerStyleProviderType)
				|| superTypes[0].isParameterized() == null) {
			throw new IllegalArgumentException(
					interfaceType + " must extend MarkerStyleResourceBinder with a type parameter");
		}
		return superTypes[0].isParameterized().getTypeArgs()[0];
	}
	
	private String getSimpleGeneratedClassName(JClassType eventBinderType) {
	    return eventBinderType.getName().replace('.', '_') + "Impl";
	  }

	  private String getFullyQualifiedGeneratedClassName(JClassType markerStyleProviderType) {
	    return new StringBuilder()
	        .append(markerStyleProviderType.getPackage().getName())
	        .append('.')
	        .append(getSimpleGeneratedClassName(markerStyleProviderType))
	        .toString();
	  }

}
