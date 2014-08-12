/**
 * 
 */
package org.cotrix.web.manage.server.modify;

import static org.cotrix.domain.dsl.Data.*;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.manage.shared.modify.GeneratedId;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;
import org.cotrix.web.manage.shared.modify.attribute.AttributeCommand;
import org.cotrix.web.manage.shared.modify.metadata.MetadataAttributeCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
@Default
public class MetadataAttributeCommandHandler {

	@Inject
	CodelistRepository repository;

	public ModifyCommandResult handle(String codelistId, MetadataAttributeCommand codeCommand)
	{
		AttributeCommand command = codeCommand.getCommand();
		
		Codelist codelist = repository.lookup(codelistId);
		
		AttributeDefinition definition = getDefinition(codelist, command.getItem());
		
		Attribute attribute = AttributeCommandUtil.handle(command, definition);
		
		Codelist changeset = modifyCodelist(codelistId).attributes(attribute).build();
		
		repository.update(changeset);

		return new GeneratedId(attribute.id());
	}
	
	private AttributeDefinition getDefinition(Codelist codelist, UIAttribute attribute) {
		if (attribute.getDefinitionId() == null) return null;
		if (codelist.attributeDefinitions().contains(attribute.getDefinitionId())) return codelist.attributeDefinitions().lookup(attribute.getDefinitionId());
		return null;
	}
}
