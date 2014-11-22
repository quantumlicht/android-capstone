package guay.philippe.capstone.quiz.repository;

import guay.philippe.capstone.mutibo.client.MutiboSvcApi;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = MutiboSvcApi.COMPLETED_QUIZ_SVC_PATH)
public interface CompletedQuizRepository extends CrudRepository<CompletedQuiz, Long>{

	// Find all videos with a matching title (e.g., Video.name)
	public Collection<CompletedQuiz> findByAnswer(
			// The @Param annotation tells Spring Data Rest which HTTP request
			// parameter it should use to fill in the "title" variable used to
			// search for Videos
			@Param("answer") int answer);
	
	public Collection<CompletedQuiz> findByPlayerName(
			// The @Param annotation tells Spring Data Rest which HTTP request
			// parameter it should use to fill in the "playerName" variable used to
			// search for CompletedQuizzes
			@Param("playerName") String playerName);
	
	public CompletedQuiz findByQuizName(
			// The @Param annotation tells Spring Data Rest which HTTP request
			// parameter it should use to fill in the "playerName" variable used to
			// search for CompletedQuizzes
			@Param("quizName") String quizName);
	
	// Find all videos that are shorter than a specified duration
	/*
	public Collection<CompletedQuiz> findByDurationLessThan(
			// The @Param annotation tells tells Spring Data Rest which HTTP request
			// parameter it should use to fill in the "duration" variable used to
			// search for Videos
			@Param(MutiboSvcApi.DURATION_PARAMETER) long maxduration);
	*/
	/*
	 * See: http://docs.spring.io/spring-data/jpa/docs/1.3.0.RELEASE/reference/html/jpa.repositories.html 
	 * for more examples of writing query methods
	 */
	
}