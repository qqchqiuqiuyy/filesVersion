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
    private Integer pid;

    private Integer uid;
    @Field(type = FieldType.String,analyzer = "ik_max_word")
    private String postTitle;
    @Field(type = FieldType.Auto)
    private String postUserName;
    @Field(type = FieldType.Auto)
    private Timestamp postCreatTime;
    @Field(type = FieldType.Auto)
    private String path;
    @Field(type = FieldType.Auto)
    private String postFileName;
    @Field(type = FieldType.Auto)
    private String postContent;
    @Field(type = FieldType.Auto)
    private Integer commentNums;
    @Field(type = FieldType.Auto)
    private String collegeName;
    @Field(type = FieldType.Auto)
    private Integer collegeId;
}
