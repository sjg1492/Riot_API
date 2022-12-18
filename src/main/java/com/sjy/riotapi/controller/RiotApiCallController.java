package com.sjy.riotapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sjy.riotapi.service.SummonerManager;

@Controller
public class RiotApiCallController {
	
	@Autowired
	SummonerManager summonerManager;
	
	/**
	 * 롤 전적 검색 서비스 컨트롤러
	 * 
	 * @version : 1.0.1
	 * @author : 송재원 
	 * @param model : 모델 객체 , summonerName : 소환사 명
	 * @return : model
	 */
	@GetMapping("/api/summoner/{summoner}")
	public String index(Model model,@PathVariable String summoner) {
		summonerManager.saveRiotApiDataInModel(model,summoner);
		return "Home/index";
	}


}
