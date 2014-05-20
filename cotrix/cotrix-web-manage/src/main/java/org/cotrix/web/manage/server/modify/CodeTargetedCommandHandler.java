/**
 * 
 */
package org.cotrix.web.manage.server.modify;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.web.manage.shared.modify.ModifyCommand;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;
import org.cotrix.web.manage.shared.modify.attribute.AttributeCommand;
import org.cotrix.web.manage.shared.modify.code.CodeTargetedCommand;
import org.cotrix.web.manage.shared.modify.link.LinkCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
@Default
public class CodeTargetedCommandHandler {
	

	private Logger logger = LoggerFactory.getLogger(CodeTargetedCommandHandler.class);
	
	@Inject
	private CodeAttributeCommandHandler codeAttributeCommandHandler;
	
	@Inject
	private CodeLinkCommandHandler codeLinkCommandHandler;

	public ModifyCommandResult handle(String codelistId, CodeTargetedCommand codeCommand)
	{
		logger.trace("handle codelistId: {}, codeCommand: {}", codelistId, codeCommand);
		
		ModifyCommand command = codeCommand.getCommand();
		String codeId = codeCommand.getCodeId();
		
		if (command instanceof AttributeCommand) return codeAttributeCommandHandler.handle(codelistId, codeId, (AttributeCommand) command);
		if (command instanceof LinkCommand) return codeLinkCommandHandler.handle(codelistId, codeId, (LinkCommand) command);

		throw new IllegalArgumentException("Unknown sub command "+command);
	}

}
