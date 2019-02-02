import utilities.Food;
import utilities.HighScores;
import utilities.RectangleObstacle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;

public class GameView extends JPanel implements MouseListener {
    //an object of this class represents the JPanel on which gameplay is displayed.
    //this class implements the mouselistener interface to allow for detection of mouse click events to restart the game after a game over

    private int MAP_SIZE;
    private boolean gameOver = false;

    private Random rand = new Random();

    private int currentPieceCount = 5; // 3 being starting amount of the squares which make up the snake;
    private int snakePieceSize = 25; // 25 by 25 pixel size of each piece of the snake

    private final int GAME_SPEED = 200; // speed at which the snake moves(lower is faster)
    private final int OBSTACLE_COUNT = 3; // the number of rectangle obstacles(higher makes the game harder)

    private ArrayList<RectangleObstacle> obstacles; // this collection stores the RectangleObstacle shape objects

    private ArrayList<Integer> snakePositionsX;
    private ArrayList<Integer> snakePositionsY; // these lists store the position of every piece(square) of the snake

    private Food apple = new Food();
    private int playerScore = 0;
    private int speedSetting;

    private Timer timer;
    private TimerTask task;
    private HighScores scores = new HighScores();

    enum Direction {
        RIGHT, LEFT, UP, DOWN
    }
    // this enum stores the direction in which the snake is currently travelling in
    private Direction currentDirection = Direction.RIGHT;

    public GameView(int MAP_SIZE){
        // constructor for the object
        setBackground(Color.BLACK);
        setFocusable(true);
        this.MAP_SIZE = MAP_SIZE;
        addKeyListener(new KeyEventHandler());
        addMouseListener(this);
        initGame();
        scores.loadScoresFromFile();
    }

    private void initGame(){
        // this method initialises the game
        // it places all the objects on the game board - the apple and obstacles randomly while the snake in the centre of the board.
        // it checks if an apple or snake is placed on top of an obstacle (if it is the obstacle or apple is moved)
        // also begins the game loop in the form of the game clock
        repaint();
        playerScore = 0;
        speedSetting = 1;
        currentPieceCount = 5;
        gameOver = false;
        currentDirection = Direction.RIGHT;
        snakePositionsX = new ArrayList<>();
        snakePositionsY = new ArrayList<>();
        obstacles = new ArrayList<>();
        for (int i = 0; i < currentPieceCount; i++) {
            if(i == 0) {
                snakePositionsX.add(0, MAP_SIZE / 2);
                snakePositionsY.add(0, MAP_SIZE / 2);
            } else if(i==1) {
                snakePositionsX.add(1, (MAP_SIZE / 2) - snakePieceSize);
                snakePositionsY.add(1, MAP_SIZE / 2);
            } else {
                snakePositionsX.add(i, (MAP_SIZE / 2) - snakePieceSize * i);
                snakePositionsY.add(i, MAP_SIZE / 2);
            }
        }
        apple.setPosX(25+rand.nextInt(600/25)*25);
        apple.setPosY(25+rand.nextInt(600/25)*25);

        int height, width;
        for (int i = 0; i < OBSTACLE_COUNT; i++){// this loop adds obstacles to the collection which are later rendered onto the game board
            height = 25+rand.nextInt(150/25)*25;
            width = 25+rand.nextInt(150/25)*25;
            RectangleObstacle obstacle = new RectangleObstacle((rand.nextInt(500/25)*25), (rand.nextInt(550/25)*25), width, height);
            obstacles.add(obstacle);
        }

        for (int i = 0; i < OBSTACLE_COUNT; i++) { //loops through all obstacles
            for (int j = 0; j < snakePositionsX.size(); j++) { //loops through all snake positions
                int currentLeftEdgeX = snakePositionsX.get(j);
                int currentRightEdgeX = snakePositionsX.get(j) + snakePieceSize - 1;
                int currentTopEdgeY = snakePositionsY.get(j);
                int currentBottomEdgeY = snakePositionsY.get(j) + snakePieceSize - 1;
                int obstacleLeftEdgeX = obstacles.get(i).getPosX();
                int obstacleRightEdgeX = obstacles.get(i).getPosX() + obstacles.get(i).getWidth() - 1;
                int obstacleTopEdgeY = obstacles.get(i).getPosY();
                int obstacleBottomEdgeY = obstacles.get(i).getPosY() + obstacles.get(i).getHeight() - 1;
                int currentLeftEdgeApple = apple.getPosX();
                int currentRightEdgeApple = apple.getPosX() + snakePieceSize -1;
                int currentTopEdgeApple = apple.getPosY();
                int currentBottomEdgeApple = apple.getPosY() + snakePieceSize -1;


                while ((currentLeftEdgeApple >= obstacleLeftEdgeX) && (currentRightEdgeApple <= obstacleRightEdgeX) && (currentTopEdgeApple >= obstacleTopEdgeY) && (currentBottomEdgeApple <= obstacleBottomEdgeY)) {
                    //checking if apple spawned on an obstacle
                    apple.setPosX(25+rand.nextInt(600/25)*25);
                    apple.setPosY(25+rand.nextInt(600/25)*25);
                    currentLeftEdgeApple = apple.getPosX();
                    currentRightEdgeApple = apple.getPosX() + snakePieceSize -1;
                    currentTopEdgeApple = apple.getPosY();
                    currentBottomEdgeApple = apple.getPosY() + snakePieceSize -1;
                }
                while ((obstacleLeftEdgeX <= currentLeftEdgeX) && (obstacleRightEdgeX >= currentRightEdgeX) && (obstacleTopEdgeY <= currentTopEdgeY) && (obstacleBottomEdgeY >= currentBottomEdgeY)) { // obstacle checking
                    //checking if player spawned on an obstacle
                    System.out.println("Player spawned on obstacle!!");
                    obstacles.remove(i);
                    height = 25+rand.nextInt(175/25)*25;
                    width = 25+rand.nextInt(175/25)*25;
                    obstacles.add(i, new RectangleObstacle((rand.nextInt(500/25)*25), (rand.nextInt(550/25)*25), width, height));
                    obstacleLeftEdgeX = obstacles.get(i).getPosX();
                    obstacleRightEdgeX = obstacles.get(i).getPosX() + obstacles.get(i).getWidth() - 1;
                    obstacleTopEdgeY = obstacles.get(i).getPosY();
                    obstacleBottomEdgeY = obstacles.get(i).getPosY() + obstacles.get(i).getHeight() - 1;
                }
            }

        }


        checkCollisions();
        gameClock(GAME_SPEED/speedSetting);

    }

