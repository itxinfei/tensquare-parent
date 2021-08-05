package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin
@RequestMapping("/spit")
public class SpitController {

    @Autowired
    private SpitService spitService;

    /**
     * 查询所有
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", spitService.findAll());
    }

    /**
     * 查询一个
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", spitService.findById(id));
    }

    /**
     * 添加
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Spit spit) {
        spitService.add(spit);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Spit spit, @PathVariable String id) {
        //设置id
        spit.setId(id);
        spitService.update(spit);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String id) {
        spitService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 根据上级ID查询吐槽数据(分页)
     */
    @RequestMapping(value = "/comment/{parentid}/{page}/{size}", method = RequestMethod.GET)
    public Result findByParentid(@PathVariable String parentid, @PathVariable int page, @PathVariable int size) {
        Page<Spit> list = spitService.findByParentid(parentid, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<>(list.getTotalElements(), list.getContent()));
    }


    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 吐槽点赞
     */
    @RequestMapping(value = "/thumbup/{id}", method = RequestMethod.PUT)
    public Result thumbup(@PathVariable String id) {

        //模拟当前登录用户
        String userid = "1";

        //1.从redis查询用户是否已经点赞过
        String flag = (String) redisTemplate.opsForValue().get("spit_" + userid + "_" + id);

        if (flag != null) {
            //点赞过该吐槽
            return new Result(false, StatusCode.REPEATE_ERROR, "该用户已经点赞过");
        }

        spitService.thumbup(id);

        //2.把数据存入redis
        redisTemplate.opsForValue().set("spit_" + userid + "_" + id, "1");
        return new Result(true, StatusCode.OK, "点赞成功");
    }

    /**
     * 浏览量，不登录浏览量也可以增加
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/visits/{id}", method = RequestMethod.PUT)
    public Result updateVisits(@PathVariable String id) {
        //判断用户是否点过赞已经浏览过
        String userid = "1";
        if (redisTemplate.opsForValue().get("visits_" + userid + "_" + id) != null) {
            return new Result(false, StatusCode.REPEATE_ERROR, "你已经浏览过了");
        }
        spitService.updateVisits(id);
        redisTemplate.opsForValue().set("visits_" + userid + "_" + id, "1");
        return new Result(true, StatusCode.OK, "浏览成功");
    }

    /**
     * 分享数，不登录也可以分享
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/share/{id}", method = RequestMethod.PUT)
    public Result updateShare(@PathVariable String id) {
        //判断用户是否已经转发过
        String userid = "1";
        if (redisTemplate.opsForValue().get("share_" + userid + "_" + id) != null) {
            return new Result(false, StatusCode.REPEATE_ERROR, "你已经分享过了");
        }
        spitService.updateVisits(id);
        redisTemplate.opsForValue().set("share_" + userid + "_" + id, "1");
        return new Result(true, StatusCode.OK, "分享成功");
    }

    /**
     * 根据条件查询Spit列表
     *
     * @param spit
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Spit spit) {
        return new Result(true, StatusCode.OK, "查询成功",
                spitService.findSearch(spit));
    }

    //查询全部吐槽
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Spit spit, @PathVariable int page, @PathVariable int size) {
        Page<Spit> pageList = spitService.findSearch(spit, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Spit>(pageList.getTotalElements(), pageList.getContent()));
    }

    //根据吐槽id查询所有评论
    @RequestMapping(value = "/commentlist/{parentId}", method = RequestMethod.GET)
    public Result findByParentid(@PathVariable String parentId) {
        List<Spit> list = spitService.findByParentid(parentId);
        return new Result(true, StatusCode.OK, "查询成功", list);
    }

    /**
     * 查询热门吐槽
     *
     * @return
     */
    @RequestMapping(value = "/hotSpits", method = RequestMethod.GET)
    public Result getHotSpits() {
        List<Spit> list = spitService.getHotSpits();
        return new Result(true, StatusCode.OK, "查询成功", list);
    }

}
