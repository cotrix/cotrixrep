/**
 * 
 */
package org.cotrix.web.manage.server.modify;

import static org.cotrix.domain.dsl.Data.*;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.CodelistActions;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.web.common.server.util.AttributeDefinitions;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;
import org.cotrix.web.manage.shared.modify.attributedefinition.AttributeDefinitionCommand;
import org.cotrix.web.manage.shared.modify.attributedefinition.UpdatedAttributeDefinition;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
@Default
public class AttributeDefinitionCommandHandler {

	@Inject
	CodelistRepository repository;

	@SuppressWarnings("incomplete-switch")
	public ModifyCommandResult handle(String codelistId, AttributeDefinitionCommand command)
	{
		Codelist codelist = repository.lookup(codelistId);

		switch (command.getAction()) {
			case REMOVE: {
				repository.update(codelist.id(), CodelistActions.deleteAttrdef(command.getItem().getId()));
				return new UpdatedAttributeDefinition();
			}
			default: {

				AttributeDefinition definition = null;

				switch (command.getAction()) {
					case ADD: definition = ChangesetUtil.addDefinition(command.getItem()); break;
					case UPDATE: definition = ChangesetUtil.updateDefinition(command.getItem()); break;
				}

				if (definition == null) throw new IllegalArgumentException("Unknown command "+command);

				Codelist changeset = modifyCodelist(codelistId).definitions(definition).build();
				repository.update(changeset);
				AttributeDefinition updatedDefinition = codelist.attributeDefinitions().lookup(definition.id());
				return new UpdatedAttributeDefinition(AttributeDefinitions.toUIAttributeDefinition(updatedDefinition));
			}
		}
	}
}
