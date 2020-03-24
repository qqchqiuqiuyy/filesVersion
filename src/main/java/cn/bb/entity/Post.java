package cn.bb.entity;

import lombok.Data;
import lombok.ToString;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


import java.sql.Timestamp;

@Data
@ToString
@Document(indexName = "post",type = "docs",shards = 1,replicas = 0)
public class Post {
    @Id
    private Integer pid;  //主键,统一存储到elasticsearch中,之后能快速得到主键

    @Field(type = FieldType.String,analyzer = "ik_max_word")
    private String postTitle;  //搜索是按贴标题搜索,所以对此字段进行标志

    private Integer uid;


    private String postUserName;


    private Timestamp postCreatTime;

    private String path;

    private String postFileName;

    private String postContent;

    private Integer commentNums;

    private String collegeName;


    private Integer collegeId;
}
