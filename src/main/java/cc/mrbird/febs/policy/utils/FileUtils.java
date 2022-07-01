package cc.mrbird.febs.policy.utils;

import java.io.File;

public class FileUtils {
    public final static String UPLOAD_FILE_PATH = "D:\\upload\\";
    public static int check(String filename)
    {
        int n=0;
        File file =new File(UPLOAD_FILE_PATH);
        if  (!file .exists()  && !file .isDirectory())
        {
            file .mkdir();

        } else
        {
            File[] tempList = file.listFiles();
            for (int i = 0; i < tempList.length; i++)
            {
                if(tempList[i].isFile())
                {
                    String name=tempList[i].getName();
                    if(name.equals(filename))
                    {
                        n=1;
                    }
                }

            }
        }
        return n;
    }


}
