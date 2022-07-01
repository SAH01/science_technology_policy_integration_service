package cc.mrbird.febs.policy.service;

import cc.mrbird.febs.common.entity.DeptTree;
import cc.mrbird.febs.common.entity.KindTree;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.TPolicyKind;
import cc.mrbird.febs.system.entity.Dept;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ITPolicyKindService extends IService<TPolicyKind> {
    List<TPolicyKind> getList();
    List<TPolicyKind> getTPolicyKindList(int id);
    List<TPolicyKind> getPTPolicyKindList(int id);
    /**
     * 获取类型树（下拉选使用）
     *
     * @return 分类树集合
     */
    List<KindTree<TPolicyKind>> findTPolicyKinds();

    /**
     * 获取部门列表（树形列表）
     *
     * @param kind 类型对象（传递查询参数）
     * @return 分类树
     */
    List<KindTree<TPolicyKind>> findTPolicyKinds(TPolicyKind kind);

    /**
     * 获取类型树（供Excel导出）
     *
     * @param kind    类型对象（传递查询参数）
     * @param request QueryRequest
     * @return List<Dept>
     */
    List<TPolicyKind> findTPolicyKinds(TPolicyKind kind, QueryRequest request);
    /**
     * 新增部门
     *
     * @param kind 部门对象
     */
    void createKind(TPolicyKind kind);

    /**
     * 修改部门
     *
     * @param kind 部门对象
     */
    void updateKind(TPolicyKind kind);

    /**
     * 删除部门
     *
     * @param kindIds 部门 ID集合
     */
    void deleteKinds(String[] kindIds);
}
