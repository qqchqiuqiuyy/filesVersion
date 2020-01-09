package cn.bb.controller;

import cn.bb.service.FileService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller()
@RequestMapping("/file")
public class FileController {
    @Autowired
    FileService fileService;

    @RequestMapping("/downloadFile/{id}")
    @ResponseBody
    public String downloadFile(@PathVariable("id") Integer id, HttpServletRequest request, HttpServletResponse response){
        return fileService.downloadFile(id,request,response);
    }

    @RequestMapping("/addFile")
    @ResponseBody
    public String addFile(MultipartFile file, @RequestParam("fileMsg") String fileMsg) {
        return fileService.addFile(file,fileMsg);

    }

    @RequestMapping("/toUpload")
    public String toUpload(){
        return "pages/uploadFile";
    }

    @RequestMapping("/delFile/{fileId}")
    @ResponseBody
    public String delFile(@PathVariable("fileId") Integer fileId) {
        return fileService.delFile(fileId);
    }


}
