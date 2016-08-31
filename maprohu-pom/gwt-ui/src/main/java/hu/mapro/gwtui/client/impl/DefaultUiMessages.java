package hu.mapro.gwtui.client.impl;

import com.google.gwt.i18n.client.Messages;

public interface DefaultUiMessages extends Messages {

	@DefaultMessage("Login")
	String login();
	
	@DefaultMessage("Logout")
	String logout();

	@DefaultMessage("Delete")
	String delete();

	@DefaultMessage("Are you sure you want to delete {0} message(s)?")
	String deleteConfirm(int size);

	@DefaultMessage("New")
	String _new();

	@DefaultMessage("Editing New {0}")
	String editingNew(String entityName);
	
	@DefaultMessage("Editing {0}")
	String editing(String entityName);

	@DefaultMessage("Viewing {0}")
	String viewing(String entityName);

	@DefaultMessage("Error in application")
	String errorInApplication();

	@DefaultMessage("User Not Logged In")
	String notLoggedInErrorTitle();
	
	@DefaultMessage("You are not logged in. It is most likely that your session has expired. It is recommended that you log out or reload the page. (Message: {0})")
	String notLoggedInErrorMessage(String msg);

	@DefaultMessage("Reload Page")
	String reloadPage();

	@DefaultMessage("Ignore")
	String ignore();

	@DefaultMessage("Access Denied")
	String accessDeniedErrorTitle();
	
	@DefaultMessage("You do not have access to the requested resource. It is most likely because of an error in the application. It is recommended that you log out or reload the page and try again. If the problem remains contact the system administrator. (Message: {0})")
	String accessDeniedErrorMessage(String msg);

	@DefaultMessage("Unexpected Error")
	String unexpectedErrorTitle();
	
	@DefaultMessage("An unexpected error has occured. It is most likely because of an error in the application. It is recommended that you log out or reload the page and try again. If the problem remains contact the system administrator. (Message: {0} - Type: {1})")
	String unexpectedErrorMessage(String msg, String type);

	@DefaultMessage("User")
	String user();

	@DefaultMessage("Switch User")
	String switchUser();

	@DefaultMessage("Administrator")
	String administrator();

	@DefaultMessage("Duplicate Session")
	String sameUser();

	@DefaultMessage("Close Session")
	String closeDesktop();
	
	@DefaultMessage("New Password")
	String newPassword();

	@DefaultMessage("Repeat New Password")
	String repeatNewPassword();
	
	@DefaultMessage("Reset Password")
	String resetPassword();

	@DefaultMessage("Passwords do not match!")
	String passwordsDoNotMatch();

	@DefaultMessage("Password successfully updated.")
	String resetPasswordSuccessful();

	@DefaultMessage("Failed to update password: {0}")
	String resetPasswordUnsuccessful(String message);

	@DefaultMessage("Refresh")
	String refresh();

	@DefaultMessage("Constraint Validation")
	String constraintValidationTitle();

	@DefaultMessage("The parameters that has been specified for the operation are invalid: {0}")
	String constraintValidationMessage(String string);
	
	@DefaultMessage("More...")
	String moreItems();

	@DefaultMessage("Main")
	String mainForm();

	@DefaultMessage("Item")
	String item();

	@DefaultMessage("View")
	String view();

	@DefaultMessage("Edit")
	String edit();

	@DefaultMessage("Loading...")
	String loading();

	@DefaultMessage("Unsaved Changes")
	String closingDirtyEditorWarningTitle();

	@DefaultMessage("If you continue you will lose your changes. Go on?")
	String closingDirtyEditorWarningMessage();

	@DefaultMessage("Operation not permitted")
	String operationNotPermitted();

	@DefaultMessage("Confirmation required")
	String confirmationRequired();

	@DefaultMessage("Open")
	String open();

	@DefaultMessage("Cancel")
	String cancel();

	@DefaultMessage("Done")
	String done();

	@DefaultMessage("Database")
	String database();

	@DefaultMessage("Cannot set value")
	String cannotSetValueTitle();

	@DefaultMessage("The selected value is not valid for the field.")
	String cannotSetValueMessage();
	
	@DefaultMessage("Type filter here...")
	String typeFilterHere();

	@DefaultMessage("Value must not be null.")
	String notNullMessage();

	@DefaultMessage("Close")
	String close();

	@DefaultMessage("Validation Errors")
	String validationErrors();

	@DefaultMessage("Success")
	String success();
	
}
