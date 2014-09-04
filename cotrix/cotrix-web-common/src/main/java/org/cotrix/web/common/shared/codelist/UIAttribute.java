package org.cotrix.web.common.shared.codelist;

import java.util.Set;

import org.cotrix.web.common.shared.Language;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UIAttribute implements Identifiable, IsSerializable {
	
	private String id;
	private UIQName name;
	private UIQName type;
	private String note;
	private String value;
	private Language language;
	private String definitionId;
	private Set<UIFacet> facets;
	
	public UIQName getName() {
		return name;
	}
	
	public void setName(UIQName name) {
		this.name = name;
	}
	
	public UIQName getType() {
		return type;
	}
	
	public void setType(UIQName type) {
		this.type = type;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public Language getLanguage() {
		return language;
	}
	
	public void setLanguage(Language language) {
		this.language = language;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getDefinitionId() {
		return definitionId;
	}

	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Set<UIFacet> getFacets() {
		return facets;
	}

	public void setFacets(Set<UIFacet> facets) {
		this.facets = facets;
	}
	
	public boolean is(UIFacet facet) {
		return facets!=null && facets.contains(facet);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UIAttribute other = (UIAttribute) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UIAttribute [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", type=");
		builder.append(type);
		builder.append(", note=");
		builder.append(note);
		builder.append(", value=");
		builder.append(value);
		builder.append(", language=");
		builder.append(language);
		builder.append(", definitionId=");
		builder.append(definitionId);
		builder.append(", facets=");
		builder.append(facets);
		builder.append("]");
		return builder.toString();
	}
}
