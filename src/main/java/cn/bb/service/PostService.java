package cn.bb.service;

import cn.bb.common.Page;
import cn.bb.dao.PostMapper;
import cn.bb.entity.File;
import cn.bb.entity.Post;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service
public class PostService {

    @Autowired
    PostMapper postMapper;

    public PageInfo<Post> GetPostList(String postName, Integer pageIndex) {
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }
        if (postName == null || postName.equals("null")) {
            postName = "";
        }
        PageHelper.startPage(pageIndex, Page.PAGE_SIZE);
        return new PageInfo<>(postMapper.GetPostList(postName));
    }


}
