package com.sensible.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuyc
 * @Description: 控制器
 * @date 2020/3/11 001117:58
 */
@RestController
@RequestMapping("hello")
public class ProviderController {
    @RequestMapping("sayHi/{name}")
    public String sayHi(@PathVariable("name") String name){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Hello,"+ name;
    }
}
