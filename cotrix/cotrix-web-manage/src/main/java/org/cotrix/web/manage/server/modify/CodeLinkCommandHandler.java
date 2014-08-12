/**
 * 
 */
package org.cotrix.web.manage.server.modify;

import static org.cotrix.domain.dsl.Entities.*;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.dsl.grammar.CodeGrammar.CodeChangeClause;
import org.cotrix.domain.links.Link;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.web.common.server.util.Codelists;
import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.common.shared.codelist.UILink;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;
import org.cotrix.web.manage.shared.modify.link.LinkCommand;
import org.cotrix.web.manage.shared.modify.link.UpdatedLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
@Default
public class CodeLinkCommandHandler {

	private Logger logger = LoggerFactory.getLogger(CodeLinkCommandHandler.class);
	
	@Inject
	private CodelistRepository repository;

	public ModifyCommandResult handle(String codelistId, String codeId, LinkCommand command)
	{
		logger.trace("handle codelistId: {}, codeId: {}, command: {}", codelistId, codeId, command);
		
		CodeChangeClause clause = modifyCode(codeId);
		UILink link = command.getItem();
		Link changeset = null;
		
		switch (command.getAction()) {
			case ADD: {
				LinkDefinition linkType = repository.lookup(codelistId).links().lookup(link.getDefinitionId());
				Code code = linkType.target().codes().lookup(link.getTargetId());
				changeset = ChangesetUtil.addCodelink(link, linkType, code);
			} break;
			case UPDATE: {
				Codelist codelist = repository.lookup(codelistId);
				LinkDefinition linkType = codelist.links().lookup(link.getDefinitionId());
				Code code = linkType.target().codes().lookup(link.getTargetId());
				Link oldLink = codelist.codes().lookup(codeId).links().lookup(link.getId());
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
		Link updatedCodeLink = getCodelink(codelistId, codeId, changeset.id());
		UILink updatedLink = updatedCodeLink==null?null:Codelists.toUiLink(updatedCodeLink);
		return new UpdatedLink(updatedCode, updatedLink);
	}
	
	protected Code getCode(String codelistId, String id) {
		for (Code code:repository.lookup(codelistId).codes()) if (code.id().equals(id)) return code;
		return null;
	}
	
	protected Link getCodelink(String codelistId, String codeId, String id) {
		for (Link link:repository.lookup(codelistId).codes().lookup(codeId).links()) if (link.id().equals(id)) return link;
		return null;
	}
}
