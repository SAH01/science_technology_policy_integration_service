package cc.mrbird.febs.policy.helper.policyImpact.helper;

import cc.mrbird.febs.policy.helper.policyImpact.util.ReadTXT;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractMenu {
    public static void main(String args[]){
        File dataSet=new File("data/trainTF");
        File[] files=dataSet.listFiles();
        for(File file: files) {//文本
            BufferedReader br = null;
            List<String> textLines = new ArrayList<>();
            try{
                br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file.getAbsolutePath()),"utf-8"));
                System.out.println(file.getName());
                String str;
                while((str=br.readLine())!=null){//特征
                    textLines.add(str);
                }
                br.close();
            }catch (IOException ex) {
                Logger.getLogger(ReadTXT.class.getName()).log(Level.SEVERE, null, ex);
            }finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            Pattern pattern1 = Pattern.compile("^[\u4e00-\u9fa5]{0,3}、");
            Pattern pattern2 = Pattern.compile("^（[\u4e00-\u9fa5]{0,3}）");
            Pattern pattern3 = Pattern.compile("^\\d+\\.");
            Pattern pattern4 = Pattern.compile("^第[\u4e00-\u9fa5]{0,5}条");
            for (String lineText:textLines) {
                Matcher matcher1 = pattern1.matcher(lineText);
                Matcher matcher2 = pattern2.matcher(lineText);
                Matcher matcher3 = pattern3.matcher(lineText);
                Matcher matcher4 = pattern4.matcher(lineText);
                if(matcher1.find()||matcher2.find()||matcher3.find()||matcher4.find()){
                    System.out.println(lineText.split("。")[0]);
                }
            }
        }

    }
}
