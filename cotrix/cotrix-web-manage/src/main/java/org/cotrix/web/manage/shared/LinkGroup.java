/**
 * 
 */
package org.cotrix.web.manage.shared;

import java.util.List;

import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.shared.codelist.Definition;
import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.common.shared.codelist.UILink;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.codelist.linkdefinition.UILinkDefinition;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkGroup implements Comparable<LinkGroup>, Group, HasPosition {
	
	private UILinkDefinition definition;
	private int position;
	
	private SafeHtml label;
	
	protected LinkGroup() {
	}
		
	public LinkGroup(UILinkDefinition definition) {
		this.definition = definition;
	}
	
	@Override
	public Definition getDefinition() {
		return definition;
	}

	public UIQName getName() {
		return definition.getName();
	}
	
	public void calculatePosition(List<UILink> links, UILink link) {
		int position = getPosition(links, link);
		setPosition(position);
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
	
	@Override
	public int getPosition() {
		return position;
	}

	private int getPosition(List<UILink> links, UILink otherLink)
	{
		int index = 0;
		for (UILink link:links) {
			if (accept(link)) {
				if (otherLink.equals(link)) return index;
				index++;
			}
		}
		return -1;
	}
	
	public boolean accept(List<UILink> links, UILink otherLink)
	{
		int index = 0;
		for (UILink link:links) {
			if (accept(link)) {
				if (otherLink.equals(link) && index == position) return true;
				index++;
			}
			if (index>position) return false;
		}
		return false;
	}
	
	public UILink match(List<UILink> links)
	{
		int index = 0;
		for (UILink link:links) {
			if (accept(link)) {
				if (index == position) return link;
				index++;
			}
		}
		return null;
	}
	
	private boolean accept(UILink link)
	{
		if (!definition.getId().equals(link.getDefinitionId())) return false;
		return true;
	}
	
	public LinkGroup cloneGroup()
	{
		LinkGroup clone = new LinkGroup(definition);
		clone.setPosition(position);
		return clone;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public CodelistEditorSortInfo getSortInfo(boolean ascending) {
		return new CodelistEditorSortInfo.CodeNameSortInfo(ascending);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public SafeHtml getLabel() {
		if (label == null) buildLabel();
		return label;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String getValue(UICode code)
	{
		List<UILink> links = code.getLinks();
		UILink link = match(links);
		
		return link!=null?link.getValue():"";
	}
	
	private void buildLabel() {
		SafeHtmlBuilder labelBuilder = new SafeHtmlBuilder();
		labelBuilder.appendHtmlConstant("<span style=\"vertical-align:middle;padding-right: 7px;\">");
		SafeHtml nameHtml = SafeHtmlUtils.fromString(ValueUtils.getValue(definition.getName()));
		labelBuilder.append(nameHtml);
		labelBuilder.appendHtmlConstant("</span>");
		
		label = labelBuilder.toSafeHtml();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((definition == null) ? 0 : definition.hashCode());
		result = prime * result + position;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LinkGroup other = (LinkGroup) obj;
		if (definition == null) {
			if (other.definition != null)
				return false;
		} else if (!definition.equals(other.definition))
			return false;
		if (position != other.position)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LinkGroup [definition=");
		builder.append(definition);
		builder.append(", position=");
		builder.append(position);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(LinkGroup o) {
		int compare = (getName() !=null)?getName().compareTo(o.getName()):1;
		if (compare!=0) return compare;
		
		compare = position > o.position ? +1 : position < o.position ? -1 : 0;
		return compare;
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public boolean isSortable() {
		return false;
	}

	@Override
	public String getSubtitle() {
		return "";
	}
}
