package org.cotrix.domain.codelist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.CommonDefinition;
import org.cotrix.domain.common.BeanContainer;
import org.cotrix.domain.common.Container;
import org.cotrix.domain.common.Status;
import org.cotrix.domain.links.Link;
import org.cotrix.domain.memory.MBeanContainer;
import org.cotrix.domain.memory.MLink;
import org.cotrix.domain.trait.Described;
import org.cotrix.domain.trait.BeanOf;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;


public interface Code extends Identified,Named,Described {
	
	Container<? extends Link> links();
	
	List<Attribute> markers();
	
	
	interface Bean extends Described.Bean, BeanOf<Private> {
		
		BeanContainer<Link.Bean> links();
		
	}
	
	
	//private logic
	
	final class Private extends Described.Private<Private,Bean> implements Code {

		public Private(Code.Bean state) {
			super(state);
		}
		
		
		@Override
		public Container.Private<Link.Private,Link.Bean> links() {
			
			return new Container.Private<>(bean().links());
			
		}
		
		@Override
		public List<Attribute> markers() {
			
			List<Attribute> mks = new ArrayList<>();
			
			for (CommonDefinition def : CommonDefinition.markers()) {
				
				Attribute mk = attributes().getFirst(def);
				
				if (mk!=null)
					mks.add(mk);
			}
			
			return mks;
				
		
		}
		
		@Override
		public void update(Code.Private changeset) throws IllegalArgumentException, IllegalStateException {
			
			super.update(changeset);
			
			//update links under "group semantics"
			updateLinks(changeset);
			
		}

		
		//group semantics: links with same target are all 'redirected' to new targets
		//a) check deltas respect this semantics
		//b) adds more deltas to maintain it
		//simplest approach is two-phase: 
		//1) perform normal update (verifying a))
		//2) perform second update to align other group members 
		private void updateLinks(Code.Private changeset) {
			
			//redirected: links that change targets
			Map<String,Code.Bean> redirected = new HashMap<>();
			
			for (Link.Bean change : changeset.bean().links())
				if (change.target()!=null)
					redirected.put(change.id(),change.target());
			
			//opt out early if no links are redirected
			if (redirected.isEmpty()) {
				links().update(changeset.links());
				return;
			}
			
			//use redirected to remember target changes for phase 2)
			Map<String,Code.Bean> targetUpdates = new HashMap<>();
			
			for (Link.Bean link : bean().links()) {
				
				if (!redirected.containsKey(link.id()))
					continue;
				
				Code.Bean update = redirected.get(link.id());
				String current = link.target().id();
				Code.Bean knownUpdate = targetUpdates.get(current);
				
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
			MBeanContainer<Link.Bean>  changes = new MBeanContainer<>();
			
			//redirect those that still need to
			for (Link.Bean link : bean().links()) {
				String target = link.target().id();
				if (targetUpdates.containsKey(target)) {
					MLink change = new MLink(link.id(),Status.MODIFIED);
					change.target(targetUpdates.get(target));
					changes.add(change);
				}
			}
			
			//compensation update
			links().update(new Container.Private<>(changes));
		}
				
		
		@Override
		public String toString() {
			return "Code [id="+id()+", name=" + qname() + ", attributes=" + attributes()+ ", links=" + links()+"]" ;
		}

	}
	
	
	
}