package guay.philippe.capstone.fragments;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tasks.TaskAuthenticate;
import tasks.TaskGetNewQuiz;


import guay.philippe.capstone.FlowHomePageActivity;
import guay.philippe.capstone.IApiAccessResponse;
import guay.philippe.capstone.FlowLoginActivity;
import guay.philippe.capstone.R;
import guay.philippe.capstone.Utils;
import guay.philippe.capstone.adapter.QuizAdapter;
import guay.philippe.capstone.auth.EasyHttpClient;
import guay.philippe.capstone.data.CompletedQuiz;
import guay.philippe.capstone.data.Player;
import guay.philippe.capstone.data.Quiz;
import guay.philippe.capstone.detailViews.DetailNewQuizActivity;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NewQuizFragment extends ListFragment {
	
	private ListView mListView;
	private TextView mIntroText;
	private String introtext;
	private WeakReference<GetNewQuizTask> asyncTaskWeakRef;
	private BroadcastReceiver mMessageReceiver;
	private QuizAdapter mArrayAdapter;
	private TextView mNbNewQuiz;
	private ArrayList<CompletedQuiz> completedQuizzes;
	List<Quiz> mItemList = new ArrayList<Quiz>();
	
	static final int NEW_QUIZ_REQUEST = 1;
	
	//PUBLIC
	//-------------------------------------------------
	public static NewQuizFragment newInstance() {
	   NewQuizFragment f =  new NewQuizFragment();
       return f;
	}
	
	public void refresh(){
		startNewAsyncTask();
	}
	
	//OVERRIDES
	//--------------------------------------------------------------------
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("MUTIBO", "NewQuizFragment::onActivityResult requestCode: " + requestCode + " resultCode: " + resultCode);
        
		if (requestCode == NEW_QUIZ_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
            	Quiz q = data.getParcelableExtra("Quiz");
            	Log.d("MUTIBO", "NewQuizFragment::onActivityResult ArrayAdapter Size " + mArrayAdapter.getCount());
            	Log.d("MUTIBO", "NewQuizFragment::onActivityResult ArrayAdapter position " + mArrayAdapter.getPosition(q));
            	mArrayAdapter.removeFromItemList(q);
            	mNbNewQuiz.setText(Integer.toString(mArrayAdapter.getCount()));
            	mArrayAdapter.notifyDataSetChanged();
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
            	//Was not able to set the vote correctly;
            	Log.d("MUTIBO", "NewQuizFragment::onActivityResult Did not set the vote properly");
            }
        }
    }
	
	@Override 
	public void onCreate(Bundle savedInstanceState){
	      super.onCreate(savedInstanceState);
	      setRetainInstance(true);
	      
	      mMessageReceiver = new BroadcastReceiver() {
	    	  @Override
	    	  public void onReceive(Context context, Intent intent){
	    		  Log.d("MUTIBO", "NewQuizFragment::BroadcastReceiver::onReceive received event");
	    		  Bundle bundle = intent.getExtras();
	    		  completedQuizzes = bundle.getParcelableArrayList("completedQuizzes");
	    		  Log.d("MUTIBO", "NewQuizFragment::BroadcastReceiver::onReceive size of bundle: " + completedQuizzes.size());
	    	      startNewAsyncTask();
	    	  }
	      };
	}
	
	@Override
	public  void onResume(){
		super.onResume();
		Log.d("MUTIBO", "NewQuizFragment::onResume registerLocalBroadCastReceiver to event 'get-completedquizzes'");
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
		      new IntentFilter("get-completedquizzes"));
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
        View rootView = inflater.inflate(R.layout.fragment_new_quiz, container, false);
        Log.d("MUTIBO", "NewQuizFragment::onCreateView Setting up QuizAdapter");
        
        mListView = (ListView) rootView.findViewById(android.R.id.list);
        mNbNewQuiz = (TextView) rootView.findViewById(R.id.nb_new_quiz);
		mArrayAdapter = new QuizAdapter(mItemList, getActivity(), "newQuiz");
		mArrayAdapter.setNotifyOnChange(true);
		setListAdapter(mArrayAdapter);
	
        return rootView;
    }
	
	@Override 
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l,v,position,id);
		
		Log.d("MUTIBO", "NewQuizFragment::onListItemClick " + position);
		Quiz q = (Quiz) getListView().getItemAtPosition(position);
		
	    Intent intent = new Intent (v.getContext(), DetailNewQuizActivity.class);
	    Bundle b = new Bundle();
	    b.putParcelable("quiz", (Parcelable) q);
	    
	    intent.putExtra("quiz", q);
        startActivityForResult(intent, NEW_QUIZ_REQUEST);
	}
	
	// PRIVATE
    //--------------------------------------------------------------------------
	
	private void startNewAsyncTask() {
		GetNewQuizTask asyncTask = new GetNewQuizTask(this);
	    this.asyncTaskWeakRef = new WeakReference<GetNewQuizTask >(asyncTask);
	    Log.d("MUTIBO", "NewQuizFragment::startNewAsyncTask executing task");
	    asyncTask.execute();
	}
	
	private List<Quiz> filterQuizForUser(ArrayList<CompletedQuiz> compQuizzes, List<Quiz> quizzes){
		Player p = Player.getCurrentPlayer(getActivity().getApplicationContext());
		for(Iterator<Quiz> iterQ = quizzes.listIterator(); iterQ.hasNext(); ){
	    	Quiz q = iterQ.next();
	    	if (q.getAuthor().equals(p.getUsername())) {
	    		//Log.d("MUTIBO", "NewQuizFragment::filterQuizForUser removing quiz for same author");
	    		iterQ.remove();
	    	}
	    	else {
	    		for (Iterator<CompletedQuiz> iter = compQuizzes.listIterator(); iter.hasNext(); ) {
		    		CompletedQuiz compQ = iter.next();
		    		if (compQ.getQuizName().equals(q.getName())) {
		    			Log.d("MUTIBO", "NewQuizFragment::filterQuizForUser removing quiz because it's completed");
		    			iterQ.remove();
		    		}
		    	}
	    	}
		}
		return quizzes;
	}
	
	// CLASS
	//--------------------------------------------------------------------------
	private class GetNewQuizTask extends AsyncTask<String, Void, List<Quiz>> {
		private WeakReference<NewQuizFragment> fragmentWeakRef;
		
		 private GetNewQuizTask (NewQuizFragment fragment) {
	            this.fragmentWeakRef = new WeakReference<NewQuizFragment>(fragment);
        }
		
		@Override
		protected List<Quiz> doInBackground(String... params) {
			List<Quiz> result = new ArrayList<Quiz>();
			String strUrl = getResources().getString(R.string.quiz_base_endpoint);
			EasyHttpClient client = new EasyHttpClient();
			HttpResponse response;
			
			try{
				URI u = new URI(strUrl);
				HttpGet request = Utils.setToken(getActivity(), new HttpGet());
				request.setURI(u);
				request.addHeader("Content-Type", "application/json");
				response = client.execute(request);
				
//				HttpURLConnection conn = (HttpURLConnection) u.openConnection();
//	            conn.setRequestMethod("GET");
//	            conn.connect();
				JSONArray arr = null;
				int statuscode = response.getStatusLine().getStatusCode();
				if (statuscode == 200) {
					arr = new JSONArray(EntityUtils.toString(response.getEntity()));
				}
//	            Log.d("MUTIBO", "NewQuizFragment::GetNewQuizTask Getting Connection");
//	            InputStream is = conn.getInputStream();
//	            // Read the stream
//	            byte[] b = new byte[1024];
//	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//	            while ( is.read(b) != -1) {
//	            	baos.write(b);
//	            }
//	            String JSONResp = new String(baos.toByteArray());
//	            Log.d("MUTIBO", "NewQuizFragment::GetNewQuizTask response: " + JSONResp);
//	            JSONArray arr = new JSONArray(JSONResp);
	            if (arr != null) {
	            	for (int i=0; i < arr.length(); i++) {
		                result.add(Utils.convertQuiz(arr.getJSONObject(i)));
		            }
		            Log.d("MUTIBO", "NewQuizFragment::GetNewQuizTask Returning result->" + result.toString());
	            }
	            
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
			super.onPostExecute(result);
			if (this.fragmentWeakRef.get() != null) {
				if (completedQuizzes !=null) {
					List<Quiz> filterQuizzes = filterQuizForUser(completedQuizzes, result);
					Log.d("MUTIBO", "NewQuizFragment::onPostExecute, initial size: " + result.size() + " new size: " + filterQuizzes.size());
					mNbNewQuiz.setText(Integer.toString(filterQuizzes.size()));
					mArrayAdapter.setItemList(filterQuizzes);
					mArrayAdapter.notifyDataSetChanged();
				}
				else {
					Log.e("MUTIBO", "NewQuizFragment::onPostExecute completeQuizzes is null");
				}
				
			}
		}
	} // end GetNewQuizTask
	
	
}
	
