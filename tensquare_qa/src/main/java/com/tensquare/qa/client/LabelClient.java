package com.tensquare.qa.client;

import com.tensquare.qa.client.impl.LabelClientImpl;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 远程调用基础微服务的标签模块 接口
 */
@FeignClient(value = "tensquare-base",fallback = LabelClientImpl.class) //对方的微服务的名称
public interface LabelClient {

    /**
     * 查询一个
     */
    @RequestMapping(value ="/label/{id}" ,method = RequestMethod.GET )
    public Result findById(@PathVariable("id") String id);
}
