package cn.bb.controller;

import cn.bb.entity.File;
import cn.bb.entity.Post;
import cn.bb.service.PostService;
import cn.bb.vo.postAndContent;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/post")
public class PostController {
    @Autowired
    PostService postService;
    @RequestMapping("/addPost")
    @ResponseBody
    public String AddPost(@RequestParam(name = "postTitle") String postTitle,
                          @RequestParam(name = "postContent") String postContent,
                          @RequestParam(name = "path") String path,
                          @RequestParam(name = "postFileName") String postFileName,
                          HttpServletRequest request) {

        return postService.AddPost(postTitle,postContent,request,path,postFileName);
    }
    @RequestMapping("/toPost")
    public String toPost(@RequestParam(name = "postTitle", defaultValue = "",required = false)  String postTitle,
                         @RequestParam(name = "indexPage",required = false) Integer indexPage,
                         Model model) {
        if (null == indexPage || indexPage < 1) {
            indexPage = 1;
        }

        PageInfo<Post> filePageInfo = postService.GetPostList(postTitle, indexPage);
        if (indexPage > filePageInfo.getPages()) {
            indexPage = filePageInfo.getPages();
        }
        model.addAttribute("postRands",postService.GetPostRand());
        model.addAttribute("indexPage",indexPage);
        model.addAttribute("posts",filePageInfo.getList());
        model.addAttribute("totalPage",filePageInfo.getPages());
        return "pages/fileList";
    }
    @RequestMapping("/toDetail/{postId}")
    public String toPostDetail(@PathVariable(name = "postId") Integer postId, Model model,HttpServletRequest request) {
        postAndContent postAndContent = postService.postContentsList(postId);
        HttpSession session = request.getSession();
        model.addAttribute("userName",session.getAttribute("name"));
        model.addAttribute("userId",session.getAttribute("userId"));
        model.addAttribute("post",postAndContent.getPost());
        model.addAttribute("contents",postAndContent.getContent());
        model.addAttribute("contentsNums",postAndContent.getContent().size());
        model.addAttribute("postRands",postService.GetPostRand());
        return "pages/postDetail";
    }

    @RequestMapping("/comment")
    public String CommentPost(@RequestParam(name = "postId") Integer postId,
                              @RequestParam(name = "userName") String userName,
                              @RequestParam(name = "userId") Integer userId,
                              @RequestParam(name = "content") String contenet) {
        postService.PostComment(postId,userName,userId,contenet);
        return "redirect:/post/toDetail/" + postId;
    }

    @RequestMapping("/collectionPost/{postTitle}/{postUserId}/{postId}")
    @ResponseBody
    public String CollectionPost(@PathVariable(name = "postTitle") String postTitle,
                                 @PathVariable(name = "postUserId") Integer postUserId,
                                 @PathVariable(name = "postId") Integer postId) {
        return postService.CollectPost(postTitle, postUserId, postId);
    }

}
