package com.tensquare.user.service;

import java.util.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import util.IdWorker;

import com.tensquare.user.dao.AdminDao;
import com.tensquare.user.pojo.Admin;
import util.JwtUtil;

/**
 * 服务层
 *
 * @author Administrator
 */
@Service
public class AdminService {

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询全部列表
     *
     * @return
     */
    public List<Admin> findAll() {
        return adminDao.findAll();
    }

    /**
     * 条件查询+分页
     *
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<Admin> findSearch(Map whereMap, int page, int size) {
        Specification<Admin> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return adminDao.findAll(specification, pageRequest);
    }

    /**
     * 条件查询
     *
     * @param whereMap
     * @return
     */
    public List<Admin> findSearch(Map whereMap) {
        Specification<Admin> specification = createSpecification(whereMap);
        return adminDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public Admin findById(String id) {
        Optional<Admin> admin = adminDao.findById(id);
        if (admin.isPresent()) {
            return adminDao.findById(id).get();
        } else {
            return null;
        }
    }

    @Autowired
    private BCryptPasswordEncoder cryptPasswordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 增加
     *
     * @param admin
     */
    public void add(Admin admin) {
        admin.setId(idWorker.nextId() + "");
        //对密码进行BCrypt加密
        admin.setPassword(cryptPasswordEncoder.encode(admin.getPassword()));
        System.out.println("加密后的：" + admin.toString());
        System.out.println("反转密码：" + cryptPasswordEncoder.matches("admin", admin.getPassword()));
        adminDao.save(admin);
    }

    /**
     * 修改
     *
     * @param admin
     */
    public void update(Admin admin) {
        adminDao.save(admin);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        adminDao.deleteById(id);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<Admin> createSpecification(Map searchMap) {

        return new Specification<Admin>() {

            @Override
            public Predicate toPredicate(Root<Admin> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                // 登陆名称
                if (searchMap.get("loginname") != null && !"".equals(searchMap.get("loginname"))) {
                    predicateList.add(cb.like(root.get("loginname").as(String.class), "%" + (String) searchMap.get("loginname") + "%"));
                }
                // 密码
                if (searchMap.get("password") != null && !"".equals(searchMap.get("password"))) {
                    predicateList.add(cb.like(root.get("password").as(String.class), "%" + (String) searchMap.get("password") + "%"));
                }
                // 状态
                if (searchMap.get("state") != null && !"".equals(searchMap.get("state"))) {
                    predicateList.add(cb.like(root.get("state").as(String.class), "%" + (String) searchMap.get("state") + "%"));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

    /**
     * 管理员登录
     */
    public Admin login(Map<String, String> map) {
        //1.判断账户是否存在
        Admin admin = adminDao.findByLoginname(map.get("username"));
        System.out.println("账号：" + admin.getLoginname() + "密码：" + admin.getPassword());
        //2.判断密码是否正确，第一个参数为明文！！！，第二个参数才是密文 ！admin != null &&
        if (cryptPasswordEncoder.matches(map.get("password"), admin.getPassword())) {
            //成功
            System.out.println("登录成功");
            return admin;
        } else {
            //失败
            System.out.println("登录失败!");
            return null;
        }
    }
}
