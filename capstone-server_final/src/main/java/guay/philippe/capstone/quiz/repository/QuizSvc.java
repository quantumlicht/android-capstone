package guay.philippe.capstone.quiz.repository;



import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import guay.philippe.capstone.mutibo.client.MutiboSvcApi;

@Controller
public class QuizSvc {
	
	@Autowired
	private QuizRepository quizzes;
	
	@RequestMapping(value="/quiz", method=RequestMethod.POST)
	public @ResponseBody Quiz addQuiz(@RequestBody Quiz q, HttpServletResponse response){
			System.out.println("==============================addQuiz");
			q.setCreationTime(new Date());
			q.setStatus(Quiz.Status.ACTIVE);
		 	quizzes.save(q);
		 	response.setStatus(HttpStatus.CREATED.value());
		 	return q;
	}
	 
	@RequestMapping(value="/quiz", method=RequestMethod.GET)
	public @ResponseBody Collection<Quiz> getQuizList(HttpServletResponse response){
			System.out.println("==================================getQuizList");
			response.setStatus(HttpStatus.OK.value());
			return Lists.newArrayList(quizzes.findByStatus(Quiz.Status.ACTIVE));
	}
	
	@RequestMapping(value="/quiz/{playerName}", method=RequestMethod.GET)
		public @ResponseBody Collection<Quiz> getQuizByAuthor(@PathVariable("playerName") String playerName, HttpServletResponse response) {
		try {
			System.out.println("==================================getQuizList for player: " + playerName);
			return quizzes.findByAuthor(playerName);
		} catch (Exception e) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="/quiz/byname/{quizName}", method=RequestMethod.GET)
	public @ResponseBody Quiz getQuizByName(@PathVariable("quizName") String quizName, HttpServletResponse response) {
		try {
			quizName = java.net.URLDecoder.decode(quizName, "UTF-8");
			System.out.println("==================================getQuizList for quizName: " + quizName);
			Quiz q = quizzes.findByName(quizName);
			System.out.println("==================================getQuizList found Quiz: " + q.toString());
			if (q.getStatus().equals(Quiz.Status.ACTIVE)){
				return q;	
			}
			else {
				response.setStatus(HttpStatus.NOT_FOUND.value());
			}
		} catch (Exception e) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="/quiz", method=RequestMethod.PUT)
	public @ResponseBody Quiz updateQuiz(@RequestBody Quiz q, HttpServletResponse response){
			System.out.println("======================================updateQuiz: " + q.getName());
			
			Quiz quiz = quizzes.findByName(q.getName());
			if (quiz != null){
				System.out.println("===updateQuiz=== Quiz found");
				quiz.setRating(q.getRating());
				quiz.setDifficulty(q.getDifficulty());
				quiz.setUnrelatedMovie(q.getUnrelatedMovie());
				quiz.setMovieSet(q.getMovieSet());
				quiz.setJustification(q.getJustification());
				
				if (quiz.getRating() < MutiboSvcApi.MINIMUM_ALLOWED_RATING) {
					quiz.setStatus(Quiz.Status.REJECTED);
				}
				else{
					q.setStatus(Quiz.Status.ACTIVE);
				}
				quizzes.save(quiz);
				response.setStatus(HttpStatus.OK.value());
				return quiz;
			}
			else{
				System.out.println("===updateQuiz=== Quiz not found");
				if (q.getRating() < MutiboSvcApi.MINIMUM_ALLOWED_RATING) {
					q.setStatus(Quiz.Status.REJECTED);
				}
				else {
					q.setStatus(Quiz.Status.ACTIVE);
				}
				quizzes.save(q);
				return q;
			}
	}
	
	@RequestMapping(value="/quiz/{quizName}", method=RequestMethod.DELETE)
	public @ResponseBody Quiz deleteQuiz(@PathVariable("quizName") String quizName, HttpServletResponse response){
		System.out.println("======================================deleteQuiz");
		try {
			quizName = java.net.URLDecoder.decode(quizName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Quiz q = quizzes.findByName(quizName);
		quizzes.delete(q);
//		quiz.setRating(q.getRating());
//		if (quiz.getRating() < MutiboSvcApi.MINIMUM_ALLOWED_RATING) {
//			quiz.setStatus(Quiz.Status.REJECTED);
//		}
//		
//		quizzes.save(quiz);
		response.setStatus(HttpStatus.OK.value());
		return q;
}
}