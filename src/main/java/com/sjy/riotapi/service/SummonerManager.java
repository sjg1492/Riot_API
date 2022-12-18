package com.sjy.riotapi.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.sjy.riotapi.model.ERiotAPIInfoType;
import com.sjy.riotapi.model.GameInfo;
import com.sjy.riotapi.model.Summoner;
import com.sjy.riotapi.model.SummonerDetailInfo;


@Service
public class SummonerManager {

	public static final String API_KEY = "RGAPI-c52baff5-9706-4518-a40d-211ca18482e6";
	public static final String RIOT_SERVER_URL = "https://kr.api.riotgames.com";
	public static final String RIOT_ASIA_SERVER_URL = "https://asia.api.riotgames.com";
	public static final String RIOT_KOR_SERVER_URL = "https://kr.api.riotgames.com";

	public static final String RIOT_RESTAPI_SOMMONER_INFO = "/lol/summoner/v4/summoners/by-name/";
	public static final String RIOT_RESTAPI_SUMMONER_LEAGUE = "/lol/league/v4/entries/by-summoner/";
	public static final String RIOT_RESTAPI_GAME_INFO = "/lol/league/v4/entries/by-summoner/";
	public static final String RIOT_RESTAPI_SUMMONER_BATTLE_INFO = "/lol/match/v5/matches/by-puuid/";
	public static final String RIOT_RESTAPI_BATTLE_INFO = "/lol/match/v5/matches/";
	public static final String INPUT_RIOT_API_KEY_PARAM = "?api_key=";
	public static final String INPUT_RIOT_API_BATTLE_PARAM = "/ids?start=0&count=20&api_key=";
	
	
	
