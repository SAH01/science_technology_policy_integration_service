package cc.mrbird.febs.policy.helper.policyImpact.util;

import java.util.*;

/**
 * Map操作类，包括排序，打印，截取
 * @author Angela
 */
public class MapUtil {

    /**对Map按键值升序排序**/
    public static <K, V extends Comparable<? super V>>
    Map<K, V> asc( Map<K, V> map){
        //将map.entrySet()转换成list
        LinkedList<Map.Entry<K, V>> list =
                new LinkedList<Map.Entry<K, V>>( map.entrySet() );
        //然后通过比较器来实现排序
        Collections.sort( list, new Comparator<Map.Entry<K, V>>() {
            //升序排序
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 ){
                return (o1.getValue()).compareTo( o2.getValue() );
            }
        } );
        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }

    /**对Map按键值降序排序**/
    public static <K, V extends Comparable<? super V>>
    Map<K, V> desc( Map<K, V> map){
        //将map.entrySet()转换成list
        LinkedList<Map.Entry<K, V>> list =
                new LinkedList<Map.Entry<K, V>>( map.entrySet() );
        //然后通过比较器来实现排序
        Collections.sort( list, new Comparator<Map.Entry<K, V>>() {
            //降序排序
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 ){
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );
        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }

    /**
     * 取键值大于最小阈值并且小于最大阈值的map子集
     * @param map
     * @param minThreshold 最小阈值
     * @param maxThreshold 最大阈值
     * @return
     */
    public static <K, V extends Comparable<? super V>> Map<K,V> between(
            Map<K,V> map,V minThreshold,V maxThreshold){
        Map<K,V> temp=new HashMap<K,V>();
        for(Map.Entry<K, V> me: map.entrySet()){
            V value=me.getValue();
            if(value.compareTo(minThreshold)>=0
                    &&value.compareTo(maxThreshold)<=0){
                temp.put(me.getKey(), value);
            }
        }
        return temp;
    }

    /**
     * 返回键值大于最小阈值的map子集
     * @param map
     * @param minThreshold 最小阈值
     * @return
     */
    public static <K, V extends Comparable<? super V>> Map<K,V> range(
            Map<K,V> map,V minThreshold){
        Map<K,V> temp=new HashMap<K,V>();
        for(Map.Entry<K, V> me: map.entrySet()){
            V value=me.getValue();
            if(value.compareTo(minThreshold)>=0){
                temp.put(me.getKey(), value);
            }
        }
        return temp;
    }

    /**
     * 选前num的特征集合
     * @param map 特征-权重集
     * @param num 个数
     * @return 前num的特征子集
     */
    public static <K, V extends Comparable<? super V>> Map<K,V> sub(
            Map<K,V> map,int num){
        Map<K,V> temp=new HashMap<K,V>();
        Set<Map.Entry<K,V>> set = map.entrySet();
        Iterator<Map.Entry<K,V>> it = set.iterator();
        int count=0;
        while(count<num&&it.hasNext()){
            Map.Entry<K,V> me = it.next();
            V value=me.getValue();
            temp.put(me.getKey(), value);
            count++;
        }
        return temp;
    }

    /**
     * 打印map的前num个数据
     * @param map 特征-权重集
     * @param num 个数
     */
    public static <K, V extends Comparable<? super V>>
    void print(Map<K,V> map,int num){
        Set<Map.Entry<K,V>> set = map.entrySet();
        Iterator<Map.Entry<K,V>> it = set.iterator();
        int count=0;
        while(it.hasNext()&&count<num){
            Map.Entry<K,V> me = it.next();
            System.out.println(me.getKey()+" "+me.getValue());
            count++;
        }
    }

    /**
     * 打印map
     * @param map 特征-权重集
     */
    public static <K, V extends Comparable<? super V>> void print(Map<K,V> map){
        for(Map.Entry<K, V> me: map.entrySet()){
            System.out.println(me.getKey()+" "+me.getValue());
        }
    }

}