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


public class Spider {


    private static int count = 0;

//	private static LinkedList<String> visitedUrlQueue = new LinkedList<String>();	
//	private static LinkedList<String> urlQueue = new LinkedList<String>();

    private static LinkedList<String> visitedUrlQueue = new LinkedList<String>();
    private static LinkedHashSet<String> urlQueue = new LinkedHashSet<String>();

    public static void main(String[] args) {
        Spider s = new Spider();

        String website = "http://www.most.gov.cn";//"http://www.yuhuagu.com/";//"http://www.chinavegan.com/";"http://www.meishichina.com/";//
        //s.addElement(website);
        s.urlQueue.add(website);
        //s.urlQueue.add(website,"");

        ArrayList<String> list_temp = new ArrayList<String>();

        //s.visitedUrlQueue.add(website);

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
                //System.out.println(list_temp.get(i));
                //s.urlQueue.add(list_temp.get(i));
                s.urlQueue.add(list_temp.get(i));

            }
        }
        System.out.println("old urlqueue:" + s.urlQueue.size());

        while (s.urlQueue.size() != 0) {
            String url = "";
            Iterator it = s.urlQueue.iterator();
            if (it.hasNext()) {
                url = it.next().toString();
                s.urlQueue.remove(url);
            }


            //queue.remove();
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
                // System.out.println(list_temp);
            } else {
                url = "http://www.most.gov.cn/" + url;
                // url="http://www.yuhuagu.com/"+url;
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

            //Set<String> keySet = set.keySet();
            //System.out.println("old:size"+keySet.size());
            if (list_temp != null) {
                for (int j = 0; j < list_temp.size() - 1; j++) {
                    //	System.out.println(list_temp.get(j));
                    //s.urlQueue.add(list_temp.get(j));

                    if (visitedUrlQueue.contains(list_temp.get(j))) {
                        continue;

                    }
                    urlQueue.add(list_temp.get(j));
                    //s.visitedUrlQueue.add(list_temp.get(j));

                }
            }
            //System.out.println(s.urlQueue);
            System.out.println("update urlqueue:" + s.urlQueue.size());
            System.out.println("update visitedurlqueue:" + s.visitedUrlQueue.size());

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
        //  System.out.println("��缓���");

    }

    public String getHtmlSource(String htmlUrl) {
        URL url;
        StringBuffer sb = new StringBuffer();
        try {
            url = new URL(htmlUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF8"));//读取网页全部内容
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
        //visitedUrlQueue.add(url);
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

            Document doc = Jsoup.parse(result.toString());//(temp);
            String title = doc.head().select("title").text(); //doc2.head().select("title").text();
            System.out.println("title:" + title);
            saveFile("D:\\policy\\科技-语料-国家\\" + temp + ".txt", result.toString());


            url_set = extract_url(result.toString());
            getMethod.releaseConnection();
            return url_set;
        }

        return null;


    }

    public ArrayList<String> extract_url(String s) {
        ArrayList<String> url_str = new ArrayList<String>();
        Document doc = Jsoup.parse(s);
        Elements links = doc.getElementsByTag("a");

        // Elements links = doc.select("a[href]");
        //Elements links2 =  links.getElementsByTag("href");
        // System.out.println(links);
        for (Element link : links) {
            String linkHref = link.attr("href");
            // System.out.println(linkHref);
            url_str.add(linkHref);
        }

        return url_str;
    }
}


