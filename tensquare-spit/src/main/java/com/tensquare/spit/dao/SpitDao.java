package com.tensquare.spit.dao;

import com.tensquare.spit.pojo.Spit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * 吐槽的dao
 */
public interface SpitDao extends MongoRepository<Spit, String> {

    /**
     * 根据上级ID查询吐槽评论
     */
    public Page<Spit> findByParentidOrderByPublishtime(String parentid, Pageable pageable);

    /**
     * 根据上级ID查询吐槽（评论）列表（分页）
     *
     * @param parentid
     * @param pageable
     * @return
     */
    public Page<Spit> findByParentid(String parentid, Pageable pageable);

    public List<Spit> findByParentid(String parentid);

    public Page<Spit> findAllByParentidAndStateOrderByPublishtimeDesc(String parentid, String state, Pageable pageable);

    public List<Spit> findTop4OrOrderByVisits();
}
