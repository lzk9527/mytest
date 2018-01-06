package com.itheima.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * @Description: 消息生产者：生产消息--》写入activeMQ服务器消息
 *
 * @Title: JMSProducer.java
 * @date 2017年10月6日 下午3:49:46
 */
public class JMSProducer {
	// 默认连接用户名
	private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;  //admin
	// 默认连接密码
	private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;  //admin
	// 默认连接地址
	private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL;  // tcp://lcoalhost:61616
	// 发送的消息数量
	private static final int SENDNUM = 10;

	public static void main(String[] args) {
		// 连接工厂
		ConnectionFactory connectionFactory;
		// 连接
		Connection connection = null;
		// 会话 接受或者发送消息的线程
		Session session;
		// 消息的目的地--队列名称
		Destination destination;
		// 消息生产者
		MessageProducer messageProducer;
		
		
		
		// 实例化连接工厂 用户名，密码，访问broker消息服务器地址
		connectionFactory = new ActiveMQConnectionFactory(JMSProducer.USERNAME, JMSProducer.PASSWORD,
				JMSProducer.BROKEURL);

		System.out.println(JMSProducer.BROKEURL);
		try {
			// 通过连接工厂获取连接
			connection = connectionFactory.createConnection();
			// 启动连接
			connection.start();
			// 创建session  p1:当设置true,session需要提交
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			// 创建一个名称为firstQuene的消息队列
			//参数：队列名称
			destination = session.createQueue("嘿嘿嘿");  // P2P  
//			session.createTopic("");  //发布订阅消息
			
			// 创建消息生产者---写入消息
			messageProducer = session.createProducer(destination);
			//默认消息：持久化消息
//			messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			// 发送消息
			for (int i = 0; i < 10; i++) {
				// 创建消息
				Message message = session.createTextMessage("你好,activeMQ" + i);
				System.out.println("发送消息：Activemq 发送消息" + i);
				// 通过消息生产者发出消息 ：向MQ服务器写入消息
				messageProducer.send(message);
			}
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
