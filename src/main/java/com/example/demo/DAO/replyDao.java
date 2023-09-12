package com.example.demo.DAO;

import com.example.demo.entity.Account;
import com.example.demo.entity.Author;
import com.example.demo.entity.Post;
import com.example.demo.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface replyDao extends JpaRepository<Reply, Integer> {


    List<Reply> findAllByPostAndFatherId(Post post, int father_id);

    Reply findAllById(int reply_id);

    List<Reply> findAllByFatherId(int father_id);

    int countAllByFatherId(int father_id);

    int countAllBy();

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "insert into reply(reply_id, father_id, post_id,content, stars, author) values (?1,?2,?3,?4,0,?5);", nativeQuery = true)
    int replyToComment(int reply_id, int father_id, int post_id, String content, String author);

    List<Reply> findAllByAuthor(Account account);

    int countByFatherIdAndPost(int id,Post p);
}
