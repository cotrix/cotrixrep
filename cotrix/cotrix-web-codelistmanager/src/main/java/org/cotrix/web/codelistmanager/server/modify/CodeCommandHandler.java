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
import org.cotrix.repository.CodelistRepository;
import org.cotrix.web.codelistmanager.shared.modify.ModifyCommandResult;
import org.cotrix.web.codelistmanager.shared.modify.UpdatedCode;
import org.cotrix.web.codelistmanager.shared.modify.code.AddCodeCommand;
import org.cotrix.web.codelistmanager.shared.modify.code.CodeCommand;
import org.cotrix.web.codelistmanager.shared.modify.code.RemoveCodeCommand;
import org.cotrix.web.codelistmanager.shared.modify.code.UpdateCodeCommand;
import org.cotrix.web.share.server.util.Codelists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
@Default
public class CodeCommandHandler {
	
	protected Logger logger = LoggerFactory.getLogger(CodeCommandHandler.class);

	@Inject
	CodelistRepository repository;

	public ModifyCommandResult handle(String codelistId, CodeCommand command)
	{
		logger.trace("handler codelistId: {} command: {}", codelistId, command);
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

		Codelist changeset = modifyCodelist(codelistId).with(code).build();
		repository.update(changeset);

		return new UpdatedCode(code.id(), Codelists.toUiCode(getCode(codelistId, code.id())));
	}
	
	
	protected Code getCode(String codelistId, String id) {
		for (Code code:repository.lookup(codelistId).codes()) if (code.id().equals(id)) return code;
		return null;
	}

}
