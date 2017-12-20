package com.example.kuba.tvmanager;

/**
 * Created by Kuba on 12/19/2017.
 */

public class Watched {
    private Account accountId;
    private Episode episodeId;

    public Watched(Account accountId, Episode episodeId){
        this.accountId = accountId;
        this.episodeId = episodeId;
    }

    public Watched(){}

    public Account getAccountId(){
        return accountId;
    }

    public void setAccountId(Account accountId){
        this.accountId = accountId;
    }

    public Episode getEpisodeId(){
        return episodeId;
    }

    public void setEpisodeId(Episode episodeId){
        this.episodeId = episodeId;
    }
}
