package com.tensquare.user.controller;

import com.tensquare.user.pojo.Admin;
import com.tensquare.user.service.AdminService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 控制器层
 *
 * @author Administrator
 */
@RestController
//@CrossOrigin
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @Resource
    private JwtUtil jwtUtil;

    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(value = "findAll", method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", adminService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", adminService.findById(id));
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
        Page<Admin> pageList = adminService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Admin>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", adminService.findSearch(searchMap));
    }

    /**
     * 增加
     * <p>
     * //@RequestBody Admin admin
     */
    @RequestMapping("add")
    public Result add() {
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param admin
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Admin admin, @PathVariable String id) {
        admin.setId(id);
        adminService.update(admin);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        adminService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 管理员登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)//
    public Result login(@RequestBody Map<String, String> map) {
        Admin admin = adminService.login(map);
        if (admin != null) {
            if (!admin.getState().equals("1")) {
                return new Result(false, StatusCode.USER_PASS_ERROR, "账号未激活，请联系管理员激活");
            }
            //登录成功
            //给前端签发Token
            String token = jwtUtil.createJWT(admin.getId(), admin.getLoginname(), "admin");
            //把token返回给前端
            Map<String, String> result = new HashMap<String, String>();
            result.put("name", admin.getLoginname());
            result.put("token", token);
            map.put("name", admin.getLoginname());    //map中存放管理员对应的JWT的token,并返回给前台
            return new Result(true, StatusCode.OK, "登录成功", result);
        } else {
            return new Result(false, StatusCode.USER_PASS_ERROR, "用户名或者密错误");
        }
    }

    /**
     * 返回管理员信息 /admin/info
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public Result getInfo(@RequestParam("token") String token) {
        //解析token后获得管理员id
        Claims claims = jwtUtil.parseJWT(token);
        Map map = new HashMap();
        map.put("roles", claims.get("roles"));
        String id = claims.getId();
        Admin admin = adminService.findById(id);
        map.put("loginname", admin.getLoginname());
        return new Result(true, StatusCode.OK, "查询成功", map);
    }
}
