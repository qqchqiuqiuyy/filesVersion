package cn.bb.service;

import cn.bb.common.Page;
import cn.bb.dao.PostMapper;
import cn.bb.dao.UserMapper;
import cn.bb.entity.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.channels.NoConnectionPendingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Resource
    UserMapper userMapper;
    @Resource
    PostMapper postMapper;
    @Autowired
    JSONObject jsonObject;
    public String login(String account, String password
                         ,HttpServletRequest request, HttpServletResponse response) {
        // 每个请求创建一个Subject 获取Subject
        Subject subject = SecurityUtils.getSubject();
        //封装用户数据 用 用户名作为盐 md5 加密密码
        Md5Hash pwd = new Md5Hash(password, ByteSource.Util.bytes(account));
        password = pwd.toString();
        //存取账号密码
        UsernamePasswordToken token = new UsernamePasswordToken(account, password);
        try {
            subject.login(token);
            //找到账号
            User user = findUserByAccount(account);
            HttpSession session = request.getSession();
            String id = session.getId();
            session.setAttribute("name",user.getName());
            session.setAttribute("userId",user.getId());
            session.setMaxInactiveInterval(360000);
            jsonObject.put("success",1);
            jsonObject.put("msg","登录成功!!");
        }catch (UnknownAccountException e){
            jsonObject.put("msg","用户不存在");
            jsonObject.put("success",0);
        }catch (IncorrectCredentialsException e){
            jsonObject.put("msg","密码错误");
            jsonObject.put("success",0);
        }
        return jsonObject.toString();
    }

    public String register(String account, String password, String name,String repassword) {
        if("".equals(account)|| "".equals(password)   || "".equals(repassword) || "".equals(name)){
            jsonObject.put("msg","信息输入不完整不能空白");
            jsonObject.put("success", 0);
            return jsonObject.toString();
        }
        if(!password.equals(repassword)){
            jsonObject.put("msg","两次密码不一致请重新输入");
            jsonObject.put("success",0);
            return jsonObject.toString();
        }
        User user = findUserByAccount(account);
        if(null == user){
            user = new User();
            //用户名加盐  MD5加密输入进来的密码
            Md5Hash pwd = new Md5Hash(password, ByteSource.Util.bytes(account));
            user.setAccount(account);
            user.setPassword(pwd.toString());

            //存入数据库
            userMapper.addUser(user);
            //新增用户给予user:common角色
            userMapper.addRole(user.getId(), 1,"common");
            jsonObject.put("msg","注册成功!!");
            jsonObject.put("success",1);
        }else if (user.getAccount().equals(account)){
            jsonObject.put("msg","账号已存在请重新输入");
            jsonObject.put("success",0);
        } else if (user.getName().equals(name)) {
            jsonObject.put("msg","昵称已存在请重新输入");
            jsonObject.put("success",0);
        }
        return jsonObject.toString();
    }
    public List<String> findUserAllRoleByUserId(Integer id) {
        return userMapper.findUserAllRoleByUserId(id);
    }

    public User findUserByAccount(String account) {
        return userMapper.GetUserByAccount(account);
    }


    public PageInfo<User> GetUserList(String account, Integer pageIndex) {
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }
        if (account == null) {
            account = "";
        }
        PageHelper.startPage(pageIndex, Page.PAGE_SIZE);
        List<User> users = userMapper.GetUserList(account);
        return new PageInfo<>(users);
    }

    public String GivM(Integer userId) {
        userMapper.GivM(userId,2,"manager");
        jsonObject.put("msg","授权成功");
        jsonObject.put("success","1");
        return jsonObject.toString();
    }


    public Map<String,List> GetUserMsg(HttpServletRequest request) {
        Integer userId = (Integer)request.getSession().getAttribute("userId");
        List<Post> posts = postMapper.GetUserPost(userId);
        List<Collections> collections = postMapper.GetUserCollections(userId);
        Map<String, List> stringListHashMap = new HashMap<>();
        stringListHashMap.put("posts",posts);
        stringListHashMap.put("collections",collections);
        return stringListHashMap;
    }

    public User GetUserById(Integer userId) {
        return userMapper.GetUserById(userId);
    }

    @Transactional(rollbackFor =  Exception.class)
    public String SetUserMsg(String username, String introduce, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer userId = (Integer)session.getAttribute("userId");
        userMapper.SetUserMsg(username,introduce,userId) ;
        jsonObject.put("success","1");
        session.setAttribute("name",username);
        return jsonObject.toString();
    }

    public String SetUserPwd( String pass, String repass, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer userId = (Integer)session.getAttribute("userId");
        User user = userMapper.GetUserById(userId);
        if("".equals(pass)   || "".equals(repass) ){
            jsonObject.put("msg","信息输入不完整不能空白");
            jsonObject.put("success", 0);
            return jsonObject.toString();
        }
        if(!pass.equals(repass)){
            jsonObject.put("msg","两次新密码不一致请重新输入");
            jsonObject.put("success",0);
            return jsonObject.toString();
        }
        //用户名加盐  MD5加密输入进来的密码
        Md5Hash pwd = new Md5Hash(pass, ByteSource.Util.bytes(user.getAccount()));
        userMapper.SetNewPwd(user.getId(),pwd.toString());
        jsonObject.put("success",1);
        return jsonObject.toString();
    }
}
