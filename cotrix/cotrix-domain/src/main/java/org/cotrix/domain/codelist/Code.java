package org.cotrix.domain.codelist;

import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.DomainUtils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.common.NamedContainer;
import org.cotrix.domain.common.NamedStateContainer;
import org.cotrix.domain.memory.CodelinkMS;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.EntityProvider;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.trait.Status;

/**
 * An {@link Identified}, {@link Attributed}, and {@link Named} symbol.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Code extends Identified,Attributed,Named {
	
	//public, read-only interface
	/**
	 * Returns the {@link Codelink}s of this code.
	 * @return the links
	 */
	NamedContainer<? extends Codelink> links();
	
	
	
	//private state interface
	
	interface State extends Identified.State, Named.State, Attributed.State, EntityProvider<Private> {
		
		NamedStateContainer<Codelink.State> links();
		
	}
	
	
	//private logic
	
	final class Private extends Named.Abstract<Private,State> implements Code {

		public Private(Code.State state) {
			super(state);
		}
		
		
		@Override
		public NamedContainer.Private<Codelink.Private,Codelink.State> links() {
			
			return namedContainer(state().links());
			
		}
		
		@Override
		public void update(Private changeset) throws IllegalArgumentException, IllegalStateException {
			
			super.update(changeset);
			
			//update links under "group semantics"
			updateLinks(changeset);
			
			if (changeset.status() == Status.MODIFIED)
				markModifiedIfRequired(state().attributes());
			
		}
		

		
		private void markModifiedIfRequired(NamedStateContainer<Attribute.State> attributes) {
			
			if (attributes.contains(PREVIOUS_VERSION_ID) 
					&&
				!attributes.contains(MODIFIED))
					
				attributes.add(stateof(attribute().with(MODIFIED).value("true")));
				
		}

		
		//group semantics: links with same target are all 'redirected' to new targets
		//a) check deltas respect this semantics
		//b) adds more deltas to maintain it
		//simplest approach is two-phase: 
		//1) perform normal update (verifying a))
		//2) perform second update to align other group members 
		private void updateLinks(Private changeset) {
			
			//redirected: links that change targets
			Map<String,Code.State> redirected = new HashMap<>();
			
			for (Codelink.State change : changeset.state().links())
				if (change.target()!=null)
					redirected.put(change.id(),change.target());
			
			//opt out early if no links are redirected
			if (redirected.isEmpty()) {
				links().update(changeset.links());
				return;
			}
			
			//use redirected to remember target changes for phase 2)
			Map<String,Code.State> targetUpdates = new HashMap<>();
			
			for (Codelink.State link : state().links()) {
				
				if (!redirected.containsKey(link.id()))
					continue;
				
				Code.State update = redirected.get(link.id());
				String current = link.target().id();
				Code.State knownUpdate = targetUpdates.get(current);
				
				if (knownUpdate==null)
					targetUpdates.put(current,update);
				else
					//changeset already inconsistent with group semantics: stop
					if (!knownUpdate.id().equals(update.id()))
						throw new IllegalArgumentException("invalid changeset: two links with the same target must change it consistently");
				
			
			}
			
			//perform normal update
			links().update(changeset.links());
			
			
			//post-process to align remaining group member
			Collection<Codelink.State>  changes = new ArrayList<>();
			
			//redirect those that still need to
			for (Codelink.State link : state().links()) {
				String target = link.target().id();
				if (targetUpdates.containsKey(target)) {
					CodelinkMS change = new CodelinkMS(link.id(),Status.MODIFIED);
					change.target(targetUpdates.get(target));
					changes.add(change);
				}
			}
			
			//compensation update
			links().update(namedContainer(namedBeans(changes)));
		}
				
		
		@Override
		public String toString() {
			return "Code [id="+id()+", name=" + qname() + ", attributes=" + attributes()+ ", links=" + links()+"]" ;
		}

	}
	
	
	
}