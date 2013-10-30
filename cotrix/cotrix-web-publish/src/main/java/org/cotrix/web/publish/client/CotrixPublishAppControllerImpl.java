package org.cotrix.web.publish.client;

import org.cotrix.web.share.client.CotrixModule;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CotrixPublishAppControllerImpl implements CotrixPublishAppController {

	
	@Inject
	public CotrixPublishAppControllerImpl() {
	}

	@Override
	public CotrixModule getModule() {
		return CotrixModule.PUBLISH;
	}

	@Override
	public void go(HasWidgets container) {
		
	}

	@Override
	public void activate() {
		
	}

	@Override
	public void deactivate() {
		
	}

}
