package cn.bb.controller;

import cn.bb.entity.User;
import cn.bb.service.UserService;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.regexp.internal.RE;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    JSONObject jsonObject;
    @RequestMapping("/toLogin")
    public String toLogin() {
        return "pages/login";
    }

    @RequestMapping("/toReg")
    public String toReg() {
        return "pages/reg";
    }

    @RequestMapping("/login")
    @ResponseBody
    public String login(@RequestParam("account") String account, @RequestParam("password") String password
                         , HttpServletRequest request, HttpServletResponse response) {

        return userService.login(account,password,request,response);
    }

    @RequestMapping("/register")
    @ResponseBody
    public String register(@RequestParam("account") String account, @RequestParam("password")String password,
                           @RequestParam("name") String name,
                           @RequestParam("repassword") String repassword){
        return userService.register(account, password,name, repassword);
    }

    @RequestMapping("/toUserList")
    public String toList(@RequestParam(name = "account", defaultValue = "",required = false)  String account,
                         @RequestParam(name = "indexPage",required = false) Integer indexPage,
                         Model model){
        if (null == indexPage || indexPage < 1) {
            indexPage = 1;
        }

        PageInfo<User> userPageInfo = userService.GetUserList(account, indexPage);
        model.addAttribute("indexPage",indexPage);
        model.addAttribute("users",userPageInfo.getList());
        return "pages/userList";
    }

    @RequestMapping("/giveMan/{userId}")
    @ResponseBody
    public String giveMan(@PathVariable("userId") Integer userId) {
        return userService.GivM(userId);
    }
    @GetMapping("/logout")
    public String toLogOut(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/post/toPost";
    }
}
