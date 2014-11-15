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

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class FlowLoginActivity extends ActionBarActivity implements IApiAccessResponse {
	private TextView mLoginResult;
	private Boolean mRes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		Button mLoginButton = (Button) findViewById(R.id.login_submit_button);
		TextView mLoginResult = (TextView) findViewById(R.id.login_result);
		
		mLoginButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				postLoginData();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.rating_group) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void postResult(Boolean asyncresult){
	     //This method will get call as soon as your AsyncTask is complete. asyncresult will be your result.
		 Log.d("MUTIBO", "asyncresult " + asyncresult);
		 
		 if(asyncresult){
        	Log.d("MUTIBO", "TRUE");
//        	mLoginResult.setText("Login successful");
        	Intent homeIntent = new Intent(FlowLoginActivity.this, FlowHomePageActivity.class);
			FlowLoginActivity.this.startActivity(homeIntent);
        }
        else {
        	Log.d("MUTIBO", "FALSE");
        	mLoginResult.setText(asyncresult.toString());             
        }
	}
	
	private void postLoginData() {
		
        // Add user name and password
    	TextView uname = (TextView) findViewById(R.id.username);
    	String username = uname.getText().toString();
    	Log.d("MUTIBO", "username" + username);
    	TextView pword = (TextView) findViewById(R.id.password);
    	String password = pword.getText().toString();
    	Log.d("MUTIBO", "password" + password);
    	
    	TaskAuthenticate authObj = new TaskAuthenticate();
    	authObj.delegate = this;
		authObj.execute(username, password);
	}

	@Override
	public void postResult(JSONObject[] jsonObjects) {
		
	}
}
