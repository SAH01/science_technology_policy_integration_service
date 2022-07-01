package cc.mrbird.febs.policy.service;

import cc.mrbird.febs.common.entity.Kind2Tree;
import cc.mrbird.febs.common.entity.KindTree;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.TPolicyKind;
import cc.mrbird.febs.policy.entity.TPolicyKind2;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ITPolicyKind2Service extends IService<TPolicyKind2> {
    TPolicyKind2 getTPolicyKindByParentId(int parentid);
    List<TPolicyKind2> getTPolicyKindListByParentId(int parentid);
    List<TPolicyKind2> getTPolicyKindListByKindoneId(int kindid);
    List<TPolicyKind2> getTPolicyKindByKindoneId();

    /**
     * 获取类型树（下拉选使用）
     *
     * @return 分类树集合
     */
    List<Kind2Tree<TPolicyKind2>> findTPolicyKinds();

    /**
     * 获取部门列表（树形列表）
     *
     * @param kind 类型对象（传递查询参数）
     * @return 分类树
     */
    List<Kind2Tree<TPolicyKind2>> findTPolicyKinds(TPolicyKind2 kind);

    /**
     * 新增部门
     *
     * @param kind 部门对象
     */
    void createKind(TPolicyKind2 kind);

    /**
     * 修改部门
     *
     * @param kind 部门对象
     */
    void updateKind(TPolicyKind2 kind);

    /**
     * 删除部门
     *
     * @param kindIds 部门 ID集合
     */
    void deleteKinds(String[] kindIds);

    void deleteTPolicyKind2s(int kindid);

}

