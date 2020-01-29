package cn.bb.dao;

import cn.bb.entity.Role;
import cn.bb.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users WHERE account = #{account}")
    public User GetUserByAccount(@Param("account") String account);

    @Select("SELECT roleName FROM userrole WHERE userId = #{id}")
    public List<String> findUserAllRoleByUserId(@Param("id") Integer id);

    @Insert("INSERT INTO users (account,name, password) VALUES (#{user.account}, #{user.name},#{user.password})")
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
}
