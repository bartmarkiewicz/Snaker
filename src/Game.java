import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game implements ActionListener{
    // this is the 'Main' class which creates the Frame and places swing objects on it
    // it implements the action listener to add functionality to the buttons in the main menu.
    private final int MAP_SIZE = 700; //700 by 700
    private MainMenu menu;
    private JFrame snaker;


    public Game(){
        // this constructor creates the Frame on which gameplay happens,
        // initially a menu is displayed
        snaker = new JFrame("Snaker by Bartosz Markiewicz");
        snaker.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        snaker.setSize(new Dimension(MAP_SIZE, MAP_SIZE));
        snaker.setVisible(true);
        snaker.setResizable(false);
        snaker.setFocusable(false);
        snaker.setLayout(new GridLayout(1,1));
        menu = new MainMenu();
        menu.startBt.addActionListener(this);
        menu.exitBt.addActionListener(this);
        snaker.add(menu);
    }

    public static void main(String[] args) {
        //an object of the Game type is instantiated in this main method
        new Game();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // this is the button handler method for the main menu buttons

        if (e.getSource().equals(menu.startBt)){
            //this button starts the game
            snaker.remove(menu);
            GameView game = new GameView(MAP_SIZE);

            snaker.setContentPane(game);
            snaker.revalidate();
            game.requestFocusInWindow();
        }

        if(e.getSource().equals(menu.exitBt)){
            // this button quits the application
            System.exit(0);
        }
    }
}


