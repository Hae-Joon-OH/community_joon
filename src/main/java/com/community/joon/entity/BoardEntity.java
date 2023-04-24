package com.community.joon.entity;

import lombok.*;

import java.util.Date;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardEntity {
    private int boardNum;
    private String boardWriter;
    private String boardTitle;
    private String boardContent;
    private int boardCount;
    private Date boardCreatedAt;
    private int boardLikes;
    private String boardLikesUser;
    private String boardValue;
}
