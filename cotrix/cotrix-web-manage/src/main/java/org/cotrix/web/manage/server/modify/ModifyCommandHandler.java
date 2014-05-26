/**
 * 
 */
package org.cotrix.web.manage.server.modify;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.web.manage.shared.modify.ModifyCommand;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;
import org.cotrix.web.manage.shared.modify.attributetype.AttributeTypeCommand;
import org.cotrix.web.manage.shared.modify.code.CodeTargetedCommand;
import org.cotrix.web.manage.shared.modify.code.CodeCommand;
import org.cotrix.web.manage.shared.modify.linktype.LinkTypeCommand;
import org.cotrix.web.manage.shared.modify.metadata.MetadataAttributeCommand;
import org.cotrix.web.manage.shared.modify.metadata.MetadataCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
@Default
public class ModifyCommandHandler {
	
	@Inject
	private CodeCommandHandler codeCommandHandler;
	
	@Inject
	private CodeTargetedCommandHandler codeTargetedCommandHandler;
	
	@Inject
	private MetadataAttributeCommandHandler metadataAttributeCommandHandler;
	
	@Inject
	private MetadataCommandHandler metadataCommandHandler;
	
	@Inject
	private LinkTypeCommandHandler linkTypeCommandHandler;
	
	@Inject
	private AttributeTypeCommandHandler attributeTypeCommandHandler;
	
	public ModifyCommandResult handle(String codelistId, ModifyCommand command)
	{
		if (command instanceof CodeTargetedCommand) return codeTargetedCommandHandler.handle(codelistId, (CodeTargetedCommand) command);
		if (command instanceof MetadataAttributeCommand) return metadataAttributeCommandHandler.handle(codelistId, (MetadataAttributeCommand) command);
		if (command instanceof CodeCommand) return codeCommandHandler.handle(codelistId, (CodeCommand) command);
		if (command instanceof MetadataCommand) return metadataCommandHandler.handle(codelistId, (MetadataCommand) command);
		if (command instanceof LinkTypeCommand) return linkTypeCommandHandler.handle(codelistId, (LinkTypeCommand) command);
		if (command instanceof AttributeTypeCommand) return attributeTypeCommandHandler.handle(codelistId, (AttributeTypeCommand) command);
		
		throw new IllegalArgumentException("Unknown command "+command);
	}
}
