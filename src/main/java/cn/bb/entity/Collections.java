package cn.bb.entity;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class Collections {
    private Integer colid;
    private String postTitle;
    private Integer pid;
    private Integer uid;
    private Timestamp collectTime;
}
