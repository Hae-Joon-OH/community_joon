package com.community.joon.entity.vo;

public class PageMaker {
    private Criteria criteria;

    private int totalCount;
    private int startPage;
    private int endPage;
    private boolean prev;
    private boolean next;
    private int displayPageNum = 5;

    private void makePaging() {
        endPage = (int)(Math.ceil(criteria.getPage()/(double)displayPageNum)*displayPageNum);
        startPage = (endPage - displayPageNum) + 1;
        if(startPage<0) startPage = 1;
        int tempEndPage = (int)(Math.ceil(totalCount/(double)criteria.getPerPageNum()));
        if(tempEndPage < endPage) {
            endPage = tempEndPage;
        }

        prev = (startPage == 1) ? false : true;
        next = (endPage < tempEndPage) ? true : false;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public PageMaker setCriteria(Criteria criteria) {
        this.criteria = criteria;
        return this;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        makePaging();
    }

    public int getStartPage() {
        return startPage;
    }

    public PageMaker setStartPage(int startPage) {
        this.startPage = startPage;
        return this;
    }

    public int getEndPage() {
        return endPage;
    }

    public PageMaker setEndPage(int endPage) {
        this.endPage = endPage;
        return this;
    }

    public boolean isPrev() {
        return prev;
    }

    public PageMaker setPrev(boolean prev) {
        this.prev = prev;
        return this;
    }

    public boolean isNext() {
        return next;
    }

    public PageMaker setNext(boolean next) {
        this.next = next;
        return this;
    }

    public int getDisplayPageNum() {
        return displayPageNum;
    }

    public PageMaker setDisplayPageNum(int displayPageNum) {
        this.displayPageNum = displayPageNum;
        return this;
    }
}
