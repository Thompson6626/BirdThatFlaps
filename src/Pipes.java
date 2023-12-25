import java.awt.*;

public class Pipes extends Rectangle {

    private static final int SPEED=-3; // - left | + right

    Pipes(int x, int y, int width, int height){
        super(x,y,width,height);
    }

    public void move(){
        x+=SPEED;
    }

    public void draw(Graphics g){
        g.setColor(new Color(32, 181, 40));
        g.fillRect(x,y,width,height);
    }

}
