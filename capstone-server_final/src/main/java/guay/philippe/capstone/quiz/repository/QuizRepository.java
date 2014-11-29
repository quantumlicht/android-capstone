package guay.philippe.capstone.quiz.repository;

import guay.philippe.capstone.mutibo.client.MutiboSvcApi;

import java.util.Collection;

import guay.philippe.capstone.quiz.repository.Quiz;
import guay.philippe.capstone.quiz.repository.Quiz.Status;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * An interface for a repository that can store Video
 * objects and allow them to be searched by title.
 * 
 * @author jules
 *
 */

//
@RepositoryRestResource(path = MutiboSvcApi.QUIZ_SVC_PATH)
public interface QuizRepository extends CrudRepository<Quiz, Long>{

	// Find all videos with a matching title (e.g., Video.name)
	public Collection<Quiz> findByAuthor(
			@Param("author") String author);
	
	public Quiz findByName(
			@Param("name") String name);
	
	public Collection<Quiz> findByStatus(
			// The @Param annotation tells Spring Data Rest which HTTP request
			// parameter it should use to fill in the "playerName" variable used to
			// search for CompletedQuizzes
			@Param("status") Status status);
	
}
