/**
 * 
 */
package org.cotrix.web.importwizard.client.resources;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.user.cellview.client.SimplePager.Resources;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CotrixSimplePager extends Resources {
	
    /**
     * The image used to go to the first page.
     */
    @ImageOptions(flipRtl = true)
    @Source("firstPage.png")
    ImageResource simplePagerFirstPage();

    /**
     * The disabled first page image.
     */
    @ImageOptions(flipRtl = true)
    @Source("firstPageDisabled.png")
    ImageResource simplePagerFirstPageDisabled();

    /**
     * The image used to go to the last page.
     */
    @ImageOptions(flipRtl = true)
    @Source("lastPage.png")
    ImageResource simplePagerLastPage();

    /**
     * The disabled last page image.
     */
    @ImageOptions(flipRtl = true)
    @Source("lastPageDisabled.png")
    ImageResource simplePagerLastPageDisabled();

    /**
     * The image used to go to the next page.
     */
    @ImageOptions(flipRtl = true)
    @Source("nextPage.png")
    ImageResource simplePagerNextPage();

    /**
     * The disabled next page image.
     */
    @ImageOptions(flipRtl = true)
    @Source("nextPageDisabled.png")
    ImageResource simplePagerNextPageDisabled();

    /**
     * The image used to go to the previous page.
     */
    @ImageOptions(flipRtl = true)
    @Source("prevPage.png")
    ImageResource simplePagerPreviousPage();

    /**
     * The disabled previous page image.
     */
    @ImageOptions(flipRtl = true)
    @Source("prevPageDisabled.png")
    ImageResource simplePagerPreviousPageDisabled();

}
