package cc.mrbird.febs.policy.helper.policyImpact.util;


import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TXT读取类
 * @author Angela
 */
public class ReadTXT {

    /**
     * 获取一篇文本的编码格式，注：所有文本的默认编码是“utf-8”
     * @filePath 文本文件路径
     * @return 文本文件的编码格式
     */
    public static String getCharset(String filePath){
        String charset = null;
        try{
            BufferedInputStream bin = new BufferedInputStream(
                    new FileInputStream(filePath));
            int p = (bin.read() << 8) + bin.read();
            switch (p) {
                case 0xefbb:
                    charset = "UTF-8";
                    break;
                case 0xfffe:
                    charset = "Unicode";
                    break;
                case 0xfeff:
                    charset = "UTF-16BE";
                    break;
                default:
                    charset = "GBK";
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadTXT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadTXT.class.getName()).log(Level.SEVERE, null, ex);
        }
        return charset;
    }

    /**
     * 读取一篇文本的全部内容
     * @param filePath 还未分词的文本
     * @return 无换行的文本内容，读取的内容用于后面的分词
     */
    public static String read(String filePath){
        StringBuilder sb=new StringBuilder();
        String charset=getCharset(filePath);
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(filePath),charset));
            String s;
            while((s=br.readLine())!=null){
                sb.append(s);
            }
            br.close();
        }catch (IOException ex) {
            Logger.getLogger(ReadTXT.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }

    /**
     * 一行一个词地读取一篇文本，得到特征集
     * @param filePath
     * @return 读取分词后的文本，得到出现在文本中的所有不重复的特征Set
     */
    public static Set<String> toSet(String filePath){
        Set<String> set=new HashSet<String>();
        //String charset=getCharset(filePath);
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(filePath),"utf-8"));
            String s;
            while((s=br.readLine())!=null){
                set.add(s);
            }
            br.close();
        }catch (IOException ex) {
            Logger.getLogger(ReadTXT.class.getName()).log(Level.SEVERE, null, ex);
        }
        return set;
    }

    /**
     * 一行一个词地读取一篇文本，得到特征列表，有重复的
     * @param filePath
     * @return 读取分词后的文本，得到出现在文本中的所有特征（有重复的）List
     */
    public static List<String> toList(String filePath){
        List<String> list=new ArrayList<String>();
        //String charset=getCharset(filePath);
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(filePath),"utf-8"));
            String s;
            while((s=br.readLine())!=null){
                list.add(s);
            }
            br.close();
        }catch (IOException ex) {
            Logger.getLogger(ReadTXT.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    /**
     * 读取文件内容，返回一个特征-权重的Map
     * @param filePath
     * @return
     */
    public static Map<String,Integer> toIntMap(String filePath){
        Map<String,Integer> map=new HashMap<String,Integer>();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(filePath),"utf-8"));
            String str;
            while((str=br.readLine())!=null){//特征
                String[] s=str.split(",");
                String key=s[0];//特征
                int value=Integer.parseInt(s[1]);//特征值
                map.put(key, value);
            }
            br.close();
        }catch (IOException ex) {
            Logger.getLogger(ReadTXT.class.getName()).log(Level.SEVERE, null, ex);
        }
        return map;
    }

    /**
     * 读取文件内容，返回一个特征-权重的Map
     * @param filePath
     * @return
     */
    public static Map<String,Double> toDoubleMap(String filePath){
        Map<String,Double> map=new HashMap<String,Double>();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(filePath),"utf-8"));
            String str;
            while((str=br.readLine())!=null){//特征
                String[] s=str.split(",");
                String key=s[0];//特征
                double value=Double.parseDouble(s[1]);//特征值
                map.put(key, value);
            }
            br.close();
        }catch (IOException ex) {
            Logger.getLogger(ReadTXT.class.getName()).log(Level.SEVERE, null, ex);
        }
        return map;
    }

    /**
     * 获取文本集的TFIDF集
     * @param filePath
     * @return
     */
    public static List<Text> readText(String filePath){
        List<Text> textList=new ArrayList<Text>();
        File path=new File(filePath);
        File[] files=path.listFiles();//类别
        int labelID=0;
        for(File file: files){
            File[] texts=file.listFiles();//文本
            for(File text: texts){
                String textPath=text.getAbsolutePath();
                Text txt=new Text();
                txt.setPath(textPath);//文本路径
                txt.setOriginLabelID(labelID);//文本类别
                txt.setWords(ReadTXT.toDoubleMap(textPath));//文本词向量
                textList.add(txt);
            }
            labelID++;
        }
        return textList;
    }

}