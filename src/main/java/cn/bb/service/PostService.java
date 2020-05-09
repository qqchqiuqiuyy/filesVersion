package cn.bb.service;

import cn.bb.common.Page;
import cn.bb.common.PostRepository;
import cn.bb.dao.PostMapper;
import cn.bb.dao.UserMapper;
import cn.bb.entity.*;
import cn.bb.vo.postAndContent;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    PostMapper postMapper;
    @Autowired
    UserMapper userMapper;

    @Autowired
    JSONObject jsonObject;

    @Autowired
    PostRepository postRepository;

    public PageInfo<Post> GetPostList(String postTitle, Integer pageIndex,Integer collegeId) {
        if (postTitle == null || postTitle.equals("null") || "".equals(postTitle)) {
            postTitle = "";
            PageHelper.startPage(pageIndex, Page.PAGE_SIZE);
            List<Post> posts = null;
            if (collegeId == -1) {
                posts = postMapper.GetPostList();
            } else {
                posts = postMapper.GetPostList2(collegeId);
            }
            return new PageInfo<>(posts);
        }
        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 添加基本分词查询
        queryBuilder.withQuery(QueryBuilders.termQuery("postTitle", postTitle));
        queryBuilder.withPageable(new PageRequest(pageIndex,Page.PAGE_SIZE,new Sort(Sort.Direction.DESC,"pid")));
        // 搜索，获取结果
        org.springframework.data.domain.Page<Post> search = postRepository.search(queryBuilder.build());
        List<Post> posts = new ArrayList<>();
        if (collegeId == -1) {
            for(Post p : search) {
                posts.add(postMapper.GetPost(p.getPid()));
            }
            if (posts.size() == 0) {
                return new PageInfo<>(postMapper.GetPost3(postTitle));
            }
            return new PageInfo<>(posts);
        }
        for(Post p : search) {
            posts.add(postMapper.GetPost2(p.getPid(),collegeId));
        }
        if (posts.size() == 0) {
            return new PageInfo<>(postMapper.GetPost4(postTitle,collegeId));
        }
        return new PageInfo<>(posts);
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
                GetId.setPostTitle(postTitle);
                postMapper.AddPost(postTitle,postContent,userId,userName,path,postFileName,collegeId,collegeName,GetId);
                postRepository.save(GetId);
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
        ReplyNotify notify = new ReplyNotify();
        postMapper.PostContent(replyUserId,replyUserName,content,postId,notify);
        postMapper.PostAddCommentNums(postId);
        if (content.charAt(0) == '@' && content.indexOf(" ") != -1) {
            String name = content.substring(1, content.indexOf(" "));
            User user = userMapper.GetUserByName(name);
            if (null != user) {
                postMapper.AddNotify(user.getName(),user.getUid(),postId,postName,replyUserId,replyUserName,notify.getConid());
            }
        }

    }

    public List<Post> GetPostRand(){
        List<Post> posts = postMapper.GetPostRand();
        return posts;
    }

    @Transactional(rollbackFor = Exception.class)
    public String CollectPost(String postTitle, Integer postUserId, Integer postId) {
        try {
            if (1 == postMapper.CollectPost(postTitle,postUserId,postId) ) {
                jsonObject.put("success",1);
            } else {
                jsonObject.put("success",0);
            }
        }catch (Exception ex) {
            ex.printStackTrace();
            jsonObject.put("success",3);
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
            postMapper.DelPost(postId);
            postRepository.delete(postId);
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
