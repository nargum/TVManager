package com.example.kuba.tvmanager;

/**
 * Created by Kuba on 12/18/2017.
 */

public class Score {
    TVShow showId;
    Account accountId;
    int score;

    public Score(TVShow showId, Account accountId, int score){
        this.showId = showId;
        this.accountId = accountId;
        this.score = score;
    }

    public Score(){}

    public TVShow getShowId(){
        return showId;
    }

    public void setShowId(TVShow showId){
        this.showId = showId;
    }

    public Account getAccountId(){
        return accountId;
    }

    public void setAccountId(Account accountId){
        this.accountId = accountId;
    }

    public int getScore(){
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }

    public String info(){
        return showId.getName() + " " + accountId.getName() + " " + String.valueOf(score);
    }
}
