package cc.mrbird.febs.policy.helper.classifyTest;


import com.hankcs.hanlp.mining.cluster.ClusterAnalyzer;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * 文本聚类的练习
 *
 */

public class DocumentCluster {
    public static void main(String args[]){
        ClusterAnalyzer<String> analyzer = new ClusterAnalyzer<String>();
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("policy");
        MongoCollection<Document> collection = mongoDatabase.getCollection("policyFile");
        FindIterable<Document> findIterable = collection.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        while (mongoCursor.hasNext()) {
            Document policyFile = mongoCursor.next();
            analyzer.addDocument(policyFile.getString("policy_title"),policyFile.getString("policy_title"));
        }
//        System.out.println(analyzer.kmeans(7));
        System.out.println(analyzer.repeatedBisection(1.0));
    }
}
