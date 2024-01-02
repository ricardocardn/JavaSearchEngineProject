package controller.broker;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class EventPublisher implements Publisher {
    private final Connection connection;
    private final Session session;
    private final MessageProducer producer;


    public EventPublisher(String port, String queue, String apiURL) throws JMSException {
        String apiIP = apiURL.substring(7);
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://" + apiIP + ":" + port);
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
            System.out.println("Book added to queue");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
