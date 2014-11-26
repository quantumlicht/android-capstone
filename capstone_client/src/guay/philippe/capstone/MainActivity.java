package guay.philippe.capstone;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button mButtonRegister = (Button) findViewById(R.id.register_button);
        Button mButtonLogin = (Button) findViewById(R.id.login_button);
        mButtonRegister.setOnClickListener(new Button.OnClickListener(){
    		public void onClick(View v){
    			Intent intent = new Intent(MainActivity.this, FlowRegisterActivity.class);
    			MainActivity.this.startActivity(intent);
    		}
        });
        
        SharedPreferences prefs = Utils.getStorage(getApplicationContext());
        String username = prefs.getString("username", "");
        String password = prefs.getString("password", "");
        String accessToken = prefs.getString("access_token", "");
        
        Log.d("MUTIBO", "MainActivity::onCreate retrieved from sharedPrefs -> username: " + username + " password: " + password + " token: " + accessToken);
        if (!username.equals("") && !password.equals("") && !accessToken.equals("")) {
        	// We have all we need to do our requests. No need to go through the login flow.
        	Intent intent = new Intent(MainActivity.this, FlowHomePageActivity.class);
        	//intent.putExtra("autologin", true);
        	//intent.putExtra("username", username);
        	//intent.putExtra("password", password);
        	//intent.putExtra("access_token", accessToken);
        	startActivity(intent);
        }
        
    	mButtonLogin.setOnClickListener(new Button.OnClickListener(){
    		public void onClick(View v){
    			Intent intent = new Intent(MainActivity.this, FlowLoginActivity.class);   			
    			startActivity(intent);
    		}
    	});
        
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.rating_group) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
