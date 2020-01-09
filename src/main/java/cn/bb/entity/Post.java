package cn.bb.entity;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class Post {
    private Integer postId;
    private Integer postFromId;
    private String postName;
    private String postFromName;
    private Timestamp postCreatTime;
}
