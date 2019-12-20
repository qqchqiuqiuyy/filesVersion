package cn.bb.controller;

import cn.bb.entity.File;
import cn.bb.service.FileService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/index")
public class IndexController {

    @Autowired
    FileService fileService;
    @RequestMapping("/toFile")
    public String toFile(@RequestParam(name = "fileName", defaultValue = "",required = false)  String fileName,
                         @RequestParam(name = "indexPage",required = false) Integer indexPage,
                         Model model) {
        if (null == indexPage || indexPage < 1) {
            indexPage = 1;
        }
        PageInfo<File> filePageInfo = fileService.GetFileList(fileName, indexPage);
        model.addAttribute("indexPage",indexPage);
        model.addAttribute("files",filePageInfo.getList());
        return "pages/fileList";
    }


}
