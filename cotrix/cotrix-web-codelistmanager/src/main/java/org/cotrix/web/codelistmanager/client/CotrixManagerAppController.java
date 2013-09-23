package org.cotrix.web.codelistmanager.client;

import org.cotrix.web.codelistmanager.client.presenter.Presenter;
import org.cotrix.web.share.client.CotrixModuleController;

import com.google.gwt.event.logical.shared.ValueChangeHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CotrixManagerAppController extends Presenter, ValueChangeHandler<String>, CotrixModuleController {
}
