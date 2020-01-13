package cn.bb.dao;

import cn.bb.entity.File;
import cn.bb.entity.PostFile;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT * from files WHERE fileName LIKE CONCAT('%',#{fileName},'%')" +
            " ORDER BY id desc")
    public List<File> GetFileList(@Param("fileName") String fileName);


    @Select("SELECT * from posts WHERE id = #{id}")
    public File GetFileById(@Param("id") Integer id);

    @Insert("INSERT INTO files (fileName,path,size,msg) VALUES (#{file.fileName}, #{file.path}, #{file.size}, #{file.msg})")
    public void AddFile(@Param("file") File file);

    @Update("DELETE FROM files WHERE fileName = #{name}")
    public void DeleteFileByName(@Param("name")String name);

    @Update("DELETE FROM files WHERE id = #{id}")
    public void DeleteFileById(@Param("id")Integer id);

    @Select("SELECT path, postFileName FROM posts WHERE postId = #{id}")
    public PostFile GetPostPath(@Param("id") Integer id);
}
