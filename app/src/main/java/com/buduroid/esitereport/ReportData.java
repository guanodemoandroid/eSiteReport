package com.buduroid.esitereport;

public class ReportData {

    private String repid, repdate, reptitle, thumbnailUrl;
    //private int year;
    private String repdesc;
    private String replocation;



    public String getRepID() {
        return repid;
    }


    public void setRepID(String repid) {
        this.repid = repid;
    }


    public String getRepDate() {
        return repdate;
    }

    public void setRepDate(String repdate) {
        this.repdate = repdate;
    }


    public String getRepTitle() {
        return reptitle;
    }

    public void setRepTitle(String reptitle) {
        this.reptitle = reptitle;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    //public int getYear() {
   //     return year;
    //}

    //public void setYear(int year) {
    //    this.year = year;
    //}

    public String getRepDesc() {
        return repdesc;
    }

    public void setRepDesc(String repdesc) {
        this.repdesc = repdesc;
    }

    public String getRepLocation() {
        return replocation;
    }

    public void setRepLocation(String replocation) {
        this.replocation = replocation;
    }



}
