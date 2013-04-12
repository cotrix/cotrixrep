package org.cotrix.web.publish.client.resources;

import com.google.gwt.cell.client.ButtonCellBase.DefaultAppearance.Resources;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTable;

public interface CellListResources extends  CellList.Resources {

        public CellListResources INSTANCE = GWT.create(CellListResources.class);

        /**
         * The styles used in this widget.
         */
        @Source("CellList.css")
        CellList.Style cellListStyle();
}