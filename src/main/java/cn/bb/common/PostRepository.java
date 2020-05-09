package cn.bb.common;

import cn.bb.entity.Post;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface PostRepository  extends ElasticsearchRepository<Post,Integer> {
    List<Post> findByPostTitle(String postTile);
}
