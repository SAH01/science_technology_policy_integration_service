package cc.mrbird.febs.policy.entity;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.util.List;
@Data
public class Columnar {
    private JSONArray array;
    private List<String> list;
    private String a;
}
