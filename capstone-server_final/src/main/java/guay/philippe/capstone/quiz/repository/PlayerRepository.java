package guay.philippe.capstone.quiz.repository;

import guay.philippe.capstone.mutibo.client.MutiboSvcApi;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = MutiboSvcApi.PLAYER_SVC_PATH)
public interface PlayerRepository extends CrudRepository<Player, Long>{

	// Find all videos with a matching title (e.g., Video.name)
	public Player findByUsername(
			// The @Param annotation tells Spring Data Rest which HTTP request
			// parameter it should use to fill in the "title" variable used to
			// search for Videos
			@Param(MutiboSvcApi.USERNAME_PARAMETER) String username);
	
	// Find all videos that are shorter than a specified duration
//	public Collection<Player> findByDurationLessThan(
//			// The @Param annotation tells tells Spring Data Rest which HTTP request
//			// parameter it should use to fill in the "duration" variable used to
//			// search for Videos
//			@Param(MutiboSvcApi.DURATION_PARAMETER) long maxduration);
	
	/*
	 * See: http://docs.spring.io/spring-data/jpa/docs/1.3.0.RELEASE/reference/html/jpa.repositories.html 
	 * for more examples of writing query methods
	 */
	
}