/**
 * 
 */
package org.cotrix.web.share.client.rpc;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CallBackListener {

	boolean onFailure(Throwable caught);

	boolean onSuccess(Object result);

}
