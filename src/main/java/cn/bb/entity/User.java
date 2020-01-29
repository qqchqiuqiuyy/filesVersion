package cn.bb.entity;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class User {
    private Integer id;
    private String password;
    private String account;
    private String name;
    private String introduce;
}
