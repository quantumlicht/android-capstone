package guay.philippe.capstone.adapter;

import guay.philippe.capstone.R;
import guay.philippe.capstone.data.CompletedQuiz;
import guay.philippe.capstone.data.Quiz;

import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CompletedQuizAdapter extends ArrayAdapter<CompletedQuiz>{
	  public View v;
	  private List<CompletedQuiz> itemList;
	  private Context context;

	  public CompletedQuizAdapter(Context context){  
        super(context, R.layout.item_newquiz);
	  }
	  
	  public CompletedQuizAdapter(List<CompletedQuiz> itemList, Context ctx) {
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
	  public CompletedQuiz getItem(int position){
		  CompletedQuiz q = null;
		  if (itemList != null)
		  	q = itemList.get(position);
		  return q;
	  }

	  @Override 
	  public View getView(int position, View convertView, ViewGroup parent) {
		  	View v = convertView; 		
		  	if (convertView == null) {
		        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        v = inflater.inflate(R.layout.item_completedquiz, null);
	  		}
		  	
		  	CompletedQuiz q = itemList.get(position);
		  	
			TextView quizNameTv = (TextView) v.findViewById(R.id.quiz_name_completed);
			TextView successTv = (TextView) v.findViewById(R.id.success);
			quizNameTv.setText(q.getQuizName());
			
			String successText;
			if (q.getSuccess()){
				successText = "Success";
				successTv.setBackgroundColor(Color.GREEN);
			}
			else {
				successText = "Missed";
				successTv.setBackgroundColor(Color.RED);
			}
			successTv.setText(successText);
			return v; 
	  }
	  
	  public void addToItemList(CompletedQuiz item){
		  Log.d("MUTIBO", "CompletedQuizAdapter::addToItemList");
		  itemList.add(item);
	  }
	  
	  public List<CompletedQuiz> getItemList(){
		  return itemList;
	  }
	  
	  public void removeFromItemList(CompletedQuiz q) {
		  Log.d("MUTIBO", "CompletedQuizAdapter::removeFromItemList removing Quiz: " + q.toString() + " from itemList. new size: "+ itemList.size());
		  for (Iterator<CompletedQuiz> iter = itemList.listIterator(); iter.hasNext(); ) {
			  CompletedQuiz compQ = iter.next();
			  if (compQ.getQuizName().equals(q.getQuizName())) {
				  Log.d("MUTIBO", "CompletedQuizAdapter::removeFromItemList found elem to remove");
				  iter.remove();
			  }
		  }
	  }
	  
	  public void setItemList(List<CompletedQuiz> itemList) {
		  Log.d("MUTIBO", "CompletedQuizAdapter::setItemList");
		  this.itemList = itemList;
	  }
	  
	}