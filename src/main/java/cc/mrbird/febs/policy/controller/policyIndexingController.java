package cc.mrbird.febs.policy.controller;

import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.PolicyEnter;
import cc.mrbird.febs.policy.service.IPolicyIndexingService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cc.mrbird.febs.common.controller.BaseController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @author Administrator
 * @date 2022-04-27 22:24
 */
@Slf4j
@RestController
@RequestMapping("indexing")
public class policyIndexingController extends BaseController {
	private String temp = "0";
	@Autowired
	private IPolicyIndexingService policyIndexingService;

	@GetMapping("list")
	@RequiresPermissions("user:view")
	public FebsResponse policyList(PolicyEnter policy, QueryRequest request) {
		Map<String, Object> dataTable = getDataTable(this.policyIndexingService.findPolicyIndexingDetail(policy, request));
		temp="0";
		return new FebsResponse().success().data(dataTable);
	}
	@GetMapping("getListById")
	@RequiresPermissions("user:view")
	public FebsResponse getListById(String policyName,String policyId,String policyOrgan,
	                                String policyTime,String policyType,String policyPublisher, QueryRequest request) {
		Map<String, Object> dataTable = getDataTable(this.policyIndexingService.
				findPolicyIndexingDetailgById(policyName, policyId,policyOrgan,policyTime,policyType,policyPublisher,request));
		temp="0";
		return new FebsResponse().success().data(dataTable);
	}
	@RequestMapping("getFlag")
	public FebsResponse getFlag(){
		JSONObject q2 = new JSONObject();
		q2.put("information",temp);
		return new FebsResponse().success().data(q2);
	}
	@RequestMapping("update")
	public ModelAndView updatePolicy(PolicyEnter policy, QueryRequest request){
		policyIndexingService.updatePolicyIndexing(policy);
		temp = "修改成功";
		return new ModelAndView("redirect:/index#/policy/policyIndexing");
	}

	@RequestMapping("deleteP")
	public FebsResponse deleteP(int id){
		JSONObject q2 = new JSONObject();
		policyIndexingService.deletePolicyIndexing(id);
		q2.put("information","删除成功！");
		return new FebsResponse().success().data(q2);
		//return new ModelAndView("redirect:/index#/policy/policyCrawling");
	}

	@RequestMapping("findById")
	public FebsResponse findById(int id){
		System.out.println("--------- findById -----------");
		PolicyEnter byId = policyIndexingService.findById(id);
		System.out.println(byId.toString());
		return new FebsResponse().success().data(byId);
	}
}