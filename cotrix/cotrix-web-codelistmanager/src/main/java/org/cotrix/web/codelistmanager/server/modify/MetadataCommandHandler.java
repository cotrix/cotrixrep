/**
 * 
 */
package org.cotrix.web.codelistmanager.server.modify;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.web.codelistmanager.shared.modify.GeneratedId;
import org.cotrix.web.codelistmanager.shared.modify.ModifyCommandResult;
import org.cotrix.web.codelistmanager.shared.modify.metadata.MetadataCommand;
import org.cotrix.web.codelistmanager.shared.modify.metadata.UpdateMetadataCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
@Default
public class MetadataCommandHandler {

	@Inject
	CodelistRepository repository;

	public ModifyCommandResult handle(String codelistId, MetadataCommand command)
	{

		Codelist changeset = null;
		
		if (command instanceof UpdateMetadataCommand){ 
			UpdateMetadataCommand updateMetadataCommand = (UpdateMetadataCommand)command;
			changeset = ChangesetUtil.updateCodelist(codelistId, updateMetadataCommand.getName());
		}

		if (changeset == null) throw new IllegalArgumentException("Unknown command "+command);

		repository.update(changeset);

		return new GeneratedId(changeset.id());
	}
}
