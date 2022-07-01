package cc.mrbird.febs.common.controller;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.system.entity.User;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MrBird
 */
public class BaseController {

    private Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    protected User getCurrentUser() {
        return (User) getSubject().getPrincipal();
    }

    protected Session getSession() {
        return getSubject().getSession();
    }

    protected Session getSession(Boolean flag) {
        return getSubject().getSession(flag);
    }

    protected void login(AuthenticationToken token) {
        getSubject().login(token);
    }

    protected Map<String, Object> getDataTable(IPage<?> pageInfo) {
        return getDataTable(pageInfo, FebsConstant.DATA_MAP_INITIAL_CAPACITY);
    }
    protected Map<String, Object> getDataTable(IPage<?> pageInfo, int dataMapInitialCapacity) {
        Map<String, Object> data = new HashMap<>(dataMapInitialCapacity);
        data.put("rows", pageInfo.getRecords());
        data.put("total", pageInfo.getTotal());
        return data;
    }
    protected Map<String, Object> getEchartsData(JSONObject json) {
        Map<String, Object> data = new HashMap<>();
        data.put("nodes", json.get("nodes"));
        data.put("links", json.get("links"));
        return data;
    }
    protected Map<String, Object> getInstrumentTableData(JSONObject json) {
        Map<String, Object> data = new HashMap<>();
        data.put("keywords", json.get("keywords"));
        data.put("frequency", json.get("frequency"));
        data.put("group_frequency", json.get("group_frequency"));
        data.put("proportion", json.get("proportion"));
        data.put("group_proportion", json.get("group_proportion"));
        data.put("words_num", json.get("words_num"));
        return data;
    }
}
