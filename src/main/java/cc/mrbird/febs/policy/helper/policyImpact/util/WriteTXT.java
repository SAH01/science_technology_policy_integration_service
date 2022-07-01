package cc.mrbird.febs.policy.helper.policyImpact.util;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 数据写入TXT类
 * @author Angela
 */
public class WriteTXT {

    /**
     * 将字符串写入文本
     * @param str 分词字符串
     * @param tarPath 保存路径
     */
    public static void write(String str,String tarPath){
        try{
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(tarPath)));
            bw.write(str);
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(WriteTXT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 传入一篇文本分词后的特征表List，将List的内容一行一个特征地写入tarPath文件中(有重复)
     * @param list 一篇文本分词后的结果：特征列表List
     * @param tarPath 保存路径
     */
    public static <K> void writeList(List<K> list,String tarPath){
        try{
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(tarPath)));
            for(K s: list){
                bw.write(s.toString());
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(WriteTXT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 传入一篇文本分词后的特征集Set，将Set的内容一行一个特征地写入tarPath文件中（无重复的）
     * @param set 特征集Set
     * @param tarPath 保存路径
     */
    public static <K> void writeSet(Set<K> set,String tarPath){
        try{
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(tarPath)));
            for(K s: set){
                bw.write(s.toString());
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(WriteTXT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 传入一篇文本分词后的特征集Map，将Map的内容一行一个地写入tarPath文件中（无重复的）
     * @param map 特征集Map
     * @param tarPath 保存路径，将特征-特征值Map内容保存在tarPath文件中
     */
    public static <K, V> void writeMap(Map<K, V> map,String tarPath){
        try{
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(tarPath)));
            for(Map.Entry<K,V> me: map.entrySet()){
                //用逗号作为分隔符
                bw.write(me.getKey()+","+me.getValue());
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(WriteTXT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 得到所有数据集中所有不重复特征的文档频，一行一行地将特征-DF结果保存到tarPath中
     * @param filePath 数据集路径，包含有所有文本的目录路径
     * @param tarPath DF结果的保存路径
     * @return 数据集的文本总数
     */
    public static int saveDF(String filePath,String tarPath){
        int sum=0;
        Map<String,Integer> df=new HashMap<String,Integer>();
        File dataSet=new File(filePath);
        File[] classes=dataSet.listFiles();
        for(File c: classes){//类别
            System.out.println(c);

            File[] files=c.listFiles();
            sum+=files.length;
            for(File file: files){//文本
                Map<String,Integer> tf= ReadTXT.toIntMap(file.getAbsolutePath());
                for(Map.Entry<String, Integer> me: tf.entrySet()){
                    String f=me.getKey();
                    if(df.containsKey(f)){
                        df.put(f, df.get(f)+1);
                    }else{
                        df.put(f, 1);
                    }
                }
            }
        }
        //保存特征及其DF值
        WriteTXT.writeMap(MapUtil.desc(df),tarPath);
        return sum;
    }
    /**
     * 得到所有数据集中所有不重复特征的文档频，一行一行地将特征-DF结果保存到tarPath中
     * @param filePath 数据集路径，包含有所有文本的目录路径
     * @param tarPath DF结果的保存路径
     * @return 数据集的文本总数
     */
    public static int saveDF_My(String filePath,String tarPath){
        int sum=0;
        Map<String,Integer> df=new HashMap<String,Integer>();
        File dataSet=new File(filePath);
        File[] files=dataSet.listFiles();
        sum=files.length;
        for(File file: files){//文本
            Map<String,Integer> tf= ReadTXT.toIntMap(file.getAbsolutePath());
            for(Map.Entry<String, Integer> me: tf.entrySet()){
                String f=me.getKey();
                if(df.containsKey(f)){
                    df.put(f, df.get(f)+1);
                }else{
                    df.put(f, 1);
                }
            }
        }
        //保存特征及其DF值
        WriteTXT.writeMap(MapUtil.desc(df),tarPath);
        return sum;
    }
    /**
     * 读取DF结果文件，计算每个特征的IDF值，保存特征-IDF值到tarPath文件中
     * @param filePath DF结果的文件路径
     * @param tarPath IDF结果的保存路径
     * @param n 总的文本数
     */
    public static void saveIDF(String filePath,String tarPath,int n){
        Map<String,Integer> df= ReadTXT.toIntMap(filePath);
        Map<String,Double> idf=new HashMap<String,Double>();
        for(Map.Entry<String, Integer> me: df.entrySet()){
            idf.put(me.getKey(), Math.log(n*1.0/me.getValue()));
        }
        //保存特征及其IDF值
        WriteTXT.writeMap(MapUtil.desc(idf),tarPath);
    }

    /**
     * 主函数，保存分词后的DF、IDF结果
     * @param args
     */
    public static void main(String args[]){
        //第一个参数为TF集路径，第二个参数为DF的保存路径，n为文本集文本总数
        int n=saveDF_My("data/trainTF","data/trainDF.txt");
        //System.out.println(n);
        //第一个参数为DF文本，第二个参数为IDF的保存路径，n为文本集文本总数
        saveIDF("data/trainDF.txt","data/trainIDF.txt",n);
    }

}