package guay.philippe.capstone;

import org.json.JSONObject;

public interface IApiAccessResponse {

	void postResult(JSONObject[] jsonObjects);
	
	void postResult(Boolean result);
}
