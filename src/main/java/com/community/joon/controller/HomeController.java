package com.community.joon.controller;

import com.community.joon.entity.MemberEntity;
import com.community.joon.mapper.IBoardMapper;
import com.community.joon.mapper.IMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    @Autowired
    private IMemberMapper memberMapper;

    @Autowired
    private IBoardMapper boardMapper;

    @RequestMapping(value="/", method=RequestMethod.GET)
    public String index(Model model){
        model.addAttribute("mFree", this.boardMapper.mainFree());
        model.addAttribute("mNotice", this.boardMapper.mainNotice());
        return "index";
    }
}
