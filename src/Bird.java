import java.awt.*;
import java.awt.event.KeyEvent;

public class Bird extends Rectangle {

    private static final int FALLING_SPEED=4;
    private static final int FLYING_SPEED=-4;
    private boolean falling=true;
    private static final Color BIRD_COLOR=new Color(206, 128, 18);

    Bird(int x,int y,int width,int height){
        super(x,y,width,height);
    }

    public void fall(){

        y+=FALLING_SPEED;

    }

    public void fly(){
        y+=FLYING_SPEED;
    }

    public void keepFalling(){
        this.falling=true;
    }
    public boolean isFalling(){
        return falling;
    }

    public void keyPressed(KeyEvent e){
        if(e.getKeyCode()==KeyEvent.VK_SPACE){
            falling=false;
        }
    }

    public void draw(Graphics g){
        g.setColor(BIRD_COLOR);
        g.fillOval(x,y,width,height);
    }
}