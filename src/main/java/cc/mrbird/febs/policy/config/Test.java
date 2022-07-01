package cc.mrbird.febs.policy.config;


public class Test {
//    public static void main( String[] args ) {
//
//        Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "751208liu" ) );
//        Session session = driver.session();
//        // session.run( "CREATE (a:Person {name: {name}, title: {title}})",parameters( "name", "Arthur001", "title", "King001" ) );
//
//        try {
////            StatementResult result = session.run( "MATCH (a:Person) WHERE a.name = {name} " +
////                    "RETURN a.name AS name, a.title AS title",parameters( "name", "Arthur001" ) );
//            StatementResult result = session.run( "MATCH (n:`政策`) RETURN n LIMIT 25" );
//
//            List<Record> records = result.list();
//            System.out.println("result:"+result);
//
//            for (Record recordItem : records) {
//
//                List<String> list=recordItem.keys();
//                System.out.println(recordItem);
//                for (String key:list){
//                    System.out.println("key:"+key);
//                    System.out.println("value:"+recordItem.get(key));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            session.close();
//            driver.close();
//        }
//
//    }
}

