package org.cotrix.web.codelistmanager.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellList;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CellListResources extends  CellList.Resources {

        public CellListResources INSTANCE = GWT.create(CellListResources.class);

        /**
         * The styles used in this widget.
         */
        @Source("CellList.css")
        CellList.Style cellListStyle();
}