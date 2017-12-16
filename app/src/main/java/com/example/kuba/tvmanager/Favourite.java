package com.example.kuba.tvmanager;

/**
 * Created by Kuba on 12/16/2017.
 */

public class Favourite {
    TVShow showId;
    Account accountId;

    public Favourite(TVShow show, Account account){
        showId = show;
        accountId = account;
    }

    public Favourite(){}

    public TVShow getShowId(){
        return showId;
    }

    public void setShowId(TVShow show){
        showId = show;
    }

    public Account getAccountId(){
        return accountId;
    }

    public void setAccountId(Account account){
        accountId = account;
    }
}
