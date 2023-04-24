package com.community.joon.entity;

import lombok.*;

import java.util.Date;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberEntity {
    private int memberNum;
    private String memberEmail;
    private String memberPassword;
    private Date memberJoinDate;
    private String memberName;
    private String memberNickname;
}
