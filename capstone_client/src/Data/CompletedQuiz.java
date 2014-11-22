package Data;

import java.util.ArrayList;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class CompletedQuiz implements Parcelable {
	private String quizName;
	private int answer;
	private String playerName;
	private boolean success;
	private boolean hasVoted = false; 
	
	
	// Constructors
	//------------------------------------------------------------------
	public CompletedQuiz(String quizName, String playerName, int answer, boolean success){
		this.quizName = quizName;
		this.playerName = playerName;
		this.answer = answer;
		this.success = success;
	}
	
	public CompletedQuiz(Parcel in){
		this.quizName= in.readString();
		this.playerName = in.readString();
		this.answer = in.readInt();
		this.success = in.readByte() !=0;
		this.hasVoted = in.readByte() !=0;
	}
	// PUBLIC
	//---------------------------------------------------------------------------
	public int getAnswer(){
		return answer;
	}
	
	
	public Boolean getHasVoted(){
		return hasVoted;
	}
	
	public String getQuizName(){
		return quizName;
	}
	
	public Boolean getSuccess(){
		return success;
	}
	
	public String getPlayerName(){
		return playerName;
	}
	
	public void setHasVoted(Boolean hasVoted) {
		this.hasVoted = hasVoted;
	}
	
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(quizName);
		dest.writeString(playerName);
		dest.writeInt(answer);
		dest.writeByte((byte) (success ? 1 : 0));
		dest.writeByte((byte) (hasVoted ? 1 : 0));
	}
	
	public static final Parcelable.Creator<CompletedQuiz> CREATOR = new Parcelable.Creator<CompletedQuiz>()
			{
			    @Override
			    public CompletedQuiz createFromParcel(Parcel source)
			    {
			        return new CompletedQuiz(source);
			    }

			    @Override
			    public CompletedQuiz[] newArray(int size)
			    {
				return new CompletedQuiz[size];
			    }
			};


	@Override
	public int describeContents() {
		return 0;
	}

}
