package guay.philippe.capstone.quiz.repository;

import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.common.base.Objects;

@Entity
public class CompletedQuiz {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String quizName;
//	private Date completedTime;
	private String playerName;
	private int answer;
	private boolean success;
	private boolean hasVoted;
	
	public CompletedQuiz(){
	}
	
	public CompletedQuiz(String quizName, String playerName,  int answer, boolean success){
		this.quizName = quizName;
		this.answer = answer;
		this.playerName = playerName;
		this.success = success;
	}
	
	
	public String getQuizName(){
		return quizName;
	}
	
	public int getAnswer(){
		return answer;
	}
	
	public Boolean getSuccess(){
		return success;
	}
	
	public String getPlayerName(){
		return playerName;
	}
	
	public Boolean getHasVoted(){
		return hasVoted;
	}
	
	
	public void setHasVoted(Boolean hasVoted){
		this.hasVoted = hasVoted;
	}
	
//	public Date getCompletedTime(){
//		return completedTime;
//	}
	
	
	
	/**
	 * Two Videos will generate the same hashcode if they have exactly the same
	 * values for their name, author, and creation_time.
	 * 
	 */
	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(quizName, answer, playerName, hasVoted);
	}

	/**
	 * Two Videos are considered equal if they have exactly the same values for
	 * their name, url, and duration.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CompletedQuiz) {
			CompletedQuiz other = (CompletedQuiz) obj;
			// Google Guava provides great utilities for equals too!
			return  answer== other.answer
					&& Objects.equal(success, other.success)
					&& quizName == other.quizName
					&& playerName == other.playerName
					&& hasVoted == other.hasVoted;
		} else {
			return false;
		}
	}
	

}
