package cn.bb.dao;

import cn.bb.entity.Collections;
import cn.bb.entity.Content;
import cn.bb.entity.Post;
import org.apache.ibatis.annotations.*;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;

import java.util.List;

@Mapper
public interface PostMapper {

    @Select("SELECT * from posts WHERE postTitle LIKE CONCAT('%',#{postTitle},'%')" +
            " ORDER BY postCreatTime desc")
    public List<Post> GetPostList(@Param("postTitle") String postTitle);

    @Insert("INSERT INTO posts (postTitle, postUserName, postUserId, postContent, path, postFileName) VALUES" +
            "   (#{postTitle}, #{postUserName}, #{postUserId}, #{postContent}, #{path}, #{postFileName})")
    public void AddPost(@Param("postTitle") String postTitle,@Param("postContent") String postContent,
                        @Param("postUserId") Integer postUserId,@Param("postUserName") String postUserName,
                        @Param("path") String path,
                        @Param("postFileName") String postFileName);

    @Select("SELECT * FROM posts WHERE postId = #{postId}")
    public Post GetPostMsg(@Param("postId") Integer postId);

    @Select("SELECT * FROM contents WHERE postId = #{postId}")
    public List<Content> GetPostContents(@Param("postId") Integer postId);


    @Insert("INSERT INTO contents (userId, userName, contentMsg, postId) VALUES " +
            " (#{userId}, #{userName}, #{contentMsg}, #{postId})")
    public void PostContent(@Param("userId") Integer userId,@Param("userName")  String userName,@Param("contentMsg")  String contentMsg,
                            @Param("postId") Integer postId);

    @Update("UPDATE posts SET commentNums = commentNums + 1 WHERE postId = #{postId}")
    public void PostAddCommentNums(@Param("postId") Integer postId);

    @Select("SELECT * FROM posts posts ORDER BY commentNums DESC limit 0, 10")
    public List<Post> GetPostRand();

    @Insert("INSERT INTO collections (postTitle,postUserId,postId) VALUES "
            + " (#{postTitle}, #{postUserId}, #{postId}) ")
    public int CollectPost(@Param("postTitle") String postTitle,@Param("postUserId") Integer postUserId,
                            @Param("postId") Integer postId);

    @Select("SELECT * FROM posts WHERE postUserId = #{postUserId}")
    public List<Post> GetUserPost(@Param("postUserId") Integer postUserId);

    @Select("SELECT * FROM collections WHERE postUserId = #{postUserId}")
    public List<Collections> GetUserCollections(@Param("postUserId") Integer postUserId);
}
