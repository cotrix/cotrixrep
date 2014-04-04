/**
 * 
 */
package org.cotrix.web.manage.server.modify;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.repository.CodelistRepository;
import org.cotrix.web.manage.shared.modify.GeneratedId;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;
import org.cotrix.web.manage.shared.modify.linktype.LinkTypeCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
@Default
public class LinkTypeCommandHandler {

	@Inject
	CodelistRepository repository;

	public ModifyCommandResult handle(String codelistId, LinkTypeCommand command)
	{

		/*Codelist changeset = null;
		
		if (command instanceof UpdateMetadataCommand){ 
			UpdateMetadataCommand updateMetadataCommand = (UpdateMetadataCommand)command;
			changeset = ChangesetUtil.updateCodelist(codelistId, updateMetadataCommand.getName());
		}

		if (changeset == null) throw new IllegalArgumentException("Unknown command "+command);

		repository.update(changeset);

		return new GeneratedId(changeset.id());*/
		System.out.println("LinkTypeCommandHandler "+codelistId+ " "+command);
		
		return new GeneratedId("1");
	}
}
