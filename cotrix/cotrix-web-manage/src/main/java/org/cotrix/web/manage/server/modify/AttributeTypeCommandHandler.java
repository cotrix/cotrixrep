/**
 * 
 */
package org.cotrix.web.manage.server.modify;

import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.web.manage.shared.modify.GenericCommand.Action.*;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.domain.attributes.Definition;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.CodelistActions;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.web.common.server.util.AttributeTypes;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;
import org.cotrix.web.manage.shared.modify.attributetype.AttributeTypeCommand;
import org.cotrix.web.manage.shared.modify.attributetype.UpdatedAttributeType;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
@Default
public class AttributeTypeCommandHandler {

	@Inject
	CodelistRepository repository;

	public ModifyCommandResult handle(String codelistId, AttributeTypeCommand command)
	{
		Codelist codelist = repository.lookup(codelistId);
		
		String definitionId = command.getItem().getId();
		
		if (command.getAction()==REMOVE) {
			
			repository.update(codelist.id(), CodelistActions.deleteDefinition(definitionId));

		}
		else {
			
			Definition definition = null;
			
			switch (command.getAction()) {
				case ADD: definition = ChangesetUtil.addDefinition(command.getItem()); break;
				case UPDATE: definition = ChangesetUtil.updateDefinition(command.getItem()); break;
			}
			
			if (definition == null) throw new IllegalArgumentException("Unknown command "+command);

			Codelist changeset = modifyCodelist(codelistId).definitions(definition).build();
			repository.update(changeset);

		}
		
		
		switch (command.getAction()) {
			case REMOVE: return new UpdatedAttributeType();
			default: {
				Definition updatedDefinition = codelist.definitions().lookup(definitionId);
				return new UpdatedAttributeType(AttributeTypes.toUIAttributeType(updatedDefinition));
			}
			
		}
	}
}
