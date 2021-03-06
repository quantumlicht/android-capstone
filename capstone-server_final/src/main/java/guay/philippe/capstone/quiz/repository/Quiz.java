package guay.philippe.capstone.quiz.repository;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import guay.philippe.capstone.quiz.repository.Quiz;

import com.google.common.base.Objects;

/**
 * A simple object to represent a video and its URL for viewing.
 * 
 * @author jules
 * 
 */
@Entity
public class Quiz {
	
	public enum Status {
		ACTIVE,
		REJECTED
	};
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;
	private Date creationTime;
	private int rating;
	private int difficulty;
	private int authorId;
	private String author;
	private String[] movieSet = new String[4];
	private String justification;
	private int unrelatedMovie;
	private Status status;
	
	public Quiz() {
	}

	public Quiz(String author, String name, int difficulty, String justification, int unrelatedMovie, String[] movieSet) {
		super();
		this.name = name;
		this.author= author;
		this.difficulty = difficulty;
		this.movieSet = movieSet;
		this.justification = justification;
		this.unrelatedMovie = unrelatedMovie;
		this.creationTime = new Date();
		this.status = Status.ACTIVE;
	}

	//GETTERS
	public String getName() {
		return name;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public int getRating() {
		return rating;
	}
	
	public int getDifficulty() {
		return difficulty;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	
	
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	
	
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	public int getAuthorId() {
		return authorId;
	}
	
	public void setAuthorid(int id) {
		this.authorId = id;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	
	public String[] getMovieSet() {
		return movieSet;
	}

	public void setMovieSet(String[] movieSet) {
		this.movieSet = movieSet;
	}
	
	
	public int getUnrelatedMovie() {
		return unrelatedMovie;
	}

	public void setUnrelatedMovie(int unrelatedMovie) {
		this.unrelatedMovie = unrelatedMovie;
	}
	
	
	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}


	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}


	public long getId() {
		return id;
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
		return Objects.hashCode(name, unrelatedMovie, creationTime, justification,author);
	}

	/**
	 * Two Videos are considered equal if they have exactly the same values for
	 * their name, url, and duration.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Quiz) {
			Quiz other = (Quiz) obj;
			// Google Guava provides great utilities for equals too!
			return Objects.equal(name, other.name)
					&& Objects.equal(creationTime, other.creationTime)
					&& Objects.equal(author, other.author)
					&& Objects.equal(unrelatedMovie, other.unrelatedMovie);
		} else {
			return false;
		}
	}

}
