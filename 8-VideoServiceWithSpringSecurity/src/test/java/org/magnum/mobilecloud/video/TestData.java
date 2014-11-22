package org.magnum.mobilecloud.video;

import guay.philippe.capstone.quiz.repository.Player;
import guay.philippe.capstone.quiz.repository.Quiz;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;



import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This is a utility class to aid in the construction of
 * Video objects with random names, urls, and durations.
 * The class also provides a facility to convert objects
 * into JSON using Jackson, which is the format that the
 * VideoSvc controller is going to expect data in for
 * integration testing.
 * 
 * @author jules
 *
 */
public class TestData {

	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * Construct and return a Video object with a
	 * rnadom name, url, and duration.
	 * 
	 * @return
	 */
	public static Quiz randomVideo() {
		// Information about the video
		// Construct a random identifier using Java's UUID class
		String id = UUID.randomUUID().toString();
		String title = "Video-"+id;
		String url = "http://coursera.org/some/video-"+id;
		int rating = 5;
		int unrelated_movie = 0;
		Set movie_set = new HashSet();
		movie_set.add("Hallo");
		movie_set.add("b");
		movie_set.add("c");
		movie_set.add("d");
		
		String justification = "No justification";
		Player player = new Player();
		long duration = 60 * (int)Math.rint(Math.random() * 60) * 1000; // random time up to 1hr
		return new Quiz();
	}
	
	/**
	 *  Convert an object to JSON using Jackson's ObjectMapper
	 *  
	 * @param o
	 * @return
	 * @throws Exception
	 */
	public static String toJson(Object o) throws Exception{
		return objectMapper.writeValueAsString(o);
	}
}
