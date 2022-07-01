package cc.mrbird.febs.policy.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppFileUtils {

    /**
     * 得到文件上传的路径
     */
    public static String PATH;

    static {
        InputStream stream = AppFileUtils.class.getClassLoader().getResourceAsStream("file.properties");
        Properties properties = new Properties();
        try {
            properties.load(stream);
            PATH = properties.getProperty("path");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
