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
import org.cotrix.repository.codelist.CodelistRepository;
import org.cotrix.web.codelistmanager.shared.modify.GeneratedId;
import org.cotrix.web.codelistmanager.shared.modify.ModifyCommandResult;
import org.cotrix.web.codelistmanager.shared.modify.code.AddCodeCommand;
import org.cotrix.web.codelistmanager.shared.modify.code.CodeCommand;
import org.cotrix.web.codelistmanager.shared.modify.code.RemoveCodeCommand;
import org.cotrix.web.codelistmanager.shared.modify.code.UpdateCodeCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
@Default
public class CodeCommandHandler {

	@Inject
	CodelistRepository repository;

	public ModifyCommandResult handle(String codelistId, CodeCommand command)
	{
		Code code = null;
		if (command instanceof AddCodeCommand) {
			AddCodeCommand addCodeCommand = (AddCodeCommand) command;
			code = ChangesetUtil.addCode(addCodeCommand.getItem());
		}
		if (command instanceof UpdateCodeCommand) {
			UpdateCodeCommand updateCodeCommand = (UpdateCodeCommand) command;
			code = ChangesetUtil.updateCode(updateCodeCommand.getCodeId(), updateCodeCommand.getName());
		}
		if (command instanceof RemoveCodeCommand) {
			RemoveCodeCommand removeCodeCommand = (RemoveCodeCommand) command;
			code = ChangesetUtil.removeCode(removeCodeCommand.getId());
		}

		Codelist changeset = codelist(codelistId).with(code).build();
		repository.update(changeset);

		return new GeneratedId(code.id());
	}

}
