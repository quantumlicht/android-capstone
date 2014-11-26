package guay.philippe.capstone.data;

import guay.philippe.capstone.Utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

public class AuthRequest implements Parcelable{
	private String access_token;
	private static AuthRequest currentAuthRequest = null;
	private String token_type;
	private String scope;
	private int expiration;
	
	// Constructors
	//------------------------------------------------------------------
	protected AuthRequest(){
		
	}
	//TODO: Set expiration date so that we know if the token is expired
	//TODO: Expose a is expired method.
	public static AuthRequest getCurrentAuthRequest(Context ctx){
		SharedPreferences prefs = Utils.getStorage(ctx);
		String token = prefs.getString("access_token", "");
		String token_type = prefs.getString("token_type", "");
		String scope = prefs.getString("scope", "");
		int expiration = prefs.getInt("expiration", 0);
		
		if (currentAuthRequest == null) {
			currentAuthRequest = new AuthRequest(token, token_type, scope, expiration);
		}
		return currentAuthRequest;
	}
	
	public AuthRequest(String access_token, String token_type, String scope, int expiration){
		this.access_token = access_token;
		this.token_type = token_type;
		this.scope = scope;
		this.expiration = expiration;
		
	}
	
	public AuthRequest(Parcel in){
		this.access_token = in.readString();
		this.token_type = in.readString();
		this.scope = in.readString();
		this.expiration = in.readInt();
	}
	//PUBLIC
	//------------------------------------------------------------------
	public String getAccessToken(){
		return access_token;
	}
	
	public String getTokenType(){
		return token_type;
	}
	
	public int getExpiration(){
		return expiration;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(access_token);
		dest.writeString(token_type);
		dest.writeString(scope);
		dest.writeInt(expiration);
	}
	
	public static final Parcelable.Creator<AuthRequest> CREATOR = new Parcelable.Creator<AuthRequest>()
			{
			    @Override
			    public AuthRequest createFromParcel(Parcel source)
			    {
			        return new AuthRequest(source);
			    }

			    @Override
			    public AuthRequest[] newArray(int size)
			    {
				return new AuthRequest[size];
			    }
			};
}
