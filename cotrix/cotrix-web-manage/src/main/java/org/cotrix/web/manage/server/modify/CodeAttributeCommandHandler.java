/**
 * 
 */
package org.cotrix.web.manage.server.modify;

import static org.cotrix.domain.dsl.Codes.*;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Attribute;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.web.common.server.util.Codelists;
import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;
import org.cotrix.web.manage.shared.modify.UpdatedCode;
import org.cotrix.web.manage.shared.modify.attribute.AttributeCommand;
/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
@Default
public class CodeAttributeCommandHandler {

	@Inject
	CodelistRepository repository;

	public ModifyCommandResult handle(String codelistId, String codeId, AttributeCommand command)
	{
		Attribute attribute = AttributeCommandUtil.handle(command);

		Code code = modifyCode(codeId).attributes(attribute).build();
		Codelist changeset = modifyCodelist(codelistId).with(code).build();
		
		repository.update(changeset);
		
		UICode updatedCode = Codelists.toUiCode(getCode(codelistId, codeId));
		return new UpdatedCode(attribute.id(), updatedCode);
	}
	
	protected Code getCode(String codelistId, String id) {
		for (Code code:repository.lookup(codelistId).codes()) if (code.id().equals(id)) return code;
		return null;
	}
}
