package com.community.joon.mapper;

import com.community.joon.entity.MemberEntity;
import org.apache.ibatis.annotations.Mapper;



@Mapper
public interface IMemberMapper {

    void register(MemberEntity member);

    MemberEntity nameDuplication(String memberNickname);

    MemberEntity emailDuplication(String memberEmail);

    MemberEntity login(String memberEmail, String memberPassword);

    void deleteUser(String memberEmail, String memberPassword);

    // ------------------------------- 회원 정보 수정
    int userModify(String memberName, String memberPassword, int memberNum);
}
