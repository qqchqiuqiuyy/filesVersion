package cn.bb.service;

import ch.qos.logback.core.util.FileUtil;
import cn.bb.common.Page;
import cn.bb.common.Util;
import cn.bb.dao.FileMapper;
import cn.bb.entity.File;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
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
    FileMapper fileMapper;
    @Autowired
    UserService userService;
    public PageInfo<File> GetFileList(String fileName,Integer pageIndex) {
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }
        if (fileName == null || fileName.equals("null")) {
            fileName = "";
        }
        PageHelper.startPage(pageIndex, Page.PAGE_SIZE);
        return new PageInfo<>(fileMapper.GetFileList(fileName));
    }

    public String downloadFile(Integer id, HttpServletRequest request, HttpServletResponse response) {
        File file = fileMapper.GetFileById(id);
        String path = file.getPath();
        java.io.File realFile = new java.io.File(path);
        if (!realFile.exists()) {
            jsonObject.put("msg","无法找到下载文件");
            jsonObject.put("success",0);
            return jsonObject.toString();
        }
        String fileName = "";
        try {
            fileName = new String(file.getFileName().getBytes("UTF-8"),"ISO-8859-1");
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
        File newF = new File();
        newF.setFileName(file.getOriginalFilename());
        newF.setPath(path);
        newF.setSize(fileSize);
        newF.setMsg(fileMsg);
        if (!target.exists()) {
            fileMapper.AddFile(newF);
        } else {
            target.delete();
            fileMapper.DeleteFileByName(newF.getFileName());
            fileMapper.AddFile(newF);
        }
        try {
            file.transferTo(target);
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonObject.put("msg","上传成功");
        jsonObject.put("success",1);
        return jsonObject.toString();
    }


    public String delFile(Integer fileId) {
        fileMapper.DeleteFileById(fileId);
        jsonObject.put("msg","删除成功");
        jsonObject.put("success",1);
        return jsonObject.toString();
    }
}
