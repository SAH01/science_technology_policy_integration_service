package cc.mrbird.febs.policy.controller;


import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.IntermediateVariable;
import cc.mrbird.febs.policy.entity.Policy;
import cc.mrbird.febs.policy.entity.PolicyAnalysis;
import cc.mrbird.febs.policy.service.IPolicy2Service;
import cc.mrbird.febs.policy.service.IPolicyAnalysisService;
import cc.mrbird.febs.policy.utils.AnalyNLP;
import cc.mrbird.febs.policy.utils.CreateFileUtil;
import cc.mrbird.febs.policy.utils.FileUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("policy")
public class PolicyController extends BaseController {
    public final static String UPLOAD_FILE_PATH = "D:\\upload\\";


    @Autowired
    private IPolicy2Service policyService;
    private IPolicyAnalysisService policyAnalysisService;

    private AnalyNLP nlp;
    private FileUtils utils;

    @GetMapping("completionPolicyProperties")
    public FebsResponse completionPolicy(Integer policyId) {
        this.policyService.completionProperties(policyId);
        return new FebsResponse().success();
    }

    @GetMapping("list4/{data}")
    @RequiresPermissions("user:view")
    public FebsResponse policyList4(@PathVariable String data,Policy policy, QueryRequest request) {
        if(!data.equals("0")) {
//            String a[] = data.split(",");
//            System.out.println(data);
//            System.out.println(a.length);
//            policy.setName(a[0].trim());
//            if (a[1] != "") {
//                String b[] = a[1].trim().split(" - ");
//                if (b.length > 1) {
//                    String createTimeFrom = b[0];
//                    String createTimeTo = b[1];
//                    policy.setCreateTimeFrom(createTimeFrom);
//                    policy.setCreateTimeTo(createTimeTo);
//                }
//            }
//            policy.setKeyword(a[2].trim());
//            policy.setOrgan(a[3].trim());
//            policy.setText(a[4].trim());
//            policy.setForm(a[5].trim());
//            policy.setRank(a[6].trim());
            policy.setName("科");
            policy.setForm("综合");
        }

        Map<String, Object> dataTable = getDataTable(this.policyService.findPolicyDetail(policy, request));
        return new FebsResponse().success().data(dataTable);
    }

    @GetMapping("list3/{policySearch}")
    @RequiresPermissions("user:view")
    public FebsResponse policyList3(@PathVariable String policySearch,Policy policy, QueryRequest request) {
        System.out.println(policySearch);
        String a[]=policySearch.split(",");
        if(a.length==1) {
            policy.setText(a[0]);
        }
        else if(a.length==2)
        {
            policy.setText(a[0]);
            policy.setForm(a[1]);
        }
        else
        {
            policy.setText(a[0]);
            policy.setForm(a[1]);
            policy.setRank(a[2]);
        }
        Map<String, Object> dataTable = getDataTable(this.policyService.findPolicyDetail(policy, request));
        return new FebsResponse().success().data(dataTable);
    }

    @GetMapping("list2/{policySearch}")
    @RequiresPermissions("user:view")
    public FebsResponse policyList2(@PathVariable String policySearch,Policy policy, QueryRequest request) {
        System.out.println(policySearch);
        String a[]=policySearch.split(",");
        if(a.length==1) {
            policy.setForm(a[0]);
        }
        else if(a.length==2)
        {
            policy.setForm(a[0]);
            policy.setRank(a[1]);
        }
        else
        {
            policy.setForm(a[0]);
            policy.setRank(a[1]);
            policy.setName(a[2]);
        }
        Map<String, Object> dataTable = getDataTable(this.policyService.findPolicyDetail(policy, request));
        return new FebsResponse().success().data(dataTable);
    }

    @GetMapping("list")
    @RequiresPermissions("user:view")
    public FebsResponse policyList(Policy policy, QueryRequest request) {
        Map<String, Object> dataTable = getDataTable(this.policyService.findPolicyDetail(policy, request));
        return new FebsResponse().success().data(dataTable);
    }
    @GetMapping("list1")
    @RequiresPermissions("user:view")
    public FebsResponse policyanalysisList(PolicyAnalysis policyanalysis, QueryRequest request) {
        Map<String, Object> dataTable = getDataTable(this.policyAnalysisService.findPolicyDetail(policyanalysis, request));
        return new FebsResponse().success().data(dataTable);
    }

