package org.cotrix.domain.codelist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Defined;
import org.cotrix.domain.trait.EntityProvider;
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
public interface Codelink extends Identified, Attributed, Named, Defined<LinkDefinition> {

	/**
	 * Returns the value of this link.
	 * 
	 * @return the link value, or <code>null</code> if the link is orphaned.
	 */
	List<Object> value();
	
	
	String valueAsString();
	
	/**
	 * Returns the target of this link.
	 * @return the target, or <code>null</code> if the link is orphaned.
	 */
	Code target();
	
	

	static interface State extends Identified.State, Attributed.State, Named.State, EntityProvider<Private> {

		LinkDefinition.State type();

		void type(LinkDefinition.State state);

		Code.State target();

		void target(Code.State code);

	}

	/**
	 * An {@link Attributed.Abstract} implementation of {@link Codelink}.
	 * 
	 */
	public class Private extends Attributed.Abstract<Private, State> implements Codelink {

		public Private(Codelink.State state) {
			super(state);
		}
		
		@Override
		public QName qname() {
			//safe: type cannot be or become null
			return state().type().name();
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

			return resolve(this.state(), this.definition().state(),new ArrayList<String>());

		}
		
		@Override
		public Code target() {
			return new Code.Private(state().target());
		}

		@Override
		public LinkDefinition.Private definition() {
			return new LinkDefinition.Private(state().type());
		}

		@Override
		public void update(Private changeset) throws IllegalArgumentException, IllegalStateException {
			
			super.update(changeset);
			
			Code.State newtarget = changeset.state().target();

			if (newtarget != null)
				state().target(newtarget);
			
			// wont'update type
		}

		
		@Override
		public String toString() {
			return "Codelink [id="+id()+", type=" + state().type() + " :--> " + state().target().id()+"]" ;
		}
		
		
		// extracted to reuse below this layer (for link-of-links) without
		// object instantiation costs
		public static List<Object> resolve(Codelink.State link, LinkDefinition.State type, List<String> ids) {

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
