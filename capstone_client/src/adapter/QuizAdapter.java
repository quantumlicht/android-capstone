package adapter;

import guay.philippe.capstone.R;

import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import Data.Quiz;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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
	  private String mItemListTemplate;
	  public QuizAdapter(Context context){  
        super(context, R.layout.item_newquiz);
	  }
	  
	  public QuizAdapter(List<Quiz> itemList, Context ctx, String itemListTemplate) {
		  super(ctx, android.R.layout.simple_list_item_1, itemList);
		  this.itemList = itemList;
		  mItemListTemplate = itemListTemplate;
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
	  public void notifyDataSetChanged() {
	      //TODO: Sort by rating order
		  
	      super.notifyDataSetChanged();
	  }
	  
	  @Override 
	  public View getView(int position, View convertView, ViewGroup parent) {
		  
		  	View v = convertView;
		  	Quiz q = itemList.get(position);
		  	if (convertView == null) {
		        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        if (mItemListTemplate =="createdQuiz") {
		        	v = inflater.inflate(R.layout.item_createdquiz, null);
		        	setContentForCreatedQuiz(v, q);
		        }
		        else if (mItemListTemplate =="newQuiz"){
		        	v = inflater.inflate(R.layout.item_newquiz, null);
		        	setContentForNewQuiz(v, q);
		        }
		        else{
		        	v = inflater.inflate(R.layout.item_newquiz, null);
		        	setContentForNewQuiz(v, q);
		        }
	  		}
			return v; 
	  }
	  
	  public void addToItemList(Quiz item){
		  Log.d("MUTIBO", "QuizAdapter::addToItemList");
		  itemList.add(item);
	  }
	  
	  public List<Quiz> getItemList(){
		  return itemList;
	  }
	  
	  public void removeFromItemList(Quiz q) {
		  
		  for (Iterator<Quiz> iter = itemList.listIterator(); iter.hasNext(); ) {
			    Quiz quiz = iter.next();
			    if (quiz.getName().equals(q.getName())) {
			    	Log.d("MUTIBO", "QuizAdapter::removeFromItemList found elem to remove");
			        iter.remove();
			    }
		  }
		  Log.d("MUTIBO", "QuizAdapter::removeFromItemList removing Quiz: " + q.toString() + " from itemList. new size: "+ itemList.size());
	  }
	  
	  public void setItemList(List<Quiz> itemList) {
		  Log.d("MUTIBO", "QuizAdapter::setItemList");
		  this.itemList = itemList;
	  }
	  // PRIVATE
	  //----------------------------------------------------------------------------------
	  private void setContentForNewQuiz(View v, Quiz q){
		  TextView nameTv = (TextView) v.findViewById(R.id.quiz_name_new);
		  TextView authorTv = (TextView) v.findViewById(R.id.author);
		  TextView difficultyTv = (TextView) v.findViewById(R.id.difficulty_new);
			
		  nameTv.setText(q.getName());
		  authorTv.setText("by: " + q.getAuthor());
		  difficultyTv.setText("(" + Integer.toString(q.getDifficulty()) + ")");
	  }
	  
	  private void setContentForCreatedQuiz(View v, Quiz q){
		  TextView nameTv = (TextView) v.findViewById(R.id.quiz_name_created);
		  TextView ratingTv = (TextView) v.findViewById(R.id.rating);
		  TextView difficultyTv = (TextView) v.findViewById(R.id.difficulty_created);
		  nameTv.setText(q.getName());
		  ratingTv.setText(Integer.toString(q.getRating()));
		  Log.d("MUTIBO", "QuizAdapter::setContentForCreatedQuiz quiz rating: " + q.getRating());
		  if (q.getRating() < 0) {
			  ratingTv.setBackgroundColor(Color.RED);
		  }
		  else {
			  ratingTv.setBackgroundColor(Color.GREEN);
		  }
		  difficultyTv.setText("(" + Integer.toString(q.getDifficulty()) + ")");
	  }
	}