    private void gameClock(int clockSpeed){
        // this is the 'game loop' method which contains the timer which runs the entire time the game is in progress.
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                if (!gameOver){
                    // while the game is not over the snake moves and collisions are checked after the move
                    // then the board is repainted
                    move();
                    checkCollisions();
                    repaint();
                }
                if (gameOver){
                    // if the game is over the highscore is saved and the timer is turned off
                    task.cancel();
                    if (playerScore>0) {
                        scores.addHighScore(playerScore);
                    }
                    scores.saveScoresToFile();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, clockSpeed);
    }

    private void checkCollisions() {
        // this method checks the collisions between all the game objects
        int headXPosLeftEdge = snakePositionsX.get(0);// left edge of snake head
        int headXPosRightEdge = snakePositionsX.get(0) + snakePieceSize - 1; // right edge of snake head
        int headYPosTopEdge = snakePositionsY.get(0);// top edge of snake head
        int headYPosBottomEdge = snakePositionsY.get(0) + snakePieceSize - 1; //bottom edge of snake head
        int currentLeftEdgeX; //left edge of snake body piece
        int currentRightEdgeX; //right edge of snake body
        int currentTopEdgeY; // top edge of snake body
        int currentBottomEdgeY;//bottom edge of snake body


        for (int i = 1; i < snakePositionsX.size(); i++) { // this checks for collision between the head and the body of the snake
            currentLeftEdgeX = snakePositionsX.get(i);
            currentRightEdgeX = snakePositionsX.get(i) + snakePieceSize - 1;
            currentTopEdgeY = snakePositionsY.get(i);
            currentBottomEdgeY = snakePositionsY.get(i) + snakePieceSize - 1;
            if ((headXPosLeftEdge == currentLeftEdgeX) && (headXPosRightEdge == currentRightEdgeX) && (headYPosTopEdge == currentTopEdgeY) && (headYPosBottomEdge == currentBottomEdgeY)) {
                //head collided with body which causes a game over
                gameOver = true;
            }
        }

        if ((apple.getPosX() >= headXPosLeftEdge) && (apple.getPosX() <= headXPosRightEdge) && (apple.getPosY() >= headYPosTopEdge) && (apple.getPosY() <= headYPosBottomEdge)) {
            // checks for snake head colliding with apple
            int priorSize = currentPieceCount;
            currentPieceCount += 3; // makes the snake bigger by 3 squares.
            playerScore += speedSetting;//increases player score based on the speed setting (higher speed = more points)
            for (int i = 0; i < 3; i++) {
                snakePositionsX.add(snakePositionsX.get(priorSize - 1 + i));
                snakePositionsY.add(snakePositionsY.get(priorSize - 1 + i));
            }
            apple.setPosX(25+(rand.nextInt(600 / 25) * 25));
            apple.setPosY(25+(rand.nextInt(600 / 25) * 25));// relocates the apple to a new location
        }


        //below checks the collisions of the head of the snake with edges of the screen and obstacles also checks if apples spawned on an obstacle-
        for (int i = 0; i < OBSTACLE_COUNT; i++) {
            currentLeftEdgeX = snakePositionsX.get(0);//these are the snake head positions
            currentRightEdgeX = snakePositionsX.get(0) + snakePieceSize - 1;
            currentTopEdgeY = snakePositionsY.get(0);
            currentBottomEdgeY = snakePositionsY.get(0) + snakePieceSize - 1;
            int obstacleLeftEdgeX = obstacles.get(i).getPosX();
            int obstacleRightEdgeX = obstacles.get(i).getPosX() + obstacles.get(i).getWidth()-1;
            int obstacleTopEdgeY = obstacles.get(i).getPosY();
            int obstacleBottomEdgeY = obstacles.get(i).getPosY() + obstacles.get(i).getHeight()-1;


            int currentLeftEdgeApple = apple.getPosX();
            int currentRightEdgeApple = apple.getPosX() + snakePieceSize -1;
            int currentTopEdgeApple = apple.getPosY();
            int currentBottomEdgeApple = apple.getPosY() + snakePieceSize -1;

            while ((currentLeftEdgeApple >= obstacleLeftEdgeX) && (currentRightEdgeApple <= obstacleRightEdgeX) && (currentTopEdgeApple >= obstacleTopEdgeY) && (currentBottomEdgeApple <= obstacleBottomEdgeY)) {
                //checking if apple spawned on top of an obstacle and if it did, relocates the apple, keeps looping until the apple is not on top of an obstacle
                apple.setPosX(rand.nextInt(625/25)*25);
                apple.setPosY(rand.nextInt(625/25)*25);
                currentLeftEdgeApple = apple.getPosX();
                currentRightEdgeApple = apple.getPosX() + snakePieceSize -1;
                currentTopEdgeApple = apple.getPosY();
                currentBottomEdgeApple = apple.getPosY() + snakePieceSize -1;
            }

            if((currentLeftEdgeApple <= 25) || (currentRightEdgeApple >= 675) || (currentTopEdgeApple <= 25) || (currentBottomEdgeApple >= 675)){
                // checks if apple spawned on top of the edge of the board
                apple.setPosX(rand.nextInt(625/25)*25);
                apple.setPosY(rand.nextInt(625/25)*25);
            }

            if ((currentLeftEdgeX < 25) || (currentRightEdgeX >= 675) || (currentTopEdgeY < 25) || (currentBottomEdgeY >= 650)) { // edge checking
                //checks if the snake collided with the edge of the board
                gameOver = true;
            }
            if ((obstacleLeftEdgeX <= currentLeftEdgeX) && (obstacleRightEdgeX >= currentRightEdgeX) && (obstacleTopEdgeY <= currentTopEdgeY) && (obstacleBottomEdgeY >= currentBottomEdgeY)) { // obstacle checking
                //checks if the snake collided with an obstacle
                gameOver = true;
            }
        }
    }


