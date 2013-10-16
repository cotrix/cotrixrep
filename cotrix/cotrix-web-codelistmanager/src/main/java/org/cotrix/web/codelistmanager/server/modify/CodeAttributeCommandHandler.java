/**
 * 
 */
package org.cotrix.web.codelistmanager.server.modify;

import static org.cotrix.domain.dsl.Codes.*;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.web.codelistmanager.shared.modify.CodeAttributeCommand;
import org.cotrix.web.codelistmanager.shared.modify.GeneratedId;
import org.cotrix.web.codelistmanager.shared.modify.ModifyCommandResult;
import org.cotrix.web.codelistmanager.shared.modify.attribute.AttributeCommand;

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
		Code code = code(codeId).modify().attributes(attribute).build();
		Codelist changeset = codelist(codelistId).modify().with(code).build();
		
		repository.update(changeset);

		return new GeneratedId(attribute.id());
	}
}
