package guay.philippe.capstone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import Data.Quiz;
import android.util.Log;


public class Utils {

	public static Quiz convertQuiz(JSONObject obj) throws JSONException {
		 Log.d("MUTIBO", "CreateQuizActivity::convertQuiz");
       String name = obj.getString("name");
       int id = obj.getInt("id");
       int difficulty= obj.getInt("difficulty");
       String author = obj.getString("author");
       ArrayList<String> movieSet = new ArrayList<String>();
       JSONArray movieObj = obj.getJSONArray("movieSet");

       for (int i=0; i< movieObj.length(); i++){
          movieSet.add(movieObj.getString(i));
       }
       
       String justification = obj.getString("justification");
       int unrelatedMovie = obj.getInt("unrelatedMovie");

       Quiz q = new Quiz(name, author, difficulty, justification, unrelatedMovie,movieSet);
       q.setId(id);
       return q;
   }
	
	public static JSONObject QuiztoJSON(Quiz q) {
		Gson gson = new Gson();
		String jsonQuiz = gson.toJson(q);
		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject(jsonQuiz);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d("MUTIBO", "Utils::QuiztoJSON" + jsonQuiz);
		return jsonObj;
	}
	
	public static JSONObject[] StringtoJSON(String result){
	    	JSONObject[] json_data=null;
	    	
	    	try{
	            JSONArray jArray = new JSONArray(result);
	            json_data = new JSONObject[jArray.length()];
	            
	            for(int i=0;i<jArray.length();i++) {
	                json_data[i] = jArray.getJSONObject(i);
	            }
	    	} catch(JSONException e){
	            Log.e("MUTIBO", "Error parsing data "+e.toString());
	    	}
	    	return json_data;
    }
	
	public static StringBuilder inputStreamToString(InputStream is) {
	        String line = "";
	        StringBuilder total = new StringBuilder();
	        // Wrap a BufferedReader around the InputStream
	        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	        // Read response until the end
	        try {
	         while ((line = rd.readLine()) != null) { 
	           total.append(line); 
	         }
	        } catch (IOException e) {
	         e.printStackTrace();
	        }
	        // Return full string
	        return total;
    }
}
