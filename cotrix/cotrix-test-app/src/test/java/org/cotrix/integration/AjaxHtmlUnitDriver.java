/**
 * 
 */
package org.cotrix.integration;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AjaxHtmlUnitDriver extends HtmlUnitDriver {
    public AjaxHtmlUnitDriver() {
        super(BrowserVersion.FIREFOX_24);
        setJavascriptEnabled(true);
    }
 
    @Override
    protected WebClient modifyWebClient(WebClient client) {
        WebClient webClient = super.modifyWebClient(client);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        return webClient;
    }
}
