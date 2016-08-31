package hu.mapro.gwtui.gxt.client.upload;

import hu.mapro.gwtui.client.Listener;
import hu.mapro.gwtui.client.upload.UploadResponse;
import hu.mapro.gwtui.client.upload.UploadResponse.Result;
import hu.mapro.gwtui.client.upload.UploadWindow;

import java.util.List;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.sencha.gxt.widget.core.client.Status;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent.SubmitCompleteHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FileUploadField;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.FormPanel.Encoding;
import com.sencha.gxt.widget.core.client.form.FormPanel.Method;
import com.sencha.gxt.widget.core.client.info.Info;

public class GxtUploadWindow implements UploadWindow {

	String mapping;
	
	String title = "Upload File";
	
	String successTitle = "Upload Successful";
	
	String successMessage = "The file has been successfully uploaded.";
	
	String failureTitle = "Upload Failed";
	
	String failureMessage = "The uploading was unsuccessful: {0}";
	
	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void setMapping(String mapping) {
		this.mapping = mapping;
	}

	@Override
	public void show() {
		UploadWindowWidget w = new UploadWindowWidget();
		w.setVisible(true);
		w.center();
	}

	List<Listener<UploadResponse>> listeners = Lists.newArrayList();
	
	class UploadWindowWidget extends Window {
		
		private FormPanel form;

		UploadWindowWidget() {
			final Window window = this;
			window.setModal(true);
			window.setSize("350", "100");
			window.setResizable(false);
			window.setClosable(false);
			window.setHeadingText(title);
	
			form = new FormPanel();
			form.setAction(GWT.getModuleBaseURL() + mapping);
			form.setEncoding(Encoding.MULTIPART);
			form.setMethod(Method.POST);
			form.setLabelWidth(30);
			form.setBorders(true);
			window.add(form);
	
			
			final FileUploadField file = new FileUploadField();
			file.setName("fileData");
			form.add(new FieldLabel(file, "File"), new MarginData(5));
	
			window.setButtonAlign(BoxLayoutPack.CENTER);
			
			final Status status = new Status();
			
			final TextButton btn = new TextButton("Submit");
			btn.addSelectHandler(new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					if (Strings.isNullOrEmpty(file.getValue())) {
						MessageBox mb = new MessageBox("Warning", "File not specfied!");
						mb.setIcon(MessageBox.ICONS.warning());
						mb.show();
					} else {
						form.setEnabled(false);
						btn.setEnabled(false);
						status.setBusy("Uploading...");
						form.submit();
					}
				}
			});
			
			window.addButton(btn);
			
			TextButton cancelButton = new TextButton("Cancel");
			cancelButton.addSelectHandler(new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					setVisible(false);
				}
			});
			window.addButton(cancelButton);
			
			window.getButtonBar().add(status);
			
			form.addSubmitCompleteHandler(new SubmitCompleteHandler() {
				@Override
				public void onSubmitComplete(SubmitCompleteEvent event) {
					UploadResponse ur = new UploadResponse();
					ur.unmarshal(event.getResults());
					
					for (Listener<UploadResponse> l : listeners) {
						l.perform(ur);
					}
					
					if (ur.getResult()==Result.SUCCESS) {
						Info.display(successTitle, successMessage);
					} else {
						AlertMessageBox mb = new AlertMessageBox(
								failureTitle,
								failureMessage + "\n\n" + ur.getErrorMessage()
						);
						mb.show();
					}
					
					setVisible(false);
				};
			});
			
		}
	}

	@Override
	public void addListener(Listener<UploadResponse> result) {
		listeners.add(result);
	}

	public String getSuccessTitle() {
		return successTitle;
	}

	public void setSuccessTitle(String successTitle) {
		this.successTitle = successTitle;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public String getFailureTitle() {
		return failureTitle;
	}

	public void setFailureTitle(String failureTitle) {
		this.failureTitle = failureTitle;
	}

	public String getFailureMessage() {
		return failureMessage;
	}

	public void setFailureMessage(String failureMessage) {
		this.failureMessage = failureMessage;
	}

	public String getTitle() {
		return title;
	}

	
	
}
