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

import detailViews.DetailCompletedQuizActivity;
import detailViews.DetailNewQuizActivity;

import guay.philippe.capstone.TaskAuthenticate;

import guay.philippe.capstone.TaskGetNewQuiz;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CompletedQuizFragment extends ListFragment {
	private ListView mListView;
	private WeakReference<GetCompletedQuizTask> asyncTaskWeakRef;
	private QuizAdapter mArrayAdapter;
	List<Quiz> mItemList = new ArrayList<Quiz>();
	
	public static ListFragment newInstance() {
	   ListFragment f =  new CompletedQuizFragment();
	   
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
        View rootView = inflater.inflate(R.layout.fragment_new_quiz, container, false);

        Log.d("MUTIBO", "CompletedQuizFragment::onCreateView Setting up QuizAdapter");
        mListView = (ListView) getActivity().findViewById(R.id.list);
		mArrayAdapter = new QuizAdapter(mItemList, getActivity());
		Log.d("MUTIBO", "CompletedQuizFragment::onCreateView Instantiated adapter");
		setListAdapter(mArrayAdapter);
		Log.d("MUTIBO", "CompletedQuizFragment::onCreateView set ListAdapter");
        return rootView;
    }
	
	@Override 
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l,v,position,id);
		
		Log.d("MUTIBO", "CompletedQuiz::onListItemClick " + position);
		Quiz q = (Quiz) getListView().getItemAtPosition(position);
	    Intent intent = new Intent (v.getContext(), DetailCompletedQuizActivity.class);
	    Bundle b = new Bundle();
	    b.putParcelable("quiz", (Parcelable) q);
	    
	    intent.putExtra("quiz", q);
        startActivity(intent);
	}
   
	
   
	private void startNewAsyncTask() {
		GetCompletedQuizTask asyncTask = new GetCompletedQuizTask(this);
	    this.asyncTaskWeakRef = new WeakReference<GetCompletedQuizTask >(asyncTask);
	    Log.d("MUTIBO", "CompletedQuizFragment::startNewAsyncTask executing task");
	    asyncTask.execute();
	}
	
	private class GetCompletedQuizTask extends AsyncTask<String, Void, List<Quiz>> {
		private WeakReference<CompletedQuizFragment> fragmentWeakRef;
		
		 private GetCompletedQuizTask (CompletedQuizFragment fragment) {
	            this.fragmentWeakRef = new WeakReference<CompletedQuizFragment>(fragment);
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
	            Log.d("MUTIBO", "CompletedQuizFragment::GetCompletedQuizTask Gettting Connection");
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
	            Log.d("MUTIBO", "CompletedQuizFragment::GetCompletedQuizTask Returning result->" + result.toString());
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
			
				Log.d("MUTIBO", "CompletedQuizFragment::onPostExecute");
				mArrayAdapter.notifyDataSetChanged();
			}
			Log.d("MUTIBO", "CompletedQuizFragment::onPostExecute NULL results");
		}

		
		
	}
		
}
	
