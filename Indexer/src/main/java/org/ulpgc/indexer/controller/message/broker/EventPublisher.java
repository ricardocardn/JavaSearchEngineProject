package org.ulpgc.indexer.controller.message.broker;

import org.ulpgc.indexer.controller.Publisher;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class EventPublisher implements Publisher {
    private final Connection connection;
    private final Session session;
    private final MessageProducer producer;

    public EventPublisher(String port, String queue) throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://mq-container:" + port);
        this.connection = factory.createConnection("artemis", "artemis");
        this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(queue);
        this.producer = session.createProducer(destination);
    }

    @Override
    public void publish(String event) {
        try{
            TextMessage message = session.createTextMessage(event);
            producer.send(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() throws JMSException {
        producer.close();
        session.close();
        connection.close();
    }

}
