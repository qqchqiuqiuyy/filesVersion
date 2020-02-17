package cn.bb.entity;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class Content {
    private Integer conid;
    private Integer uid;
    private String userName;
    private Timestamp contentTime;
    private String contentMsg;
    private Integer pid;
}
