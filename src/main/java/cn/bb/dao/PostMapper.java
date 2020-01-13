package cn.bb.dao;

import cn.bb.entity.Content;
import cn.bb.entity.Post;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
}
