package com.example.kuba.tvmanager;

/**
 * Created by Kuba on 12/15/2017.
 */

public class Episode {
    private String id;
    private String season;
    private String episodeNumber;
    private String name;
    private TVShow showId;

    public Episode() {
    }

    public Episode(String id, String season, String episodeNumber, String name, TVShow showId) {
        this.id = id;
        this.season = season;
        this.episodeNumber = episodeNumber;
        this.name = name;
        this.showId = showId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(String episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TVShow getShowId() {
        return showId;
    }

    public void setShowId(TVShow showId) {
        this.showId = showId;
    }
}
