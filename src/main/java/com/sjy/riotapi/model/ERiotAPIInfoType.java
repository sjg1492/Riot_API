package com.sjy.riotapi.model;

public enum ERiotAPIInfoType {
	
	SUMMONER_INFO("SUMMONER_INFO"),
	SUMMONER_DETAIL_INFO("SUMMONER_DETAIL_INFO"),
	SUMMONER_BATTLE_INFO("SUMMONER_BATTLE_INFO");
    
    private final String value;
    
    ERiotAPIInfoType(String value){
        this.value = value;
            
    }
    
    public String getValue(){
        return value;
    }

}
