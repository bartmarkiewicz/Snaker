package utilities;

public abstract class Shape {
    // this is the parent abstract class for the Food and Obstacle classes, it represents some sort of object on the board

    protected int size;
    protected int posX, posY;

    public abstract  int getPosX();
    public abstract int getPosY();
}
