package cn.bb.controller;

import cn.bb.entity.Friend;
import cn.bb.entity.Post;
import cn.bb.entity.ReplyNotify;
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
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

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
    public String toReg(Model model) {
        model.addAttribute("colleges",userService.GetAllColleges());
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
                           @RequestParam("repassword") String repassword,
                           @RequestParam("sex") String sex,
                           @RequestParam("collegeValue") Integer collegeValue,
                           @RequestParam("collegeName") String collegeName){
        return userService.register(account, password,name, repassword,sex,collegeValue,collegeName);
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
    public String ToLogOut(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/post/toPost";
    }

    @RequestMapping("/index")
    public String ToIndex(HttpServletRequest request,Model model){
        Map<String, List> stringListMap = userService.GetUserMsg(request);
        model.addAttribute("posts",stringListMap.get("posts"));
        model.addAttribute("collections",stringListMap.get("collections"));
        return "pages/userIndex";
    }
    @RequestMapping("/set")
    public String ToSet(HttpServletRequest request,Model model){
        HttpSession session = request.getSession();
        Integer userId = (Integer)session.getAttribute("userId");
        User user = userService.GetUserById(userId);
        model.addAttribute("user",user);
        return "pages/userSet";
    }
    @RequestMapping("/setMsg")
    @ResponseBody
    public String SetUserMsg(@RequestParam(name = "username") String username
            ,@RequestParam(name = "introduce") String introduce,HttpServletRequest request){
        return userService.SetUserMsg(username, introduce, request);
    }

    @RequestMapping("/setPwd")
    @ResponseBody
    public String SetUserPwd(@RequestParam(name = "pass") String pass,
                             @RequestParam(name = "repass") String repass,HttpServletRequest request){
        return userService.SetUserPwd(pass,repass, request);
    }

    @RequestMapping("/home/{uid}")
    public String ToUserHome(@PathVariable(name = "uid") Integer userId,Model model){
        Map<String, Object> map = userService.ToUserHome(userId);
        model.addAttribute("user",(User)map.get("user"));
        model.addAttribute("posts",(List<Post>)map.get("posts"));
        model.addAttribute("friends",(List<Friend>)map.get("friends"));
        return "pages/userHome";
    }

    @RequestMapping("/notify")
    public String ToNotify(HttpServletRequest request,Model model){
        List<ReplyNotify> replyNotifies = userService.GetNotify(request);
        model.addAttribute("notifies",replyNotifies);
        return "pages/userNotify";
    }


    @RequestMapping("/delNotify/{notifyId}")
    @ResponseBody
    public String DelNotify(@PathVariable(name = "notifyId") Integer notifyId){
        return userService.DelNotify(notifyId);
    }

    @RequestMapping("/delAllNotify/{userId}")
    @ResponseBody
    public String DelAllNotify(@PathVariable(name = "userId") Integer userId){
        return userService.DelAllNotify(userId);
    }

    @RequestMapping("/addFriend/{friendId}/{friendName}/{userId}")
    @ResponseBody
    public String AddFriend(@PathVariable(name = "friendId") Integer friendId,
                            @PathVariable(name = "friendName") String friendName,
                            @PathVariable(name = "userId") Integer userId){
        return userService.AddFriend(friendId,friendName,userId);
    }
}
