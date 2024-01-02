import api.APIController;

import javax.jms.JMSException;

public class UserServiceMain {
    public static void main(String[] args) throws JMSException {
        APIController apiController = new APIController();
        apiController.run();
    }
}
