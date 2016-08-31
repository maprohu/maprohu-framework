package hu.mapro.gwtui.client.upload;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class UploadResponse {

	protected static final String ID = "id";
	protected static final String RESULT = "result";
	protected static final String ERROR_MESSAGE = "errorMessage";

	public enum Result {
		SUCCESS, FAILURE
	}

	protected Result result;

	protected String id;

	protected String errorMessage;

	public Result getResult() {
		return result;
	}

	public String getId() {
		return id;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void unmarshal(String json) {
		JSONObject o = (JSONObject) JSONParser.parseStrict(json);
		result = Result.valueOf(((JSONString) o.get(RESULT)).stringValue());
		JSONValue i = o.get(ID);
		if (i != null) {
			id = ((JSONString) i).stringValue();
		}
		JSONValue em = o.get(ERROR_MESSAGE);
		if (em != null) {
			errorMessage = ((JSONString) em).stringValue();
		}
	}

}
