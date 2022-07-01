package cc.mrbird.febs.policy.utils;

import cc.mrbird.febs.policy.helper.JsonFormatTool;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;

public class CreateFileUtil {
    /**
     * 生成.json格式文件
     */
    public static boolean createJsonFile(String jsonString, String filePath, String fileName) {
        // 标记文件生成是否成功
        boolean flag = true;

        // 拼接文件完整路径
        String fullPath = filePath + File.separator + fileName + ".json";

        // 生成json格式文件
        try {
            // 保证创建一个新文件
            File file = new File(fullPath);
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
            file.createNewFile();

            if (jsonString.indexOf("'") != -1) {
                //将单引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
                jsonString = jsonString.replaceAll("'", "\\'");
            }
            if (jsonString.indexOf("\"") != -1) {
                //将双引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
                jsonString = jsonString.replaceAll("\"", "\\\"");
            }

            if (jsonString.indexOf("\r\n") != -1) {
                //将回车换行转换一下，因为JSON串中字符串不能出现显式的回车换行
                jsonString = jsonString.replaceAll("\r\n", "\\u000d\\u000a");
            }
            if (jsonString.indexOf("\n") != -1) {
                //将换行转换一下，因为JSON串中字符串不能出现显式的换行
                jsonString = jsonString.replaceAll("\n", "\\u000a");
            }

            // 格式化json字符串
            jsonString = JsonFormatTool.formatJson(jsonString);

            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(jsonString);
            write.flush();
            write.close();
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }

        // 返回是否成功的标记
        return flag;
    }

    /**
     * 读取json文件，转换成JSONObject对象返回
     *
     * @param fileName json文件名
     * @return JSONObject对象
     */
    public static JSONObject readJsonFile(String fileName) {
        try {
            File file = new File(fileName);//定义一个file对象，用来初始化FileReader
            FileReader reader = new FileReader(file);
            //BufferedReader bReader = new BufferedReader(reader);
            BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"UTF-8"));
            StringBuilder sb = new StringBuilder();//定义一个字符串缓存，将字符串存放缓存中
            String s = "";
            while ((s = bReader.readLine()) != null) {//逐行读取文件内容，不读取换行符和末尾的空格
                sb.append(s).append("\n");//将读取的字符串添加换行符后累加存放在缓存中
            }
            bReader.close();
            String str = sb.toString();
            return JSONObject.parseObject(str);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static JSONArray readJSONArrayFile(String fileName) {
        try {
            File file = new File(fileName);//定义一个file对象，用来初始化FileReader
            FileReader reader = new FileReader(file);
            //BufferedReader bReader = new BufferedReader(reader);
            BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"UTF-8"));
            StringBuilder sb = new StringBuilder();//定义一个字符串缓存，将字符串存放缓存中
            String s = "";
            while ((s = bReader.readLine()) != null) {//逐行读取文件内容，不读取换行符和末尾的空格
                sb.append(s).append("\n");//将读取的字符串添加换行符后累加存放在缓存中
            }
            bReader.close();
            String str = sb.toString();
            return JSONArray.parseArray(str);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

