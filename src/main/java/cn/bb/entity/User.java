package cn.bb.entity;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class User {
    public Integer id;
    public String password;
    public String account;
    public String name;
}
