package hangmanDudeTest;

import static org.junit.Assert.*;

import java.net.URISyntaxException;

import javax.jms.JMSException;

import HangmanDude.*;

import org.junit.Test;


/**
 * 
 * @author Team 17
 *
 */
public class hangmanDudeTest
{
    HangmanDudeGameModel test;


    @Test
    public void hangmanDudeTesting()
        throws JMSException,
        InterruptedException,
        URISyntaxException
    {
        // Initialize GUI Hangman
        try
        {
            test = new HangmanDudeGUIView( "words.txt" );
        }
        catch ( Exception e )
        {
            System.out.println( "hangmanDudeGameTest() Exception " + e );
        }

        // check if its null
        assertNotNull( "Is it null yo", test );

        // print out the string
        test.setWord( "boss" );

        // check if they equal
        assertEquals( "Is it Equal yo", "boss", test.getWord() );
    }
}
