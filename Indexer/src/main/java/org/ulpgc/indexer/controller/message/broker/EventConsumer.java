package org.ulpgc.indexer.controller.message.broker;

import org.apache.activemq.ActiveMQConnectionFactory;

import org.ulpgc.indexer.controller.Consumer;

import javax.jms.*;

public class EventConsumer implements Consumer {
    private final Connection connection;
    private final Session session;
    private final MessageConsumer consumer;

    public EventConsumer(String port, String queue) throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://mq-container:" + port);
        this.connection = factory.createConnection("artemis", "artemis");
        this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(queue);
        connection.start();
        this.consumer = session.createConsumer(destination);
    }

    @Override
    public String getMessage() {
        try {
            while (true) {
                Message message = consumer.receive();
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    return textMessage.getText();
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void closeConnection() throws JMSException {
        consumer.close();
        session.close();
        connection.close();
    }
}