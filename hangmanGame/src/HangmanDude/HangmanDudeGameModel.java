package HangmanDude;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.jms.JMSException;


/**
 * HangmanDudeGameModel contains all the abstract methods necessary to implement
 * a representation of the hangmandude game. This class contains the underlying
 * logic for the game, and can be extended to improve or enhance functionality
 * and representation.
 * 
 * @author Team 17
 * 
 */
public abstract class HangmanDudeGameModel implements HangmanMessageListener
{
    public static final int MAXSTRIKES = 6;

    public String word;

    ArrayList<String> dict = new ArrayList<String>();

    public int strikes;

    public ArrayList<Character> guesses = new ArrayList<Character>();

    private int playerType;

    NetworkingController player;


    /**
     * Constructor
     * 
     * Initializes the dictionary.
     * 
     * @throws JMSException
     * @throws InterruptedException
     * @throws URISyntaxException
     * @throws IOException
     */
    public HangmanDudeGameModel( String wordsFile ) throws IOException
    {
        initializeDictionary( wordsFile );
    }


    /**
     * This method calls the beginning of the game in whatever representation is
     * extended.
     * 
     * @throws Exception
     */
    public abstract void playGame() throws Exception;


    /**
     * This method initializes the dictionary of valid words into the
     * hangmandude class. It takes a list of valid words and puts it into a
     * dictionary list.
     * 
     * @param wordsFile
     * @throws IOException
     */
    private void initializeDictionary( String wordsFile ) throws IOException
    {
        // Opens the File
        File dictionary = new File( wordsFile );

        // Creates a bufferedreader to read the file.
        BufferedReader reader = null;
        reader = new BufferedReader( new FileReader( dictionary ) );
        String text = null;

        // Loops through every line.
        while ( ( text = reader.readLine() ) != null )
        {
            dict.add( text );
        }

        // Close bufferedreader.
        reader.close();

    }


    /**
     * Sets the type of player, whether the player is the guesser or the setter,
     * and creates a new networking controller for the player.
     * 
     * If the player is the setter, playerType is 1.
     * 
     * If the player is the guesser, playerType is 2.
     * 
     * @param i
     * 
     * @throws JMSException
     */
    public void setPlayerType( int i ) throws JMSException
    {
        playerType = i;
        player = new NetworkingController( this );
    }


    /**
     * Returns the type of player.
     * 
     * @return
     */
    public int getPlayerType()
    {
        return playerType;
    }


    /**
     * Returns true if the game is over. Counts the strikes or if all the
     * letters are guessed.
     * 
     * @return
     */
    public boolean isGameOver()
    {
        return ( strikes >= MAXSTRIKES ) || ( areAllLettersGuessed() );
    }


    /**
     * Returns true if all the letters have been guessed.
     * 
     * @return
     */
    public boolean areAllLettersGuessed()
    {
        // Cycles through every single letter of the word.
        for ( int i = 0; i < getWord().length(); i++ )
        {
            // Returns false if this letter has not been guessed.
            if ( !guesses.contains( getWord().charAt( i ) ) )
            {
                return false;
            }
        }
        return true;
    }


    /**
     * Guesses the letter given by the first letter in the string.
     * 
     * @param s
     * @throws Exception
     */
    public void guessLetter( String s ) throws Exception
    {
        player.GamerGuessLetter( s );
        char c = s.charAt( 0 );

        // If the character has not been guessed, add it to the guesses.
        if ( !guesses.contains( c ) )
        {
            guesses.add( c );

            // If the character is not in the word, increase the number of
            // strikes.
            if ( getWord().indexOf( c ) == -1 )
            {
                strikes++;
            }
        }
    }


    /**
     * Returns the word to be guessed.
     * 
     * @return
     */
    public String getWord()
    {
        return word;
    }


    /**
     * Sets the word to be guessed.
     * 
     * @param word
     * @return
     */
    public boolean setWord( String word )
    {
        if ( dict.contains( word ) )
        {
            this.word = word;

            return true;
        }
        return false;
    }


    /**
     * Gets the word from the server from the guesser side.
     * 
     * @throws Exception
     */
    protected void getWordFromServer() throws Exception
    {
        word = player.GamerGetGameChallangeWord();
        player.GamerAcceptChallange();
    }


    /**
     * Pushes the word to the server from the setter side.
     * 
     * @throws JMSException
     */
    protected void pushWordToServer() throws JMSException
    {
        player.ChallangerCreateGame( word );
    }


    /**
     * 
     */
    public void challangeAccepted() throws JMSException
    {
        // Empty code.
    }


    /**
     * 
     */
    public void challangeRejected()
    {
        // Empty code.

    }


    /**
     * 
     */
    public void playerQuit()
    {
        // Empty code.

    }


    /**
     * If the player is the setter, this method is called everytime the guesser
     * sends a letter.
     */
    public void playerGuessed( String guess )
    {
        char c = guess.charAt( 0 );
        guesses.add( c );
        if ( getWord().indexOf( c ) == -1 )
        {
            strikes++;
        }
    }

}