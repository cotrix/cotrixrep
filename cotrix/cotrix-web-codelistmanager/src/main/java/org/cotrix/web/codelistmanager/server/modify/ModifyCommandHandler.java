/**
 * 
 */
package org.cotrix.web.codelistmanager.server.modify;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.web.codelistmanager.shared.modify.ModifyCommand;
import org.cotrix.web.codelistmanager.shared.modify.ModifyCommandResult;
import org.cotrix.web.codelistmanager.shared.modify.code.CodeAttributeCommand;
import org.cotrix.web.codelistmanager.shared.modify.code.CodeCommand;
import org.cotrix.web.codelistmanager.shared.modify.metadata.MetadataAttributeCommand;
import org.cotrix.web.codelistmanager.shared.modify.metadata.MetadataCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
@Default
public class ModifyCommandHandler {
	@Inject
	protected CodeAttributeCommandHandler codeAttributeCommandHandler;
	
	@Inject
	protected CodeCommandHandler codeCommandHandler;
	
	@Inject
	protected MetadataAttributeCommandHandler metadataAttributeCommandHandler;
	
	@Inject
	protected MetadataCommandHandler metadataCommandHandler;
	
	public ModifyCommandResult handle(String codelistId, ModifyCommand command)
	{
		if (command instanceof CodeAttributeCommand) return codeAttributeCommandHandler.handle(codelistId, (CodeAttributeCommand) command);
		if (command instanceof MetadataAttributeCommand) return metadataAttributeCommandHandler.handle(codelistId, (MetadataAttributeCommand) command);
		if (command instanceof CodeCommand) return codeCommandHandler.handle(codelistId, (CodeCommand) command);
		if (command instanceof MetadataCommand) return metadataCommandHandler.handle(codelistId, (MetadataCommand) command);

		throw new IllegalArgumentException("Unknown command "+command);
	}
}
