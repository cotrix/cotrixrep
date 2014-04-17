package org.cotrix.domain.codelist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.links.ValueFunction;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.EntityProvider;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;

/**
 * An {@link Identified} and {@link Attributed} instance of a
 * {@link CodelistLink}.
 * 
 * @author Fabio Simeoni
 * 
 */
public interface Codelink extends Identified, Attributed, Named {

	/**
	 * Returns the type of this link.
	 * 
	 * @return the type
	 */
	CodelistLink type();

	/**
	 * Returns the value of this link.
	 * 
	 * @return the link value, or <code>null</code> if the link is orphaned.
	 */
	Collection<Object> value();
	
	
	/**
	 * Returns the target of this link.
	 * @return the target, or <code>null</code> if the link is orphaned.
	 */
	Code target();
	
	

	static interface State extends Identified.State, Attributed.State, Named.State, EntityProvider<Private> {

		CodelistLink.State type();

		void type(CodelistLink.State state);

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
		public QName name() {
			//safe: type cannot be or become null
			return state().type().name();
		}

		@Override
		public Collection<Object> value() {

			return resolve(this.state(), this.type().state(),new ArrayList<String>());

		}
		
		@Override
		public Code target() {
			return new Code.Private(state().target());
		}

		@Override
		public CodelistLink.Private type() {
			return new CodelistLink.Private(state().type());
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
		public static Collection<Object> resolve(Codelink.State link, CodelistLink.State type, List<String> ids) {

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
			
				Collection<Object> values = type.valueType().valueIn(link.id(),link.target(),ids);
				
				Collection<Object> results = new ArrayList<>();
				
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