	/**
	 * 소환사 정보 입력
	 * 
	 * @version : 1.0.1
	 * @author : 송재원 
	 * @param summoner : 소환사 객체 , summonerName : 소환사 명
	 */
	public static void addSummoner(Summoner summoner,String summonerName) {
		if (summoner != null  ) {
			if(!(connectionRiotApiServer(getApiUrl(ERiotAPIInfoType.SUMMONER_INFO,summonerName))).equals("")) {
				JSONObject jsonObject = new JSONObject(connectionRiotApiServer(getApiUrl(ERiotAPIInfoType.SUMMONER_INFO,summonerName)));
				summoner.setPuuid((String) jsonObject.get("puuid"));
				summoner.setId((String) jsonObject.get("id"));
				summoner.setLevel(Integer.parseInt(jsonObject.get("summonerLevel").toString()));
				summoner.setNickName((String) jsonObject.get("name"));
			}
		}

	}

	
	/**
	 * 소환사 객체를 입력 받아 소환사 상세 정보 반환 
	 * 
	 * @version : 1.0.1
	 * @author : 송재원 
	 * @param summoner : 소환사 객체  
	 * @return : JSONArray : 소환사 상세 정보
	 */
	public JSONArray getSummonnerLeagueInfo(Summoner summoner) {
		if (summoner != null) {
			if (connectionRiotApiServer(getApiUrl(ERiotAPIInfoType.SUMMONER_DETAIL_INFO, summoner)) != "") {
				return new JSONArray(
						connectionRiotApiServer(getApiUrl(ERiotAPIInfoType.SUMMONER_DETAIL_INFO, summoner)));
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 소환사 객체를 입력 받아 소환사 최근 게임 정보 반환
	 * 
	 * @version : 1.0.1
	 * @author : 송재원 
	 * @param summoner : 소환사 객체  
	 * @return : List<String> : 최근 게임 정보
	 */
	public List<String> getSummonerGameInfo(Summoner summoner) {
		if (summoner != null) {
			// 소환사 20게임 정보
			String summonerBattleApiInfo = connectionRiotApiServer(
					getApiUrl(ERiotAPIInfoType.SUMMONER_BATTLE_INFO, summoner)).replaceAll("\"", "")
					.replaceAll("\\[", "").replaceAll("\\]", "");
			if (!summonerBattleApiInfo.equals("")) {
				return Arrays.asList(summonerBattleApiInfo.split(","));
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	/**
	 * 최근 게임 정보를 입력 받아 게임 리스트를 반환
	 * 
	 * @version : 1.0.1
	 * @author : 송재원 
	 * @param summoner : 소환사 객체  
	 * @return : List<String> : 최근 게임 정보
	 */
	public JSONArray getRecentGameHistory(Summoner summoner) {
		if (summoner != null) {
			List<String> matchList = getSummonerGameInfo(summoner);
			JSONArray arrGameInfo = new JSONArray();
			for (String matchInfo : matchList) {
				JSONObject gameInfo = new JSONObject(connectionRiotApiServer(getApiUrl(matchInfo)));
				arrGameInfo.put(gameInfo);
			}
			return arrGameInfo;
		} else {
			return null;
		}
	}

	/**
	 * RIOT API URL을 입력 받아  RIOT REST API HTTP 통신 
	 * 
	 * @version : 1.0.1
	 * @author : 송재원 
	 * @param apiServerUrl : RIOT API 서버 URL 
	 * @return : String : RIOT API 서버 요청 후 받은 데이터
	 */
	public static String connectionRiotApiServer(String apiServerUrl) {
		BufferedReader br = null;
		String result = "";
		try {
			URL url = new URL(apiServerUrl);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
			urlconnection.setRequestMethod("GET");
			br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));

			String line = "";
			while ((line = br.readLine()) != null) {
				result = result + line;
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return result;
	}

	/**
	 * 시간을 초 단위로 입력 받아 날짜형태로 변환 
	 * 
	 * @version : 1.0.1
	 * @author : 송재원 
	 * @param mm : 초 
	 * @return : String : 날짜 형식
	 */
	public static String dateTime(long mm) {
		mm = 1670918072517L;
		Date wantDate = new Date(mm);
		DateFormat dateFomatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String dateTime = "-";

		if (wantDate != null) {
			dateTime = dateFomatter.format(wantDate.getTime());
		}
		return dateTime;
	}

	/**
	 * RIOT API 통신을 위한 URL 병합 작업 
	 * 
	 * @version : 1.0.1
	 * @author : 송재원 
	 * @param ERiotAPIInfoType : enum 객체 , summonerName : 소환사 명
	 * @return : String : 전체 URL
	 */
	public static String getApiUrl(ERiotAPIInfoType type,String summonerName) {
		StringBuilder url = new StringBuilder();
		if (type.equals(ERiotAPIInfoType.SUMMONER_INFO)) {
			url.append(RIOT_SERVER_URL);
			url.append(RIOT_RESTAPI_SOMMONER_INFO);
			url.append(summonerName);
			url.append(INPUT_RIOT_API_KEY_PARAM);
			url.append(API_KEY);
		}

		return url.toString();
	}

	/**
	 * RIOT API 통신을 위한 URL 병합 작업 
	 * 
	 * @version : 1.0.1
	 * @author : 송재원 
	 * @param ERiotAPIInfoType : enum 객체 , summoner : 소환사 객체
	 * @return : String : 전체 URL
	 */
	public static String getApiUrl(ERiotAPIInfoType type, Summoner summoner) {
		StringBuilder url = new StringBuilder();

		// 소환사 정보(티어 등)
		if (type.equals(ERiotAPIInfoType.SUMMONER_DETAIL_INFO)) {
			url.append(RIOT_KOR_SERVER_URL);
			url.append(RIOT_RESTAPI_SUMMONER_LEAGUE);
			url.append(summoner.getId().trim());
			url.append(INPUT_RIOT_API_KEY_PARAM);
			url.append(API_KEY);

		} else if (type.equals(ERiotAPIInfoType.SUMMONER_BATTLE_INFO)) {
			url.append(RIOT_ASIA_SERVER_URL);
			url.append(RIOT_RESTAPI_SUMMONER_BATTLE_INFO);
			url.append(summoner.getPuuid());
			url.append(INPUT_RIOT_API_BATTLE_PARAM);
			url.append(API_KEY);
		}

		return url.toString();
	}

	/**
	 * RIOT API 통신을 위한 URL 병합 작업 
	 * 
	 * @version : 1.0.1
	 * @author : 송재원 
	 * @param matchInfo : 게임 정보
	 * @return : String : 전체 URL
	 */
	public static String getApiUrl(String matchInfo) {
		StringBuilder url = new StringBuilder();
		url.append(RIOT_ASIA_SERVER_URL);
		url.append(RIOT_RESTAPI_BATTLE_INFO);
		url.append(matchInfo);
		url.append(INPUT_RIOT_API_KEY_PARAM);
		url.append(API_KEY);

		return url.toString();
	}


	/**
	 * RIOT REST API에서 받아온 데이터를 Model에 저장
	 * 
	 * @version : 1.0.1
	 * @author : 송재원 
	 * @param model : 모델, summonerName : 소환사명 
	 * @return : String : 전체 URL
	 */
	public void saveRiotApiDataInModel(Model model,String summonerName) {
		Summoner summoner = new GameInfo();
		addSummoner(summoner,summonerName);
		JSONArray jsonArrSummonerLeagueInfo = getSummonnerLeagueInfo(summoner);
		JSONObject jsonSummonerLeagueInfo = (JSONObject)jsonArrSummonerLeagueInfo.get(0);
		SummonerDetailInfo summonerDetailInfo = new SummonerDetailInfo();
		
		
		JSONArray jsonArrRecentGameHistory = getRecentGameHistory(summoner);
		model.addAttribute("jsonSummonerLeagueInfo", jsonSummonerLeagueInfo);
		model.addAttribute("jsonArrRecentGameHistory", jsonArrRecentGameHistory);
		
	}
	
//	public void url() {
//
//		// 소환사 puuid 정보
//		String summonerApiUrl = RIOT_SERVER_URL + "/lol/summoner/v4/summoners/by-name/" + SummonerName + "?api_key="
//				+ API_KEY;
//		// 소환사 정보(티어 등)
//		String summonerDetailApiUrl = RIOT_KOR_SERVER_URL + "/lol/league/v4/entries/by-summoner/" + id + "?api_key="
//				+ API_KEY;
//
//		// 소환사 20게임 정보
//		String summonerBattleApiServerUrl = RIOT_ASIA_SERVER_URL
//				+ "/lol/match/v5/matches/by-puuid/Iq_dOgWq2UKEB1XAWMm9f86Spmi4MzONjoNJgBm50ffCOwS3PcQikwGALImf2EFlujBD55_ZcSAQsw/ids?start=0&count=20&api_key="
//				+ API_KEY;
//
//		// 소환사 게임 정보
//		String gameInfoApiServerUrl = RIOT_ASIA_SERVER_URL + "/lol/match/v5/matches/" + matchId + "?api_key=" + API_KEY;
//	}
}
