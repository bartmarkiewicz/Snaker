import javax.swing.*;
import java.awt.*;

public class MainMenu extends JPanel  {
    //this class represents the JPanel for the main menu
    //it is only the physical look of the main menu, the actual functionality of the buttons is added elsewhere
    public JButton startBt = new JButton();
    public JButton exitBt = new JButton();
    private JPanel topPanel = new JPanel();
    private JPanel bottomPanel = new JPanel();
    private JLabel gameLabel = new JLabel("Snaker", SwingConstants.CENTER);


    public MainMenu(){
        // this constructor defines the layout of the different components of the main menu and makes it all look appropriately
        setLayout(new GridLayout(2,1));
        setMinimumSize(new Dimension(700, 700));

        add(topPanel);
        add(bottomPanel);

        topPanel.setLayout(new GridLayout(1,1));
        topPanel.add(gameLabel);
        gameLabel.setFont(new Font("Helvetica", Font.ITALIC, 144));
        gameLabel.setForeground(Color.ORANGE);

        startBt.setText("Play");
        startBt.setFont(new Font("Helvetica", Font.BOLD, 22));
        startBt.setBackground(Color.ORANGE);


        exitBt.setText("Quit Game");
        exitBt.setFont(new Font("Helvetica", Font.BOLD, 22));
        exitBt.setBackground(Color.ORANGE);



        bottomPanel.setLayout(new GridLayout(2,3, 5,10));
        bottomPanel.add(new JPanel());
        bottomPanel.add(startBt);
        bottomPanel.add(new JPanel());

        bottomPanel.add(new JPanel());
        bottomPanel.add(exitBt);

        JPanel rightCorner = new JPanel();
        rightCorner.setLayout(new BorderLayout());
        JLabel byMeLabel = new JLabel("By Bartosz Markiewicz", SwingConstants.RIGHT);
        byMeLabel.setFont(new Font("Helvetica", Font.ITALIC, 10));
        rightCorner.add(byMeLabel, BorderLayout.PAGE_END);
        bottomPanel.add(rightCorner);
    }
}
