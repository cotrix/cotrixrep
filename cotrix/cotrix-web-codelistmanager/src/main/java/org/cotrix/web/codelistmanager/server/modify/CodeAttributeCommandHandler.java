/**
 * 
 */
package org.cotrix.web.codelistmanager.server.modify;

import static org.cotrix.domain.dsl.Codes.*;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Attribute;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.web.codelistmanager.shared.modify.ModifyCommandResult;
import org.cotrix.web.codelistmanager.shared.modify.UpdatedCode;
import org.cotrix.web.codelistmanager.shared.modify.attribute.AttributeCommand;
import org.cotrix.web.codelistmanager.shared.modify.code.CodeAttributeCommand;
import org.cotrix.web.share.server.util.Codelists;
import org.cotrix.web.share.shared.codelist.UICode;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
@Default
public class CodeAttributeCommandHandler {

	@Inject
	CodelistRepository repository;

	public ModifyCommandResult handle(String codelistId, CodeAttributeCommand codeCommand)
	{
		AttributeCommand command = codeCommand.getCommand();
		Attribute attribute = AttributeCommandUtil.handle(command);

		String codeId = codeCommand.getCodeId();
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
