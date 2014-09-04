/**
 * 
 */
package org.cotrix.web.manage.server.modify;

import static org.cotrix.domain.dsl.Data.*;
import static org.cotrix.repository.CodelistActions.*;
import static org.cotrix.web.common.server.util.LinkDefinitions.*;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.repository.CodelistRepository;

import org.cotrix.web.common.shared.codelist.linkdefinition.UILinkDefinition;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;
import org.cotrix.web.manage.shared.modify.linkdefinition.LinkDefinitionCommand;
import org.cotrix.web.manage.shared.modify.linkdefinition.UpdatedLinkDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
@Default
public class LinkDefinitionCommandHandler {
	
	private Logger logger = LoggerFactory.getLogger(LinkDefinitionCommandHandler.class);

	@Inject
	CodelistRepository repository;

	public ModifyCommandResult handle(String codelistId, LinkDefinitionCommand command)
	{
		logger.trace("handle codelistId: {} command: {}", codelistId, command);
		
		UILinkDefinition linkDefinition = command.getItem();
		
		switch (command.getAction()) {
			case ADD: {
				Codelist target = repository.lookup(linkDefinition.getTargetCodelist().getId());
				LinkDefinition codelistChangeSet = ChangesetUtil.addLinkDefinition(linkDefinition, target);
				update(codelistId, codelistChangeSet);
				return new UpdatedLinkDefinition(toUILinkDefinition(lookupLinkDefinition(codelistId, codelistChangeSet.id())));
			}
			case UPDATE: {
				Codelist target = repository.lookup(linkDefinition.getTargetCodelist().getId());
				LinkDefinition oldCodelistLink = lookupLinkDefinition(codelistId, linkDefinition.getId());
				LinkDefinition codelistChangeSet = ChangesetUtil.updateLinkDefinition(linkDefinition, target, oldCodelistLink);
				repository.update(modifyCodelist(codelistId).links(codelistChangeSet).build());
				return new UpdatedLinkDefinition(toUILinkDefinition(lookupLinkDefinition(codelistId, codelistChangeSet.id())));
			}
			case REMOVE: {
				repository.update(codelistId, deleteLinkdef(linkDefinition.getId()));
				return new UpdatedLinkDefinition(null);
			}
		}
		
		throw new IllegalArgumentException("Unknown command "+command);
	}
	
	private void update(String codelistId, LinkDefinition codelistChangeSet) {
		repository.update(modifyCodelist(codelistId).links(codelistChangeSet).build());
	}
	
	private LinkDefinition lookupLinkDefinition(String codelistId, String definitionId) {
		return repository.lookup(codelistId).linkDefinitions().lookup(definitionId);
	}
}
