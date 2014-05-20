/**
 * 
 */
package org.cotrix.web.demo.client;

import org.cotrix.web.common.client.ext.CotrixExtension;

import com.google.gwt.core.shared.GWT;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DemoExtension implements CotrixExtension {

	@Override
	public String getName() {
		return "DemoExtension";
	}

	@Override
	public void activate() {
		CotrixDemoGinInjector demoGinInjector = GWT.create(CotrixDemoGinInjector.class);
		demoGinInjector.getCredentialPopupController().init();
		demoGinInjector.getResources().css().ensureInjected();
	}

}
