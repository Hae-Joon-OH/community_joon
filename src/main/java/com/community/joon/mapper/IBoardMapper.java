package com.community.joon.mapper;

import com.community.joon.entity.BoardEntity;
import com.community.joon.entity.CommentEntity;
import com.community.joon.entity.vo.Criteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IBoardMapper {

    // --------------------------- 공지 사항 시작
    BoardEntity[] selectNotice(Criteria criteria);

    int totalCountOfNoticeBoard();

    BoardEntity selectNoticeDetail(@Param("boardNum") int boardNum);

    void noticeInsert(String boardWriter, String boardTitle, String boardContent, String boardValue);

    BoardEntity detailAfterInsert();

    void noticeDelete(int boardNum);
    void noticeUpdate(String boardWriter, String boardTitle, String boardContent, String boardValue, int boardNum);

    // ----------------------------- 자유 게시판 시작

    BoardEntity[] selectFree(Criteria criteria);

    int totalCountOfFreeBoard();

    int freeInsert(String boardWriter, String boardTitle, String boardContent, String boardValue);

    BoardEntity selectFreeDetail(int boardNum);

    int freeUpdate(String boardWriter, String boardValue, String boardTitle, String boardContent, int boardNum);

    void freeDelete(int boardNum);

    // ----------------------------------------------- 조회수 시작
    void boardCount(int boardNum);

    // ----------------------------------------------- 좋아요 기능 시작
    int likeCount(@Param("boardWriter") String boardWriter, @Param("boardNum") int boardNum);

    int undoLikeCount(String boardWriter, int boardNum);

    BoardEntity selectLikedUser(@Param("boardNum") int boardNum);

    // ----------------------------------------------- 메인 영역
    BoardEntity[] mainFree();

    BoardEntity[] mainNotice();

    // ----------------------------------------------- 마이페이지
    BoardEntity[] myBoard(String boardWriter);

    CommentEntity[] myComment(String commentWriter);

    //===============================댓글 달기====================================

    CommentEntity selectParentComments(@Param("commentIndex") int commentIndex);

    void replySequence(CommentEntity comment);

    int replyInsert(CommentEntity comment);

    CommentEntity[] AllComment(@Param("boardNum") int boardNum);

    int commentInsert(CommentEntity comment);

    void deleteComments(@Param("commentIndex")int commentIndex);
}
