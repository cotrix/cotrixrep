/**
 * 
 */
package org.cotrix.web.manage.server.modify;

import static org.cotrix.domain.dsl.Codes.*;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.web.manage.shared.modify.GeneratedId;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;
import org.cotrix.web.manage.shared.modify.linktype.AddLinkTypeCommand;
import org.cotrix.web.manage.shared.modify.linktype.LinkTypeCommand;
import org.cotrix.web.manage.shared.modify.linktype.RemoveLinkTypeCommand;
import org.cotrix.web.manage.shared.modify.linktype.UpdateLinkTypeCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
@Default
public class LinkTypeCommandHandler {

	@Inject
	CodelistRepository repository;

	public ModifyCommandResult handle(String codelistId, LinkTypeCommand command)
	{

		CodelistLink codelistLink = null;
		if (command instanceof AddLinkTypeCommand) {
			AddLinkTypeCommand addLinkTypeCommand = (AddLinkTypeCommand)command;
			Codelist target = repository.lookup(addLinkTypeCommand.getItem().getTargetCodelist().getId());
			codelistLink = ChangesetUtil.addCodelistLink(addLinkTypeCommand.getItem(), target);
		}

		if (command instanceof UpdateLinkTypeCommand) {
			UpdateLinkTypeCommand updateLinkTypeCommand = (UpdateLinkTypeCommand)command;
			codelistLink = ChangesetUtil.updateCodelistLink(updateLinkTypeCommand.getLinkType());
		}

		if (command instanceof RemoveLinkTypeCommand) {
			RemoveLinkTypeCommand removeLinkTypeCommand = (RemoveLinkTypeCommand)command;
			codelistLink = ChangesetUtil.removeCodelistLink(removeLinkTypeCommand.getId());
		}
		
		if (codelistLink == null) throw new IllegalArgumentException("Unknown command "+command);

		Codelist changeset = modifyCodelist(codelistId).links(codelistLink).build();
		repository.update(changeset);
		

		return new GeneratedId(codelistLink.id());
	}
}
