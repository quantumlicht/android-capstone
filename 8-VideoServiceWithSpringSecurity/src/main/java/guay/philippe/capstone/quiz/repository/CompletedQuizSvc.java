package guay.philippe.capstone.quiz.repository;


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

@Controller
public class CompletedQuizSvc {
	
	@Autowired
	private CompletedQuizRepository completedQuizzes;
		
	@RequestMapping(value="/completedquiz", method=RequestMethod.POST)
	 	public @ResponseBody CompletedQuiz addCompletedQuiz(@RequestBody CompletedQuiz q){
		System.out.println("addCompletedQuiz");
		 	completedQuizzes.save(q);
		 	return q;
	}
	 
	@RequestMapping(value="/completedquiz", method=RequestMethod.GET)
	 public @ResponseBody Collection<CompletedQuiz> getCompletedQuizList(){
			return Lists.newArrayList(completedQuizzes.findAll());
	}
	
	@RequestMapping(value="/completedquiz", method=RequestMethod.PUT)
	 public @ResponseBody CompletedQuiz updateCompletedQuizList(@RequestBody CompletedQuiz q){
		CompletedQuiz compQ = completedQuizzes.findByQuizName(q.getQuizName());
		compQ.setHasVoted(q.getHasVoted());
		completedQuizzes.save(compQ);
		return compQ;
	}
	
	@RequestMapping(value="/completedquiz/{playerName}", method=RequestMethod.GET)
 		public @ResponseBody Collection<CompletedQuiz> getCompletedQuiz(@PathVariable("playerName") String playerName, HttpServletResponse response) {
			try {
				return completedQuizzes.findByPlayerName(playerName);
			} catch (Exception e) {
				response.setStatus(HttpStatus.NOT_FOUND.value());
				e.printStackTrace();
			}
			return null;
	 }
}