package cc.mrbird.febs.common.entity;

import cc.mrbird.febs.policy.entity.PolicyName;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Data
public class NameTree<T> implements Serializable {

    private static final long serialVersionUID = 7681873362531265829L;

    private String id;
    private String icon;
    private String href;
    private String name;
    private Map<String, Object> state;
    private boolean checked = false;
    private Map<String, Object> attributes;
    private List<NameTree<T>> children;
    private String parentId;
    private boolean hasParent = false;
    private boolean hasChild = false;

    private PolicyName data;

    public void initChildren(){
        this.children = new ArrayList<>();
    }
}
