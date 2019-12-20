package cn.bb.service;

import cn.bb.dao.UserMapper;
import cn.bb.entity.User;
import net.sf.json.JSONObject;
import netscape.javascript.JSObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Service
public class IndexService {
    @Autowired
    JSONObject jsonObject;
    @Autowired
    UserMapper userMapper;
    public String check(String account, String password, HttpServletRequest request, HttpServletResponse response) {
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
            User user = userMapper.GetUserByAccount(account);
            HttpSession session = request.getSession();
            String id = session.getId();
            session.setAttribute("account",user.getAccount());
            session.setMaxInactiveInterval(500000);
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


}
