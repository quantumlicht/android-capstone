package guay.philippe.capstone;

import guay.philippe.capstone.data.AuthRequest;
import guay.philippe.capstone.data.CompletedQuiz;
import guay.philippe.capstone.data.Player;
import guay.philippe.capstone.data.Quiz;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class Utils {
	
	private static final String STORAGE_FILE = "mutibo";
	
	//-----------------------------------------------------------------------
	public static Quiz convertQuiz(JSONObject obj) throws JSONException {
		String name = obj.getString("name");
		int id = obj.getInt("id");
		int difficulty= obj.getInt("difficulty");
		int rating = obj.getInt("rating");
		String author = obj.getString("author");
		ArrayList<String> movieSet = new ArrayList<String>();
		JSONArray movieObj = obj.getJSONArray("movieSet");

		for (int i=0; i< movieObj.length(); i++){
			movieSet.add(movieObj.getString(i));
		}
       
		String justification = obj.getString("justification");
		int unrelatedMovie = obj.getInt("unrelatedMovie");

		Quiz q = new Quiz(name, author, difficulty, justification, unrelatedMovie,movieSet, rating);
		q.setId(id);
		return q;
	}
	
	public static AuthRequest convertAuthRequest(JSONObject obj) throws JSONException {
		String access_token = obj.getString("access_token");
		String token_type = obj.getString("token_type");
		int expiration = obj.getInt("expires_in");
		String scope = obj.getString("scope");
		
		AuthRequest auth = new AuthRequest(access_token, token_type, scope, expiration);
		return auth;
	}
	
	//-----------------------------------------------------------------------
	public static CompletedQuiz convertCompletedQuiz(JSONObject obj) throws JSONException {
		Log.d("MUTIBO", "Utils::convertCompletedQuiz");
		String quizName = obj.getString("quizName");
		String playerName = obj.getString("playerName");
		int answer = obj.getInt("answer");
		boolean success = obj.getBoolean("success");
		boolean hasVoted = obj.getBoolean("hasVoted");

		CompletedQuiz compQ = new CompletedQuiz(quizName,playerName,answer,success);
		compQ.setHasVoted(hasVoted);
		return compQ;
	}
	
	//-----------------------------------------------------------------------
	public static JSONObject QuiztoJSON(Quiz q) {
		Gson gson = new Gson();
		String jsonQuiz = gson.toJson(q);
		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject(jsonQuiz);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d("MUTIBO", "Utils::QuiztoJSON " + jsonQuiz);
		return jsonObj;
	}
	
	//-----------------------------------------------------------------------
	public static JSONObject PlayertoJSON(Player p) {
		Gson gson = new Gson();
		String jsonPlayer = gson.toJson(p);
		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject(jsonPlayer);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d("MUTIBO", "Utils::PlayertoJSON " + jsonPlayer);
		return jsonObj;
	}
	
	//-----------------------------------------------------------------------
	public static JSONObject CompletedQuiztoJSON(CompletedQuiz p) {
		Gson gson = new Gson();
		String jsonCompletedQuiz = gson.toJson(p);
		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject(jsonCompletedQuiz);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d("MUTIBO", "Utils::QuiztoJSON " + jsonCompletedQuiz);
		return jsonObj;
	}
	
	//-----------------------------------------------------------------------
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
	//-----------------------------------------------------------------------	
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
	        return total;
    }
	
	//-----------------------------------------------------------------------
	public static SharedPreferences getStorage(Context ctx){
		return ctx.getSharedPreferences(STORAGE_FILE, Context.MODE_PRIVATE);
	}
	
	public static HttpPost setToken(Context ctx, HttpPost req){
		AuthRequest auth = AuthRequest.getCurrentAuthRequest(ctx);
		Log.d("MUTIBO", "Utils::setToken tokentype: " + auth.getTokenType() + " token: " + auth.getAccessToken());
		req.addHeader("Authorization", auth.getTokenType() +" " + auth.getAccessToken());
		return req;
	}
	
	public static HttpPut setToken(Context ctx, HttpPut req){
		AuthRequest auth = AuthRequest.getCurrentAuthRequest(ctx);
		Log.d("MUTIBO", "Utils::setToken tokentype: " + auth.getTokenType() + " token: " + auth.getAccessToken());
		req.addHeader("Authorization", auth.getTokenType() +" " + auth.getAccessToken());
		return req;
	}
	
	public static HttpGet setToken(Context ctx, HttpGet req){
		AuthRequest auth = AuthRequest.getCurrentAuthRequest(ctx);
		Log.d("MUTIBO", "Utils::setToken tokentype: " + auth.getTokenType() + " token: " + auth.getAccessToken());
		req.addHeader("Authorization", auth.getTokenType() +" " + auth.getAccessToken());
		return req;
	}

	public static HttpDelete setToken(Context ctx, HttpDelete req) {
		AuthRequest auth = AuthRequest.getCurrentAuthRequest(ctx);
		Log.d("MUTIBO", "Utils::setToken tokentype: " + auth.getTokenType() + " token: " + auth.getAccessToken());
		req.addHeader("Authorization", auth.getTokenType() +" " + auth.getAccessToken());
		return req;
	}

	public static void clearStorageAndCache(Context applicationContext) {
		SharedPreferences prefs = Utils.getStorage(applicationContext);
		prefs.edit().clear().commit();	
		clearCache(applicationContext);
	}

	private static void clearCache(Context context) {
      try {
         File dir = context.getCacheDir();
         if (dir != null && dir.isDirectory()) {
            deleteDir(dir);
         }
      } catch (Exception e) {
      }
   }

   private static boolean deleteDir(File dir) {
      if (dir != null && dir.isDirectory()) {
         String[] children = dir.list();
         for (int i = 0; i < children.length; i++) {
            boolean success = deleteDir(new File(dir, children[i]));
            if (!success) {
               return false;
            }
         }
      }

      // The directory is now empty so delete it
      return dir.delete();
   }
	
}
