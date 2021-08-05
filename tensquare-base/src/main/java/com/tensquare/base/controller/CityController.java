package com.tensquare.base.controller;

import com.tensquare.base.pojo.City;
import com.tensquare.base.service.CityService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Administrator
 */
@RestController
@CrossOrigin
@RefreshScope  //实时更新配置信息
@RequestMapping("city")
public class CityController {

    @Resource
    private CityService cityService;

    /**
     * 查询全部数据
     */
    @RequestMapping(method = RequestMethod.GET)//http://127.0.0.1:9001/city/list
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", cityService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", cityService.findById(id));
    }


    /**
     * 分页+多条件查询
     *
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<City> pageList = cityService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<City>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", cityService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param city
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody City city) {
        int flag = cityService.add(city);
        if (flag == 0) {
            return new Result(false, StatusCode.ERROR, "该城市已存在");
        }
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param city
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody City city, @PathVariable String id) {
        city.setId(id);
        int flag = cityService.update(city);
        if (flag == 0) {
            return new Result(false, StatusCode.ERROR, "该城市已存在");
        }
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        cityService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

}