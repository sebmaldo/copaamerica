package com.unab.copaamerica.model;

import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("match")
    private OldMatch[] oldMatches;
    @SerializedName("next_page")
    private String nextPage;
    @SerializedName("prev_page")
    private String prevPage;

    public Data(OldMatch[] oldMatches, String nextPage, String prevPage) {
        this.oldMatches = oldMatches;
        this.nextPage = nextPage;
        this.prevPage = prevPage;
    }

    public OldMatch[] getOldMatches() {
        return oldMatches;
    }

    public void setOldMatches(OldMatch[] oldMatches) {
        this.oldMatches = oldMatches;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public String getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(String prevPage) {
        this.prevPage = prevPage;
    }
}
