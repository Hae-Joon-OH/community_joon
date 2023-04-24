package com.community.joon.entity.vo;

public class Criteria {
    // 페이징 관련에서 가변하지만 기준이 되는 클래스 생성

    private int page;

    private int perPageNum;

    public Criteria() {
        this.page = 1;
        this.perPageNum = 5;
    }

    public int getPageStart() { return (page - 1) * perPageNum;}

    public int getPage() { return page; }

    public Criteria setPage(int page) {
        this.page = page;
        return this;
    }

    public int getPerPageNum() { return perPageNum; }

    public Criteria getPerPageNum(int perPageNum) {
        this.perPageNum = perPageNum;
        return this;
    }
}
