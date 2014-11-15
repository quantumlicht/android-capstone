package fragments;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import detailViews.DetailCreatedQuizActivity;
import detailViews.DetailNewQuizActivity;

import guay.philippe.capstone.TaskAuthenticate;
import guay.philippe.capstone.FlowCreateQuizActivity;
import guay.philippe.capstone.FlowHomePageActivity;
import guay.philippe.capstone.IApiAccessResponse;
import guay.philippe.capstone.FlowLoginActivity;
import guay.philippe.capstone.R;
import guay.philippe.capstone.Utils;
import Data.Quiz;
import adapter.QuizAdapter;
import android.app.Activity;
//import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CreatedQuizFragment extends ListFragment {
	private ListView mListView;
	private WeakReference<GetCreatedQuizTask> asyncTaskWeakRef;
	private QuizAdapter mArrayAdapter;
	List<Quiz> mItemList = new ArrayList<Quiz>();
	
	static final int CREATE_QUIZ_REQUEST = 0;
	
	public static ListFragment newInstance() {
	   final ListFragment f =  new CreatedQuizFragment();
	   
	   Bundle args = new Bundle();
       return f;
	}
	
	@Override 
	public void onCreate(Bundle savedInstanceState){
	      super.onCreate(savedInstanceState);
	        setRetainInstance(true);
	        startNewAsyncTask();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
        
		View rootView = inflater.inflate(R.layout.fragment_created_quiz, container, false);
        Log.d("MUTIBO", "CreatedQuizFragment::onCreateView Setting up QuizAdapter");
        
        mListView = (ListView) getActivity().findViewById(R.id.list);
		mArrayAdapter = new QuizAdapter(mItemList, getActivity());
		mArrayAdapter.setNotifyOnChange(true);
		
		Log.d("MUTIBO", "CreatedQuizFragment::onCreateView Instantiated adapter");
		setListAdapter(mArrayAdapter);
		Log.d("MUTIBO", "CreatedQuizFragment::onCreateView set ListAdapter");
		
		Button mButton = (Button) rootView.findViewById(R.id.createButton);
	       mButton.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent (getActivity().getApplicationContext(), FlowCreateQuizActivity.class);
			        startActivityForResult(intent, CREATE_QUIZ_REQUEST);
				}
			});
        return rootView;
    }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode,
            Intent data) {
		Log.d("MUTIBO", "onActivityResult requestCode: " + requestCode + " resultCode: " + resultCode);
        if (requestCode == CREATE_QUIZ_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
            	Quiz q = data.getParcelableExtra("Quiz");
            	mArrayAdapter.addToItemList(q);            	
            	mArrayAdapter.notifyDataSetChanged();
            }
        }
    }
	
	@Override 
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l,v,position,id);
		
		Log.d("MUTIBO", "CreatedQuizFragment::onListItemClick " + position);
		Quiz q = (Quiz) getListView().getItemAtPosition(position);
	    
		Intent intent = new Intent (v.getContext(), DetailCreatedQuizActivity.class);
	    Bundle b = new Bundle();
	    b.putParcelable("quiz", (Parcelable) q);
	    
	    intent.putExtra("quiz", q);
        startActivity(intent);
	}
   
	private void startNewAsyncTask() {
		GetCreatedQuizTask asyncTask = new GetCreatedQuizTask(this);
	    this.asyncTaskWeakRef = new WeakReference<GetCreatedQuizTask >(asyncTask);
	    Log.d("MUTIBO", "CreatedQuizFragment::startNewAsyncTask executing task");
	    asyncTask.execute();
	}
	
	private class GetCreatedQuizTask extends AsyncTask<String, Void, List<Quiz>> {
		private WeakReference<CreatedQuizFragment> fragmentWeakRef;
		
		 private GetCreatedQuizTask (CreatedQuizFragment fragment) {
	            this.fragmentWeakRef = new WeakReference<CreatedQuizFragment>(fragment);
        }
		
		@Override
		protected List<Quiz> doInBackground(String... params) {
			List<Quiz> result = new ArrayList<Quiz>();
			try{
				Log.d("MUTIBO", "execution Quiz GET request");
				URL u;
				u = new URL("http://10.0.2.2:8080/quiz");
				HttpURLConnection conn = (HttpURLConnection) u.openConnection();
	            conn.setRequestMethod("GET");
	            conn.connect();
	            Log.d("MUTIBO", "CreatedQuizFragment::GetCreatedQuizTask Gettting Connection");
	            InputStream is = conn.getInputStream();
	            
	            // Read the stream
	            byte[] b = new byte[1024];
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            while ( is.read(b) != -1)
	            baos.write(b);
	            String JSONResp = new String(baos.toByteArray());
	            JSONArray arr = new JSONArray(JSONResp);
	            
	            for (int i=0; i < arr.length(); i++) {
	                result.add(Utils.convertQuiz(arr.getJSONObject(i)));
	            }
	            Log.d("MUTIBO", "CreatedQuizFragment::GetCreatedQuizTask Returning result->" + result.toString());
	            conn.disconnect();
	        	return result;
			}
			catch (Throwable t){
				t.printStackTrace();
			}
			finally {
				Log.d("MUTIBO", "Finally Block");
				return result;
			}

		}
		
		@Override
		protected void onPostExecute(List<Quiz> result) {
//			super.onPostExecute(result);
			if (this.fragmentWeakRef.get() != null) {	
				mArrayAdapter.setItemList(result);
			
				Log.d("MUTIBO", "CreatedQuizFragment::onPostExecute");
				mArrayAdapter.notifyDataSetChanged();
			}
			Log.d("MUTIBO", "CreatedQuizFragment::onPostExecute NULL results");
		}

		
		
	}	
}
	
