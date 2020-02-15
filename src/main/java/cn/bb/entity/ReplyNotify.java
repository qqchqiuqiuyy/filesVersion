package cn.bb.entity;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class ReplyNotify {
    private Integer id;
    private String replyUserName;
    private Integer replyUserId;
    private String replyPostName;
    private Integer replyPostId;
    private String userName;
    private Integer userId;
    private Timestamp replyTime;
}
