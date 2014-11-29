package guay.philippe.capstone.fragments;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;



import guay.philippe.capstone.FlowCreateQuizActivity;
import guay.philippe.capstone.FlowHomePageActivity;
import guay.philippe.capstone.IApiAccessResponse;
import guay.philippe.capstone.FlowLoginActivity;
import guay.philippe.capstone.R;
import guay.philippe.capstone.Utils;
import guay.philippe.capstone.adapter.QuizAdapter;
import guay.philippe.capstone.auth.EasyHttpClient;
import guay.philippe.capstone.data.Player;
import guay.philippe.capstone.data.Quiz;
import guay.philippe.capstone.detailViews.DetailCreatedQuizActivity;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CreatedQuizFragment extends ListFragment {
	private ListView mListView;
	private WeakReference<GetCreatedQuizTask> asyncTaskWeakRef;
	private QuizAdapter mArrayAdapter;
	List<Quiz> mItemList = new ArrayList<Quiz>();
	
	static final int CREATE_QUIZ_REQUEST = 0;
	static final int UPDATE_DELETE_QUIZ_REQUEST = 1;
	static final int DELETE_QUIZ_REQUEST = 2;
	
	// PUBLIC
	//--------------------------------------------------------------------
	public static CreatedQuizFragment newInstance() {
	   final CreatedQuizFragment f =  new CreatedQuizFragment();
       return f;
	}
	
	public void refresh(){
		startNewAsyncTask();
	}

	//OVERRIDES
	//--------------------------------------------------------------------
	@Override 
	public void onCreate(Bundle savedInstanceState){
	      super.onCreate(savedInstanceState);
	        setRetainInstance(true);
	        startNewAsyncTask();
	}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
        
		View rootView = inflater.inflate(R.layout.fragment_created_quiz, container, false);
        Log.d("MUTIBO", "CreatedQuizFragment::onCreateView Setting up QuizAdapter");
        
        mListView = (ListView) rootView.findViewById(android.R.id.list);
		mArrayAdapter = new QuizAdapter(mItemList, getActivity(), "createdQuiz");
		mArrayAdapter.setNotifyOnChange(true);
		setListAdapter(mArrayAdapter);
		
		Button mCreateButton = (Button) rootView.findViewById(R.id.createButton);
		mCreateButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent (getActivity().getApplicationContext(), FlowCreateQuizActivity.class);
		        startActivityForResult(intent, CREATE_QUIZ_REQUEST);
			}
		});
        return rootView;
    }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("MUTIBO", "CreatedQuizFragment::onActivityResult requestCode: " + requestCode + " resultCode: " + resultCode);
        if (requestCode == CREATE_QUIZ_REQUEST) {
        	Log.d("MUTIBO", "CreatedQuizFragment::onActivityResult CREATE");
            if (resultCode == Activity.RESULT_OK) {
            	Quiz q = data.getParcelableExtra("Quiz");
            	mArrayAdapter.addToItemList(q);        	
            	mArrayAdapter.notifyDataSetChanged();
            }
        }
        else if (requestCode == UPDATE_DELETE_QUIZ_REQUEST) {
        	Log.d("MUTIBO", "CreatedQuizFragment::onActivityResult UPDATE_DELETE");
        	if (resultCode == Activity.RESULT_OK) {
            	Quiz q = data.getParcelableExtra("Quiz");
            	Boolean toDelete = data.getBooleanExtra("delete", false);
            	if (toDelete){
            		Log.d("MUTIBO", "CreatedQuizFragment::onActivityResult deleting quiz");
            		mArrayAdapter.removeFromItemList(q);
            	}
            	else {
            		Log.d("MUTIBO", "CreatedQuizFragment::onActivityResult updating quiz");
            		mArrayAdapter.replaceItemInList(q);
            	}
            	        	
            	mArrayAdapter.notifyDataSetChanged();
            }
        }
        else {
        	Log.d("MUTIBO", "CreatedQuizFragment::onActivityResult requestCode does not match expected codes");
        }
    }
	
	@Override 
	public void onListItemClick(ListView l, View v, int position, long id) {
//		super.onListItemClick(l,v,position,id);
		
		Log.d("MUTIBO", "CreatedQuizFragment::onListItemClick " + position);
		Quiz q = (Quiz) getListView().getItemAtPosition(position);
	    
		Intent intent = new Intent (v.getContext(), DetailCreatedQuizActivity.class);
	    Bundle b = new Bundle();
	    b.putParcelable("quiz", (Parcelable) q);
	    
	    intent.putExtra("quiz", q);
        startActivityForResult(intent, UPDATE_DELETE_QUIZ_REQUEST);
	}
	// PRIVATE
	//----------------------------------------------------------------------------------
	private void startNewAsyncTask() {
		GetCreatedQuizTask asyncTask = new GetCreatedQuizTask(this);
	    this.asyncTaskWeakRef = new WeakReference<GetCreatedQuizTask >(asyncTask);
	    Log.d("MUTIBO", "CreatedQuizFragment::startNewAsyncTask executing task");
	    asyncTask.execute();
	}
	
	// PRIVATE CLASS
	//----------------------------------------------------------------------------------
	private class GetCreatedQuizTask extends AsyncTask<String, Void, List<Quiz>> {
		private WeakReference<CreatedQuizFragment> fragmentWeakRef;
		
		 private GetCreatedQuizTask (CreatedQuizFragment fragment) {
	            this.fragmentWeakRef = new WeakReference<CreatedQuizFragment>(fragment);
        }
		
		@Override
		protected List<Quiz> doInBackground(String... params) {
			List<Quiz> result = new ArrayList<Quiz>();
			Player p = Player.getCurrentPlayer(getActivity().getApplicationContext());
			EasyHttpClient client = new EasyHttpClient();
			HttpResponse response;
			
			String strUrl = getActivity().getResources().getString(R.string.quiz_by_author_endpoint) + p.getUsername();
			Log.d("MUTIBO", "CreatedQuizFragment::GetCreatedQuizTask Quiz Url: " + strUrl);
			try{
				
				URI u = new URI(strUrl);
				HttpGet request = Utils.setToken(getActivity(), new HttpGet());
				request.setURI(u);
				request.addHeader("Content-Type", "application/json");
				response = client.execute(request);
			
				JSONArray arr = null;
				int statuscode = response.getStatusLine().getStatusCode();
				if (statuscode == 200) {
					arr = new JSONArray(EntityUtils.toString(response.getEntity()));
				}
				
				if (arr != null) {
					for (int i=0; i < arr.length(); i++) {
						result.add(Utils.convertQuiz(arr.getJSONObject(i)));
					}
				}
	        	return result;
			}
			catch (Throwable t){
				t.printStackTrace();
			}
			finally {
				return result;
			}

		}
		
		@Override
		protected void onPostExecute(List<Quiz> result) {
			super.onPostExecute(result);
			for (Iterator<Quiz> iter = result.listIterator(); iter.hasNext();) {
				Quiz q = iter.next();
				Log.d("MUTIBO", "CreatedQuizFragment::GetCreatedQuizTask::onPostExecute quizName: " + q.getName() + " rating: " + q.getRating());
			}
			mArrayAdapter.setItemList(result);
			mArrayAdapter.notifyDataSetChanged();
		}
	}	// end GetCreatedQuizTask
	

}
	
