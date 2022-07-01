package cc.mrbird.febs.policy.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class FormulaAndEntityTree<T> implements Serializable {
    private static final long serialVersionUID = 7681873362531265829L;

    private String id;
    private String icon;
    private String content;
    private String name;
    private Map<String, Object> state;
    private boolean checked = false;
    private Map<String, Object> attributes;
    private List<FormulaAndEntityTree<T>> children = new ArrayList<>();
    private String parentId;
    private boolean hasParent = false;
    private boolean hasChild = false;

    private AnalysisFormula formulaData;
    private ScienceTechnologyEntityIndex entityIndexData;
    public void initChildren(){
        this.children = new ArrayList<>();
    }
}
