package guay.philippe.capstone;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Data.Quiz;
import android.util.Log;


public class Utils {

	public static Quiz convertQuiz(JSONObject obj) throws JSONException {
		 Log.d("MUTIBO", "CreateQuizActivity::convertQuiz");
       String name = obj.getString("name");

       int difficulty= obj.getInt("difficulty");
       String author = obj.getString("author");
       ArrayList<String> movieSet = new ArrayList<String>();
       JSONArray movieObj = obj.getJSONArray("movieSet");

       for (int i=0; i< movieObj.length(); i++){
          movieSet.add(movieObj.getString(i));
       }
       
       String justification = obj.getString("justification");
       int unrelatedMovie = obj.getInt("unrelatedMovie");

       return new Quiz(name,author, difficulty, justification, unrelatedMovie,movieSet);
   }
}
