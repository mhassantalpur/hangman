package HangmanDude;

import java.util.Scanner;


/**
 * The class containing the main method to run the program.
 * 
 * @author Team 17: Daniel Kao, Mir Ali Talpur, Saurabh Sharma, Rachel
 *         Schneiderman, Martin Vanek, Jeffrey Syang
 * 
 */
public class HangmanDude
{
    public static final String DICTIONARY = "words.txt";


    @SuppressWarnings("resource")
    public static void main( String[] args ) throws Exception
    {
        int numberOfLines;
        Scanner scan = new Scanner( System.in );

        do
        {
            System.out.println( "Would you like to play Hangman via" );
            System.out.println( "1) Console" );
            System.out.println( "2) GUI" );
            System.out.print( "Please choose 1 or 2 : " );
            numberOfLines = Integer.parseInt( scan.nextLine() );
        } while ( numberOfLines != 1 && numberOfLines != 2 );

        if ( numberOfLines == 1 )
        {
            System.out.println( "\n\n" );
            new HangmanDudePlainView( DICTIONARY );
        }
        else
            new HangmanDudeGUIView( DICTIONARY );
    }
}