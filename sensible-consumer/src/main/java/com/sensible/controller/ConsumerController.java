package com.sensible.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author liuyc
 * @Description: 服务消费者控制器
 * @date 2020/3/11 001117:07
 */
@RestController
@RequestMapping("consumer")
@DefaultProperties(defaultFallback = "hystrixFail" )
public class ConsumerController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    private RibbonLoadBalancerClient ribbonLoadBalancerClient;


    public String sayHelloToHystrixFail(){
        return "不好意思，调用打招呼服务出现故障了！";
    }


    /**
     * 模拟熔断器的三个状态
     * @param status
     * @return
     */
    @GetMapping("testCircuitBreakerStatus/{status}")
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "6"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "50")
    })
    public String testCircuitBreakerStatus(@PathVariable("status") String status){
        if ("open".equals(status)){
            throw new RuntimeException("熔断器打开");
        }
        String url = "http://sensible-service/hello/sayHi/Hystrix";
        return restTemplate.getForObject(url,String.class);
    }



    /**
     * HystrixCommand 启用降级逻辑，还可以指定降级方法，也可以不指定
     * @return
     */
    @GetMapping("sayHelloToHystrix")
//    @HystrixCommand(fallbackMethod = "sayHelloToHystrixFail")
//    @HystrixCommand(commandProperties = {
//            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value = "3000")
//    } )
    @HystrixCommand
    public String sayHelloToHystrix(){
        String url = "http://sensible-service/hello/sayHi/Hystrix";
        return restTemplate.getForObject(url,String.class);
    }

    public String hystrixFail(){
        return "不好意思，现在访问的人数太多了，请刷新重试！";
    }

    @GetMapping("sayHelloToEureka")
    public String sayHelloToEureka(){
        /** === 未使用负载均衡 === **/
        // 获取服务实例列表
        List<ServiceInstance> clientInstances = discoveryClient.getInstances("sensible-service");
        // 获取其中一个实例
        ServiceInstance serviceInstance = clientInstances.get(0);
        //String url = "http://"+serviceInstance.getHost()+":"+serviceInstance.getPort()+"/hello/sayHi/Eureka";

        /** === 使用负载均衡 === **/

        // 1.通过RibbonLoadBalancerClient，负载均衡获取一个服务实例，不需要像上面一样获取所有
        //ServiceInstance instance = ribbonLoadBalancerClient.choose("sensible-service");
        //String url = "http://"+instance.getHost()+":"+instance.getPort()+"/hello/sayHi/Ribbon";

        // 2.直接省略以上步骤,底层逻辑还是RibbonLoadBalancerClient
        String url = "http://sensible-service/hello/sayHi/Ribbo";
        System.out.println("服务地址："+url);
        return restTemplate.getForObject(url,String.class);
    }
}
