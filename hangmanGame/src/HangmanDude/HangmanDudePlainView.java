package HangmanDude;

import java.util.Scanner;



/**
 * 
 * @author Team 17
 * 
 */
public class HangmanDudePlainView extends HangmanDudeGameModel
{
    private Scanner scan;


    /**
     * 
     * @param wordsFile
     * @throws Exception
     */
    public HangmanDudePlainView( String wordsFile ) throws Exception
    {
        super( wordsFile );
        scan = new Scanner( System.in );

        initGame();
    }


    /**
     * 
     * @throws Exception
     */
    private void initGame() throws Exception
    {
        System.out.println( "WELCOME TO HANGMANDUDE \n" );
        while ( getPlayerType() == 0 )
        {
            System.out.print( "Type \"set\" to set the word, \n"
                + "or type \"guess\" to guess a word: " );
            String s = scan.next().toLowerCase();
            if ( s.equals( "set" ) )
            {
                setPlayerType(1);
                scan.nextLine();

                while ( this.word == null )
                {
                    System.out.print( "\nPick a Word: " );
                    String word = scan.nextLine();
                    if ( !setWord( word ) )
                    {
                        System.out.println( "Invalid Word." );
                    }
                }
                pushWordToServer();
            }
            else if ( s.equals( "guess" ) )
            {
                setPlayerType(2);
                getWordFromServer();
            }
            else
                System.out.println( "Invalid Choice. \n" );
        }

        playGame();

    }


    /**
     * 
     */
    public void playGame() throws Exception
    {
        int last = 0;
        ;
        while ( !isGameOver() )
        {
            if ( guesses.size() != last || getPlayerType() == 2 )
            {
                drawHangman();
                if ( getPlayerType() == 2 )
                {
                    System.out.print( "Guess next Letter: " );
                    String s = scan.next().toLowerCase();
                    guessLetter( s );
                }
                last = guesses.size();
            }
        }

        drawHangman();
    }


    /**
     * 
     */
    public void drawHangman()
    {
        // Draws the hangman in plain text.
        System.out.println();
        System.out.println();
        System.out.println( "\t----------------" );
        System.out.println( "\t|              |" );
        System.out.print( "\t|" );
        if ( strikes >= 4 )
        {
            System.out.print( "             \\O/" );
        }
        else if ( strikes == 3 )
        {
            System.out.print( "             \\O" );
        }
        else if ( strikes >= 1 )
        {
            System.out.print( "              O" );
        }
        System.out.println();
        System.out.print( "\t|" );
        if ( strikes >= 2 )
        {
            System.out.print( "              |" );
        }
        System.out.println();
        System.out.print( "\t|" );
        if ( strikes >= 6 )
        {
            System.out.print( "             / \\" );
        }
        else if ( strikes == 5 )
        {
            System.out.print( "             /  " );
        }
        System.out.println();
        System.out.println( "\t|" );
        System.out.print( "\t|  " );
        for ( char c : this.guesses )
        {
            if ( getWord().indexOf( c ) == -1 )
                System.out.print( c + " " );
        }
        System.out.println();
        System.out.println( "   ---------------------------" );
        System.out.println();

        // Draws the letter blanks by looping over each letter in word.
        System.out.print( "\t" );
        for ( int i = 0; i < getWord().length(); i++ )
        {
            char c = getWord().charAt( i );
            if ( guesses.contains( c ) )
            {
                System.out.print( c + " " );
            }
            else
            {
                System.out.print( "_ " );
            }
        }
        System.out.println();
        System.out.println();

        // Draws the ending if game is over.
        if ( isGameOver() )
        {
            if ( getPlayerType() == 2 && areAllLettersGuessed() )
            {
                System.out.println( "YOU WIN" );
            }
            else if ( getPlayerType() == 1 && areAllLettersGuessed() )
            {
                System.out.println( "YOUR OPPONENT HAS SUCCEEDED." );
            }
            else if ( !areAllLettersGuessed() && getPlayerType() == 2 )
            {
                System.out.println();
                System.out.println( " .-\"\"-." );
                System.out.println( "/ _  _ \\" );
                System.out.println( "|(_)(_)|" );
                System.out.println( "(_ /\\ _)" );
                System.out.println( " L====J" );
                System.out.println( " '-..-'" );
                System.out.println( "YOU LOSE" );
                System.out.println( "THE WORD WAS " + getWord().toUpperCase() );
            }
            else
            {
                System.out.println( "YOU HAVE SUCCESSFULLY HUNG YOUR OPPONENT." );
            }
            System.exit( 0 );
        }
    }
}
