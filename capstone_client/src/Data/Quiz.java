package Data;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Quiz implements Parcelable{
	
	private String name;
	private int id;
	private int rating;
	private String justification;
	private int unrelatedMovie;
	private int authorId;
	private String author;
	private ArrayList<String> movieSet;
	private int difficulty;
	
	// Constructors
	//------------------------------------------------------------------
	public Quiz(String name,String author, int difficulty, String justification, int unrelatedMovie, ArrayList<String> movieSet, int rating){
		this.name = name;
		this.author = author;
		this.difficulty = difficulty;
		this.justification = justification;
		this.unrelatedMovie = unrelatedMovie;
		this.movieSet = movieSet;
		this.rating = rating;
	}
	
	public Quiz(Parcel in){
		this.name = in.readString();
		this.author = in.readString();
		this.difficulty = in.readInt();
		this.justification = in.readString();
		this.unrelatedMovie = in.readInt();
		this.movieSet = in.createStringArrayList();
		this.rating = in.readInt();
		this.id = in.readInt();
	}
	
	public int getRating() {
		return rating;
	}
	
	public String getAuthor(){
		return author;
	}
	
	public String getName() {
		return name;
	}
	
	public int getDifficulty() {
		return difficulty;
	}
	
	public ArrayList<String> getMovieSet(){
		return movieSet;
	}
	
	public String getJustification(){
		return justification;
	}
	
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	public void setAuthorId(int authorId){
		this.authorId = authorId;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	
	public int getUnrelatedMovie() {
		return unrelatedMovie;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(author);
		dest.writeInt(difficulty);
		dest.writeString(justification);
		dest.writeInt(unrelatedMovie);
		dest.writeStringList(movieSet);
		dest.writeInt(rating);
		dest.writeInt(id);
	}
	
	public static final Parcelable.Creator<Quiz> CREATOR = new Parcelable.Creator<Quiz>()
			{
			    @Override
			    public Quiz createFromParcel(Parcel source)
			    {
			        return new Quiz(source);
			    }

			    @Override
			    public Quiz[] newArray(int size)
			    {
				return new Quiz[size];
			    }
			};

}
