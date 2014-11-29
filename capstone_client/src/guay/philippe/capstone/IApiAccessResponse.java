package guay.philippe.capstone;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IApiAccessResponse {

	void processResponse(JSONArray arr);

	void processResponse(JSONObject[] stringtoJSON);


}
