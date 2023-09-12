package com.example.demo.controller;

import com.example.demo.DAO.accountDao;
import com.example.demo.DAO.postDao;
import com.example.demo.DAO.replyDao;
import com.example.demo.entity.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class replyController {
    @Autowired
    private replyDao replyDao;
    @Autowired
    private postDao postDao;
    @Autowired
    private accountDao accountDao;

    @RequestMapping("/viewComments/{post_id}")
    List<Reply> viewComments(@PathVariable("post_id") int post_id) {
        return replyDao.findAllByPostAndFatherId(postDao.findById(post_id), -1);
    }

    @RequestMapping("/viewSR/{post_id}/{reply_id}")
    List<Reply> viewSR(@PathVariable("post_id") int post_id, @PathVariable("reply_id") int reply_id) {
        Reply temp = replyDao.findAllById(reply_id);
        return replyDao.findAllByFatherId(temp.getId());
    }

    @RequestMapping("/checkSR/{reply_id}")
    boolean checkSR(@PathVariable("reply_id") int id) {
        return replyDao.countAllByFatherId(id) != 0;
    }

    @RequestMapping("/checkReplySize")
    int checkReplySize() {
        return replyDao.countAllBy();
    }

    @RequestMapping("/replyToComment/{reply_id}/{father_id}/{post_id}/{content}/{author}")
    int replyToComment(@PathVariable("reply_id") int reply_id, @PathVariable("father_id") int father_id,
                       @PathVariable("post_id") int post_id, @PathVariable("content") String content, @PathVariable("author") String author) {
        return replyDao.replyToComment(reply_id, father_id, post_id, content, author);
    }

    @RequestMapping("/selectOwnReply/{name}")
    List<Reply> selectOwnReply(@PathVariable("name") String name) {
        return replyDao.findAllByAuthor(accountDao.findAccountByName(name));
    }

    @RequestMapping("/checkFather/{postid}/{id}")
    boolean checkFather(@PathVariable("id") int id, @PathVariable("postid") int post_id) {
        return replyDao.countByFatherIdAndPost(id, postDao.findById(post_id)) != 0;
    }
}
