package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.Date;
import java.util.List;

/**
 * 吐槽的service
 */
@Service
public class SpitService {

    @Autowired
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询所有
     */
    public List<Spit> findAll() {
        return spitDao.findAll();
    }

    /**
     * 查询一个
     */
    public Spit findById(String id) {
        return spitDao.findById(id).get();
    }

    /**
     * 添加
     */
    public void add(Spit spit) {

        //使用idWord获取一个id值
        spit.setId(idWorker.nextId() + "");
        spitDao.save(spit);

        //判断哪些是吐槽的评论
        if (spit.getParentid() != null && !spit.getParentid().equals("")) {
            //更新该评论对应的吐槽的回复数+1

            //1.创建查询对象
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));

            //2.创建修改对象
            Update update = new Update();
            update.inc("comment", 1);

            mongoTemplate.updateFirst(query, update, "spit");
        }
    }

    /**
     * 修改
     */
    public void update(Spit spit) {
        spitDao.save(spit);
    }

    /**
     * 删除
     */
    public void deleteById(String id) {
        spitDao.deleteById(id);
    }

    /**
     * 根据上级ID查询吐槽数据
     *
     * @param parentid
     * @param page
     * @param size
     * @return
     */
    public Page<Spit> findByParentid(String parentid, int page, int size) {
        return spitDao.findByParentidOrderByPublishtime(parentid, PageRequest.of(page - 1, size));
    }

    //    /**
//     * 点赞
//     * @param id
//     */
//    public void thumbup(String id) {
//        //1.先根据id查询该吐槽记录
//        Spit spit = findById(id);
//        //2.修改字段
//        spit.setThumbup(spit.getThumbup()+1);
//        //3.保存
//        update(spit);
//    }
    /**
     * 注入MongoDBTemplate
     */
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 点赞只修改对应的字段
     */
    public void thumbup(String id) {

        //1.创建条件
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));

        //2.创建修改对象
        Update update = new Update();
        //增加字段值
        update.inc("thumbup", 1);

        //3.使用模块进行修改
        mongoTemplate.updateFirst(query, update, "spit");
    }

    /**
     * 浏览量
     *
     * @param id
     */
    public void updateVisits(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.inc("visits", 1);
        mongoTemplate.updateFirst(query, update, "spit");
    }

    /**
     * 分享数
     *
     * @param id
     */
    public void updateShare(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.inc("share", 1);
        mongoTemplate.updateFirst(query, update, "spit");
    }

    /**
     * 发布吐槽（或吐槽评论）
     *
     * @param spit
     */
   /* public void add(Spit spit) {
        spit.setId(idWorker.nextId() + "");
        spit.setPublishtime(new Date());//发布日期
        spit.setVisits(0);//浏览量
        spit.setShare(0);//分享数
        spit.setThumbup(0);//点赞数
        spit.setComment(0);//回复数
        spit.setState("1");//状态
        if (spit.getParentid() != null && !"".equals(spit.getParentid())) {//如果存在上级ID,则这个吐槽是评论
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));
            Update update = new Update();
            update.inc("comment", 1);
            mongoTemplate.updateFirst(query, update, "spit");
        } else {
            spit.setParentid("0");
        }
        spitDao.save(spit);
    }*/

    /**
     * 根据条件查询吐槽
     *
     * @param spit
     * @return
     */
    public List<Spit> findSearch(Spit spit) {
        //使用MongoDBTemplate来实现多条件查询
        Criteria c1 = Criteria.where("content").regex(".*" + spit.getContent() + ".*");
        Criteria c2 = Criteria.where("nickname").is(spit.getNickname());
        Criteria cr = new Criteria();
        Query query = new Query(cr.orOperator(c1, c2));
        return mongoTemplate.find(query, Spit.class);
    }

    //查询全部吐槽列表，父id=0，按照时间排序
    public Page<Spit> findSearch(Spit spit, int page, int size) {
        Pageable pageable = new PageRequest(page - 1, size);
        //条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("content", ExampleMatcher.GenericPropertyMatchers.contains());
        /*if(spit.getContent()!=null&&!"".equals(spit.getContent())){
            System.out.println("参数没空:"+spit.getContent());
            exampleMatcher.withMatcher("content",ExampleMatcher.GenericPropertyMatchers.contains());
        }
        else{
            System.out.println("参数空了");
            exampleMatcher.withIgnorePaths("content");
        }
        exampleMatcher.withIgnorePaths("id","publishtime","userid","nickname","visits","thumbup","share","comment","state","parentid");*/
        //创建条件实例
        Example<Spit> example = Example.of(spit, exampleMatcher);
        //Page<Spit> all=spitDao.findAll(example,pageable);
        Page<Spit> all = spitDao.findAllByParentidAndStateOrderByPublishtimeDesc("0", "1", pageable);
        return all;
    }

    //根据吐槽查询所有评论列表
    public List<Spit> findByParentid(String parentId) {
        return spitDao.findByParentid(parentId);
    }

    //查询热门吐槽
    public List<Spit> getHotSpits() {
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "visits");
        Sort sort = new Sort(order);
        List<Spit> list = spitDao.findAll(sort);
        return list.subList(0, 4);
        //return spitDao.findTop4OrOrderByVisits();
    }
}