    @GetMapping("echartsData")
    @RequiresPermissions("user:view")
    public FebsResponse userList(IntermediateVariable variable) {
        List<Integer> regionIds = new ArrayList<>();


        String regionIdStr = variable.getRegionIds();
        System.out.println(regionIdStr);
        for (String regionTmp : regionIdStr.split(",")) {
            regionIds.add(Integer.parseInt(regionTmp));
        }
        JSONObject json = this.policyService.getJsonData(regionIds,variable);

        //CreateFileUtil.createJsonFile(json.toString(), "json_for_helpers", "policyKeywords");
        return new FebsResponse().success().data(json);
    }

    @GetMapping("RelationData")
    @RequiresPermissions("user:view")
    public FebsResponse RelationData(IntermediateVariable variable) {
        List<Integer> regionIds = new ArrayList<>();
        regionIds.add(110000);
        regionIds.add(120000);
        regionIds.add(130000);


//        String regionIdStr = variable.getRegionIds();
//        System.out.println(regionIdStr);
//        for (String regionTmp : regionIdStr.split(",")) {
//            regionIds.add(Integer.parseInt(regionTmp));
//        }
        JSONObject json = this.policyService.getJsonData(regionIds,variable);

        //CreateFileUtil.createJsonFile(json.toString(), "json_for_helpers", "policyKeywords");
        return new FebsResponse().success().data(json);
    }

    @GetMapping("instrumentData")
    @RequiresPermissions("user:view")
    public FebsResponse getPolicyInstrumentTableJsonData(IntermediateVariable variable) {
        List<Integer> regionIds = new ArrayList<>();
        //最终返回的json对象
        JSONObject json = new JSONObject();
        String regionIdStr = variable.getRegionIds();
        //要进行查找，地区不能为空
        for (String regionTmp : regionIdStr.split(",")) {
            regionIds.add(Integer.parseInt(regionTmp));
        }

        Map<String, Object> jsonData = getInstrumentTableData(this.policyService.getPolicyInstrumentTableJsonData(regionIds,variable));
        return new FebsResponse().success().data(jsonData);
    }

    @RequestMapping(value = "uploadFile")
    public String upload(@RequestParam("file") MultipartFile file) throws ParseException {
        String text = "";
        String title = "";
        Date date = null;
        Policy policy = new Policy();
        if (!file.isEmpty()) {
            try {
                XWPFDocument xwpfDocument = new XWPFDocument(file.getInputStream());
                XWPFParagraph para;
                Iterator<XWPFParagraph> iterator = xwpfDocument.getParagraphsIterator();
                while (iterator.hasNext()) {
                    para = iterator.next();
                    if (date == null) {
                        date = nlp.NLP(para.getText());
                    }
                    if (text != "") {
                        text = text + "\n" + para.getText();
                    } else {
                        text = para.getText();
                    }
                    if (title == "") {
                        title = para.getText();
                    }

                }
                policy.setText(text);
                policy.setName(title);
                policy.setViadata(date);
                this.policyService.createPolicy(policy);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Map<String, String> resObj = new HashMap<>();
            try {
                int n = utils.check(file.getOriginalFilename());
                if (n == 0) {
                    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(UPLOAD_FILE_PATH, file.getOriginalFilename())));
                    out.write(file.getBytes());
                    out.flush();
                    out.close();
                }
                if (n == 1) {
                    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(UPLOAD_FILE_PATH, "副" + file.getOriginalFilename())));
                    out.write(file.getBytes());
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                resObj.put("msg", "error");
                resObj.put("code", "1");
                return JSONObject.toJSONString(resObj);
            }
            resObj.put("msg", "ok");
            resObj.put("code", "0");
            return JSONObject.toJSONString(resObj);
        } else {
            return null;
        }
    }
}