    private void move(){
        // this method actually makes the snake move
        for (int i = 0; i < currentPieceCount; i++) {
            // it loops through every piece of the snake

            if (i == 0) { // this adds the new positions of the snake to the arrayList based on the current direction of the snake
                int tempX = snakePositionsX.get(i);
                int tempY = snakePositionsY.get(i);

                if (currentDirection.equals(Direction.RIGHT)) {
                    snakePositionsX.add(i, (tempX + snakePieceSize));
                    snakePositionsY.add(i, tempY);
                } else if (currentDirection.equals(Direction.LEFT)) {
                    snakePositionsX.add(i, (tempX - snakePieceSize));
                    snakePositionsY.add(i, tempY);
                } else if (currentDirection.equals(Direction.DOWN)) {
                    snakePositionsY.add(i, (tempY + snakePieceSize));
                    snakePositionsX.add(i, tempX);
                } else if (currentDirection.equals(Direction.UP)) {
                    snakePositionsY.add(i, (tempY - snakePieceSize));
                    snakePositionsX.add(i, tempX);
                }
                snakePositionsX.remove(snakePositionsX.size()-1);// this removes the last piece of the snake
                snakePositionsY.remove(snakePositionsY.size()-1);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        // this is the method used for rendering the graphics to the screen
        super.paintComponent(g);

        for (int i = 0; i < currentPieceCount; i++) {
            //this loop displays a green snake with an orange 'head'
            if (i==0){
                g.setColor(Color.ORANGE);
                g.fillRect(snakePositionsX.get(i), snakePositionsY.get(i), snakePieceSize, snakePieceSize);

            } else {
                g.setColor(Color.GREEN);
                g.fillRect(snakePositionsX.get(i), snakePositionsY.get(i), snakePieceSize, snakePieceSize);
            }
        }
        RectangleObstacle currentObstacle;

        for (int j = 0; j < OBSTACLE_COUNT; j++){
            // this method loops through all obstacles and displays them onto the visible board as gray rectangles.
            currentObstacle = obstacles.get(j);
            g.setColor(Color.GRAY);
            g.fillRect(currentObstacle.getPosX(), currentObstacle.getPosY(), currentObstacle.getWidth(), currentObstacle.getHeight());
        }

        //this renders the apple as a red oval
        g.setColor(Color.RED);
        g.fillOval(apple.getPosX(),apple.getPosY(),apple.getSize(),apple.getSize());

        //printing the borders of the board
        g.setColor(Color.blue);
        g.fillRect(0,0,25,MAP_SIZE);
        g.fillRect(MAP_SIZE-snakePieceSize,0,25,MAP_SIZE);
        g.fillRect(0,0, MAP_SIZE,25);
        g.fillRect(0,MAP_SIZE-(snakePieceSize*2), MAP_SIZE,25);


        g.setColor(Color.RED);
        if (gameOver){
            // if game over the top 10 highest scores are rendered
            g.setFont(new Font("Helvetica", Font.ITALIC, 33));
            g.drawString("High Scores -", 50,40);
            ArrayList<Integer> topTen = scores.returnList();
            Collections.sort(topTen, Collections.reverseOrder());
            int scoreYPos = 75;
            for (int i = 0; (i < topTen.size()) && i < 10; i++) {
                    int score = topTen.get(i);
                    g.drawString(String.format("%d. %d", i + 1, score), 50, scoreYPos);
                    scoreYPos+=35;
            }
            //head of the snake is turned red to show the snake's 'death' upon game over
            g.fillRect(snakePositionsX.get(0), snakePositionsY.get(0), snakePieceSize,snakePieceSize);
            g.setFont(new Font("Helvetica", Font.ITALIC, 30));
            g.drawString("Game Over",MAP_SIZE/2, 400);
            g.drawString(String.format("%d Score Gained", playerScore),MAP_SIZE/2, 600); //amount of score gained is displayed
            g.drawString("Click to play again.",MAP_SIZE/2, 500);
        } else if (!gameOver){
            // when the game is in progress the current score is displayed near the top of the screen.
            g.setFont(new Font ("Helvetica", Font.ITALIC, 30));
            g.drawString(String.format("Score: %d", playerScore), MAP_SIZE/2, 40);
        }

        g.setColor(Color.GRAY);
        g.setFont(new Font("Helvetica", Font.ITALIC, 14));
        g.drawString("By Bartosz Markiewicz", 375, 665); // my name displayed at the bottom of the screen
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        e.consume();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // this method is used to detect a mouse event to restart the game after a game over.
        if (gameOver){
            task.cancel();
            initGame();
        }
        e.consume();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        e.consume();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        e.consume();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        e.consume();
    }

    public class KeyEventHandler extends KeyAdapter {
        //This is the key event handler class to allow the user to control the snake using their keyboard.
        @Override
        public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
            e.consume();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            // this method specifies which button does what.

                int key = e.getKeyCode();

                // arrow keys change direction of snake travel but direction can't be opposite of which it was already travelling in
                if ((key == KeyEvent.VK_LEFT) && (!currentDirection.equals(Direction.RIGHT))) {
                    currentDirection = Direction.LEFT;
                }

                if ((key == KeyEvent.VK_RIGHT) && (!currentDirection.equals(Direction.LEFT))) {
                    currentDirection = Direction.RIGHT;
                }

                if ((key == KeyEvent.VK_UP) && (!currentDirection.equals(Direction.DOWN))) {
                    currentDirection = Direction.UP;
                }

                if ((key == KeyEvent.VK_DOWN) && (!currentDirection.equals(Direction.UP))) {
                    currentDirection = Direction.DOWN;
                }

                if(!gameOver && key == KeyEvent.VK_S){ // the 'S' key speeds up the snake (allowing the user to gain more points for each apple eaten)
                    task.cancel();
                    if (speedSetting < 5){
                        speedSetting++;// '5' being the max speed setting of the snake
                        System.out.println("Speed setting - " + speedSetting);
                    } else { // once you reach speed 5 and press 'S' again the speed reverts to the slowest setting ie - 1
                        speedSetting = 1;
                    }
                    gameClock(GAME_SPEED/speedSetting);
                }
            e.consume();

        }

        @Override
        public void keyReleased(KeyEvent e) {
            super.keyReleased(e);
            e.consume();
        }
    }
}
