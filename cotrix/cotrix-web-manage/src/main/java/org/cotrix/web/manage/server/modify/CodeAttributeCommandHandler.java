/**
 * 
 */
package org.cotrix.web.manage.server.modify;

import static org.cotrix.domain.dsl.Entities.*;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.attributes.CommonDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.web.common.server.util.Codelists;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;
import org.cotrix.web.manage.shared.modify.UpdatedCode;
import org.cotrix.web.manage.shared.modify.attribute.AttributeCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
@Default
public class CodeAttributeCommandHandler {
	
	private Logger logger = LoggerFactory.getLogger(CodeAttributeCommandHandler.class);

	@Inject
	private CodelistRepository repository;

	public ModifyCommandResult handle(String codelistId, String codeId, AttributeCommand command)
	{
		logger.trace("handle codelistid: {}, codeId: {}, command: {}", codelistId, codeId, command);
		
		Codelist codelist = repository.lookup(codelistId);
		
		AttributeDefinition definition = getDefinition(codelist, command.getItem());
		
		Attribute attribute = AttributeCommandUtil.handle(command, definition);

		Code code = modifyCode(codeId).attributes(attribute).build();
		Codelist changeset = modifyCodelist(codelistId).with(code).build();
		
		repository.update(changeset);
		
		UICode updatedCode = Codelists.toUiCode(codelist.codes().lookup(codeId));
		return new UpdatedCode(attribute.id(), updatedCode);
	}
	
	private AttributeDefinition getDefinition(Codelist codelist, UIAttribute attribute) {
		if (attribute.getDefinitionId() == null) return null;
		if (codelist.definitions().contains(attribute.getDefinitionId())) return codelist.definitions().lookup(attribute.getDefinitionId());
		for (CommonDefinition marker:CommonDefinition.markers()) if (marker.get().id().equals(attribute.getDefinitionId())) return marker.get();
		return null;
	}
}
