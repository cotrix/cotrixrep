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
import org.cotrix.web.common.server.util.LinkTypes;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;
import org.cotrix.web.manage.shared.modify.linktype.AddLinkTypeCommand;
import org.cotrix.web.manage.shared.modify.linktype.LinkTypeCommand;
import org.cotrix.web.manage.shared.modify.linktype.RemoveLinkTypeCommand;
import org.cotrix.web.manage.shared.modify.linktype.UpdateLinkTypeCommand;
import org.cotrix.web.manage.shared.modify.linktype.UpdatedLinkType;

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
		Codelist codelist = repository.lookup(codelistId);
		
		CodelistLink codelistLink = null;
		if (command instanceof AddLinkTypeCommand) {
			AddLinkTypeCommand addLinkTypeCommand = (AddLinkTypeCommand)command;
			Codelist target = repository.lookup(addLinkTypeCommand.getItem().getTargetCodelist().getId());
			codelistLink = ChangesetUtil.addCodelistLink(addLinkTypeCommand.getItem(), target);
		}

		if (command instanceof UpdateLinkTypeCommand) {
			UpdateLinkTypeCommand updateLinkTypeCommand = (UpdateLinkTypeCommand)command;
			Codelist target = repository.lookup(updateLinkTypeCommand.getLinkType().getTargetCodelist().getId());	
			CodelistLink oldCodelistLink = lookupLink(codelist, updateLinkTypeCommand.getLinkType().getId());
			codelistLink = ChangesetUtil.updateCodelistLink(updateLinkTypeCommand.getLinkType(), target, oldCodelistLink);
		}

		if (command instanceof RemoveLinkTypeCommand) {
			RemoveLinkTypeCommand removeLinkTypeCommand = (RemoveLinkTypeCommand)command;
			codelistLink = ChangesetUtil.removeCodelistLink(removeLinkTypeCommand.getId());
		}
		
		if (codelistLink == null) throw new IllegalArgumentException("Unknown command "+command);

		Codelist changeset = modifyCodelist(codelistId).links(codelistLink).build();
		repository.update(changeset);
		
		CodelistLink updatedLink = lookupLink(codelist, codelistLink.id());
		
		return new UpdatedLinkType(updatedLink==null?null:LinkTypes.toUILinkType(updatedLink));
	}
	
	private CodelistLink lookupLink(Codelist codelist, String id) {
		for (CodelistLink link:codelist.links()) if (link.id().equals(id)) return link;
		return null;
	}
}
