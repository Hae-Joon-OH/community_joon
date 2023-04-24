package com.community.joon.controller;

import com.community.joon.entity.BoardEntity;
import com.community.joon.entity.CommentEntity;
import com.community.joon.entity.vo.Criteria;
import com.community.joon.entity.vo.PageMaker;
import com.community.joon.mapper.IBoardMapper;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.annotations.Param;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

@Controller
@RequestMapping(value = "/board")
public class BoardController {

    @Autowired
    private IBoardMapper boardMapper;

    // --------------------------------- 공지 사항 시작

    @RequestMapping(value = "/notice", method = RequestMethod.GET)
    public String notice(Model model, Criteria ncriteria) {
        model.addAttribute("nList", this.boardMapper.selectNotice(ncriteria));

        PageMaker npage = new PageMaker();
        npage.setCriteria(ncriteria);
        npage.setTotalCount(this.boardMapper.totalCountOfNoticeBoard());
        model.addAttribute("npageMaker", npage);

        return "board/noticeBoard";
    }

    @GetMapping("/noticeDetail")
    public String noticeDetail(HttpServletRequest request, Model model, @RequestParam("boardNum") int boardNum) {
        model.addAttribute("dList", this.boardMapper.selectNoticeDetail(boardNum));
        this.boardMapper.boardCount(boardNum);

        return "/board/noticeBoardDetail";
    }

    @GetMapping("/noticeInsert")
    public String noticeInsert() {
        return "/board/noticeBoardInsert";
    }

    @RequestMapping(value = "/noticeInsert", method = RequestMethod.POST)
    @ResponseBody
    public String postNoticeInsert(
            @RequestParam("boardWriter") String boardWriter,
            @RequestParam("boardTitle") String boardTitle,
            @RequestParam("boardContent") String boardContent,
            @RequestParam("boardValue") String boardValue
    ) {
        this.boardMapper.noticeInsert(boardWriter, boardTitle, boardContent, boardValue);

        JSONObject responseJSON = new JSONObject();
        BoardEntity boardEntity = this.boardMapper.detailAfterInsert();
        int boardNum = boardEntity.getBoardNum();
        responseJSON.put("result", boardNum);
        return responseJSON.toString();
    }

    @GetMapping(value = "/noticeDelete")
    public String noticeDelete(int boardNum) {
        this.boardMapper.noticeDelete(boardNum);
        return "redirect:/board/notice";
    }

    @GetMapping(value = "/noticeEdit")
    public String noticeEdit(Model model, int boardNum) {
        model.addAttribute("eList", this.boardMapper.selectNoticeDetail(boardNum));
        return "/board/noticeBoardEdit";
    }

    @RequestMapping(value = "/noticeEdit", method = RequestMethod.POST)
    public String noticeUpdate(
            @RequestParam("boardWriter") String boardWriter,
            @RequestParam("boardTitle") String boardTitle,
            @RequestParam("boardContent") String boardContent,
            @RequestParam("boardValue") String boardValue,
            int boardNum) {
        this.boardMapper.noticeUpdate(boardWriter, boardTitle, boardContent, boardValue, boardNum);
        return "redirect:/board/notice";
    }

    // --------------------------------------- 자유게시판 시작

    @RequestMapping(value = "/free", method = RequestMethod.GET)
    public String free(Model model, Criteria criteria) {
        model.addAttribute("fList", this.boardMapper.selectFree(criteria));

        PageMaker page =new PageMaker();
        page.setCriteria(criteria);
        page.setTotalCount(this.boardMapper.totalCountOfFreeBoard());
        model.addAttribute("pageMaker", page);

        return "board/freeBoard";
    }

    @GetMapping("/freeInsert")
    public String freeInsert() {
        return "/board/freeBoardInsert";
    }

    @RequestMapping(value = "/freeInsert", method = RequestMethod.POST)
    @ResponseBody
    public String freeInsertPost(
            @RequestParam("boardWriter") String boardWriter,
            @RequestParam("boardTitle") String boardTitle,
            @RequestParam("boardContent") String boardContent,
            @RequestParam("boardValue") String boardValue) {
        int state = this.boardMapper.freeInsert(boardWriter, boardTitle, boardContent, boardValue);
        JSONObject responseJSON = new JSONObject();
        if(state == 1) {
            responseJSON.put("result", "success");
        } else {
            responseJSON.put("result", "fail");
        }
        return responseJSON.toString();
    }

