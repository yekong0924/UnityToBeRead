package com.weavernorth.datadeal.toread.manage.config;

import com.weavernorth.datadeal.toread.manage.util.WxMappingJackson2HttpMessageConverter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * All rights Reserved, Designed By www.weaver.com.cn
 *
 * @version V1.0
 * @Title: UrlConfig
 * @Package com.weavernorth.datadeal.toread.manage.config
 * @Description:(用一句话描述该文件做什么)
 * @author: liupeng(1203153229 @ qq.com)
 * @date: 2020/7/7 17:15
 * @Copyright: 2020 www.weaver.com.cn Inc. All rights reserved.
 * 注意：本内容仅限于上海泛微网络科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Configuration
public class UrlConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder)
    {
        //此方法为了解决异常问题  引入来自  https://blog.csdn.net/u011768325/article/details/77097655
        //org.springframework.web.client.RestClientException: Could not write request: no suitable HttpMessageConverter found for request type [org.springframework.util.LinkedMultiValueMap] and content type [application/x-www-form-urlencoded;charset=UTF-8]

        RestTemplate restTemplate = builder.build();
        restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
        return restTemplate;
    }
}
