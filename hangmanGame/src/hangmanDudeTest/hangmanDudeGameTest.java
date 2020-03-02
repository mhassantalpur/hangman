package hangmanDudeTest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;
import javax.jms.JMSException;

import org.junit.Test;

import HangmanDude.*;

/*
 * Tests Console View
 */

public class hangmanDudeGameTest {
	HangmanDudeGameModel hangmanDudeGameTest;
	
	@Test
	public void hangmanDudeGameTesting() throws JMSException, InterruptedException, URISyntaxException, IOException
	{
		//Initialize Console Hangman
		try {hangmanDudeGameTest = new HangmanDudePlainView("words.txt");}
			catch(Exception e) {System.out.println("hangmanDudeGameTest() Exception " + e);}

		// test if null
		assertNotNull("Is it null yo", hangmanDudeGameTest);

		// input 7 into strikes
		hangmanDudeGameTest.strikes = 7;

		// check if it is equal to 7
		assertEquals("Is it equal yo", 7, hangmanDudeGameTest.strikes);

		// input boss into word
		hangmanDudeGameTest.word = "boss";

		// check if word is actually boss
		assertEquals("boss", hangmanDudeGameTest.word);

		// check if gameOver it should be true since we inserted 7 for strikes
		// and MAXSTRIKE is 6

		assertEquals(true, hangmanDudeGameTest.isGameOver());

		// set strikes to 4
		hangmanDudeGameTest.strikes = 4;

		// now gameover should be false
		assertEquals(false, hangmanDudeGameTest.isGameOver());

		// check if all letters are guessed and should be false
		assertEquals(false, hangmanDudeGameTest.areAllLettersGuessed());
	}

}
