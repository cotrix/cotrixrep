/**
 * 
 */
package org.cotrix.web.manage.server.modify;

import static org.cotrix.domain.dsl.Codes.*;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.domain.attributes.Definition;
import org.cotrix.domain.codelist.Codelist;
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
		
		Definition definition = null;
		switch (command.getAction()) {
			case ADD: definition = ChangesetUtil.addDefinition(command.getItem()); break;
			case UPDATE: definition = ChangesetUtil.updateDefinition(command.getItem()); break;
			case REMOVE: definition = ChangesetUtil.removeDefinition(command.getItem()); break;
		}
		
		if (definition == null) throw new IllegalArgumentException("Unknown command "+command);

		Codelist changeset = modifyCodelist(codelistId).definitions(definition).build();
		repository.update(changeset);
		
		Definition updatedDefinition = lookupLink(codelist, definition.id());
		
		return new UpdatedAttributeType(updatedDefinition==null?null:AttributeTypes.toUIAttributeType(updatedDefinition));
	}
	
	private Definition lookupLink(Codelist codelist, String id) {
		return codelist.definitions().contains(id)?codelist.definitions().lookup(id):null;
	}
}
