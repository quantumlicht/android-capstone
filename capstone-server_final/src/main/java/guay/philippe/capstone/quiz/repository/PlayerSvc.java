package guay.philippe.capstone.quiz.repository;

import guay.philippe.capstone.mutibo.client.MutiboSvcApi;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

@Controller
public class PlayerSvc {
	
	@Autowired
	public PlayerRepository players;
		
	@RequestMapping(value="/player", method=RequestMethod.POST)
	 	public @ResponseBody Player addPlayer(@RequestBody Player p){
			System.out.println("=======================addPlayer");
			p.setScore(0);
			p.setCreationTime(new Date());
		 	players.save(p);
		 	return p;
	}
	 
	@RequestMapping(value="/player", method=RequestMethod.GET)
	 public @ResponseBody Collection<Player> getPlayerList(){
			System.out.println("==================getPlayerList");
			return Lists.newArrayList(players.findAll());
	}
	
	@RequestMapping(value="/player", method=RequestMethod.PUT)
	public @ResponseBody Player updatePlayer(@RequestBody Player p, HttpServletResponse response){
		System.out.println("===================updatePlayer");
		Player player = players.findByUsername(p.getUsername());
		if (player != null){
			player.setScore(p.getScore());
			players.save(player);
			response.setStatus(HttpStatus.OK.value());
			return player;
		}
		else{
			players.save(p);
			response.setStatus(HttpStatus.OK.value());
			return p;
		}
		
	}
}