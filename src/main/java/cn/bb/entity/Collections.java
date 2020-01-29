package cn.bb.entity;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class Collections {
    private Integer id;
    private String postTitle;
    private Integer postId;
    private Integer postUserId;
    private Timestamp collectTime;
}
