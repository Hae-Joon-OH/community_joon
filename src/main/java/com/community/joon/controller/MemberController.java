package com.community.joon.controller;

import com.community.joon.entity.MemberEntity;
import com.community.joon.mapper.IBoardMapper;
import com.community.joon.mapper.IMemberMapper;
import com.community.joon.util.CryptoUtils;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value="/member")
public class MemberController {

    @Autowired
    private IMemberMapper memberMapper;

    @Autowired
    private IBoardMapper boardMapper;

    @GetMapping(value="/register")
    public String register() {
        return "member/register";
    }

    @PostMapping(value="/registerProcess")
    public String registerProcess(MemberEntity member) {
        member.setMemberPassword(CryptoUtils.hashSha512(member.getMemberPassword()));
        this.memberMapper.register(member);
        return "redirect:/";

    }

    @RequestMapping(value="/nameDuplication", method= RequestMethod.POST)
    @ResponseBody
    public String duplicationNickname(@RequestParam("memberNickname")String memberNickname) {
        MemberEntity member = this.memberMapper.nameDuplication(memberNickname);
        JSONObject responseJson = new JSONObject();
        if(member != null) { // 별명이 중복이면
            responseJson.put("result", "fail");
        } else {
            responseJson.put("result", "success");
        }
        return responseJson.toString();
    }

    @RequestMapping(value="/emailDuplication", method= RequestMethod.POST)
    @ResponseBody
    public String duplicationEmail(@RequestParam("memberEmail")String memberEmail) {
        MemberEntity member = this.memberMapper.emailDuplication(memberEmail);
        JSONObject responseJson = new JSONObject();
        if(member != null) { // 별명이 중복이면
            responseJson.put("result", "fail");
        } else {
            responseJson.put("result", "success");
        }
        return responseJson.toString();
    }

    @GetMapping(value = "/login")
    public String login(@RequestParam(value="error", defaultValue = "0") int error, Model model) {
        model.addAttribute("error", error);
        return "member/login";
    }

    @RequestMapping(value="/login", method = RequestMethod.POST)
    @ResponseBody
    public String login(HttpServletRequest request, @RequestParam("memberEmail")String memberEmail, @RequestParam("memberPassword")String memberPassword) {
        String hashPassword = CryptoUtils.hashSha512(memberPassword);
        MemberEntity member = this.memberMapper.login(memberEmail, hashPassword);

        JSONObject responseJson = new JSONObject();
        HttpSession session = request.getSession();

        if(member != null) {
            session.setAttribute("member", member);
            responseJson.put("result", "success");
        } else{
            responseJson.put("result", "fail");
        }
        return responseJson.toString();
    }

    @GetMapping (value="/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/";

    }

    // --------------------------------------------------- 마이 페이지 시작
    @GetMapping("/myPage")
    public String mypage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        MemberEntity member = (MemberEntity) session.getAttribute("member");
        model.addAttribute("mList", this.boardMapper.myBoard(member.getMemberNickname()));
        model.addAttribute("cList", this.boardMapper.myComment(member.getMemberNickname()));
        if(session.getAttribute("myPage") == null) {
            return "redirect:/member/access";
        }
        return "/member/myPage";
    }

    @GetMapping("/access")
    public String getAccess() {
        return "/member/myPageAccess";
    }

    @RequestMapping(value="/access", method = RequestMethod.POST)
    @ResponseBody
    public String postAccess(HttpServletRequest request, @RequestParam("memberEmail")String memberEmail, @RequestParam("memberPassword")String memberPassword) {
        String hashPassword = CryptoUtils.hashSha512(memberPassword);
        MemberEntity member = this.memberMapper.login(memberEmail, hashPassword);

        JSONObject responseJson = new JSONObject();
        HttpSession session = request.getSession();

        if(member != null) {
            session.setAttribute("myPage", member);
            session.setMaxInactiveInterval(60 * 10);
            responseJson.put("result", "success");
        } else{
            responseJson.put("result", "fail");
        }
        return responseJson.toString();
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    @ResponseBody
    public String deleteUser(String memberEmail, String memberPassword) {
        this.memberMapper.deleteUser(memberEmail, memberPassword);
        return null;
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    public String userModify(String memberName, String memberPass, int memberNum) {
        String memberPassword = CryptoUtils.hashSha512(memberPass);
        int state = this.memberMapper.userModify(memberName, memberPassword, memberNum);

        JSONObject responseJson = new JSONObject();

        if(state != 0) {
            responseJson.put("result", "success");
            return responseJson.toString();
        } else {
            responseJson.put("result", "fail");
            return responseJson.toString();
        }
    }

}
