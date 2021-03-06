package guay.philippe.capstone.quiz.repository;


import java.util.Date;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.common.base.Objects;

@Entity
public class Player {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String username;
	private String passwordHash;
	private Date creationTime;
	private int score;
	
	public Player() {
	}
	
	public Player(String username, String passwordHash) {
		super();
		this.username = username;
		this.passwordHash = passwordHash;
	}
	
	public Player(String username, String passwordHash,int score) {
		super();
		this.username = username;
		this.score = score;
		this.passwordHash = passwordHash;
	}
	
	public String getUsername(){
		return username;
	}
	
	public String getPasswordHash(){
		return passwordHash;
	}
	
	public Date getCreationTime(){
		return creationTime;
	}
	
	
	public int getScore(){
		return score;
	}
	
	public long getId() {
		return id;
	}
	
	public void setUsername(String uname){
		this.username = uname;
	}
	
	public void setPasswordHash(String hash){
		this.passwordHash = hash;
	}
	
	public void setCreationTime(Date creationTime){
		this.creationTime = creationTime;
	}
	
	
	public void setScore(int score){
		this.score = score;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Two Videos will generate the same hashcode if they have exactly the same
	 * values for their name, author, and creation_time.
	 * 
	 */
	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(username,creationTime, passwordHash);
	}

	/**
	 * Two Videos are considered equal if they have exactly the same values for
	 * their name, url, and duration.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Player) {
			Player other = (Player) obj;
			// Google Guava provides great utilities for equals too!
			return Objects.equal(username, other.username)
					&& Objects.equal(creationTime, other.creationTime)
					&& Objects.equal(passwordHash, other.passwordHash);
		} else {
			return false;
		}
	}

	
}

