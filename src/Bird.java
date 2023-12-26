import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Bird extends Rectangle {

    private static final int FLYING_SPEED = -8;
    private static final int FLOATING_SPEED = 0;
    private static final int MAX_FALLING_VELOCITY = 9;
    private float fallingVelocity = 0f;

    private String BIRD_STATUS="falling";
    private static final File BIRD_IMAGE=new File("flappybird.png");

    Bird(int x,int y,int width,int height){
        super(x,y,width,height);
    }

    public void fall() {
        fallingVelocity += 0.2f;
        if (fallingVelocity > MAX_FALLING_VELOCITY) {
            fallingVelocity = MAX_FALLING_VELOCITY;
        }
        y += fallingVelocity;
    }

    public void fly(){
        y+=FLYING_SPEED;
        fallingVelocity=0f; // Reseting the falling velocity
    }
    public void floatt(){
        y+=FLOATING_SPEED;
    }

    public void setStatus(String status){
        BIRD_STATUS = status;
    }
    public String getStatus(){
        return BIRD_STATUS;
    }

    public void keyPressed(KeyEvent e){
        if(e.getKeyCode()==KeyEvent.VK_SPACE){
            BIRD_STATUS="flying";
        }
    }

    public void draw(Graphics g){
        BufferedImage birdImage= null;

        try {
            birdImage = ImageIO.read(BIRD_IMAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        g.drawImage(birdImage,x,y,width,height,null);
    }
}
