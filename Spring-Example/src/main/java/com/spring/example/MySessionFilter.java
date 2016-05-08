package com.spring.example;

import com.hansong.session.RedisHttpSessionFilter;
import org.springframework.stereotype.Component;

/**
 * Created by xhans on 2016/5/8.
 */
@Component
public class MySessionFilter extends RedisHttpSessionFilter{
}
