package com.qiuxk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ribbon")
public class RibbonController {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/first")
    public String first(String s) {
        System.out.println("first请求参数：" + s);
        String s1 = new RestTemplate().getForObject("http://127.0.0.1:4002/hello/world?param=" + s, String.class);

        return s1;
    }

    @RequestMapping("/second")
    public String second(String s) {
        //根据服务名 获取服务列表 根据算法选取某个服务 并访问某个服务的网络位置
        System.out.println("second请求参数：" + s);
        ServiceInstance serviceInstance = loadBalancerClient.choose("EUREKA-CLIENT");
        String s1 = new RestTemplate().getForObject("http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/hello/world?param=" + s, String.class);
        return s1;
    }


    @RequestMapping("/third")
    public String third(String s) {
        System.out.println("third请求参数：" + s);
        String s1 = restTemplate.getForObject("http://EUREKA-CLIENT/hello/world?param=" + s, String.class);
        return s1;
    }

}
