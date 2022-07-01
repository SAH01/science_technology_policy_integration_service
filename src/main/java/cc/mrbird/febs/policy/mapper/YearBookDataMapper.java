package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.IntermediateVariable;
import cc.mrbird.febs.policy.entity.YearBookData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface YearBookDataMapper extends BaseMapper<YearBookData> {
    /**
     * 测试
     * @param SQL  执行固定的包含YearBookData对象值的sql语句
     * @return
     */
    List<YearBookData> selectListBySQL(@Param("SQL") String SQL);

    /**
     * 查找实体数据
     * @param regionIds  地域id列表
     * @param intermediate  传递数据中间值对象
     * @param searchValues  查找指标
     * @return
     */
    List<YearBookData> getEntityNum(List<String> regionIds, @Param("intermediate") IntermediateVariable intermediate,List<String> searchValues);
    /**
     * 统计规模以上工业企业科技活动情况
     * 结合年鉴数据的特点，一共统计5个指标：规模以上工业企业新产品开发经费支出、科技活动人员数、R&amp;D人员、规模以上工业企业R&amp;D经费外部支出、规模以上工业企业R&amp;D经费内部支出
     * @param regionId 地区ID
     * @return 年鉴数据数组
     */
    List<YearBookData> getCorporateTechnologyActivities(String regionId);
    /**
     * 统计高校和科研院所承担课题情况
     * 结合年鉴数据的特点，一共统计6个指标：研究与开发机构R&D课题数、高等学校R&D课题数、研究与开发机构R&D课题投入人员、高等学校R&D课题投入人员、研究与开发机构R&D课题投入经费、高等学校R&D课题投入经费
     * @param regionId 地区ID
     * @return 年鉴数据数组
     */
    List<YearBookData> getUndertakeProject(String regionId);
    /**
     * 高新技术企业发展情况
     * 结合年鉴数据的特点，一共统计3个指标：国家高新技术产业开发区总产值、开发区高新技术企业数、国家高新技术产业开发区实交税金
     * @param regionId 地区ID
     * @return 年鉴数据数组
     */
    List<YearBookData> getEnterpriseDevelopment(String regionId);
}
