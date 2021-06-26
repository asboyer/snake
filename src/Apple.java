import java.awt.*;

public class Apple {

    private int x, y, s;

    public Apple(int x, int y){
        this.x = x;
        this.y = y;
        s = 20;
    }

    public void draw(Graphics2D g2){
        g2.setColor(Color.RED);
        g2.fillRect(x, y, s, s);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getS() {
        return s;
    }

    public Rectangle getHitBox(){
        return new Rectangle(x, y, s, s);
    }
}
