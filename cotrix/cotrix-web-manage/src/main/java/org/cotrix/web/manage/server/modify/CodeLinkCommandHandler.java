/**
 * 
 */
package org.cotrix.web.manage.server.modify;

import static org.cotrix.domain.dsl.Codes.*;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.dsl.grammar.CodeGrammar.CodeDeltaClause;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.web.common.server.util.Codelists;
import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.common.shared.codelist.UILink;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;
import org.cotrix.web.manage.shared.modify.UpdatedCode;
import org.cotrix.web.manage.shared.modify.link.LinkCommand;
/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
@Default
public class CodeLinkCommandHandler {

	@Inject
	CodelistRepository repository;

	public ModifyCommandResult handle(String codelistId, String codeId, LinkCommand command)
	{
		CodeDeltaClause clause = modifyCode(codeId);
		UILink link = command.getItem();
		Codelink changeset = null;
		
		switch (command.getAction()) {
			case ADD: {
				CodelistLink linkType = repository.lookup(codelistId).links().lookup(link.getTypeId());
				Code code = linkType.target().codes().lookup(link.getTargetId());
				changeset = ChangesetUtil.addCodelink(link, linkType, code);
			} break;
			case UPDATE: {
				CodelistLink linkType = repository.lookup(codelistId).links().lookup(link.getTypeId());
				Code code = linkType.target().codes().lookup(link.getTargetId());
				Codelink oldLink = code.links().lookup(link.getId());
				changeset = ChangesetUtil.updateCodelink(link, oldLink, linkType, code);
				break;
			}
			case REMOVE: changeset = ChangesetUtil.removeCodelink(link); break;
		}
		
		clause.links(changeset);
		
		Code code = clause.build();
		Codelist codelistChangeset = modifyCodelist(codelistId).with(code).build();
		
		repository.update(codelistChangeset);
		
		UICode updatedCode = Codelists.toUiCode(getCode(codelistId, codeId));
		return new UpdatedCode(changeset.id(), updatedCode);
	}
	
	protected Code getCode(String codelistId, String id) {
		for (Code code:repository.lookup(codelistId).codes()) if (code.id().equals(id)) return code;
		return null;
	}
}
