package cc.mrbird.febs.policy.helper.policyImpact.prepareCorpora;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.*;

public class corpora_fromMongo {

    public static void main(String[] args){
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("policy");
        MongoCollection<Document> collection = mongoDatabase.getCollection("policyFile");
        FindIterable<Document> findIterable = collection.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        while (mongoCursor.hasNext()) {
            Document policyFile = mongoCursor.next();
            String title = policyFile.getString("policy_title");
            String document = policyFile.getString("policy_article");
            try{
                saveFile("D:\\policy\\科技-语料-国家\\" + title + ".txt", document);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    public static void saveFile(String fileName, String content) throws IOException {
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
}
