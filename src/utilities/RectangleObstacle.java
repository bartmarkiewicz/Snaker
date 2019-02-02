package utilities;

public class RectangleObstacle extends Shape {
    // an object of this class represents an obstacle in the snake game in the form of a rectangle
    // it extends the abstract Shape class and overrides its methods.

    private int width, height;

    public RectangleObstacle(int posX, int posY, int width, int height){
        // this constructor sets up the obstacle using the values provided
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        // returns the width of the rectangle
        return width;
    }

    public int getHeight() {
        // returns the height of the rectangle
        return height;
    }

    @Override
    public int getPosX() {
        // returns the  X coordinate of the rectangle on the board
        return posX;
    }

    @Override
    public int getPosY() {
        // returns the  Y coordinate of the rectangle on the board
        return posY;
    }
}
