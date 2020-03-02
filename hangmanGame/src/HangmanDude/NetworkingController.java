package HangmanDude;

import java.util.ArrayList;
import java.util.Random;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;


@Configuration
@ComponentScan
/**
 * 
 * @author Team 17
 *
 */
public class NetworkingController implements MessageListener
{
    Connection connection = null;

    HangmanMessageListener listener = null;

    Session session = null;

    ArrayList<MessageConsumer> consumers = new ArrayList<MessageConsumer>();

    private String game_challange_queue_name = null;

    private final boolean debug = false;


    /**
     * 
     * @param msg_listener
     * @throws JMSException
     */
    public NetworkingController( HangmanMessageListener msg_listener )
        throws JMSException
    {
        // AnnotationConfigApplicationContext context = new
        // AnnotationConfigApplicationContext(Networking.class);
        // ConnectionFactory factory =
        // context.getBean(CachingConnectionFactory.class);
        ConnectionFactory factory = connectionFactory();
        listener = msg_listener;

        connection = factory.createConnection();
        connection.start();
        session = connection.createSession( false, Session.AUTO_ACKNOWLEDGE );
    }


    /**
     * 
     * @param size
     * @return
     */
    public String randomString( int size )
    {
        String s = "abcdefghijklmnopqrstuvwxyz";
        Random r = new Random();
        String randomStr = "";
        for ( int i = 0; i < size; i++ )
            randomStr += s.charAt( r.nextInt( s.length() ) );
        return randomStr;
    }


    /**
     * 
     * @throws JMSException
     */
    public void end() throws JMSException
    {
        if ( game_challange_queue_name != null )
        {
            queueMessage( game_challange_queue_name,
                NetworkingConstants.PLAYER_QUIT );
        }
        for ( MessageConsumer consumer : consumers )
            consumer.close();
        session.close();
        connection.close();
        if ( debug )
            System.err.println( "ENDED ALL CONNECTIONS" );
    }


    /**
     * 
     * @return
     */
    ConnectionFactory connectionFactory()
    {
        return new CachingConnectionFactory( new ActiveMQConnectionFactory( NetworkingConstants.USERNAME,
            NetworkingConstants.PASSWORD,
            NetworkingConstants.ACTIVEMQ_URL ) );
    }


    /**
     * 
     * @param queue_name
     * @return
     * @throws JMSException
     */
    private String readQueueSync( String queue_name ) throws JMSException
    {
        Queue queue = session.createQueue( NetworkingConstants.QUEUE_PREFIX
            + queue_name );
        MessageConsumer consumer = session.createConsumer( queue );
        String word = ( (TextMessage)consumer.receive() ).getText();
        consumer.close();
        if ( debug )
            System.err.println( "READ QUEUE " + queue_name
                + " synchronously. Got item: " + word );
        return word;
    }


    /**
     * 
     * @param queue_name
     * @throws JMSException
     */
    private void readQueueAsync( String queue_name ) throws JMSException
    {
        Queue queue = session.createQueue( NetworkingConstants.QUEUE_PREFIX
            + queue_name );
        MessageConsumer consumer = session.createConsumer( queue );
        consumers.add( consumer );
        consumer.setMessageListener( this );
        if ( debug )
            System.err.println( "READING " + queue_name + " asynchronously" );

    }


    /**
     * 
     * @param queue_name
     * @param message
     * @throws JMSException
     */
    private void queueMessage( String queue_name, String message )
        throws JMSException
    {
        Queue queue = session.createQueue( NetworkingConstants.QUEUE_PREFIX
            + queue_name );
        MessageProducer producer = session.createProducer( queue );
        producer.send( session.createTextMessage( message ) );
        producer.close();
        if ( debug )
            System.err.println( message + " added to " + queue_name );
    }


    /**
     * 
     * @throws Exception
     */
    public void GamerAcceptChallange() throws Exception
    {
        if ( game_challange_queue_name == null )
            throw new Exception( "No challange available. Did you forget to call GetGameChallangeWord?" );
        else
        {
            queueMessage( game_challange_queue_name,
                NetworkingConstants.ChallangeAccepted );
            if ( debug )
                System.err.println( "ACCEPTING CHALLANGE" );
        }

    }


    /**
     * 
     * @throws Exception
     */
    public void GamerRejectChallange() throws Exception
    {
        if ( game_challange_queue_name == null )
            throw new Exception( "No challange available. Did you forget to call GetGameChallangeWord?" );
        else
        {
            queueMessage( game_challange_queue_name,
                NetworkingConstants.ChallangeRejected );
            if ( debug )
                System.err.println( "REJECTING CHALLANGE" );
        }
    }


    /**
     * 
     * @return
     * @throws JMSException
     * @throws InterruptedException
     */
    public String GamerGetGameChallangeWord()
        throws JMSException,
        InterruptedException
    {
        String word = null;
        while ( true )
        {
            word = readQueueSync( NetworkingConstants.GAMEQUEUE );
            if ( word != null )
            {
                game_challange_queue_name = word;
                return word.substring( 0, word.indexOf( '-' ) );
            }
            else
                Thread.sleep( 500 );
        }

    }


    /**
     * 
     * @param word
     * @throws JMSException
     */
    public void ChallangerCreateGame( String word ) throws JMSException
    {
        String random_hash = randomString( 5 );
        game_challange_queue_name = word + "-" + random_hash;
        queueMessage( NetworkingConstants.GAMEQUEUE, game_challange_queue_name );
        readQueueAsync( game_challange_queue_name );

    }


    /**
     * 
     * @param guess
     * @throws Exception
     */
    public void GamerGuessLetter( String guess ) throws Exception
    {
        if ( game_challange_queue_name == null )
            throw new Exception( "No challange available. Did you forget to call GetGameChallangeWord?" );
        queueMessage( game_challange_queue_name,
            NetworkingConstants.GUESS_PREFIX + guess );
    }


    /**
     * 
     */
    public void onMessage( Message msg )
    {
        if ( debug )
            System.err.println( "THIS HAPPENED!" );
        try
        {
            String text = ( (TextMessage)msg ).getText();
            if ( debug )
                System.err.println( "Received Message: " + text );

            if ( text == NetworkingConstants.ChallangeAccepted )
            {
                listener.challangeAccepted();
            }
            else if ( text == NetworkingConstants.ChallangeRejected )
            {
                listener.challangeRejected();
            }
            else if ( text == NetworkingConstants.PLAYER_QUIT )
            {
                listener.playerQuit();
            }
            else if ( text.contains( NetworkingConstants.GUESS_PREFIX ) )
            {
                listener.playerGuessed( text.substring( NetworkingConstants.GUESS_PREFIX.length() ) );
            }

        }
        catch ( JMSException e )
        {
            e.printStackTrace();
        }

    }

}
