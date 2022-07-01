package cc.mrbird.febs.policy.controller;

import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.Policyan;
import cc.mrbird.febs.policy.service.IPolicyAnalysisService;
import cc.mrbird.febs.policy.service.IPolicyanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("policyan")
public class PolicyanController extends BaseController {
    @Autowired
    private IPolicyanService policyanService;


}
