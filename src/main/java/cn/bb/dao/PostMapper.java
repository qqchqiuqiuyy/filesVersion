package cn.bb.dao;

import cn.bb.entity.*;
import org.apache.ibatis.annotations.*;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;

import java.util.List;

@Mapper
public interface PostMapper {

    @Select("SELECT * from posts  ORDER BY postCreatTime desc")
    public List<Post> GetPostList();

    @Select("SELECT * from posts WHERE collegeId = #{collegeId}" +
            " ORDER BY postCreatTime desc")
    public List<Post> GetPostList2(@Param("collegeId") Integer collegeId);

    @Insert("INSERT INTO posts (postTitle, postUserName, uid, postContent, path, postFileName,collegeId, collegeName  ) VALUES" +
            "   (#{postTitle}, #{postUserName}, #{uid}, #{postContent}, #{path}, #{postFileName}, #{collegeId}, #{collegeName})")
    @Options(useGeneratedKeys = true, keyProperty = "po.pid", keyColumn = "pid")
    public Integer AddPost(@Param("postTitle") String postTitle,@Param("postContent") String postContent,
                        @Param("uid") Integer uid,@Param("postUserName") String postUserName,
                        @Param("path") String path,
                        @Param("postFileName") String postFileName,
                        @Param("collegeId") Integer collegeId,
                        @Param("collegeName") String collegeName,@Param("po") Post returnId);

    @Select("SELECT * FROM posts WHERE pid = #{postId}")
    public Post GetPostMsg(@Param("postId") Integer postId);

    @Select("SELECT * FROM contents WHERE pid = #{postId}")
    public List<Content> GetPostContents(@Param("postId") Integer postId);


    @Insert("INSERT INTO contents (uid, userName, contentMsg, pid) VALUES " +
            " (#{uid}, #{userName}, #{contentMsg}, #{postId})")
    @Options(useGeneratedKeys = true, keyProperty = "notify.conid", keyColumn = "conid")
    public void PostContent(@Param("uid") Integer uid,@Param("userName")  String userName,@Param("contentMsg")  String contentMsg,
                            @Param("postId") Integer postId, @Param("notify") ReplyNotify notify);

    @Update("UPDATE posts SET commentNums = commentNums + 1 WHERE pid = #{postId}")
    public void PostAddCommentNums(@Param("postId") Integer postId);

    @Select("SELECT * FROM posts  ORDER BY commentNums DESC limit 0, 10")
    public List<Post> GetPostRand();

    @Insert("INSERT INTO collections (postTitle,uid,pid) VALUES "
            + " (#{postTitle}, #{uid}, #{postId}) ")
    public int CollectPost(@Param("postTitle") String postTitle,@Param("uid") Integer uid,
                            @Param("postId") Integer postId);

    @Select("SELECT * FROM posts WHERE uid = #{uid}")
    public List<Post> GetUserPost(@Param("uid") Integer uid);

    @Select("SELECT * FROM collections WHERE uid = #{uid}")
    public List<Collections> GetUserCollections(@Param("uid") Integer uid);

    @Insert("INSERT INTO replynotify (userName, uid,pid,replyPostName,replyUserId,replyUserName,conid)" +
            " VALUES (#{userName},#{userId},#{postId},#{postName},#{replyUserId},#{replyUserName},#{conid})")
    public void AddNotify(@Param("userName") String userName,@Param("userId")  Integer userId,@Param("postId")  Integer postId,
                          @Param("postName") String postName,@Param("replyUserId")  Integer replyUserId,
                          @Param("replyUserName")   String replyUserName,
                          @Param("conid")  Integer conid);


    @Select("SELECT * FROM friends WHERE uid = #{uid}")
    public List<Friend> GetUserFriends(@Param("uid") Integer userId);

    @Select("SELECT * FROM colleges ")
    public List<College> GetAllColleges();

    @Delete("DELETE FROM collections WHERE pid = #{postId}")
    public Integer DelCollections(@Param("postId") Integer postId);

    @Delete("DELETE FROM contents WHERE pid = #{postId}")
    public Integer DelContents(@Param("postId") Integer postId);

    @Delete("DELETE FROM posts WHERE pid = #{postId}")
    public Integer DelPost(@Param("postId") Integer postId);

    @Delete("DELETE FROM replynotify WHERE pid = #{postId}")
    public Integer DelReplyNotify(@Param("postId") Integer postId);

    @Delete("DELETE FROM contents WHERE conid = #{contentId}")
    void DelContentByContentId(@Param("contentId") Integer contentId);

    @Select("SELECT path, postFileName FROM posts WHERE pid = #{id}")
    public PostFile GetPostPath(@Param("id") Integer id);

    @Select("SELECT * FROM posts WHERE pid = #{id}")
    Post GetPost(@Param("id")  Integer pid);
    @Select("SELECT * FROM posts WHERE pid = #{id} AND collegeId = #{collegeId}")
    Post GetPost2(@Param("id") Integer pid, @Param("collegeId") Integer collegeId);


    @Select("SELECT * from posts WHERE postTitle LIKE CONCAT('%',#{postTitle},'%')" +
            " ORDER BY postCreatTime desc")
    public List<Post> GetPost3(@Param("postTitle") String postTitle);

    @Select("SELECT * from posts WHERE postTitle LIKE CONCAT('%',#{postTitle},'%') AND collegeId = #{collegeId} " +
            " ORDER BY postCreatTime desc" )
    public List<Post>  GetPost4(@Param("postTitle") String postTitle, @Param("collegeId") Integer collegeId);

    @Select("select * from collections where pid = #{postId} AND uid = #{postUserId}")
    public Collections GetCollections(@Param("postUserId") Integer postUserId, @Param("postId") Integer postId);
}
