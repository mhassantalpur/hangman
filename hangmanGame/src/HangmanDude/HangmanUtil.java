package HangmanDude;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Contains a main method to clear the server queue.
 * 
 * @author Team 17
 *
 */
public class HangmanUtil {
	public static void main(String...args) throws JMSException, InterruptedException {
		 Connection connection =  (Connection) new ActiveMQConnectionFactory(
				NetworkingConstants.USERNAME, NetworkingConstants.PASSWORD, NetworkingConstants.ACTIVEMQ_URL).createConnection();
			connection.start();
			Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
			MessageConsumer consumer = session.createConsumer(session.createQueue(NetworkingConstants.QUEUE_PREFIX + NetworkingConstants.GAMEQUEUE));
			for(int i = 0; i < 10; i++) {
				System.out.println("Cleaning...");
				String text = ((TextMessage) consumer.receive()).getText();
				System.out.println(text);
			}
			
			consumer.close();
			session.close();
			connection.close();
			System.err.println("Done");
	}
}
