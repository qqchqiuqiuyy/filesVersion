package cn.bb.controller;

import cn.bb.entity.File;
import cn.bb.entity.Post;
import cn.bb.service.FileService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/post")
public class PostController {
    @Autowired
    PostService postService;
    @Autowired
    FileService fileService;

    @RequestMapping("/addPost")
    @ResponseBody
    public String AddPost(@RequestParam(name = "postTitle") String postTitle,
                          @RequestParam(name = "postContent") String postContent,
                          @RequestParam(name = "path") String path,
                          @RequestParam(name = "postFileName") String postFileName,
                          @RequestParam(name = "collegeId" ) Integer collegeId,
                          @RequestParam(name = "collegeName" ) String collegeName,
                          HttpServletRequest request) {

        return postService.AddPost(postTitle,postContent,request,path,postFileName,collegeId,collegeName);
    }
    @RequestMapping("/toPost")
    public String toPost(@RequestParam(name = "postTitle", defaultValue = "",required = false)  String postTitle,
                         @RequestParam(name = "indexPage",required = false) Integer indexPage,
                         @RequestParam(name = "collegeId",defaultValue = "-1" ,required = false) Integer collegeId,
                         Model model,HttpServletRequest request) {
        if (null == indexPage || indexPage < 1) {
            indexPage = 1;
        }

        PageInfo<Post> filePageInfo = postService.GetPostList(postTitle, indexPage,collegeId);
        if (indexPage > filePageInfo.getPages()) {
            indexPage = filePageInfo.getPages();
        }
        HttpSession session = request.getSession();
        session.setAttribute("collegeId",collegeId);
        model.addAttribute("postRands",postService.GetPostRand());
        model.addAttribute("indexPage",indexPage);
        model.addAttribute("posts",filePageInfo.getList());
        model.addAttribute("totalPage",filePageInfo.getPages());
        model.addAttribute("colleges",postService.GetAllColleges());
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
                              @RequestParam(name = "postName") String postName,
                              @RequestParam(name = "userName") String userName,
                              @RequestParam(name = "userId") Integer userId,
                              @RequestParam(name = "content") String contenet) {
        postService.PostComment(postId,postName,userName,userId,contenet);
        return "redirect:/post/toDetail/" + postId;
    }

    @RequestMapping("/collectionPost/{postTitle}/{postUserId}/{postId}")
    @ResponseBody
    public String CollectionPost(@PathVariable(name = "postTitle") String postTitle,
                                 @PathVariable(name = "postUserId") Integer postUserId,
                                 @PathVariable(name = "postId") Integer postId) {
        return postService.CollectPost(postTitle, postUserId, postId);
    }

    @RequestMapping("/delPost/{postId}")
    @ResponseBody
    public String DelPost(@PathVariable(name = "postId") Integer postId) {
        return postService.DelPost(postId);
    }
    @RequestMapping("/delContent/{contentId}")
    @ResponseBody
    public String DelContent(@PathVariable(name = "contentId") Integer contentId) {
        return postService.DelContent(contentId);
    }

    @RequestMapping("/addFile")
    @ResponseBody
    public String addFile(MultipartFile file, @RequestParam("fileMsg") String fileMsg) {
        return fileService.addFile(file,fileMsg);
    }
}
