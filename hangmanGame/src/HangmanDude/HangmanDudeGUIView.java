package HangmanDude;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.jms.JMSException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;


/**
 * GUI that extends hangmandudegamemodel and implements a gui representation of
 * the game.
 * 
 * @author Team 17
 */
public class HangmanDudeGUIView extends HangmanDudeGameModel
{

    // Variables declaration

    private javax.swing.JToggleButton[] letterButtons = new javax.swing.JToggleButton[26];

    private javax.swing.JLabel[] images = new javax.swing.JLabel[8];

    private javax.swing.JButton EXIT;

    private javax.swing.JToggleButton START;

    private javax.swing.JToggleButton SET;

    private JLabel AREA;

    javax.swing.JLabel waitingLabel;

    private javax.swing.JPanel jPanel2;


    // End of variables declaration

    /**
     * Creates new form GUI
     */
    public HangmanDudeGUIView( String wordsFile )
        throws JMSException,
        InterruptedException,
        URISyntaxException,
        IOException
    {
        super( wordsFile );

        playGame();
    }


    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void initComponents()
    {
        JFrame frame = new javax.swing.JFrame();
        jPanel2 = new javax.swing.JPanel();
        START = new javax.swing.JToggleButton();
        SET = new javax.swing.JToggleButton();
        EXIT = new javax.swing.JButton();

        // Initialize the 8 images for the hangman.
        for ( int x = 0; x < 8; x++ )
        {
            images[x] = new javax.swing.JLabel();
        }

        // Initialize the 26 letter buttons.
        for ( int x = 0; x < 26; x++ )
        {
            letterButtons[x] = new javax.swing.JToggleButton();
        }

        frame.setDefaultCloseOperation( javax.swing.WindowConstants.EXIT_ON_CLOSE );

        jPanel2.setLayout( null );

        waitingLabel = new javax.swing.JLabel();
        waitingLabel.setText( "WAITING FOR OPPONENT..." );
        waitingLabel.setFont( new java.awt.Font( "Traditional Arabic", 1, 14 ) ); // NOI18N
        waitingLabel.setForeground( Color.white );
        jPanel2.add( waitingLabel );
        waitingLabel.setBounds( 70, 10, 200, 30 );
        waitingLabel.setVisible( false );

        START.setFont( new java.awt.Font( "Traditional Arabic", 1, 14 ) ); // NOI18N
        START.setText( "GUESS" );
        START.setBackground( new Color( 89, 69, 42 ) );
        START.setForeground( Color.WHITE );
        START.addActionListener( new java.awt.event.ActionListener()
        {
            public void actionPerformed( java.awt.event.ActionEvent evt )
            {

                START.setText( "WAIT..." );
                AREA.setVisible( true );
                new Thread( new Runnable()
                {

                    public void run()
                    {
                        try
                        {
                            setPlayerType( 2 );
                            getWordFromServer();

                            START.setVisible( false );
                            SET.setVisible( false );
                            EXIT.setVisible( false );
                            String str = "";
                            for ( int i = 0; i < word.length(); i++ )
                                str += "_ ";
                            AREA.setText( str );
                            AREA.setVisible( true );
                            images[0].setVisible( false );
                        }
                        catch ( Exception e )
                        {
                            e.printStackTrace();
                        }

                    }
                } ).start();

            }
        } );
        jPanel2.add( START );
        START.setBounds( 110, 200, 90, 31 );

        SET.setFont( new java.awt.Font( "Traditional Arabic", 1, 14 ) ); // NOI18N
        SET.setText( "SET" );
        SET.setBackground( new Color( 89, 69, 42 ) );
        SET.setForeground( Color.WHITE );
        SET.addActionListener( new java.awt.event.ActionListener()
        {
            public void actionPerformed( java.awt.event.ActionEvent evt )
            {
                try
                {
                    setPlayerType( 1 );
                    String s = "";
                    while ( !setWord( s ) )
                    {
                        s = JOptionPane.showInputDialog( "Input Word for Your Opponent." );
                    }
                    pushWordToServer();
                    String str = "";
                    for ( int i = 0; i < word.length(); i++ )
                        str += "_ ";
                    AREA.setText( str );

                    AREA.setVisible( true );
                    START.setVisible( false );
                    SET.setVisible( false );
                    EXIT.setVisible( false );
                    images[0].setVisible( false );
                    // waitingLabel.setVisible( true );

                    for ( int x = 0; x < letterButtons.length; x++ )
                    {
                        letterButtons[x].setVisible( false );
                    }
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }
            }
        } );
        jPanel2.add( SET );
        SET.setBounds( 110, 260, 90, 31 );

        EXIT.setFont( new java.awt.Font( "Traditional Arabic", 1, 14 ) ); // NOI18N
        EXIT.setText( "EXIT" );
        EXIT.setBackground( new Color( 89, 69, 42 ) );
        EXIT.setForeground( Color.WHITE );
        EXIT.addActionListener( new java.awt.event.ActionListener()
        {
            public void actionPerformed( java.awt.event.ActionEvent evt )
            {
                System.exit( 1 );
            }
        } );
        jPanel2.add( EXIT );
        EXIT.setBounds( 110, 320, 90, 30 );

        AREA = new JLabel();

        // AREA.setBackground(new Color(Color.TRANSLUCENT));
        AREA.setForeground( Color.WHITE );
        AREA.setFont( new Font( Font.MONOSPACED, Font.BOLD, 18 ) );
        AREA.setHorizontalAlignment( SwingConstants.CENTER );
        jPanel2.add( AREA );
        AREA.setVisible( false );
        AREA.setBounds( 10, 200, 300, 100 );
        images[0].setIcon( new javax.swing.ImageIcon( "HangmanPics/background.png" ) ); // NOI18N
        jPanel2.add( images[0] );
        images[0].setBounds( 0, 0, 330, 510 );

        for ( int x = 0; x < 26; x++ )
        {
            letterButtons[x].setText( Character.toString( (char)( x + 65 ) ) );
            letterButtons[x].addActionListener( new java.awt.event.ActionListener()
            {
                public void actionPerformed( java.awt.event.ActionEvent evt )
                {
                    javax.swing.JToggleButton src = (javax.swing.JToggleButton)evt.getSource();
                    src.setVisible( false );
                    try
                    {
                        checkStrikes( src.getText().toLowerCase() );
                    }
                    catch ( Exception e )
                    {
                        e.printStackTrace();
                    }
                }
            } );
            jPanel2.add( letterButtons[x] );
            letterButtons[x].setBounds( ( x % 5 ) * 60 + 17,
                ( x / 5 ) * 30 + 317,
                50,
                20 );
        }

        for ( int x = 1; x <= 7; x++ )
        {
            images[x].setIcon( new javax.swing.ImageIcon( "HangmanPics/hangman"
                + x + ".jpg" ) );
            jPanel2.add( images[x] );
            images[x].setBounds( 0, 0, 330, 500 );
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout( frame.getContentPane() );
        frame.getContentPane().setLayout( layout );
        layout.setHorizontalGroup( layout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING )
            .addComponent( jPanel2,
                javax.swing.GroupLayout.PREFERRED_SIZE,
                324,
                javax.swing.GroupLayout.PREFERRED_SIZE ) );
        layout.setVerticalGroup( layout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING )
            .addComponent( jPanel2,
                javax.swing.GroupLayout.PREFERRED_SIZE,
                514,
                javax.swing.GroupLayout.PREFERRED_SIZE ) );

