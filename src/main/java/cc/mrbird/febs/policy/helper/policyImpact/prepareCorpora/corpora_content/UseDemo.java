package cc.mrbird.febs.policy.helper.policyImpact.prepareCorpora.corpora_content;
/**
 * 构建语料库
 * 将CC.java爬取到的html文件中的文本解析出来
 */

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

/**
 * TextExtractor功能测试类.
 */

public class UseDemo {


    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }

    public static void main(String[] args) throws IOException {

        /*
         * 测试网站：
         * 百度博客空间             http://hi.baidu.com/liyanhong/
         * 新浪娱乐音乐新闻与信息	http://ent.sina.com.cn/music/roll.html
         * 腾讯娱乐新闻与信息		http://ent.qq.com/m_news/mnews.htm
         * 搜狐音乐新闻				http://music.sohu.com/news.shtml
         * 哈尔滨工业大学校内信息网 http://today.hit.edu.cn/
         * 哈尔滨工业大学校内新闻网 http://news.hit.edu.cn/
         */


        /* 注意：本处只为展示抽取效果，不处理网页编码问题，getHTML只能接收GBK编码的网页，否则会出现乱码 */
        //String content = getHTML("http://www.yuhuagu.com/jiating/2016/0622/13125.html");


        ArrayList<File> files = getListFiles("D:\\policy\\科技-语料-国家\\");
        files.addAll(getListFiles("D:\\policy\\科技-语料-河北科技厅\\"));
        files.addAll(getListFiles("D:\\policy\\科技-语料-石家庄\\"));
        System.out.println(files);
        UseDemo t = new UseDemo();
        TextExtract tt = new TextExtract();

        for (int i = 0; i < files.size() - 1; i++) {

            String s2 = files.get(i).toString();//"D:\\绱��\\httpwwwchinavegancomdaleiDWJFDWYQ.html";
            //  System.out.println(s2);

            //t.solve(s2,i);
            String temp = readTxtFile(s2);
            // System.out.println(temp);

            //提取标题

            // String sbyte=getEncoding(temp);
            //  String a = new String(temp.getBytes(sbyte),"utf8");
            //  System.out.println(TextExtract.parse(temp)+"\n\n\n\n\n\n");
            System.out.println(TextExtract.parse(temp) + "\n\n\n\n\n\n");
            //  System.out.println("##############################");


            //写入文件
            saveFile_zhuijia("D:\\policy\\科技-正文\\" + i + ".txt", TextExtract.parse(temp));
        }



        /*
         * 当待抽取的网页正文中遇到成块的新闻标题未剔除时，只要增大此阈值即可。
         * 相反，当需要抽取的正文内容长度较短，比如只有一句话的新闻，则减小此阈值即可。
         * 阈值增大，准确率提升，召回率下降；值变小，噪声会大，但可以保证抽到只有一句话的正文
         */
        //TextExtract.setThreshold(76); // 默认值86

        System.out.println("got html");
        //System.out.println(TextExtract.parse(content));
    }


    public static void saveFile_zhuijia(String fileName, String content) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println(file.createNewFile());
            file.createNewFile();
        }


        FileOutputStream fos = new FileOutputStream(file, true);
        OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");//(fos,"gb2312");
        BufferedWriter bw = new BufferedWriter(out);
        bw.write(content);
        bw.write("\r\n");


        bw.flush();
    }


    public static ArrayList<File> getListFiles(Object obj) {
        File directory = null;
        if (obj instanceof File) {
            directory = (File) obj;
        } else {
            directory = new File(obj.toString());
        }
        ArrayList<File> files = new ArrayList<File>();
        if (directory.isFile()) {
            files.add(directory);
            return files;
        } else if (directory.isDirectory()) {
            File[] fileArr = directory.listFiles();
            for (int i = 0; i < fileArr.length; i++) {
                File fileOne = fileArr[i];
                files.addAll(getListFiles(fileOne));
            }
        }
        return files;
    }


    public static String readTxtFile(String filePath) {

        try {
            StringBuilder build = new StringBuilder();
            String encoding = "UTF8";//"GBK";
            File file = new File(filePath);

            if (file.isFile() && file.exists()) { //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    // System.out.println(lineTxt);
                    lineTxt = lineTxt + "\n";
                    build.append(lineTxt);
                }

                read.close();

                return build.toString();


            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }

        return null;
    }

    public static String getHTML(String strURL) throws IOException {
        URL url = new URL(strURL);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        String s = "";
        StringBuilder sb = new StringBuilder("");
        while ((s = br.readLine()) != null) {
            sb.append(s + "\n");
        }
        return sb.toString();
    }

    public void solve(String filepath, int index) throws IOException {
        FileReader fr = new FileReader(filepath);
        StringBuilder build = new StringBuilder();
        BufferedReader br = new BufferedReader(fr);
        while (br.readLine() != null) {
            String ss = br.readLine();
            System.out.println(ss);
            build.append(ss + "\n");
            //System.out.println("浣�ソ��������);
        }
        br.close();
        String sss = build.toString();
        System.out.println(sss);

        TextExtract.parse(sss);
    }
}
