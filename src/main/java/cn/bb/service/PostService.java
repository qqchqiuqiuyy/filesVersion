package cn.bb.service;

import cn.bb.common.Page;
import cn.bb.dao.PostMapper;
import cn.bb.entity.Post;
import cn.bb.entity.Content;
import cn.bb.vo.postAndContent;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class PostService {

    @Autowired
    PostMapper postMapper;

    @Autowired
    JSONObject jsonObject;

    public PageInfo<Post> GetPostList(String postTitle, Integer pageIndex) {
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }
        if (postTitle == null || postTitle.equals("null")) {
            postTitle = "";
        }
        PageHelper.startPage(pageIndex, Page.PAGE_SIZE);
        return new PageInfo<>(postMapper.GetPostList(postTitle));
    }

    @Transactional(rollbackFor = Exception.class)
    public String AddPost(String postTitle, String postContent, HttpServletRequest request,String path,String postFileName){
        String userName = (String)request.getSession().getAttribute("name");
        Integer userId = (Integer)request.getSession().getAttribute("userId");
        try {
            postMapper.AddPost(postTitle,postContent,userId,userName,path,postFileName);
            jsonObject.put("success",1);
            jsonObject.put("successUrl","/index/toFile");
        }catch (Exception ex) {
            jsonObject.put("success",0);
        }
        return jsonObject.toString();
    }


    public postAndContent postContentsList(Integer postId) {
        Post post = postMapper.GetPostMsg(postId);
        List<Content> contents = postMapper.GetPostContents(postId);
        return new postAndContent(post,contents);
    }
}
