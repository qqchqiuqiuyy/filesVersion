package cn.bb.entity;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class File {
    private Integer id;
    private String fileName;
    private Timestamp uploadTime;
    private String path;
    private String size;
    private String msg;
}
