package guay.philippe.capstone;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.json.JSONObject;

import tasks.TaskAuthenticate;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FlowLoginActivity extends Activity implements IApiAccessResponse {
	private TextView mLoginResult;
	private Boolean mRes;
	private String mUsername;
	private String mPassword;
	private TextView mTvUsername;
	private TextView mTvPassword;
	private boolean autologin = false;
	private Context ctx;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = getApplicationContext();
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			autologin = bundle.getBoolean("autologin");
			mUsername = bundle.getString("username");
			mPassword = bundle.getString("password");
		}
		Log.d("MUTIBO", "FlowLoginActivity::onCreate retrieve from intent -> autologin: " + autologin + " username: " + mUsername + " password: " + mPassword);
		
		if (autologin){
			Log.d("MUTIBO", "FlowLoginActivity::onCreate detected Autologin");
			postLoginData();
		} else{
			
			setContentView(R.layout.activity_login);
			
			Button mLoginButton = (Button) findViewById(R.id.login_submit_button);
			TextView mLoginResult = (TextView) findViewById(R.id.login_result);
			mTvPassword = (TextView) findViewById(R.id.password);
			mTvUsername = (TextView) findViewById(R.id.player_name);
			
			mLoginButton.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					mUsername = mTvUsername.getText().toString();
					mPassword = mTvPassword.getText().toString();
					postLoginData();
				}
			});
		}
		
	}

	@Override
	public void postResult(Boolean asyncresult){
	     //This method will get call as soon as your AsyncTask is complete. asyncresult will be your result.
		 Log.d("MUTIBO", "FlowLoginActivity::postResult asyncresult: " + asyncresult);
		 
		 if(asyncresult){
        	Log.d("MUTIBO", "FlowLoginActivity::postResult TRUE");
//        	mLoginResult.setText("Login successful");
        	SharedPreferences prefs = Utils.getStorage(ctx);
        	SharedPreferences.Editor editor = prefs.edit();
        	Log.d("MUTIBO", "FlowLoginActivity::postResult Writing Sharedpreferences: username: " + mUsername+ " password: " + mPassword);
        	editor.putString("username", mUsername);
        	editor.putString("password", mPassword);
        	editor.commit();
        	Intent homeIntent = new Intent(FlowLoginActivity.this, FlowHomePageActivity.class);
			startActivity(homeIntent);
        }
        else {
        	Log.d("MUTIBO", "FlowLoginActivity::postResult FALSE");
//        	mLoginResult.setText(asyncresult.toString());             
        }
	}
	
	private void postLoginData() {
		
		if (!autologin) {
			TextView uname = (TextView) findViewById(R.id.player_name);
	    	String mUsername = uname.getText().toString();
	    	Log.d("MUTIBO", "username" + mUsername);
	    	TextView pword = (TextView) findViewById(R.id.password);
	    	String mPassword = pword.getText().toString();
	    	Log.d("MUTIBO", "password" + mPassword);
		}
    	TaskAuthenticate authObj = new TaskAuthenticate(getApplicationContext().getResources().getString(R.string.auth_endpoint));
    	authObj.delegate = this;
		authObj.execute(mUsername, mPassword);
	}

	@Override
	public void postResult(JSONObject[] jsonObjects) {
		
	}
}
