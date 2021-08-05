package com.tensquare.base.dao;

import com.tensquare.base.pojo.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/*JpaRepository提供了基本的增删改查
JpaSpecificationExecutor用于做复杂的条件查询*/
public interface LabelDao extends JpaRepository<Label, String>, JpaSpecificationExecutor<Label> {
    /**
     * 查询推荐标签列表
     *
     * @param recommend
     * @return
     */
    public List<Label> findAllByRecommend(String recommend);

    /**
     * 查询有效标签列表
     *
     * @param state
     * @return
     */
    public List<Label> findAllByState(String state);

    Label findByLabelname(String name);

    /*查询热门标签列表页*/
    @Query(value = "select * from tb_label t order by count desc limit 6;", nativeQuery = true)
    public List<Label> findTop6ByCount();

    @Query(value = "select labelid from tb_ul where userid=?", nativeQuery = true)
    public List<String> getMyLabels(String me);
}
