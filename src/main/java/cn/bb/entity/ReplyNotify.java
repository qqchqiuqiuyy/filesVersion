package cn.bb.entity;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class ReplyNotify {
    private Integer rnid;
    private String replyUserName;
    private Integer replyUserId;
    private String replyPostName;
    private Integer pid;
    private String userName;
    private Integer uid;
    private Timestamp replyTime;
}
