package guay.philippe.capstone.fragments;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import guay.philippe.capstone.R;
import guay.philippe.capstone.Utils;
import guay.philippe.capstone.adapter.CompletedQuizAdapter;
import guay.philippe.capstone.auth.EasyHttpClient;
import guay.philippe.capstone.data.CompletedQuiz;
import guay.philippe.capstone.data.Player;
import guay.philippe.capstone.detailViews.DetailCompletedQuizActivity;
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
import android.widget.ListView;

public class CompletedQuizFragment extends ListFragment {
	private ListView mListView;
	private WeakReference<GetCompletedQuizTask> asyncTaskWeakRef;
	private CompletedQuizAdapter mArrayAdapter;
	private BroadcastReceiver mMessageReceiver;
	List<CompletedQuiz> mItemList = new ArrayList<CompletedQuiz>();
	
	
	// PUBLIC
	//--------------------------------------------------------------------
	public static CompletedQuizFragment newInstance() {
		CompletedQuizFragment f =  new CompletedQuizFragment();	   
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
		mMessageReceiver = new BroadcastReceiver() {
			@Override
	    	public void onReceive(Context context, Intent intent){
				Log.d("MUTIBO", "CompletedQuizFragment::BroadcastReceiver::onReceive received event");
				Bundle bundle = intent.getExtras();
				CompletedQuiz q = bundle.getParcelable("completedquiz");
				Boolean replace = bundle.getBoolean("replace");
				if (replace){
					mArrayAdapter.replaceItemInList(q);
				}
				else{
					mArrayAdapter.addToItemList(q);
				}
				mArrayAdapter.notifyDataSetChanged();
			}
		};
		
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
			      new IntentFilter("quiz-completed"));
		
  		startNewAsyncTask();
	}

	//OVERRIDES
	//--------------------------------------------------------------------
//	@Override
//	public  void onResume(){
//		super.onResume();
//		Log.d("MUTIBO", "NewQuizFragment::onResume registerLocalBroadCastReceiver to event 'get-completedquizzes'");
//		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
//		      new IntentFilter("quiz-completed"));
//	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
        View rootView = inflater.inflate(R.layout.fragment_completed_quiz, container, false);
        Log.d("MUTIBO", "CompletedQuizFragment::onCreateView Setting up QuizAdapter");
        
        mListView = (ListView) rootView.findViewById(android.R.id.list);
		mArrayAdapter = new CompletedQuizAdapter(mItemList, getActivity());
		mArrayAdapter.setNotifyOnChange(true);
		
		Log.d("MUTIBO", "CompletedQuizFragment::onCreateView Instantiated adapter");
		setListAdapter(mArrayAdapter);
		Log.d("MUTIBO", "CompletedQuizFragment::onCreateView set ListAdapter");
        return rootView;
    }
	
	@Override 
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l,v,position,id);
		
		Log.d("MUTIBO", "CompletedQuizFragment::onListItemClick " + position);
		CompletedQuiz compQ = (CompletedQuiz) getListView().getItemAtPosition(position);
		
	    Intent intent = new Intent (v.getContext(), DetailCompletedQuizActivity.class);
	    Bundle b = new Bundle();
	    b.putParcelable("completedQuiz", (Parcelable) compQ);
	    
	    intent.putExtra("completedQuiz", compQ);
	    intent.putExtra("hasVoted", compQ.getHasVoted());
        startActivity(intent);
	}
   
	// PRIVATE
	//----------------------------------------------------------------------------------
	private void startNewAsyncTask() {
		GetCompletedQuizTask asyncTask = new GetCompletedQuizTask(this);
	    this.asyncTaskWeakRef = new WeakReference<GetCompletedQuizTask >(asyncTask);
	    Log.d("MUTIBO", "CompletedQuizFragment::startNewAsyncTask executing task");
	    asyncTask.execute();
	}
	
	private class GetCompletedQuizTask extends AsyncTask<String, Void, List<CompletedQuiz>> {
		private WeakReference<CompletedQuizFragment> fragmentWeakRef;
		
		private GetCompletedQuizTask (CompletedQuizFragment fragment) {
	            this.fragmentWeakRef = new WeakReference<CompletedQuizFragment>(fragment);
        }
		
		@Override
		protected List<CompletedQuiz> doInBackground(String... params) {
			HttpResponse response;
			List<CompletedQuiz> result = new ArrayList<CompletedQuiz>();
			Player p = Player.getCurrentPlayer(getActivity().getApplicationContext());
			EasyHttpClient client = new EasyHttpClient();
			
			String strUrl = getActivity().getResources().getString(R.string.completedquiz_base_endpoint) + p.getUsername();
			Log.d("MUTIBO", "CompletedQuizFragment::GetCompletedQuizTask CompletedQuiz Url: " + strUrl);
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
				
	            for (int i=0; i < arr.length(); i++) {
	                result.add(Utils.convertCompletedQuiz(arr.getJSONObject(i)));
	            }
	            Log.d("MUTIBO", "CompletedQuizFragment::GetCompletedQuizTask Returning result->" + result.toString());
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
		protected void onPostExecute(List<CompletedQuiz> result) {
			//super.onPostExecute(result);	
			Intent intent = new Intent("get-completedquizzes");
			  // add data
			intent.putParcelableArrayListExtra("completedQuizzes", (ArrayList<? extends Parcelable>) result);
			Log.d("MUTIBO", "CompletedQuizFragment::onPostExecute Broadcasting event: 'get-completedquizzes'");
			LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent); 
			mArrayAdapter.setItemList(result);
			mArrayAdapter.notifyDataSetChanged();
		}
	}
		
}
	
