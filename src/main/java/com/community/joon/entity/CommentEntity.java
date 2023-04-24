package com.community.joon.entity;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntity {
    private int commentIndex;
    private String commentWriter;
    private String commentContent;
    private int commentGroup;
    private int commentSequence;
    private int commentLevel;
    private boolean commentAvailable;
    private int boardNum;
}
