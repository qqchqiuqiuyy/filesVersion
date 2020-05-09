package cn.bb.service;

import cn.bb.common.Util;
import cn.bb.dao.PostMapper;
import cn.bb.entity.File;
import cn.bb.entity.PostFile;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import static cn.bb.common.Util.PATH;

@Service
public class FileService {
    @Autowired
    JSONObject jsonObject;
    @Resource
    PostMapper postMapper;
    @Autowired
    UserService userService;


    public String downloadFile(Integer id, HttpServletRequest request, HttpServletResponse response) {
        //根据id查找文件存放路径
        PostFile postFile = postMapper.GetPostPath(id);
        String path = postFile.getPath();
        java.io.File realFile = new java.io.File(path);
        if (!realFile.exists()) {
            jsonObject.put("msg","无法找到下载文件");
            jsonObject.put("success",0);
            return jsonObject.toString();
        }
        String fileName = "";
        try {
            fileName = new String(postFile.getPostFileName().getBytes("UTF-8"),"ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("application/force-download");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type","text/html;charset=UTF-8");
        response.setHeader("Content-Transfer-Encoding","binary");
        response.setHeader("Content-Disposition","attachment;filename="+fileName);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            inputStream = new FileInputStream(realFile);
            outputStream = response.getOutputStream();
            bufferedInputStream = new BufferedInputStream(inputStream);
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            byte[] bytes = new byte[1024];
            while ((bufferedInputStream.read(bytes)) > 0) {
                bufferedOutputStream.write(bytes);
            }
            bufferedOutputStream.flush();
        }  catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedOutputStream.close();
                bufferedInputStream.close();
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        jsonObject.put("msg","下载成功");
        jsonObject.put("success",1);
        return jsonObject.toString();
    }

    public String addFile(MultipartFile file, String fileMsg)  {
        String fileSize = Util.GetPrintSize(file.getSize());
        String path = PATH + "\\" + file.getOriginalFilename();
        java.io.File target = new java.io.File(path);
        try {
            if(!target.exists()) {
                file.transferTo(target);
            }

        } catch (IOException e) {
            e.printStackTrace();

        }
        jsonObject.put("msg","上传成功");
        jsonObject.put("success",1);
        jsonObject.put("path",path);
        return jsonObject.toString();
    }



}
