package cc.mrbird.febs.policy.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.Kind2Tree;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.policy.entity.TPolicyKind2;
import cc.mrbird.febs.policy.service.ITPolicyKind2Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("kind2")
public class Kind2Controller {
    @Autowired
    private ITPolicyKind2Service kind2Service;

    @GetMapping("select/tree")
    @ControllerEndpoint(exceptionMessage = "获取部门树失败")
    public List<Kind2Tree<TPolicyKind2>> getDeptTree() throws FebsException {
        return this.kind2Service.findTPolicyKinds();
    }
    @GetMapping("tree")
    @ControllerEndpoint(exceptionMessage = "获取分类树失败")
    public FebsResponse getKindTree(TPolicyKind2 kind) throws FebsException {
        List<Kind2Tree<TPolicyKind2>> kinds = this.kind2Service.findTPolicyKinds(kind);


        return new FebsResponse().success().data(kinds);
    }
}
