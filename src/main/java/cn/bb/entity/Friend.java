package cn.bb.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Friend {
    private Integer fid;
    private Integer friendId;
    private String friendName;
    private Integer uid;

}