    // 이미지 처리관련 메서드
    @RequestMapping(value = "/images", produces = "application/json; charset=utf8")
    @ResponseBody
    public String uploadSummernoteImageFile(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) {
        JsonObject jsonObject = new JsonObject();


        String contextRoot = new HttpServletRequestWrapper(request).getRealPath("/");
        String fileRoot = contextRoot + "board/images/";

        String originalFileName = multipartFile.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = UUID.randomUUID() + extension;

        File targetFile = new File(fileRoot + savedFileName);
        try {
            InputStream fileStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile);
            jsonObject.addProperty("url", "/board/images/" + savedFileName);
            jsonObject.addProperty("responseCode", "success");

        } catch (IOException e) {
            FileUtils.deleteQuietly(targetFile);
            jsonObject.addProperty("responseCode", "error");
            e.printStackTrace();
        }
        String a = jsonObject.toString();
        return a;
    }

    @GetMapping("/freeDetail")
    public String freeDetail(
            Model model,
            @RequestParam("boardNum") int boardNum,
            HttpServletRequest request) {

        HttpSession session = request.getSession();
        if(session.getAttribute("member") == null) {
            return "redirect:/member/login?error=1";
        }

        model.addAttribute("dList", this.boardMapper.selectFreeDetail(boardNum));
        model.addAttribute("Pcomment", this.boardMapper.AllComment(boardNum));
        this.boardMapper.boardCount(boardNum);
        return "/board/freeBoardDetail";
    }

    // 자유게시판 수정하기
    @GetMapping("/editPost")
    public String editPost(int boardNum, Model model) {
        model.addAttribute("eList", this.boardMapper.selectFreeDetail(boardNum));
        return "board/freeBoardEdit";
    }

    @RequestMapping(value = "/editPost", method = RequestMethod.POST)
    @ResponseBody
    public String editPost(
            @RequestParam("boardWriter") String boardWriter,
            @RequestParam("boardTitle") String boardTitle,
            @RequestParam("boardContent") String boardContent,
            @RequestParam("boardValue") String boardValue,
            int boardNum) {
        int state = this.boardMapper.freeUpdate(boardWriter, boardValue, boardTitle, boardContent, boardNum);
        JSONObject responseJSON = new JSONObject();
        if(state == 1) {
            responseJSON.put("result", "success");
        } else {
            responseJSON.put("result", "fail");
        }
        return responseJSON.toString();
    }

    @GetMapping(value = "/deletePost")
    public String deletePost(int boardNum) {
        this.boardMapper.freeDelete(boardNum);
        return "redirect:/board/free";
    }

    // ----------------------------------------- 좋아요 기능 시작
    @RequestMapping(value = "/like", method = RequestMethod.GET)
    @ResponseBody
    public String likeIt(@RequestParam("boardWriter") String boardWriter, @RequestParam("boardNum") int boardNum) {
        int state = this.boardMapper.likeCount(boardWriter, boardNum);

        JSONObject responseJson = new JSONObject();

        if(state != 0) {
            responseJson.put("result", 1);
            return responseJson.toString();
        } else {
            responseJson.put("result", 0);
            return responseJson.toString();
        }
    }

    @RequestMapping(value = "/undolike", method = RequestMethod.GET)
    @ResponseBody
    public String undoLikeIt(@RequestParam("boardWriter") String boardWriter, @RequestParam("boardNum") int boardNum) {
        int state = this.boardMapper.undoLikeCount(boardWriter, boardNum);

        JSONObject responseJson = new JSONObject();

        if(state != 0) {
            responseJson.put("result", 1);
            return responseJson.toString();
        } else {
            responseJson.put("result", 0);
            return responseJson.toString();
        }
    }

    @RequestMapping(value = "/SelectLike", method = RequestMethod.GET)
    @ResponseBody
    public String DidYouLikeIt(@RequestParam("boardLikesUser") String boardLikesUser, @RequestParam("boardNum") int boardNum) {
        JSONObject responseJson = new JSONObject();
        BoardEntity board = this.boardMapper.selectLikedUser(boardNum);
        System.out.println(board);
        int num = board.getBoardLikes();
        System.out.println(num);
        responseJson.put("num", num);

        ArrayList<String> list = new ArrayList<>();
        String nName = board.getBoardLikesUser();
        for(String e : nName.split(","))
            list.add(e);
        for (String s : list) {
            if(s.equals(boardLikesUser)) {
                responseJson.put("result", "LikedIt");
                return responseJson.toString();
            }
        }
        responseJson.put("result", "nope");
        return responseJson.toString();
    }


    //    =======================댓글 시작==========================================
    @RequestMapping(value = "/insertComment", method = RequestMethod.GET)
    @ResponseBody
    public String getComment(
            @RequestParam(value = "boardNum") int boardNum,
            @RequestParam(value = "commentWriter") String commentWriter,
            @RequestParam(value = "commentContent") String commentContent
    ) {
        CommentEntity comment = new CommentEntity();
        comment.setCommentContent(commentContent);
        comment.setCommentWriter(commentWriter);
        comment.setBoardNum(boardNum);
        int state = this.boardMapper.commentInsert(comment);

        JSONObject responseJson = new JSONObject();

        if (state != 0) {
            responseJson.put("result", 1);
            return responseJson.toString();
        } else {
            responseJson.put("result", 0);
            return responseJson.toString();
        }


    }

    @RequestMapping(value = "/InsertReplyComment", method = RequestMethod.GET)
    public String postComment(
            CommentEntity comment,
            @RequestParam(value = "boardNum",required = false) int boardNum,
            @RequestParam(value = "commentWriter",required = false) String commentWriter,
            @RequestParam(value = "commentContent",required = false) String commentContent,
            @RequestParam(value = "commentIndex") int commentIndex

    ) {
        CommentEntity parent = this.boardMapper.selectParentComments(commentIndex);

        comment.setCommentContent(commentContent);
        comment.setCommentWriter(commentWriter);
        comment.setCommentGroup(parent.getCommentGroup());
        comment.setCommentSequence(parent.getCommentSequence() + 1);
        comment.setCommentLevel(parent.getCommentLevel() + 1);
        comment.setBoardNum(boardNum);
        this.boardMapper.replySequence(parent);



        int state = this.boardMapper.replyInsert(comment);


        if (state != 0) {
            return "redirect:/board/freeDetail?boardNum="+boardNum;
        } else {
            return "redirect:/board/freeDetail?boardNum="+boardNum;
        }

    }

    @RequestMapping(value = "/deleteComment", method = RequestMethod.GET)
    public String deleteComment(@RequestParam("commentIndex") int commentIndex, int boardNum) {
        this.boardMapper.deleteComments(commentIndex);
        return "redirect:/board/freeDetail?boardNum="+boardNum;
    }

}
