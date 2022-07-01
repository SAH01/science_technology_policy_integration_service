package cc.mrbird.febs.policy.helper.policyImpact.prepareCorpora.corpora_spider;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;


/**
 * 构建语料库
 * 特定网站的链接发现并抓取html
 */
public class CC {
    private static int count = 0;
    private static LinkedList<String> visitedUrlQueue = new LinkedList<String>();
    private static LinkedHashSet<String> urlQueue = new LinkedHashSet<String>();
    public static void main(String[] args) {
        CC s = new CC();
//        String website = "http://mobile.zol.com.cn/";//"http://it.21cn.com/mobile/";//"http://www.meishichina.com/";//"http://www.yuhuagu.com/";//"http://www.chinavegan.com/";
        String website = "http://www.most.gov.cn";//"http://it.21cn.com/mobile/";//"http://www.meishichina.com/";//"http://www.yuhuagu.com/";//"http://www.chinavegan.com/";
        urlQueue.add(website);
        ArrayList<String> list_temp = new ArrayList<String>();

        try {
            list_temp = s.fetch(website);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        if (list_temp != null)
            System.out.println("list_temp size:" + list_temp.size());
        int index = 0;
        if (list_temp != null) {
            for (int i = 0; i < list_temp.size() - 1; i++) {
                urlQueue.add(list_temp.get(i));
            }
        }
        System.out.println("old urlqueue:" + urlQueue.size());

        while (urlQueue.size() != 0) {
            String url = "";
            Iterator it = urlQueue.iterator();
            if (it.hasNext()) {
                url = it.next().toString();
                urlQueue.remove(url);
            }
            if (url.contains("http")) {
                if (url.contains("http://www.most.gov.cn/"))
                    try {
                        list_temp = s.fetch(url.replace("\"", ""));
                    } catch (IOException e) {
                        // TODO Auto-generated catch block

                    } catch (IllegalArgumentException e2) {
                        list_temp = null;
                    }
                else
                    list_temp = null;
                System.out.println(url + "\n");
            } else {
                url = "http://www.most.gov.cn/" + url;
                try {
                    list_temp = s.fetch(url.replace("\"", ""));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalArgumentException e2) {
                    list_temp = null;
                }


                System.out.println(url + "\n");
            }

            if (list_temp != null) {
                for (int j = 0; j < list_temp.size() - 1; j++) {
                    if (visitedUrlQueue.contains(list_temp.get(j))) {
                        continue;

                    }
                    if (urlQueue.size() < 100000)  //现在
                        urlQueue.add(list_temp.get(j));
                }
            }
            System.out.println("update urlqueue:" + urlQueue.size());
            System.out.println("update visitedurlqueue:" + visitedUrlQueue.size());

        }
    }

    public void saveFile(String fileName, String content) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println(file.createNewFile());
            file.createNewFile();
        }


        FileOutputStream fos = new FileOutputStream(file, false);
        OutputStreamWriter out = new OutputStreamWriter(fos, "UTF8");
        BufferedWriter bw = new BufferedWriter(out);
        bw.write(content);
        bw.write("\r\n");
        bw.flush();
    }
    public String getHtmlSource(String htmlUrl) {
        URL url;
        StringBuffer sb = new StringBuffer();
        try {
            url = new URL(htmlUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "GB2312"));//读取网页全部内容
            String temp;
            while ((temp = in.readLine()) != null) {
                sb.append(temp);
            }
            in.close();
        } catch (MalformedURLException e) {
            System.out.println("你输入的URL格式有问题！请仔细输入");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    public ArrayList<String> fetch(String url) throws IOException {
        if (visitedUrlQueue.contains(url))
            return null;
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod(url);
        int statusCode = httpClient.executeMethod(getMethod);
        if (statusCode >= 200 && statusCode < 400) {
            count++;
            visitedUrlQueue.add(url);
            String result;
            ArrayList<String> url_set = new ArrayList<String>();
            result = getMethod.getResponseBodyAsString();
            String temp = url.replaceAll("[^a-zA-Z0-9]", "");
            Document doc = Jsoup.parse(result);//(temp);
            String title = doc.head().select("title").text(); //doc2.head().select("title").text();
            System.out.println("title:" + title);

            saveFile("D:\\policy\\科技-语料-国家\\" + temp + ".txt", result);
            url_set = extract_url(result);
            getMethod.releaseConnection();
            return url_set;
        }
        return null;
    }
    public ArrayList<String> extract_url(String s) {
        ArrayList<String> url_str = new ArrayList<String>();
        Document doc = Jsoup.parse(s);
        Elements links = doc.getElementsByTag("a");
        for (Element link : links) {
            String linkHref = link.attr("href");
            url_str.add(linkHref);
        }
        return url_str;
    }
}