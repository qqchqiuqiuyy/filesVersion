package cn.bb.dao;

import cn.bb.entity.*;
import org.apache.ibatis.annotations.*;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;

import java.util.List;

@Mapper
public interface PostMapper {

    @Select("SELECT * from posts WHERE postTitle LIKE CONCAT('%',#{postTitle},'%') AND collegeValue = #{collegeValue}" +
            " ORDER BY postCreatTime desc")
    public List<Post> GetPostList(@Param("postTitle") String postTitle, @Param("collegeValue") Integer collegeValue);

    @Select("SELECT * from posts WHERE postTitle LIKE CONCAT('%',#{postTitle},'%')" +
            " ORDER BY postCreatTime desc")
    public List<Post> GetPostList2(@Param("postTitle") String postTitle, @Param("collegeValue") Integer collegeValue);

    @Insert("INSERT INTO posts (postTitle, postUserName, postUserId, postContent, path, postFileName,collegeValue, collegeName  ) VALUES" +
            "   (#{postTitle}, #{postUserName}, #{postUserId}, #{postContent}, #{path}, #{postFileName}, #{collegeValue}, #{collegeName})")
    @Options(useGeneratedKeys = true, keyProperty = "po.postId", keyColumn = "postId")
    public Integer AddPost(@Param("postTitle") String postTitle,@Param("postContent") String postContent,
                        @Param("postUserId") Integer postUserId,@Param("postUserName") String postUserName,
                        @Param("path") String path,
                        @Param("postFileName") String postFileName,
                        @Param("collegeValue") Integer collegeValue,
                        @Param("collegeName") String collegeName,@Param("po") Post returnId);

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

    @Insert("INSERT INTO replynotify (userName, userId,replyPostId,replyPostName,replyUserId,replyUserName)" +
            " VALUES (#{userName},#{userId},#{postId},#{postName},#{replyUserId},#{replyUserName})")
    public void AddNotify(@Param("userName") String userName,@Param("userId")  Integer userId,@Param("postId")  Integer postId,
                          @Param("postName") String postName,@Param("replyUserId")  Integer replyUserId,
                          @Param("replyUserName")   String replyUserName);


    @Select("SELECT * FROM friends WHERE userId = #{userId}")
    public List<Friend> GetUserFriends(@Param("userId") Integer userId);

    @Select("SELECT * FROM colleges ")
    public List<College> GetAllColleges();

    @Delete("DELETE FROM collections WHERE postId = #{postId}")
    public Integer DelCollections(@Param("postId") Integer postId);

    @Delete("DELETE FROM contents WHERE postId = #{postId}")
    public Integer DelContents(@Param("postId") Integer postId);

    @Delete("DELETE FROM posts WHERE postId = #{postId}")
    public Integer DelPost(@Param("postId") Integer postId);

    @Delete("DELETE FROM replynotify WHERE replyPostId = #{postId}")
    public Integer DelReplyNotify(@Param("postId") Integer postId);

    @Delete("DELETE FROM contents WHERE contentId = #{contentId}")
    void DelContentByContentId(@Param("contentId") Integer contentId);
}
