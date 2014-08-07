package org.cotrix.web.manage.client.codelist.common;

import static org.cotrix.web.common.client.widgets.button.ButtonResourceBuilder.*;

import org.cotrix.web.common.client.widgets.button.ButtonResources;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface Icons extends ClientBundle {

	public static Icons icons = GWT.create(Icons.class);
	
	public static ButtonResources BLUE_MINUS = create().upFace(icons.blueRemove()).hover(icons.blueRemoveHover()).disabled(icons.removeDisabled()).title("Remove").build();
	public static ButtonResources BLUE_PLUS = create().upFace(icons.blueAdd()).hover(icons.blueAddHover()).disabled(icons.addDisabled()).title("Add").build();
	
	public static ButtonResources RED_MINUS = create().upFace(icons.redRemove()).hover(icons.redRemoveHover()).disabled(icons.removeDisabled()).title("Remove").build();
	public static ButtonResources RED_PLUS = create().upFace(icons.redAdd()).hover(icons.redAddHover()).disabled(icons.addDisabled()).title("Add").build();

	
	@Source("org/cotrix/web/manage/client/codelist/common/addDisabled.png")
	ImageResource addDisabled();

	@Source("org/cotrix/web/manage/client/codelist/common/addition.png")
	ImageResource addition();

	@Source("org/cotrix/web/manage/client/codelist/common/attribute.png")
	ImageResource attribute();

	@Source("org/cotrix/web/manage/client/codelist/common/attributeDisabled.png")
	ImageResource attributeDisabled();

	@Source("org/cotrix/web/manage/client/codelist/common/attributeHover.png")
	ImageResource attributeHover();

	@Source("org/cotrix/web/manage/client/codelist/common/blueAdd.png")
	ImageResource blueAdd();

	@Source("org/cotrix/web/manage/client/codelist/common/blueAddHover.png")
	ImageResource blueAddHover();

	@Source("org/cotrix/web/manage/client/codelist/common/blueCancel.png")
	ImageResource blueCancel();

	@Source("org/cotrix/web/manage/client/codelist/common/blueCancelHover.png")
	ImageResource blueCancelHover();

	@Source("org/cotrix/web/manage/client/codelist/common/blueEdit.png")
	ImageResource blueEdit();

	@Source("org/cotrix/web/manage/client/codelist/common/blueEditHover.png")
	ImageResource blueEditHover();

	@Source("org/cotrix/web/manage/client/codelist/common/blueRemove.png")
	ImageResource blueRemove();

	@Source("org/cotrix/web/manage/client/codelist/common/blueRemoveHover.png")
	ImageResource blueRemoveHover();

	@Source("org/cotrix/web/manage/client/codelist/common/blueSave.png")
	ImageResource blueSave();

	@Source("org/cotrix/web/manage/client/codelist/common/blueSaveHover.png")
	ImageResource blueSaveHover();

	@Source("org/cotrix/web/manage/client/codelist/common/change.png")
	ImageResource change();

	@Source("org/cotrix/web/manage/client/codelist/common/columnToggle.png")
	ImageResource columnToggle();

	@Source("org/cotrix/web/manage/client/codelist/common/columnToggleHover.png")
	ImageResource columnToggleHover();

	@Source("org/cotrix/web/manage/client/codelist/common/deletion.png")
	ImageResource deletion();

	@Source("org/cotrix/web/manage/client/codelist/common/error.png")
	ImageResource error();

	@Source("org/cotrix/web/manage/client/codelist/common/link.png")
	ImageResource link();

	@Source("org/cotrix/web/manage/client/codelist/common/linkDisabled.png")
	ImageResource linkDisabled();

	@Source("org/cotrix/web/manage/client/codelist/common/linkHover.png")
	ImageResource linkHover();

	@Source("org/cotrix/web/manage/client/codelist/common/logBook.png")
	ImageResource logBook();

	@Source("org/cotrix/web/manage/client/codelist/common/logBookDisabled.png")
	ImageResource logBookDisabled();

	@Source("org/cotrix/web/manage/client/codelist/common/logBookHover.png")
	ImageResource logBookHover();

	@Source("org/cotrix/web/manage/client/codelist/common/marker.png")
	ImageResource marker();

	@Source("org/cotrix/web/manage/client/codelist/common/markerDisabled.png")
	ImageResource markerDisabled();

	@Source("org/cotrix/web/manage/client/codelist/common/markerHover.png")
	ImageResource markerHover();

	@Source("org/cotrix/web/manage/client/codelist/common/play.png")
	ImageResource play();

	@Source("org/cotrix/web/manage/client/codelist/common/playDisabled.png")
	ImageResource playDisabled();

	@Source("org/cotrix/web/manage/client/codelist/common/playHover.png")
	ImageResource playHover();

	@Source("org/cotrix/web/manage/client/codelist/common/redAdd.png")
	ImageResource redAdd();

	@Source("org/cotrix/web/manage/client/codelist/common/redAddHover.png")
	ImageResource redAddHover();

	@Source("org/cotrix/web/manage/client/codelist/common/redCancel.png")
	ImageResource redCancel();

	@Source("org/cotrix/web/manage/client/codelist/common/redCancelHover.png")
	ImageResource redCancelHover();

	@Source("org/cotrix/web/manage/client/codelist/common/redEdit.png")
	ImageResource redEdit();

	@Source("org/cotrix/web/manage/client/codelist/common/redEditHover.png")
	ImageResource redEditHover();

	@Source("org/cotrix/web/manage/client/codelist/common/redRemove.png")
	ImageResource redRemove();

	@Source("org/cotrix/web/manage/client/codelist/common/redRemoveHover.png")
	ImageResource redRemoveHover();

	@Source("org/cotrix/web/manage/client/codelist/common/redSave.png")
	ImageResource redSave();

	@Source("org/cotrix/web/manage/client/codelist/common/redSaveHover.png")
	ImageResource redSaveHover();

	@Source("org/cotrix/web/manage/client/codelist/common/removeDisabled.png")
	ImageResource removeDisabled();

	@Source("org/cotrix/web/manage/client/codelist/common/task.png")
	ImageResource task();

	@Source("org/cotrix/web/manage/client/codelist/common/taskDisabled.png")
	ImageResource taskDisabled();

	@Source("org/cotrix/web/manage/client/codelist/common/taskHover.png")
	ImageResource taskHover();

	@Source("org/cotrix/web/manage/client/codelist/common/columnToggleDown.png")
	ImageResource columnToggleDown();

	@Source("org/cotrix/web/manage/client/codelist/common/markerChecked.png")
	ImageResource markerChecked();

	@Source("org/cotrix/web/manage/client/codelist/common/markerUnchecked.png")
	ImageResource markerUnchecked();

}
