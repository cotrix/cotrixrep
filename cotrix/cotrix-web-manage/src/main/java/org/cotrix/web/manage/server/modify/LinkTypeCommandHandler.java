/**
 * 
 */
package org.cotrix.web.manage.server.modify;

import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.repository.CodelistActions.*;
import static org.cotrix.web.common.server.util.LinkTypes.*;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.LinkDefinition;
import org.cotrix.repository.CodelistRepository;

import org.cotrix.web.common.shared.codelist.linktype.UILinkType;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;
import org.cotrix.web.manage.shared.modify.linktype.LinkTypeCommand;
import org.cotrix.web.manage.shared.modify.linktype.UpdatedLinkType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
@Default
public class LinkTypeCommandHandler {
	
	private Logger logger = LoggerFactory.getLogger(LinkTypeCommandHandler.class);

	@Inject
	CodelistRepository repository;

	public ModifyCommandResult handle(String codelistId, LinkTypeCommand command)
	{
		logger.trace("handle codelistId: {} command: {}", codelistId, command);
		
		UILinkType linkType = command.getItem();
		
		switch (command.getAction()) {
			case ADD: {
				Codelist target = repository.lookup(linkType.getTargetCodelist().getId());
				LinkDefinition codelistChangeSet = ChangesetUtil.addCodelistLink(linkType, target);
				update(codelistId, codelistChangeSet);
				return new UpdatedLinkType(toUILinkType(lookupLinkType(codelistId, codelistChangeSet.id())));
			}
			case UPDATE: {
				Codelist target = repository.lookup(linkType.getTargetCodelist().getId());
				LinkDefinition oldCodelistLink = lookupLinkType(codelistId, linkType.getId());
				LinkDefinition codelistChangeSet = ChangesetUtil.updateCodelistLink(linkType, target, oldCodelistLink);
				repository.update(modifyCodelist(codelistId).links(codelistChangeSet).build());
				return new UpdatedLinkType(toUILinkType(lookupLinkType(codelistId, codelistChangeSet.id())));
			}
			case REMOVE: {
				repository.update(codelistId, deleteCodelistLink(linkType.getId()));
				return new UpdatedLinkType(null);
			}
		}
		
		throw new IllegalArgumentException("Unknown command "+command);
	}
	
	private void update(String codelistId, LinkDefinition codelistChangeSet) {
		repository.update(modifyCodelist(codelistId).links(codelistChangeSet).build());
	}
	
	private LinkDefinition lookupLinkType(String codelistId, String typeId) {
		return repository.lookup(codelistId).links().lookup(typeId);
	}
}
