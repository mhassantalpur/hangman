package hangmanDudeTest;

import static org.junit.Assert.*;

import javax.jms.JMSException;

import org.junit.Test;

import HangmanDude.HangmanMessageListener;
import HangmanDude.NetworkingController;

public class hangmanDudeNetworkControllerTests  {
	public class SampleClassListener implements HangmanMessageListener {

		public void challangeAccepted() throws JMSException {
			
			
			
		}

		public void challangeRejected() {
			// TODO Auto-generated method stub
			
		}

		public void playerQuit() {
			// TODO Auto-generated method stub
			
		}

		public void playerGuessed(String guess) {
			assertEquals(guess, "g");
			
		}
		
	}

	@Test
	public void test() throws Exception {
		NetworkingController gamer = new NetworkingController(new SampleClassListener());
		NetworkingController challanger= new NetworkingController(new SampleClassListener());
		
		String word = "test_word";
		
		challanger.ChallangerCreateGame(word);
		Thread.sleep(1000);
		assertEquals(word,gamer.GamerGetGameChallangeWord());
		gamer.GamerAcceptChallange();
		String guess = "g";
		
		gamer.GamerGuessLetter(guess);
		
		gamer.end();
		challanger.end();
		
		
	}

}
