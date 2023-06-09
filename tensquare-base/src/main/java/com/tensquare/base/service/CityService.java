package com.tensquare.base.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import util.IdWorker;

import com.tensquare.base.dao.CityDao;
import com.tensquare.base.pojo.City;

/**
 * 城市
 */
@Service
public class CityService {

    @Resource
    private CityDao cityDao;

    @Resource
    private IdWorker idWorker;

    /**
     * 查询全部列表
     */
    public List<City> findAll() {
        return cityDao.findAll();
    }

    /**
     * 条件查询+分页
     *
     * @param whereMap
     * @param page
     * @param size
     */
    public Page<City> findSearch(Map whereMap, int page, int size) {
        Specification<City> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return cityDao.findAll(specification, pageRequest);
    }

    /**
     * 条件查询
     */
    public List<City> findSearch(Map whereMap) {
        Specification<City> specification = createSpecification(whereMap);
        return cityDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     */
    public City findById(String id) {
        return cityDao.findById(id).get();
    }

    /**
     * 增加
     */
    public int add(City city) {
        city.setId(idWorker.nextId() + "");
        String name = city.getName();
        if (cityDao.findByName(name) != null) {
            return 0;
        }
        cityDao.save(city);
        return 1;
    }

    /**
     * 修改
     *
     * @param city
     */
    public int update(City city) {
        String name = city.getName();
        if (cityDao.findByName(name) != null) {
            return 0;
        }
        cityDao.save(city);
        return 1;
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        cityDao.deleteById(id);
    }

    /**
     * 动态条件构建
     */
    private Specification<City> createSpecification(Map searchMap) {
        return new Specification<City>() {
            @Override
            public Predicate toPredicate(Root<City> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                // 城市名称
                if (searchMap.get("name") != null && !"".equals(searchMap.get("name"))) {
                    predicateList.add(cb.like(root.get("name").as(String.class), "%" + (String) searchMap.get("name") + "%"));
                }
                // 是否热门
                if (searchMap.get("ishot") != null && !"".equals(searchMap.get("ishot"))) {
                    predicateList.add(cb.like(root.get("ishot").as(String.class), "%" + (String) searchMap.get("ishot") + "%"));
                }
                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };
    }

    /**
     * 根据城市id返回城市名
     */
    public String getCityName(String id) {
        City one = cityDao.getOne(id);
        System.out.println("城市名称:" + one.getName());
        return one.getName();
    }
}
