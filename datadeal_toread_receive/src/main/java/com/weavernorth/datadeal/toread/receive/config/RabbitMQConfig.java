package com.weavernorth.datadeal.toread.receive.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @auther liupeng
 * @Date 2019/4/26 15:08
 */
@Configuration
public class RabbitMQConfig
{
    public static Log log = LogFactory.getLog(RabbitMQConfig.class.getName());
    //指定RabbitMQ host.默认为: localhost)
    @Value("${spring.rabbitmq.host}")
    private String host;
    //指定RabbitMQ 的端口，默认: 5672)
    @Value("${spring.rabbitmq.port}")
    private int port;
    //指定登陆broker的用户名
    @Value("${spring.rabbitmq.username}")
    private String username;
    //指定broker的密码
    @Value("${spring.rabbitmq.password}")
    private String password;

    //MessageQueue、Exchange和Binding构成了AMQP协议的核心
    //messageData ---> virtual host{ Exchanges(Topic,Fanout,Direct)--Bindings--->Queues}
    //在AMQP模型中，Exchange是接受生产者消息并将消息路由到消息队列的关键组件。
    // ExchangeType和Binding决定了消息的路由规则。
    // 生产者想要发送消息，首先必须要声明一个Exchange和该Exchange对应的Binding。
    // 可以通过 ExchangeDeclare和BindingDeclare完成。
    // 在Rabbit MQ中，声明一个Exchange需要三个参数：ExchangeName，ExchangeType和Durable。
    // ExchangeName是该Exchange的名字，该属性在创建Binding和生产者通过publish推送消息时需要指定。
    // ExchangeType，指Exchange的类型，在RabbitMQ中，有三种类型的Exchange：direct ，fanout和topic，不同的Exchange会表现出不同路由行为。
    // Durable是该Exchange的持久化属性，
            //durable是为了防止宕机等异常而导致消息无法及时接收设计的,这个对queue无太多影响，但对topic影响比较大,在topic模式下，如果设置durable为true，就要设置clientID给 JMS provider，让他来维护记录 每个订阅者接收消息状态。同时topic的订阅者没接收一条消息也要反馈一条成功接收信息给JMS provider。
    // 声明一个Binding需要提供一个QueueName，ExchangeName和BindingKey


    //生产者在发送消息时，都需要指定一个RoutingKey和Exchange，Exchange在接到该RoutingKey以后，会判断该ExchangeType:
    //a) 如果是Direct类型，则会将消息中的RoutingKey与该Exchange关联的所有Binding中的BindingKey进行比较，如果相等，则发送到该Binding对应的Queue中。
    //b)   如果是  Fanout  类型，则会将消息发送给所有与该  Exchange  定义过  Binding  的所有  Queues  中去，其实是一种广播行为。
    //c)如果是Topic类型，则会按照正则表达式，对RoutingKey与BindingKey进行匹配，如果匹配成功，则发送到对应的Queue中。


    /**
     * Broker:消息队列服务器实体, 它提供一种传输服务,它的角色就是维护一条从生产者到消费者的路线，保证数据能按照指定的方式进行传输,
     * Exchange：消息交换机,它指定消息按什么规则,路由到哪个队列。
     * Queue:消息的载体,每个消息都会被投到一个或多个队列。
     * Binding:绑定，它的作用就是把exchange和queue按照路由规则绑定起来.
     * Routing Key:路由关键字,exchange根据这个关键字进行消息投递。
     * vhost:虚拟主机,一个broker里可以有多个vhost，用作不同用户的权限分离。
     * Producer:消息生产者,就是投递消息的程序.
     * Consumer:消息消费者,就是接受消息的程序.
     * Channel:消息通道,在客户端的每个连接里,可建立多个channel.
     */

    //Direct Exchange - 处理路由键。需要将一个队列绑定到交换机上，要求该消息与一个特定的路由键完全匹配。这是一个完整的匹配。如果一个队列绑定到该交换机上要求路由键 “dog”，则只有被标记为“dog”的消息才被转发，不会转发dog.puppy，也不会转发dog.guard，只会转发dog。
    //这种模式下不需要将Exchange进行任何绑定(binding)操作
    //消息传递时需要一个“RouteKey”，可以简单的理解为要发送到的队列名字。
    //如果vhost中不存在RouteKey中指定的队列名，则该消息会被抛弃
    // 配置“交换机”
    // 接收队列交换机
    /**
          1.多个队列会导致待办已办顺序出现问题，固待办已办数据均使用同一个队列
          2.目前队列包括 接收队列，回调队列，异常队列
     */
    //接收队列交换机
    public static final String EXECHANGE_RECEIVE_READDATA= "mq_exchange_direct_receive_readdata";


