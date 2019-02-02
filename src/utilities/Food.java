package utilities;

public class Food extends Shape {
    // this class represents the food object on the board the snake is meant to eat to get bigger and get score.
    // it extends the Shape abstract class

    public Food(){
        // constructor defines the default size of a food object as 25
        this.size = 25;
    }

    public void setPosX(int newPosX) {
        // this sets the position of the object on the X axis
        this.posX = newPosX;
    }

    public void setPosY(int newPosY) {
        // this sets the position of the object on the Y axis
        this.posY = newPosY;
    }

    public int getSize() {
        // this returns the size of the object
        return size;
    }

    @Override
    public int getPosX() {
        // this returns the position of the object on the X axis
        return posX;
    }

    @Override
    public int getPosY() {
        // this returns the position of the object on the Y axis
        return posY;
    }


}
