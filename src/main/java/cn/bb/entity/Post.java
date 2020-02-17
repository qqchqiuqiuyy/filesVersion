package cn.bb.entity;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class Post {
    private Integer pid;
    private Integer uid;
    private String postTitle;
    private String postUserName;
    private Timestamp postCreatTime;
    private String path;
    private String postFileName;
    private String postContent;
    private Integer commentNums;
    private String collegeName;
    private Integer collegeId;
}