    // 接收队列名称
    public static final String QUEUE_RECEIVE_READDATA = "mq_queue_direct_readdata";//请求队列名称
    // 异常数据处理队列名称

    // 配置“路由关键字”
    // 接收队列路由关键字
    public static final String ROUTING_RECEIVE_READDATA = "mq_routing_receive_readdata";//路由关键字
//    public static final String ROUTING_RESPONSE = "ur_reponse_key";//
    // 异常数据处理队列路由关键字
   /*

   AMQP建立连接 过程大致可以认为是三个同步的RPC调用，Connection.Start ---> Connection.StartOk ---> Connection.Open。
    也就是说我们每次建立一个Connection的消耗是很大的，所以有没有一种方式来解决这个问题
    但是在这里有一个问题，我是应该缓存Channel还是Connection
    方案一：只缓存Connection
    方案二：Connection和Channel都缓存
        spirng提供了连接池CachingConnectionFactory
    CachingConnectionFactory为我们提供了两种缓存的模式：
    CHANNEL模式：这也是CachingConnectionFactory的默认模式，在这种模式下，所有的createConnection（）方法实际上返回的都是同一个Connection，同样的Connection.close()方法是没用的，因为就一个，默认情况下，Connection中只缓存了一个Channel，在并发量不大的时候这种模式是完全够用的，当并发量较高的时候，我们可以setChannelCacheSize（）来增加Connection中缓存的Channel的数量。
    CONNECTION模式：在CONNECTION模式下，每一次调用createConnection（）方法都会新建一个或者从缓存中获取，根据你设置的ConnectionCacheSize的大小，当小于的时候会采用新建的策略，当大于等于的时候会采用从缓存中获取的策略，与CHANNEL模式不同的是，CONNECTION模式对Connection和Channel都进行了缓存，最新版本的client中已经将Channel的缓存数量从1增加到了25，但是在并发量不是特别大的情况下，作用并不是特别明显。
    使用CachingConnectionFactory需要注意的一点是：所有你获取的Channel对象必须要显式的关闭，所以finally中一定不要忘记释放资源，如果忘记释放，则可能造成连接池中没有资源可用。
    */
    @Bean
    public ConnectionFactory connectionFactory()
    {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    //必须是prototype类型
    public RabbitTemplate rabbitTemplate()
    {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        return template;
    }
    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     * HeadersExchange ：通过添加属性key-value匹配
     * DirectExchange:按照routingkey分发到指定队列
     * TopicExchange:多关键字匹配
     * 只需要设置交换机和队列都持久化 就能实现队列和消息持久化
     * RabbitMQ的交换器类型一共有四种（direct、fanout、topic以及headers），每一种类型实现了不同的路由算法，
     * 其中direct类型交换器非常简单，当声明一个队列时，它会自动绑定到direct类型交换器（默认条件下，是一个空白字符串名称的交换器），
     * 并以队列名称作为路由键；当消息发送到RabbitMQ后所拥有的路由键与绑定使用的路由键匹配，消息就被投递到对应的队列。
     * headers交换器和direct交换器完全一致，但性能会差很多，headers交换器允许匹配AMQP消息的是header而非路由键，因此它并不实用。
     * fanout交换器可以将收到的消息投递给所有附件在此交换器上的队列。topic交换器可以使得来自不同源头的消息能够到达同一个队列
     */
    // 接收  交换机
    @Bean
    public DirectExchange defaultExchange_receive_toread()
    {
        //默认是交换机持久化
        return new DirectExchange(EXECHANGE_RECEIVE_READDATA);
    }
    /**
     * 获取队列recieve_todo
     *
     * @return
     */
    // 接收  队列
    @Bean
    public Queue queue_recieve_toread()
    {
        return new Queue(QUEUE_RECEIVE_READDATA, true); //队列持久
    }

    /**
     * 将消息和交换机进行绑定
     * @return
     */
    // 接收队列和接收交换机绑定
    @Bean
    public Binding binding_queue_reecive_toread()
    {
        return BindingBuilder.bind(queue_recieve_toread()).to(defaultExchange_receive_toread()).with(ROUTING_RECEIVE_READDATA);
    }

}
