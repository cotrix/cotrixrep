/**
 * 
 */
package org.cotrix.web.manage.server.modify;

import static org.cotrix.domain.dsl.Data.*;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.web.common.server.util.Codelists;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;
import org.cotrix.web.manage.shared.modify.UpdatedCode;
import org.cotrix.web.manage.shared.modify.code.AddCodeCommand;
import org.cotrix.web.manage.shared.modify.code.CodeCommand;
import org.cotrix.web.manage.shared.modify.code.RemoveCodeCommand;
import org.cotrix.web.manage.shared.modify.code.UpdateCodeCommand;
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
		boolean removed = false;
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
			removed = true;
		}

		Codelist changeset = modifyCodelist(codelistId).with(code).build();
		repository.update(changeset);

		return new UpdatedCode(code.id(), removed?null:Codelists.toUiCode(getCode(codelistId, code.id())));
	}
	
	
	protected Code getCode(String codelistId, String id) {
		return repository.lookup(codelistId).codes().lookup(id);
	}

}
