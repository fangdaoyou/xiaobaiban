package com.whiteboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class UploadController {

    @GetMapping(value = "/upload")
    public String upload(){
        return "upload";
    }

    @PostMapping(value = "/upload")
    @ResponseBody
    public String upload(@RequestParam("avatar") MultipartFile file){
        //重命名文件
        if (file != null && file.getOriginalFilename() != null){
            //获取扩展名
            String originalFilename = file.getOriginalFilename();
            String extend = originalFilename.substring(originalFilename.lastIndexOf("."));
            //重新生成唯一文件名
            String newName = UUID.randomUUID().toString();
            File file1 = new File("D:" + File.separator + "桌面" + File.separator  +
                    "小白板" + File.separator + "upload", newName + extend);
            try {
                file.transferTo(file1);
                return file1.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return null;
    }
}
