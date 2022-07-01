package cc.mrbird.febs.policy.controller;

import cc.mrbird.febs.policy.entity.YearBookData;
import cc.mrbird.febs.policy.service.IYearBookDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class YearBookDataController {
    @Autowired
    private IYearBookDataService yearBookDataService;

    @GetMapping()
    public List<YearBookData> selUser() {
        return yearBookDataService.selectListBySQL("SELECT c.ID,s.Name BOOK_NAME,c.REGION_ID,c.REGION,c.VALUE,c.UNIT_C,c.YEARS,c.NAME,c.SEARCH_VALUE From annual_cell c join BASE_ANNUAL_SORT s on s.ID = substr(c.ID,0,3)  where SEARCH_VALUE like '%高等学校在校%' and REGION_ID = '110000'  ORDER BY YEARS");
    }
}
