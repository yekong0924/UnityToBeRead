package com.weavernorth.datadeal.toread.receive.webservice.config;
import com.weavernorth.datadeal.toread.receive.webservice.ReceiveMsgService;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

@Configuration
public class WebServiceConfig {

    @Bean
    public ServletRegistrationBean webServiceDispatcherServlet(){
        //发布服务名称
        return new ServletRegistrationBean(new CXFServlet(),"/services/*");
    }

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus()
    {
        return  new SpringBus();
    }

    @Autowired
    private ReceiveMsgService receiveMsgService;
    @Bean
    public Endpoint endpoint() {
        //对应webservices服务访问地址   http://127.0.0.1:6868/services/receivemsg?wsdl
        //绑定要发布的服务
        EndpointImpl endpoint=new EndpointImpl(springBus(), receiveMsgService);
        endpoint.publish("/receivemsg"); //显示要发布的名称
        return endpoint;
    }
/*
    @Autowired
    private WorkflowPlus wfPlus;
    @Bean
    public Endpoint endpoint1() {
        EndpointImpl endpoint=new EndpointImpl(springBus(), wfPlus);//绑定要发布的服务
        endpoint.publish("/wfplus"); //显示要发布的名称
        return endpoint;
    }

    */
}