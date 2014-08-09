package org.cotrix.domain.codelist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Defined;
import org.cotrix.domain.trait.BeanOf;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.values.ValueFunction;

/**
 * An {@link Identified} and {@link Attributed} instance of a
 * {@link LinkDefinition}.
 * 
 * @author Fabio Simeoni
 * 
 */
public interface Link extends Identified, Named, Attributed, Defined<LinkDefinition> {

	/**
	 * @return the link value, or <code>null</code> if the link is orphaned.
	 */
	List<Object> value();
	
	
	String valueAsString();
	
	/**
	 * @return the target, or <code>null</code> if the link is orphaned.
	 */
	Code target();
	
	

	static interface Bean extends Attributed.Bean, BeanOf<Private> {

		LinkDefinition.Bean definition();

		void definition(LinkDefinition.Bean bean);

		Code.Bean target();

		void target(Code.Bean code);

	}

	/**
	 * An {@link Attributed.Private} implementation of {@link Link}.
	 * 
	 */
	public class Private extends Attributed.Private<Private, Bean> implements Link {

		public Private(Link.Bean bean) {
			super(bean);
		}
		
		@Override
		public QName qname() {
			//safe: def cannot be or become null
			
			LinkDefinition.Bean def = bean().definition();
			return def==null? null : def.qname();
		}
		
		@Override
		public String valueAsString() {
			
			List<Object> linkval = value();
			
			if (linkval.isEmpty())
				return null; 
						
			else 
				
				if (linkval.size()==1) {	
				
					Object val = linkval.get(0);
					return val==null?null:val.toString();
				
				}
				else
			     	return linkval.toString();
		}

		@Override
		public List<Object> value() {

			return resolve(this.bean(), this.definition().bean(),new ArrayList<String>());

		}
		
		@Override
		public Code target() {
			return new Code.Private(bean().target());
		}

		@Override
		public LinkDefinition.Private definition() {
			return new LinkDefinition.Private(bean().definition());
		}

		@Override
		public void update(Link.Private changeset) throws IllegalArgumentException, IllegalStateException {
			
			super.update(changeset);
			
			Code.Bean newtarget = changeset.bean().target();

			if (newtarget != null)
				bean().target(newtarget);
			
			// wont'update definition
		}

		
		@Override
		public String toString() {
			return "Codelink [id="+id()+", definition=" + bean().definition() + " :--> " + bean().target().id()+"]" ;
		}
		
		
		// extracted to reuse below this layer (for link-of-links) without
		// object instantiation costs
		public static List<Object> resolve(Link.Bean link, LinkDefinition.Bean type, List<String> ids) {

			if (ids.contains(link.id())) {
				StringBuilder cycle = new StringBuilder();
				for (String id : ids)
					cycle.append(id+"->");
				cycle.append(link.id());
				throw new IllegalStateException("cycle detected:"+cycle);
			}
			
			ids.add(link.id());
			

			// dynamic resolution (includes orphan link case)
			if (link.target() == null)
				return Collections.<Object>emptyList();
			
			else {
			
				List<Object> values = type.valueType().valueIn(link.id(),link.target(),ids);
				
				List<Object> results = new ArrayList<>();
				
				ValueFunction function = type.function();
				
				for (Object value : values) {
					Object result = null;
					if (value instanceof QName) {
						QName name = (QName) value;
						result = new QName(name.getNamespaceURI(),function.apply(name.getLocalPart()));
;					}
					else {
						String val = (String) value;
						result = function.apply(val);
					}
					 
					results.add(result);
					
				}
				
				return results;
			}
				
				
			
		}

	}
	
}
