package cn.bb.dao;

import cn.bb.entity.*;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users WHERE account = #{account} ")
    public User GetUserByAccount(@Param("account") String account);

    @Select("SELECT * FROM users WHERE account = #{account} OR name = #{name}")
    public User GetUserByAccountAndName(@Param("account") String account,@Param("name") String name);

    @Select("SELECT roleName FROM userrole WHERE userId = #{id}")
    public List<String> findUserAllRoleByUserId(@Param("id") Integer id);

    @Insert("INSERT INTO users (account,name, password, sex, collegeValue, collegeName) VALUES (#{user.account}, #{user.name},#{user.password}" +
            " ,#{user.sex},#{user.collegeValue},#{user.collegeName})")
    @Options(useGeneratedKeys = true, keyProperty = "user.id", keyColumn = "id")
    public void addUser(@Param("user") User user);

    @Insert("INSERT INTO userrole (userId, roleId, roleName) VALUES (#{userId}, #{roleId}, #{roleName})")
    public void addRole(@Param("userId") Integer userId, @Param("roleId") Integer roleId, @Param("roleName") String roleName);

    @Select("SELECT * FROM users WHERE account LIKE CONCAT( '%', #{account}, '%' ) ")
    public List<User> GetUserList(@Param("account") String account);

    @Insert("INSERT INTO userrole (userId, roleId, roleName) VALUES (#{userId}, #{roleId}, #{roleName})")
    public void GivM(@Param("userId") Integer userId,@Param("roleId") Integer roleId,@Param("roleName") String roleName );

    @Select("SELECT * FROM users WHERE id = #{userId}")
    public User GetUserById(@Param("userId") Integer userId);

    @Update("UPDATE users SET name = #{username},introduce = #{introduce}  WHERE id = #{userId}")
    public Integer SetUserMsg(@Param("username") String username,
                    @Param("introduce")  String introduce,
                    @Param("userId")  Integer userId);


    @Update("UPDATE users SET password = #{pwd} WHERE id = #{userId}")
    public void SetNewPwd(@Param("userId") Integer id,@Param("pwd") String pwd);

    @Select("SELECT * FROM users WHERE name = #{username}")
    public User GetUserByName(@Param("username") String username);

    @Select("SELECT * FROM replynotify WHERE userId = #{userId}")
    public List<ReplyNotify> GetNotify(@Param("userId") Integer userId);


    @Delete("DELETE FROM replynotify where id = #{notifyId}")
    public void DelNotify(@Param("notifyId") Integer notifyId);

    @Delete("DELETE FROM replynotify where userId = #{userId}")
    public void DelAllNotify(@Param("userId") Integer userId);

    @Select("SELECT * FROM friends where friendId = #{friendId} AND userId = #{userId}")
    public Friend GetFiend(@Param("friendId") Integer friendId,@Param("userId")  Integer userId);

    @Insert("INSERT INTO friends (friendId,friendName,userid) VALUES (#{friendId},#{friendName},#{userId})")
    public void AddFriend(@Param("friendId") Integer friendId,@Param("friendName")  String friendName,@Param("userId")  Integer userId);

    @Select("SELECT * FROM colleges")
    public List<College> GetAllColleges();
}
