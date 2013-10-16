/**
 * 
 */
package org.cotrix.web.codelistmanager.client.data;

import org.cotrix.web.codelistmanager.shared.UIAttribute;
import org.cotrix.web.codelistmanager.shared.UICode;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeAttributeEditor extends DataEditor<CodeAttributeEditor.CodeAttribute> {
	
	public void added(UICode code, UIAttribute attribute)
	{
		this.added(new CodeAttribute(code.getCode(), attribute));
	}
	
	public void updated(UICode code, UIAttribute attribute)
	{
		this.updated(new CodeAttribute(code.getCode(), attribute));
	}
	
	public void removed(UICode code, UIAttribute attribute)
	{
		this.removed(new CodeAttribute(code.getCode(), attribute));
	}
	
	public class CodeAttribute {
		
		protected String codeId;
		protected UIAttribute attribute;
		
		/**
		 * @param codeId
		 * @param attribute
		 */
		public CodeAttribute(String codeId, UIAttribute attribute) {
			this.codeId = codeId;
			this.attribute = attribute;
		}
		/**
		 * @return the codeId
		 */
		public String getCodeId() {
			return codeId;
		}
		/**
		 * @return the attribute
		 */
		public UIAttribute getAttribute() {
			return attribute;
		}
		
		
	}

}
