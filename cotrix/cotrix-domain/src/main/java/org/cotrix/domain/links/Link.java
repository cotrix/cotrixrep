package org.cotrix.domain.links;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.trait.Described;
import org.cotrix.domain.trait.BeanOf;
import org.cotrix.domain.trait.Defined;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.values.ValueFunction;

public interface Link extends Identified, Named, Described, Defined<LinkDefinition> {

	Code target(); //null if orphaned
	
	List<Object> value();
	
	String valueAsString();
	
	
	//----------------------------------------
	
	static interface Bean extends Described.Bean, Defined.Bean<LinkDefinition.Bean>, BeanOf<Private> {

		Code.Bean target();

		void target(Code.Bean code);

	}

	
	//----------------------------------------
	
	public class Private extends Described.Private<Private, Bean> implements Link {

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
		
		
		@Override
		public String toString() {
			return "Codelink [id="+id()+", definition=" + bean().definition() + " :--> " + bean().target().id()+"]" ;
		}

	}
	
}
