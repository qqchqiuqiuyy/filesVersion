package cn.bb.service;

import cn.bb.common.Page;
import cn.bb.dao.PostMapper;
import cn.bb.dao.UserMapper;
import cn.bb.entity.*;
import cn.bb.vo.postAndContent;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

@Service
public class PostService {

    @Autowired
    PostMapper postMapper;
    @Autowired
    UserMapper userMapper;

    @Autowired
    JSONObject jsonObject;

    public PageInfo<Post> GetPostList(String postTitle, Integer pageIndex,Integer subjectValue) {
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }
        if (postTitle == null || postTitle.equals("null")) {
            postTitle = "";
        }
        PageHelper.startPage(pageIndex, Page.PAGE_SIZE);
        if (subjectValue == -1) {
            return new PageInfo<>(postMapper.GetPostList2(postTitle));
        }

        return new PageInfo<>(postMapper.GetPostList(postTitle,subjectValue));
    }

    @Transactional(rollbackFor = Exception.class)
    public String AddPost(String postTitle, String postContent, HttpServletRequest request,String path,String postFileName,Integer collegeId,String collegeName){
        String userName = (String)request.getSession().getAttribute("name");
        Integer userId = (Integer)request.getSession().getAttribute("userId");
        if (postTitle == "" || postContent == "" || collegeId == null ) {
            jsonObject.put("success",0);
        } else {
            try {
                Post GetId = new Post();
                postMapper.AddPost(postTitle,postContent,userId,userName,path,postFileName,collegeId,collegeName,GetId);
                jsonObject.put("success",1);
                jsonObject.put("successUrl","/post/toDetail/" + GetId.getPid());
            }catch (Exception ex) {
                jsonObject.put("success",0);
                ex.printStackTrace();
            }
        }

        return jsonObject.toString();
    }


    public postAndContent postContentsList(Integer postId) {
        Post post = postMapper.GetPostMsg(postId);
        List<Content> contents = postMapper.GetPostContents(postId);
        return new postAndContent(post,contents);
    }

    @Transactional(rollbackFor = Exception.class)
    public void PostComment(Integer postId,String postName,String replyUserName,Integer replyUserId, String content) {
        if (content.charAt(0) == '@' && content.indexOf(" ") != -1) {
            String name = content.substring(1, content.indexOf(" "));
            User user = userMapper.GetUserByName(name);
            if (null != user) {
                postMapper.AddNotify(user.getName(),user.getUid(),postId,postName,replyUserId,replyUserName);
            }
        }
        postMapper.PostContent(replyUserId,replyUserName,content,postId);
        postMapper.PostAddCommentNums(postId);
    }

    public List<Post> GetPostRand(){
        List<Post> posts = postMapper.GetPostRand();
        return posts;
    }

    @Transactional(rollbackFor = Exception.class)
    public String CollectPost(String postTitle, Integer postUserId, Integer postId) {
        if (1 == postMapper.CollectPost(postTitle,postUserId,postId) ) {
            jsonObject.put("success",1);
        } else {
            jsonObject.put("success",0);
        }
        return jsonObject.toString();
    }

    public List<College> GetAllColleges(){
        return postMapper.GetAllColleges();
    }

    @Transactional(rollbackFor = Exception.class)
    public String DelPost(Integer postId) {
        try {
            PostFile postFile = postMapper.GetPostPath(postId);
            File file = new File(postFile.getPath());
            if (file.exists()) {
                file.delete();
            }
           /* postMapper.DelCollections(postId);
            postMapper.DelContents(postId);*/
            postMapper.DelPost(postId);
          /*  postMapper.DelReplyNotify(postId);*/
            jsonObject.put("success","1");

        }catch (Exception ex) {
            jsonObject.put("success","0");
            System.out.println(ex.getMessage());
        }
        return jsonObject.toString();
    }

    @Transactional(rollbackFor = Exception.class)
    public String DelContent(Integer contentId) {
        try {
            postMapper.DelContentByContentId(contentId);
            jsonObject.put("success","1");
        } catch (Exception ex) {
            jsonObject.put("success","0");
        }
        return jsonObject.toString();
    }
}
