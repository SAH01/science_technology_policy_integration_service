package cc.mrbird.febs.policy.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 政策分类维度树
 * @author MrBird
 */
@Data
public class PolicyDimensionalityTree<T> implements Serializable {

    private static final long serialVersionUID = 7681873362531265829L;

    private String id;
    private String icon;
    private String href;
    private String name;
    private Map<String, Object> state;
    private boolean checked = false;
    private Map<String, Object> attributes;
    private List<PolicyDimensionalityTree<T>> children;
    private String parentId;
    private boolean hasParent = false;
    private boolean hasChild = false;

    private PolicyDept government_data;
    private PolicyIndustrial industrial_data;
    private PolicyInstrument instrument_data;
    private PolicyCategory category_data;

    public void initChildren(){
        this.children = new ArrayList<>();
    }

}
