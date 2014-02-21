/**
 * 
 */
package org.cotrix.web.common.client.rpc;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CallBackListener {

	boolean onFailure(Throwable caught);

	boolean onSuccess(Object result);

}