        frame.pack();

        // System.out.println( "test" );
        frame.setVisible( true );
    }


    /**
     * 
     */
    public void guessLetter( String guess ) throws Exception
    {

        if ( word.contains( guess ) )
        {
            int offset = 0;
            String subword = word;
            while ( subword.contains( guess ) )
            {
                String curr_val = AREA.getText();
                int index = subword.indexOf( guess );
                char[] arr = curr_val.toCharArray();
                arr[( index + offset ) * 2] = guess.charAt( 0 );
                AREA.setText( new String( arr ) );
                subword = subword.substring( index + 1 );
                offset += index + 1;
            }
        }
        images[strikes].setVisible( false );
    }


    /**
     * 
     * @param guess
     * @throws Exception
     */
    private void checkStrikes( String guess ) throws Exception
    {
        super.guessLetter( guess );
        guessLetter( guess );

        if ( isGameOver() )
        {
            if ( areAllLettersGuessed() )
            {
                JOptionPane.showMessageDialog( null, "You Won!" );
            }
            else
            {
                JOptionPane.showMessageDialog( null, "You Lost! The word was " +  word);
            }
            // GAME IS OVER. END
            System.exit( 0 );
        }
    }


    /**
     * 
     */
    @Override
    public void playGame()
    {
        initComponents();
    }


    /**
     * 
     */
    @Override
    public void challangeAccepted() throws JMSException
    {
        waitingLabel.setVisible( false );
    }


    /**
     * 
     */
    @Override
    public void playerGuessed( String guess )
    {
        super.playerGuessed( guess );
        try
        {
            guessLetter( guess );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        images[strikes].setVisible( false );
        if ( isGameOver() )
        {
            // GAME IS OVER. END
            if ( areAllLettersGuessed() )
            {
                JOptionPane.showMessageDialog( null, "Your opponent Won!" );
            }
            else
            {
                JOptionPane.showMessageDialog( null, "Your opponent Lost!" );
            }
            System.exit( 0 );
        }

    }
}
