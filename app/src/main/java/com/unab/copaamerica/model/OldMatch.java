package com.unab.copaamerica.model;

import com.google.gson.annotations.SerializedName;

public class OldMatch {
    private String id;
    private String date;
    @SerializedName("home_name")
    private String homeName;
    @SerializedName("away_name")
    private String awayName;
    private String score;
    @SerializedName("ht_score")
    private String htScore;
    @SerializedName("ft_score")
    private String ftScore;
    @SerializedName("et_score")
    private String etScore;
    private String time;
    @SerializedName("league_id")
    private String leagueId;
    private String status;
    private String added;
    @SerializedName("last_changed")
    private String lastChanged;
    @SerializedName("home_id")
    private String homeId;
    @SerializedName("away_id")
    private String awayId;
    @SerializedName("source_id")
    private String sourceId;

    public OldMatch(String id, String date, String homeName, String awayName, String score, String htScore, String ftScore, String etScore, String time, String leagueId, String status, String added, String lastChanged, String homeId, String awayId, String sourceId) {
        this.id = id;
        this.date = date;
        this.homeName = homeName;
        this.awayName = awayName;
        this.score = score;
        this.htScore = htScore;
        this.ftScore = ftScore;
        this.etScore = etScore;
        this.time = time;
        this.leagueId = leagueId;
        this.status = status;
        this.added = added;
        this.lastChanged = lastChanged;
        this.homeId = homeId;
        this.awayId = awayId;
        this.sourceId = sourceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getAwayName() {
        return awayName;
    }

    public void setAwayName(String awayName) {
        this.awayName = awayName;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getHtScore() {
        return htScore;
    }

    public void setHtScore(String htScore) {
        this.htScore = htScore;
    }

    public String getFtScore() {
        return ftScore;
    }

    public void setFtScore(String ftScore) {
        this.ftScore = ftScore;
    }

    public String getEtScore() {
        return etScore;
    }

    public void setEtScore(String etScore) {
        this.etScore = etScore;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdded() {
        return added;
    }

    public void setAdded(String added) {
        this.added = added;
    }

    public String getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(String lastChanged) {
        this.lastChanged = lastChanged;
    }

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    public String getAwayId() {
        return awayId;
    }

    public void setAwayId(String awayId) {
        this.awayId = awayId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
}
