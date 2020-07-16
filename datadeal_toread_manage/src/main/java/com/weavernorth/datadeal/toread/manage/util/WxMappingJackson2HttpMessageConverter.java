package com.weavernorth.datadeal.toread.manage.util;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther Lylong
 * @Date 2019/5/17 17:39
 * 异常解决方案来自 https://blog.csdn.net/u011768325/article/details/77097655
 */
public class WxMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter
{
	public WxMappingJackson2HttpMessageConverter(){
        List<MediaType> mediaTypes = new ArrayList<MediaType>();
        mediaTypes.add(MediaType.TEXT_PLAIN);
//        mediaTypes.add(MediaType.TEXT_HTML);  //加入text/html类型的支持
//        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        setSupportedMediaTypes(mediaTypes);// tag6
    }

}
