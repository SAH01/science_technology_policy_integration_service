package cc.mrbird.febs.policy.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class RegionTree<T> implements Serializable {

    private static final long serialVersionUID = 7681873362531265829L;

    private String id;
    private String title;
    private String name;
    private boolean checked = false;
    private List<RegionTree<T>> children;
    private String parentId;
    private boolean hasParent = false;
    private boolean hasChild = false;
    public void initChildren(){
        this.children = new ArrayList<>();
    }
}
