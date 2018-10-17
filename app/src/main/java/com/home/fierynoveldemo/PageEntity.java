package com.home.fierynoveldemo;

public class PageEntity {

    private int number;
    private String mainTitle;
    private String mainContent;
    private String pageNumber;

    public PageEntity() {
    }

    public PageEntity(int number, String mainTitle, String mainContent, String pageNumber) {
        this.number = number;
        this.mainTitle = mainTitle;
        this.mainContent = mainContent;
        this.pageNumber = pageNumber;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getMainContent() {
        return mainContent;
    }

    public void setMainContent(String mainContent) {
        this.mainContent = mainContent;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }
}
