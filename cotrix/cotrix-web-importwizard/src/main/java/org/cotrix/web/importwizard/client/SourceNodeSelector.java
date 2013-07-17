/**
 * 
 */
package org.cotrix.web.importwizard.client;

import java.util.List;

import org.cotrix.web.importwizard.client.flow.FlowNode;
import org.cotrix.web.importwizard.client.flow.SwitchNode.NodeSelector;
import org.cotrix.web.importwizard.client.step.WizardStep;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SourceNodeSelector implements NodeSelector<WizardStep> {

	@Override
	public FlowNode<WizardStep> selectNode(List<FlowNode<WizardStep>> children) {
		
		for (FlowNode<WizardStep> child:children) if (child.getItem().getId().equals("upload")) return child;
		
		return null;
	}

}
