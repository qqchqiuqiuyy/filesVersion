package cn.bb.entity;

import lombok.Data;
import lombok.Getter;

import java.sql.Timestamp;

@Data
@Getter
public class User {
    private Integer id;
    private String password;
    private String account;
    private String name;
    private String introduce;
    private Timestamp creatTime;
    private String sex;
    private Integer collegeValue;
    private String collegeName;
}
