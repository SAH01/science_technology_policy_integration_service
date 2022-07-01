package cc.mrbird.febs.common.controller;

import com.alibaba.fastjson.JSONException;
import com.baidu.ueditor.ActionEnter;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.net.URL;
@RestController
@RequestMapping("/api/ueditor")
public class CommonController {
    /**
     * 判断当前系统是否是Windows系统
     * @return true：Windows系统，false：Linux系统
     */
    private boolean isWindowsSystem(){
        String property = System.getProperty("os.name").toLowerCase();
        return property.contains("windows");
    }

    /**
     * 获取Ueditor的配置文件
     * @return
     */
    @RequestMapping("/config")
    public void getConfigInfo(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
        System.out.println("读取ueditor配置文件！");
        response.setContentType("application/json");
        String rootPath = "";
        // 判断当前系统是否是Windows系统
        if(isWindowsSystem()){
            URL Path = ClassUtils.getDefaultClassLoader().getResource("");
            String afterPath = URLDecoder.decode(Path.getPath(), "UTF-8");
            rootPath = afterPath+ "static/UEditor/jsp";
        } else {
            // 将config.json文件放在jar包同级目录下
            rootPath = "/usr/local/zgxsoft/yunapp-backend/service";
        }
        System.out.println("rootPath：{}"+ rootPath);
        try {
            String exec = new ActionEnter(request, rootPath, "/config.json").exec();
            PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Ueditor上传文件
     * 这里以上传图片为例，图片上传后，imgPath将存储图片的保存路径，返回到编辑器中做展示
     * @param upfile
     * @return
     */

    @RequestMapping("uploadimage")
    @ResponseBody
    public Map<String,String> uploadImage(@RequestParam("upfile") MultipartFile upfile, HttpServletRequest request) throws IOException {
        System.out.println("上传图片！");
        //文件原名称
        String fileName = upfile.getOriginalFilename();

        // 保存文件的新名字

        String timeFileName = DateHelper.getDateToString(new Date());
        String nowName = timeFileName+"_"+UUID.randomUUID()+fileName.substring(upfile.getOriginalFilename().lastIndexOf("."));
        String uploadPath = "";
        if(!upfile.isEmpty()){
            String path = "D:/science-2.0/";
            File f = new File(path);
            if(!f.exists()){
                // 第一次上传文件新建文件夹
                f.mkdirs();
            }
            uploadPath = path+nowName;
            //按照路径新建文件
            File newFile = new File(uploadPath);
            if(!newFile.exists()){
                newFile.createNewFile();
            }
            //复制
            FileCopyUtils.copy(upfile.getBytes(), newFile);
        }
        //返回结果信息(UEditor官方要求这个json格式)
        Map<String,String> map = new HashMap<String,String >();
        //是否上传成功
        map.put("state", "SUCCESS");
        //现在文件名称
        map.put("title", nowName);
        //文件原名称
        map.put("original", fileName);
        //文件类型 .+后缀名
        map.put("type", fileName.substring(upfile.getOriginalFilename().lastIndexOf(".")));
        //文件路径
        // map.put("url", uploadPath);    // 浏览器不能直接访问项目外目录的图片等文件，需要做虚拟路径映射
        map.put("url", "/PathImage/"+nowName);  // 这个路径的 /PathImage/ 是在配置类里指定的映射到本地的绝对路径
        //文件大小（字节数）
        map.put("size", upfile.getSize()+"");
        return map;
    }

    @RequestMapping("uploadfile")
    @ResponseBody
    public Map<String,String> uploadfile(@RequestParam("upfile") MultipartFile upfile, HttpServletRequest request) throws IOException {
        System.out.println("上传文件！");
        //文件原名称
        String fileName = upfile.getOriginalFilename();

        // 保存文件的新名字

        String timeFileName = DateHelper.getDateToString(new Date());
        String nowName = timeFileName+"_"+UUID.randomUUID()+fileName.substring(upfile.getOriginalFilename().lastIndexOf("."));
        //System.out.println("name---:"+nowName);
        String uploadPath = "";
        if(!upfile.isEmpty()){
            String path = "D:/science-2.0/";
            File f = new File(path);
            if(!f.exists()){
                // 第一次上传文件新建文件夹
                f.mkdirs();
            }
            uploadPath = path+nowName;
            //按照路径新建文件
            File newFile = new File(uploadPath);
            if(!newFile.exists()){
                newFile.createNewFile();
            }
            //复制
            FileCopyUtils.copy(upfile.getBytes(), newFile);
        }
        //返回结果信息(UEditor官方要求这个json格式)
        Map<String,String> map = new HashMap<String,String >();
        //是否上传成功
        map.put("state", "SUCCESS");
        //现在文件名称
        map.put("title", nowName);
        //文件原名称
        map.put("original", fileName);
        //文件类型 .+后缀名
        map.put("type", fileName.substring(upfile.getOriginalFilename().lastIndexOf(".")));
        //文件路径
        // map.put("url", uploadPath);    // 浏览器不能直接访问项目外目录的图片等文件，需要做虚拟路径映射
        map.put("url", "/PathFile/"+nowName);  // 这个路径的 /PathImage/ 是在配置类里指定的映射到本地的绝对路径
        //文件大小（字节数）
        map.put("size", upfile.getSize()+"");
        return map;
    }



}