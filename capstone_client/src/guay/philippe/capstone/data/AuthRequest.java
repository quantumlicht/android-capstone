package guay.philippe.capstone.data;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tasks.TaskAuthenticate;

import guay.philippe.capstone.IApiAccessResponse;
import guay.philippe.capstone.R;
import guay.philippe.capstone.Utils;
import guay.philippe.capstone.auth.EasyHttpClient;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;
import android.util.Log;

public class AuthRequest implements Parcelable, IApiAccessResponse{
	private String access_token;
	private static AuthRequest currentAuthRequest = null;
	private String token_type;
	private String scope;
	private int expiration;
	private long expirationDate = 0;
	private String username;
	private String password;
//	private static Context ctx;
	private String client_string = "mobile:";

	// Constructors
	//------------------------------------------------------------------
	
	public static AuthRequest getCurrentAuthRequest(Context ctx){
		
		SharedPreferences prefs = Utils.getStorage(ctx);
		String token = prefs.getString("access_token", "");
		String token_type = prefs.getString("token_type", "");
		String username = prefs.getString("username", "");
		String password = prefs.getString("password", "");
		String scope = prefs.getString("scope", "");
		int expiration = prefs.getInt("expiration", 0);
		long expDate = prefs.getLong("expiration_date",0);
		if (currentAuthRequest == null) {
			currentAuthRequest = new AuthRequest(token, token_type, scope, expiration);
			currentAuthRequest.setExpirationDate(expDate);
		}
		
		Log.d("MUTIBO","AuthRequest::getCurrentAuthRequest expDate: " + expDate);
		if (currentAuthRequest.isTokenExpired()){
			Log.d("MUTIBO","AuthRequest::getCurrentAuthRequest Refreshing access_token");
			//TODO: Implement refresh token properly
			currentAuthRequest.refreshToken(ctx, username, password);
		}
		return currentAuthRequest;
	}
		
	public AuthRequest(String access_token, String token_type, String scope, int expiration){
		this.access_token = access_token;
		this.token_type = token_type;
		this.scope = scope;
		this.expiration = expiration;
		this.expirationDate = new Date().getTime() + expiration;
		currentAuthRequest = this;
	}
	
	public AuthRequest(Parcel in){
		this.access_token = in.readString();
		this.token_type = in.readString();
		this.scope = in.readString();
		this.expiration = in.readInt();
	}
	//PUBLIC
	//------------------------------------------------------------------
	private Boolean isTokenExpired(){
		Date d = new Date();
		Log.d("MUTIBO","AuthRequest::isTokenExpired expiration: " + expiration + " expirationDate: " + expirationDate);
		return d.compareTo(new Date(expirationDate + expiration)) > 0 ? true : false;
	}
	
	private void refreshToken(Context ctx, String username, String password) {
		 String strTokenUrl = ctx.getResources().getString(R.string.oauth2_endpoint);
		 EasyHttpClient client = new EasyHttpClient();
		 
		 HttpPost mHttppost = new HttpPost();
		 try {
			 mHttppost.setURI(new URI(strTokenUrl));
			 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			 nameValuePairs.add(new BasicNameValuePair("username", username));
			 nameValuePairs.add(new BasicNameValuePair("password", password));
			 nameValuePairs.add(new BasicNameValuePair("grant_type", "password"));
			 nameValuePairs.add(new BasicNameValuePair("client_id", "mobile"));
			 nameValuePairs.add(new BasicNameValuePair("client_secret",""));
			 
			 // NO_WRAP bitmask is important because post request should not have end of line characters in header. For get requests it is ok..
			String header = "Basic " + Base64.encodeToString(client_string.getBytes("UTF-8"), Base64.URL_SAFE|Base64.NO_WRAP);
			mHttppost.setHeader("Authorization", header);
			mHttppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			mHttppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			
			String strResponse  = client.post(mHttppost);
			JSONObject resp = new JSONObject(strResponse);
			this.access_token = resp.getString("access_token");			
		 }
		 catch (URISyntaxException e1) {
			 e1.printStackTrace();
		 } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public String getAccessToken() {
		return access_token;
	}
	
	public String getTokenType(){
		return token_type;
	}
	
	public int getExpiration(){
		return expiration;
	}
	
	public long getExpirationDate(){
		return expirationDate;
	}
	
	public void setExpirationDate(long expirationDate){
		this.expirationDate = expirationDate;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(access_token);
		dest.writeString(token_type);
		dest.writeString(scope);
		dest.writeInt(expiration);
	}
	
	public static final Parcelable.Creator<AuthRequest> CREATOR = new Parcelable.Creator<AuthRequest>()
			{
			    @Override
			    public AuthRequest createFromParcel(Parcel source)
			    {
			        return new AuthRequest(source);
			    }

			    @Override
			    public AuthRequest[] newArray(int size)
			    {
				return new AuthRequest[size];
			    }
			};
	@Override
	public void processResponse(JSONArray arr) {
		
	}

	@Override
	public void processResponse(JSONObject[] stringtoJSON) {
		
	}

}
