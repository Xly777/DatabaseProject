package com.example.demo.DAO;

import com.example.demo.entity.Author;
import com.example.demo.entity.Post;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface postDao extends JpaRepository<Post, Integer> {
    int countPostById(int id);

    Post findById(int id);

    int countAllBy();

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "insert into posts(post_id, title, content, posting_city, author,anonymous) values (?1,?2,?3,?4,?5,?6);", nativeQuery = true)
    int post(int post_id, String title, String content, String posting_ciy, String author, boolean a);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "insert into reply(reply_id, father_id, post_id, content , stars, author) values (?1,?2,?3,?4,0,?5);", nativeQuery = true)
    int comment(int reply_id, int father_id, int post_id, String content, String author);

    List<Post> findAllByAuthor(Author author);

    @Query(value = "select * from posts limit ?1 offset ?2", nativeQuery = true)
    List<Post> showRandomPosts(int limit, int offset);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "update posts set viewCount=viewCount+1 where posts.post_id=:id", nativeQuery = true)
    int viewCount(@Param("id") int post_id);

    @Query(value = "select * from posts order by posts.viewcount desc limit ?1 offset ?2 ", nativeQuery = true)
    List<Post> showHotPosts(int limit, int offset);

    @Query(value = "select * from posts where posts.content similar to :string or posts.title similar to :string ;", nativeQuery = true)
    List<Post> search(String string);

}
