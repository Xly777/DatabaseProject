package com.example.demo.controller;

import com.example.demo.DAO.authorDao;
import com.example.demo.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class postController {
    @Autowired
    private com.example.demo.DAO.postDao postDao;
    @Autowired
    private authorDao authorDao;

    @RequestMapping("/checkPostExist/{id}")
    public boolean checkPostExist(@PathVariable("id") int id) {
        return postDao.countPostById(id) != 0;
    }

    @RequestMapping("/viewPost/{id}")
    public Post viewPost(@PathVariable("id") int id) {
        return postDao.findById(id);
    }

    @RequestMapping("/checkPostSize")
    public int checkPostSize() {
        return postDao.countAllBy();
    }

    @RequestMapping("/post/{postid}/{title}/{content}/{posting_city}/{author}/{anonymous}")
    public int post(@PathVariable("postid") int post_id, @PathVariable("title") String title, @PathVariable("content") String content,
                    @PathVariable("posting_city") String posting_city, @PathVariable("author") String author, @PathVariable("anonymous") String anonymous) {
        if (anonymous.equals("true")) {
            return postDao.post(post_id, title, content, posting_city, author, true);
        } else {
            return postDao.post(post_id, title, content, posting_city, author, false);
        }
    }

    @RequestMapping("/writeComment/{replyid}/{father_id}/{post_id}/{content}/{author}")
    int writeComment(@PathVariable("replyid") int id, @PathVariable("father_id") int father_id, @PathVariable("post_id") int post_id,
                     @PathVariable("content") String content, @PathVariable("author") String author) {
        return postDao.comment(id, father_id, post_id, content, author);
    }

    @RequestMapping("/selectOwnPosts/{name}")
    List<Post> selectOwnPosts(@PathVariable("name") String name) {
        return postDao.findAllByAuthor(authorDao.findAuthorByAuthor(name));
    }

    @RequestMapping("/showRandomPosts/{limit}/{offset}")
    List<Post> showRandomPosts(@PathVariable("limit") int limit, @PathVariable("offset") int offset) {
        return postDao.showRandomPosts(limit, offset);
    }

    @RequestMapping("/viewCount/{post_id}")
    int viewCount(@PathVariable("post_id") int post_id) {
        return postDao.viewCount(post_id);
    }

    @RequestMapping("/showHotPosts/{limit}/{offset}")
    List<Post> showHotPosts(@PathVariable("limit") int limit, @PathVariable("offset") int offset) {
        return postDao.showHotPosts(limit, offset);
    }

    @RequestMapping("/search/{string}")
    List<Post> search(@PathVariable("string") String string) {
        return postDao.search(string);
    }
}
