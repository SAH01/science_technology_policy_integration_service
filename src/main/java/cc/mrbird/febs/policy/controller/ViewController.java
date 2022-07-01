package cc.mrbird.febs.policy.controller;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.policy.entity.*;
import cc.mrbird.febs.policy.service.*;
import cc.mrbird.febs.policy.utils.ClearUp;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author MrBird
 */
@Controller("policyView")
public class ViewController extends BaseController {

    @Autowired
    private IPolicy2Service policyService;
    @Autowired
    private IPolicyAnalysisService policyanalysisService;
    @Autowired
    private IPolicyanService policyanService;
    @Autowired
    private IPolicyTypeService policytypeService;
    @Autowired
    private IPolicyEvolutionService policyevolutionService;
    @Autowired
    private IPolicyContrastService policyContrastService;
    @Autowired
    private ITPolicyKindService policyKindService;
    @Autowired
    private IPolicyEvoService policyEvoService;
    @Autowired
    private IPolicyNewService policyNewService;
    @Autowired
    private IPolicyNeo4jService policyNeo4jService;
    @Autowired
    private IPolicyCrawlingService policyCrawlingService;

    @Autowired
    private IPolicyEnterService policyEnterService;
    @Autowired
    private IPolicyChangeService policyChangeService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/policyFile")
    @RequiresPermissions("user:view")
    public String systemUser() {
        return FebsUtil.view("policy/policyFile/policyFile");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/tupu")
    @RequiresPermissions("user:view")
    public String tupu() {
        return FebsUtil.view("policy/policytu/tupu");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/policyComprehensive")
    @RequiresPermissions("user:view")
    public String policyCm() {
        return FebsUtil.view("policy/policyFile/policyCm");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/shiyan")
    @RequiresPermissions("user:view")
    public String policyshiyan() {
        return FebsUtil.view("policy/policyFile/shiyan");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/policyEvolutionManage")
    @RequiresPermissions("user:view")
    public String policyEvolutionManage() {
        return FebsUtil.view("policy/policyFile/policyEvolutionManage");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/policyContrast")
    @RequiresPermissions("user:view")
    public String policyContrast() {
        return FebsUtil.view("policy/policyFile/policyContrast");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/Contrast")
    @RequiresPermissions("user:view")
    public String Contrast() {
        return FebsUtil.view("policy/policyType/Contrast");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/policyContrast2")
    @RequiresPermissions("user:view")
    public String policyContrast2() {
        return FebsUtil.view("policy/policyFile/policyContrast2");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/policySearch")
    @RequiresPermissions("user:view")
    public String policySearch() {
        return FebsUtil.view("policy/policySearch/policySearch");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/policySimilarity")
    @RequiresPermissions("user:view")
    public String policySimilarity() {
        return FebsUtil.view("policy/policyRank/policySimilarity");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/policyRank")
    @RequiresPermissions("user:view")
    public String policyRank() {
        return FebsUtil.view("policy/policyRank/policyRank");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/policyEvolution")
    @RequiresPermissions("user:view")
    public String policyEvolution() {
        return FebsUtil.view("policy/policyEvolution/policyEvolution");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/policyAtlas")
    @RequiresPermissions("user:view")
    public String policyAtlas() {
        return FebsUtil.view("policy/policyAtlas/policyAtlas");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/policyQuestion")
    @RequiresPermissions("user:view")
    public String policyQuestion() {
        return FebsUtil.view("policy/policyAtlas/policyQuestion");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/policyTheme")
    @RequiresPermissions("user:view")
    public String policyTheme() {
        return FebsUtil.view("policy/policyAtlas/policyTheme");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/policyAtlasSearch")
    @RequiresPermissions("user:view")
    public String policyAtlasSearch() {
        return FebsUtil.view("policy/policyAtlas/policyAtlasSearch");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/policyCrawling")
    @RequiresPermissions("user:view")
    public String policyCrawling() {
        return FebsUtil.view("policy/policyCrawling/policyCrawling");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/policyDictionary")
    @RequiresPermissions("user:view")
    public String policyDictionary() {
        return FebsUtil.view("policy/policyCrawling/policyDictionary");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/policyIndexing")
    @RequiresPermissions("user:view")
    public String policyIndexing() {
        return FebsUtil.view("policy/policyCrawling/policyIndexing");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/content")
    @RequiresPermissions("user:view")
    public String content() {
        return FebsUtil.view("policy/policyCrawling/content");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/policyType")
    @RequiresPermissions("user:view")
    public String policyType() {
        return FebsUtil.view("policy/policyType/policyType");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/policyKind")
    @RequiresPermissions("user:view")
    public String policyKind() {
        return FebsUtil.view("policy/policyType/policyKind");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/policynew")
    @RequiresPermissions("user:view")
    public String policynew() {
        return FebsUtil.view("policy/policyFile/policynew");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/yanbian")
    @RequiresPermissions("user:view")
    public String policybian() {
        return FebsUtil.view("policy/policyFile/yan");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/contrast/{kindid}")
    @RequiresPermissions("user:view")
    public String policyContrastDetail(@PathVariable int kindid, Model model) throws ParseException {
        List<Policy> listpolicy=new ArrayList<Policy>();
        List<PolicyContrast> list=policyContrastService.getPolicyContrastList(kindid);
        for(int i=0;i<list.size();i++)
        {
            int policyid=list.get(i).getPolicyid();
            listpolicy.add(policyService.getPolicyById(policyid));
        }
        PolicyContrast policyContrast=new PolicyContrast();
        ClearUp clearp=new ClearUp();
        policyContrast=clearp.ClearUpContrast(list,listpolicy);
        model.addAttribute("policyContrast", policyContrast);
        return FebsUtil.view("policy/policyFile/policyCon");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/contrast3/{kindName}")
    @RequiresPermissions("user:view")
    public String policyContrast3Detail(@PathVariable String kindName, Model model) throws ParseException {
        List<PolicyContrast> list=policyContrastService.getPolicyContrastListByName(kindName);
        PolicyContrast policyContrast=new PolicyContrast();
        ClearUp clearp=new ClearUp();
        policyContrast=clearp.ClearUpContrast2(list);
        policyContrast.setPolicyname(kindName);
        System.out.println(policyContrast.getPolicykey());
        System.out.println(policyContrast.getPolicymain());
        System.out.println(policyContrast.getKind());
        model.addAttribute("policyContrast", policyContrast);
        return FebsUtil.view("policy/policyFile/policyCon2");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/contrast2/{policyida}")
    @RequiresPermissions("user:view")
    public String policyContrast2Detail(@PathVariable int policyida, Model model) throws ParseException {
        List<Policy> listpolicy=new ArrayList<Policy>();
        List<PolicyContrast> list=policyContrastService.getPolicyContrastList(policyida);
        for(int i=0;i<list.size();i++)
        {
            int policyid=list.get(i).getPolicyid();
            listpolicy.add(policyService.getPolicyById(policyid));
        }
        PolicyContrast policyContrast=new PolicyContrast();
        ClearUp clearp=new ClearUp();
        policyContrast=clearp.ClearUpContrast(list,listpolicy);
        model.addAttribute("policyContrast", policyContrast);
        return FebsUtil.view("policy/policyFile/policyCon");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/evolution2/{kindid}")
    @RequiresPermissions("user:view")
    public String policyevolution2Detail(@PathVariable int kindid, Model model) throws ParseException {
        List<Policy> listpolicy=new ArrayList<Policy>();
        List<PolicyContrast> list=policyContrastService.getPolicyContrastListByTime(kindid);
        for(int i=0;i<list.size();i++)
        {
            int policyid=list.get(i).getPolicyid();
            listpolicy.add(policyService.getPolicyById(policyid));
        }
        PolicyContrast policyContrast=new PolicyContrast();
        ClearUp clearp=new ClearUp();
        policyContrast=clearp.ClearUpContrast(list,listpolicy);
        model.addAttribute("policyContrast", policyContrast);
        return FebsUtil.view("policy/policyEvolution/policyEvolution2");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/type/{id}")
    @RequiresPermissions("user:view")
    public String policytypeDetail(@PathVariable int id, Model model) throws ParseException {
        PolicyType policytype = policytypeService.getPolicyTypeById(id);
        PolicyEvo policyevo=new PolicyEvo();
        String type=policytype.getPolicytype();
        List<PolicyEvo> list=policyEvoService.getPolicyEvoList(type);
        ClearUp clearp=new ClearUp();
        policyevo=clearp.ClearUpPolicyEvolution(list,type);
        String time=policyevo.getPolicytime();
        String a[]=time.split("/");
        System.out.println(policyevo.getPolicytext2());
        model.addAttribute("policyevo", policyevo);
        return FebsUtil.view("policy/policyFile/policyEvolution");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/typ/{type}")
    @RequiresPermissions("user:view")
    public String policytypDetail(@PathVariable String type, Model model) throws ParseException {
        PolicyEvo policyevo=new PolicyEvo();
        List<PolicyEvo> list=policyEvoService.getPolicyEvoList(type);
        ClearUp clearp=new ClearUp();
        policyevo=clearp.ClearUpPolicyEvolution(list,type);
        String time=policyevo.getPolicytime();
        String a[]=time.split("/");
        System.out.println(policyevo.getPolicytext2());
        model.addAttribute("policyevo", policyevo);
        return FebsUtil.view("policy/policyFile/policyEvolution");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/detail/{id}")
    @RequiresPermissions("user:view")
    public String systemUserDetail(@PathVariable Integer id, Model model) {
        Policy policy = policyService.getPolicyById(id);
        List<Policyan> list=policyanService.getPolicyList(id);
        Policyan policyan=new Policyan();
        ClearUp clearp=new ClearUp();
        policyan=clearp.ClearUpPolicy(list,policy);
        model.addAttribute("policy", policyan);
        return FebsUtil.view("policy/policyFile/policyDetail");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/detailnew/{id}")
    @RequiresPermissions("user:view")
    public String detailnew(@PathVariable Integer id, Model model) {
        Policy policy = policyService.getPolicyById(id);
        model.addAttribute("policy", policy);
        return FebsUtil.view("policy/helpers/policyDetails");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/changedetail/{id}")
    @RequiresPermissions("user:view")
    public String changedetail(@PathVariable int id, Model model) {
        PolicyChange policyChange=policyChangeService.getPolicyChange(id);
        model.addAttribute("policy", policyChange);
        return FebsUtil.view("policy/helpers/policyChanges");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/detailCrawling/{id}")
    @RequiresPermissions("user:view")
    public String detailCrawling(@PathVariable Integer id, Model model) {
        PolicyEnter policyCrawling = policyEnterService.findById(id);
        String text=policyCrawling.getText();
        text=text.replace(".h1 {\n" +
                "\tPAGE-BREAK-AFTER: avoid; TEXT-JUSTIFY: inter-ideograph; TEXT-ALIGN: justify; LINE-HEIGHT: 240%; MARGIN: 17pt 0cm 16.5pt; FONT-SIZE: 22pt; FONT-WEIGHT: bold\n" +
                "}\n" +
                ".h2 {\n" +
                "\tPAGE-BREAK-AFTER: avoid; TEXT-JUSTIFY: inter-ideograph; TEXT-ALIGN: justify; LINE-HEIGHT: 173%; MARGIN: 13pt 0cm; FONT-SIZE: 16pt; FONT-WEIGHT: bold\n" +
                "}\n" +
                ".h3 {\n" +
                "\tPAGE-BREAK-AFTER: avoid; TEXT-JUSTIFY: inter-ideograph; TEXT-ALIGN: justify; LINE-HEIGHT: 173%; MARGIN: 13pt 0cm; FONT-SIZE: 16pt; FONT-WEIGHT: bold\n" +
                "}\n" +
                "DIV.union {\n" +
                "\tLINE-HEIGHT: 18px; FONT-SIZE: 14px\n" +
                "}\n" +
                "DIV.union TD {\n" +
                "\tLINE-HEIGHT: 18px; FONT-SIZE: 14px\n" +
                "}\n" +
                ".h1 {\n" +
                "\tPAGE-BREAK-AFTER: avoid; TEXT-JUSTIFY: inter-ideograph; TEXT-ALIGN: justify; LINE-HEIGHT: 240%; MARGIN: 17pt 0cm 16.5pt; FONT-SIZE: 22pt; FONT-WEIGHT: bold\n" +
                "}\n" +
                ".h2 {\n" +
                "\tPAGE-BREAK-AFTER: avoid; TEXT-JUSTIFY: inter-ideograph; TEXT-ALIGN: justify; LINE-HEIGHT: 173%; MARGIN: 13pt 0cm; FONT-SIZE: 16pt; FONT-WEIGHT: bold\n" +
                "}\n" +
                ".h3 {\n" +
                "\tPAGE-BREAK-AFTER: avoid; TEXT-JUSTIFY: inter-ideograph; TEXT-ALIGN: justify; LINE-HEIGHT: 173%; MARGIN: 13pt 0cm; FONT-SIZE: 16pt; FONT-WEIGHT: bold\n" +
                "}\n" +
                ".union {\n" +
                "\tLINE-HEIGHT: 18px; FONT-SIZE: 14px\n" +
                "}\n" +
                ".union TD {\n" +
                "\tLINE-HEIGHT: 18px; FONT-SIZE: 14px\n" +
                "}","");
        policyCrawling.setText(text);
        model.addAttribute("policy", policyCrawling);
        return FebsUtil.view("policy/helpers/policyDetailCrawling");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/detailneo4j/{policyname}")
    @RequiresPermissions("user:view")
    public String detailneo4j(@PathVariable String policyname, Model model) {
        PolicyNeo4j policyNeo4j=policyNeo4jService.getPolicyNeo4j(policyname);
        String text=policyNeo4j.getText();
        String theme[]=policyNeo4j.getTheme().split(" ");
        String[] list=text.split("。|\n");
        String result="";
        for(int i=0;i<theme.length;i++)
        {
            int flag=0;
            for(int j=0;j<list.length;j++)
            {
                if(list[j].indexOf(theme[i])!=-1&&list[j].length()>50&&flag==0)
                {
                    if(result.indexOf(list[j])==-1) {
                        result = result + "\n" + list[j];
                        flag=1;
                    }
                }
            }
        }
        for(int i=0;i<theme.length;i++)
        {
            result=result.replace(theme[i],"<b style='color:red'>" + theme[i] + "</b>");
        }
        System.out.println(result);
        System.out.println(list.length);
        policyNeo4j.setMain(result);
        model.addAttribute("policy", policyNeo4j);
        return FebsUtil.view("policy/policyAtlas/policyAtlasDetail");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/detailtheme/{policyname}")
    @RequiresPermissions("user:view")
    public String detailtheme(@PathVariable String policyname, Model model) {
        PolicyNeo4j policyNeo4j=policyNeo4jService.getPolicyNeo4j(policyname);
        model.addAttribute("policy", policyNeo4j);
        return FebsUtil.view("policy/policyAtlas/policyAtlasDetail");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/detailnew2/{result}")
    @RequiresPermissions("user:view")
    public String detailnew2(@PathVariable String result, Model model) {
        String a[]=result.split(",");
        int id=Integer.parseInt(a[0]);
        Policy policy = policyService.getPolicyById(id);
        String text=policy.getText();
        if (text.indexOf(a[1]) != -1) {
            text = text.replace(a[1], "<b style='color:red'>" + a[1] + "</b>");
        }
        policy.setText(text);
        model.addAttribute("policy", policy);
        return FebsUtil.view("policy/helpers/policyDetails");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/similar/{id}")
    @RequiresPermissions("user:view")
    public String policySimilar(@PathVariable Integer id, Model model) {
        List<PolicyAnalysis> list=policyanalysisService.getPolicyAnalysisList(id);
        PolicyAnalysis policyanalysis=new PolicyAnalysis();
        ClearUp clearp=new ClearUp();
        policyanalysis=clearp.ClearUpPolicyAnalysis(list);

        model.addAttribute("policy", policyanalysis);
        return FebsUtil.view("policy/policyFile/policySimilar");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/map/{name}")
    @RequiresPermissions("user:view")
    public String policya(@PathVariable String name, Model model) {
        List<PolicyEvo> list=policyEvoService.getPolicyEvoByNameList(name);
        PolicyEvo policyEvo=list.get(0);
        ClearUp clearp=new ClearUp();
        int id=policyEvo.getId();
        List<PolicyNewAnalysis> policyNewAnalysisList=policyNewService.getPolicyNewList(id);
        System.out.println(policyNewAnalysisList.size());
        PolicyEvo policyEvo1=clearp.ClearUpEvolutionList(policyNewAnalysisList,name);
//        PolicyEvo policyEvo1=clearp.ClearUpEvolution(policyEvo);
//        System.out.println(policyEvo1.getKeyword());
        model.addAttribute("policyEvo", policyEvo1);
        return FebsUtil.view("policy/policyFile/map");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/jindutiao")
    @RequiresPermissions("user:view")
    public String policyline() {
        return FebsUtil.view("policy/policyFile/jindutiao");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/keywords")
    @RequiresPermissions("user:view")
    public String policyAnalyse() {
        return FebsUtil.view("policy/helpers/showKeywords");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/steering")
    @RequiresPermissions("user:view")
    public String policySteering() {
        return FebsUtil.view("policy/helpers/policySteering");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/adaptation")
    @RequiresPermissions("user:view")
    public String policyAdaptation(@PathVariable Integer id, Model model) {
        Policy policy = policyService.getPolicyById(id);
        model.addAttribute("policy", policy);
        return FebsUtil.view("policy/policyFile/policyDetail");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/uploadfileView")
    @RequiresPermissions("user:view")
    public String generator() {
        return FebsUtil.view("policy/policyFile/uploadfile");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/policyDept")
    public String systemDept() {
        return FebsUtil.view("policy/policyDept/policyDept");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/policyAnalysisFormula")
    public String policyAnalysisFormula() {
        return FebsUtil.view("policy/indexSet/policyAnalysisFormula");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/scienceTechnologyEntityIndex")
    public String scienceTechnologyEntityIndex() {
        return FebsUtil.view("policy/indexSet/scienceTechnologyEntityIndex");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/policyMapRegionComparison")
    public String policyMapRegionComparison() {
        return FebsUtil.view("policy/analyse/policyMapRegionComparison");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/instrument")
    public String policyInstrument() {
        return FebsUtil.view("policy/helpers/policyInstrument");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/communityService")
    public String communityService() {
        return FebsUtil.view("policy/analyse/communityService");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/mapDetail")
    public String mapDetailData() {
        return FebsUtil.view("policy/analyse/mapDetail");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/sentenceDetail")
    public String sentenceDetail() {
        return FebsUtil.view("policy/analyse/sentenceDetail");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/policyConstitute")
    public String policyConstitute() {
        return FebsUtil.view("policy/analyse/policyConstitute");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/policyEffectEvaluation")
    public String policyEffectEvaluation() {
        return FebsUtil.view("policy/analyse/policyEffectEvaluation");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "policy/policyObjectDetail")
    public String policyObjectDetail() {
        return FebsUtil.view("policy/analyse/policyObjectDetail");
    }
}
