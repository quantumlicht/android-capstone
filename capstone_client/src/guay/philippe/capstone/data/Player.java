package guay.philippe.capstone.data;


import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

public class Player implements Parcelable {
	private static Player currentPlayer  = null;
	private static final String STORAGE_FILE = "mutibo";
	private String username;
	private String passwordHash;
	private int score;
	
	// Constructors
	//------------------------------------------------------------------
	protected Player(){
	}
	
	public static Player getCurrentPlayer(Context ctx){
		SharedPreferences prefs = ctx.getSharedPreferences(STORAGE_FILE, 0);
		String username = prefs.getString("username", "");
		String password = prefs.getString("password", "");
		int score = prefs.getInt("score", 0);
		
		if (currentPlayer == null) {
			currentPlayer = new Player(username, password, score);
		}
		return currentPlayer;
	}
	
	public Player(String username, String passwordHash, int score){
		this.username = username;
		this.passwordHash = passwordHash;
		this.score = score;
	}
	
	public Player(Parcel in){
		this.username = in.readString();
		this.passwordHash = in.readString();
		this.score = in.readInt();
	}
	
	//PUBLIC
	//------------------------------------------------------------------
	
//	public void addCompletedQuiz(CompletedQuiz compQ){
//		CompletedQuiz[] newArray = new CompletedQuiz[completedQuizzes.length + 1];
//		newArray[completedQuizzes.length]  = compQ;
//	    System.arraycopy(completedQuizzes, 0, newArray, 0, completedQuizzes.length);
//	    completedQuizzes = newArray;
//	}
	
	public String getUsername(){
		return username;
	}
	
	public int getScore(){
		return score;
	}
	
	public void setScore(int score){
		this.score = score;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(username);
		dest.writeString(passwordHash);
		dest.writeInt(score);
	}
	
	public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>()
			{
			    @Override
			    public Player createFromParcel(Parcel source)
			    {
			        return new Player(source);
			    }

			    @Override
			    public Player[] newArray(int size)
			    {
				return new Player[size];
			    }
			};
	
}
