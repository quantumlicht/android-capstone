package adapter;

import guay.philippe.capstone.R;

import java.util.List;

import org.json.JSONObject;

import Data.Quiz;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class QuizAdapter extends ArrayAdapter<Quiz>{
	  public View v;
	  private List<Quiz> itemList;
	  private Context context;

	  public QuizAdapter(Context context){  
        super(context, R.layout.newquiz_item);
	  }
	  
	  public QuizAdapter(List<Quiz> itemList, Context ctx) {
		  super(ctx, android.R.layout.simple_list_item_1, itemList);
		  this.itemList = itemList;
		  context = ctx;
	  }
	  
	  @Override
	  public int getCount(){
		  int count = 0;
		  if (itemList != null)
			  count = itemList.size();
		  return count;
	  }
	  @Override
	  public Quiz getItem(int position){
		  Quiz q = null;
		  if (itemList != null)
		  	q = itemList.get(position);
		  return q;
	  }

	  @Override 
	  public View getView(int position, View convertView, ViewGroup parent) {
		  Log.d("MUTIBO", "QuizAdapter::getView");
		  	View v = convertView; 		
		  	if (convertView == null) {
		        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        v = inflater.inflate(R.layout.newquiz_item, null);
	  		}
		  	
		  	Quiz q = itemList.get(position);
		  	
			TextView nameTv = (TextView) v.findViewById(R.id.name);
			TextView ratingTv = (TextView) v.findViewById(R.id.author);
			TextView unrelatedMovieTv = (TextView) v.findViewById(R.id.difficulty);
			
			nameTv.setText(q.getName());
			ratingTv.setText("by: " + q.getAuthor());
			unrelatedMovieTv.setText("(" + Integer.toString(q.getDifficulty()) + ")");
			 
			return v;
			 
	  }
	  
	  public void addToItemList(Quiz item){
		  Log.d("MUTIBO", "QuizAdapter::addToItemList");
		  itemList.add(item);
	  }
	  
	  public List<Quiz> getItemList(){
		  return itemList;
	  }
	  
	  public void setItemList(List<Quiz> itemList) {
		  Log.d("MUTIBO", "QuizAdapter::setItemList");
		  this.itemList = itemList;
	  }
	  
	}