package guay.philippe.capstone.mutibo.client;

import guay.philippe.capstone.quiz.repository.CompletedQuiz;
import guay.philippe.capstone.quiz.repository.Player;
import guay.philippe.capstone.quiz.repository.Quiz;

import java.util.Collection;

import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * This interface defines an API for a VideoSvc. The
 * interface is used to provide a contract for client/server
 * interactions. The interface is annotated with Retrofit
 * annotations so that clients can automatically convert the
 * 
 * 
 * @author jules
 *
 */
public interface MutiboSvcApi {
	
	public static final int MINIMUM_ALLOWED_RATING = -3;
	public static final String PASSWORD_PARAMETER = "password";
	public static final String USERNAME_PARAMETER = "username";
	public static final String NAME_PARAMETER = "name";
	public static final String DURATION_PARAMETER = "duration";
	public static final String LOGIN_PATH = "/login";
	
	
	public static final String LOGOUT_PATH = "/logout";
	
	// The path where we expect the VideoSvc to live
	public static final String QUIZ_SVC_PATH = "/quiz";
	
	public static final String PLAYER_SVC_PATH = "/player";
	public static final String PLAYER_AUTH_PATH = "player/auth";
	
	public static final String COMPLETED_QUIZ_SVC_PATH = "/completedquiz";

	// The path to search videos by title
	public static final String VIDEO_NAME_SEARCH_PATH = QUIZ_SVC_PATH + "/search/findByName";
	public static final String PLAYER_NAME_SEARCH_PATH = QUIZ_SVC_PATH + "/player/findByName";
	public static final String COMPLETED_QUIZ_DURATION_SEARCH_PATH = QUIZ_SVC_PATH + "/player/findByName"; 
	// The path to search videos by title
//	public static final String VIDEO_DURATION_SEARCH_PATH = QUIZ_SVC_PATH + "/search/findByDurationLessThan";

	@FormUrlEncoded
	@POST(LOGIN_PATH)
	public Void login(@Field(USERNAME_PARAMETER) String username, @Field(PASSWORD_PARAMETER) String pass);
	
	@GET(LOGOUT_PATH)
	public Void logout();
	
	//-----------------------------------	
	@GET(QUIZ_SVC_PATH)
	public Collection<Quiz> getQuizList();
	//-----------------------------------
	@POST(QUIZ_SVC_PATH)
	public Void addQuiz(@Body Quiz q);
	//-----------------------------------
	@PUT(QUIZ_SVC_PATH)
	public Void updateQuiz(@Body Quiz q);
	
	@GET(QUIZ_SVC_PATH + "/{author}")
	public Collection<Quiz> getQuizByAuthor(@Path("author") String author);
	
	
	@POST(PLAYER_SVC_PATH)
	public Void addPLayer(@Body Player p);

	@GET(PLAYER_SVC_PATH)
	public Collection<Player> getPlayerList();
	
	@PUT(PLAYER_SVC_PATH)
	public Void updatePlayer(@Body Player p);
	

	//@POST(PLAYER_AUTH_PATH)
	//public Player authenticatePlayer();
	
	@POST(COMPLETED_QUIZ_SVC_PATH)
	public Void addCompletedQuiz(@Body CompletedQuiz q);

	@GET(COMPLETED_QUIZ_SVC_PATH)
	public Collection<CompletedQuiz> getCompletedQuizList();

	@GET(COMPLETED_QUIZ_SVC_PATH + "/{playername}")
	public Collection<CompletedQuiz> getCompletedQuizByPlayerName(@Path("playerName") String playerName);
	
	
	
//	@GET(VIDEO_NAME_SEARCH_PATH)
//	public Collection<Quiz> findByName(@Query(NAME_PARAMETER) String name);
	
//	@GET(PLAYER_NAME_SEARCH_PATH)
//	public Collection<Player> findByUsername(@Query(NAME_PARAMETER) String name);
//	
//	@GET(COMPLETED_QUIZ_DURATION_SEARCH_PATH)
//	public Collection<CompletedQuiz> findByDurationLessThan(@Query(DURATION_PARAMETER) String title);
	
}
