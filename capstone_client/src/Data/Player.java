package Data;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Player implements Parcelable {
	
	private String username;
	private String passwordHash;
	public Player(String username, String passwordHash){
		this.username = username;
		this.passwordHash = passwordHash;
	}
	
	public Player(Parcel in){
		this.username = in.readString();
		this.passwordHash = in.readString();
	}
	
	public String getUsername(){
		return username;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(username);
		dest.writeString(passwordHash);
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
