package cn.bb.dao;

import cn.bb.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PostMapper {

    @Select("SELECT * from posts WHERE fileName LIKE CONCAT('%',#{postName},'%')" +
            " ORDER BY postCreatTime desc")
    public List<Post> GetPostList(@Param("postName") String postName);
}
