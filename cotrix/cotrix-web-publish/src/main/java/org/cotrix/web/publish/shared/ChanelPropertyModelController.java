package org.cotrix.web.publish.shared;

import com.google.gwt.user.client.Window;

public class ChanelPropertyModelController {
	public ChanelPropertyModel chanelProperty;
	
	public ChanelPropertyModelController() {
		this.chanelProperty = new ChanelPropertyModel();
	}
	public interface OnChanelPropertyValidated{
		void onValidatedResult(boolean pass);
	}
	private OnChanelPropertyValidated onChanelPropertyValidated;
	
	public void setOnValidated(OnChanelPropertyValidated onChanelPropertyValidated){
		this.onChanelPropertyValidated = onChanelPropertyValidated;
	}
	public ChanelPropertyModel getChanelProperty() {
		return chanelProperty;
	}

	public void setChanelProperty(ChanelPropertyModel chanelProperty) {
		this.chanelProperty = chanelProperty;
	}
	
	public void validate(){
		boolean isValidated = true;
		if(chanelProperty.getUsername() == null || chanelProperty.getUsername().length() == 0){
			isValidated = false;
		}
		if(chanelProperty.getPassword() == null || chanelProperty.getPassword().length() == 0){
			isValidated = false;
		}
		if(chanelProperty.getUrl() == null || chanelProperty.getUrl().length() == 0){
			isValidated = false;
		}
		if(isValidated){
			onChanelPropertyValidated.onValidatedResult(isValidated);
		}
	}
}
