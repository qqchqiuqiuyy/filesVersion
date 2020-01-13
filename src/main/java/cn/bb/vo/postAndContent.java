package cn.bb.vo;

import cn.bb.entity.Post;
import cn.bb.entity.Content;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
public class postAndContent {
    private Post post;
    private List<Content> content;
}
