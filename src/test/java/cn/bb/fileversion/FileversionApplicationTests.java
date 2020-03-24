package cn.bb.fileversion;

import cn.bb.FileversionApplication;
import cn.bb.common.PostRepository;
import cn.bb.entity.Post;


import com.github.pagehelper.Page;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.suggest.Suggest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FileversionApplication.class)
public class FileversionApplicationTests {


	@Autowired
	PostRepository postRepository;
	@Test
	public void contextLoads() {
		//elasticsearchTemplate.createIndex(Post.class);
		/*Post p = new Post();
		p.setPostTitle("C语言程序设计");
		p.setPid(1);
		postRepository.save(p);
		Post p2 = new Post();
		p2.setPid(2);
		p2.setPostTitle("大学英语");
		postRepository.save(p2);
		Post p3 = new Post();
		p3.setPid(3);
		p3.setPostTitle("星火大学");
		postRepository.save(p3);*/
/*
		List<Post> dx = postRepository.findByPostTitle("大学");
		System.out.println(dx.toString());
		// 构建查询条件
		/*NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
		// 添加基本分词查询
		queryBuilder.withQuery(QueryBuilders.termQuery("postTitle", "大学"));
		// 分页：
		int page = 1;
		int size = 2;
		queryBuilder.withPageable(new PageRequest(page,size));
		// 搜索，获取结果
		org.springframework.data.domain.Page<Post> search = postRepository.search(queryBuilder.build());
		// 总条数
		long total = search.getTotalElements();
		System.out.println("总条数 = " + total);
		// 总页数
		System.out.println("总页数 = " + search.getTotalPages());
		// 当前页
		System.out.println("当前页：" + search.getNumber());
		// 每页大小
		System.out.println("每页大小：" + search.getSize());

		for (Post item : search) {
			System.out.println(item);
		}*/
		// 构建查询条件
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
		// 添加基本分词查询
		queryBuilder.withQuery(QueryBuilders.termQuery("postTitle", "大学"));
		queryBuilder.withPageable(new PageRequest(0, 2,new Sort(Sort.Direction.DESC,"pid")));
		// 搜索，获取结果
		org.springframework.data.domain.Page<Post> search = postRepository.search(queryBuilder.build());
		for(Post pp : search) {
			System.out.println(pp);
		}
	}

	@Test
	public void clear() {
		postRepository.deleteAll();
	}
}
