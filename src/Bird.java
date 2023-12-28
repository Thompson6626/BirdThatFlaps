import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Bird extends Rectangle {

    private static final int FLYING_SPEED = -8;
    private static final float FLOATING_SPEED = 0.09f;
    private static final int MAX_FALLING_VELOCITY = 10;
    private float fallingVelocity = 0f;
    private String BIRD_STATUS = "falling";
    private static final File BIRD_IMAGE = new File("flappybird.png");
    private static  BufferedImage BIRD ;
    private double rotationAngle = 0.0;
    private final double MAX_UP_DEGREES = Math.toRadians(-25);
    private final double  MAX_DOWN_DEGREES = Math.toRadians(65);

    static{
        try{
            BIRD = ImageIO.read(BIRD_IMAGE);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    Bird(int x,int y,int width,int height){
        super(x,y,width,height);
    }

    public void setFallingVelocity(float fallingVelocity) {
        this.fallingVelocity = fallingVelocity;
    }

    public void setRotationAngle(double rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    public void setStatus(String status){
        BIRD_STATUS = status;
    }
    public String getStatus(){
        return BIRD_STATUS;
    }

    public void fall() {
        fallingVelocity += 0.2f;
        if (fallingVelocity > MAX_FALLING_VELOCITY) {
            fallingVelocity = MAX_FALLING_VELOCITY;
        }
        y += fallingVelocity;

        if(rotationAngle > MAX_DOWN_DEGREES){
            rotationAngle =  MAX_DOWN_DEGREES;
        }
        rotationAngle += Math.toRadians(3);
    }

    public void fly() {
        y += FLYING_SPEED;
        fallingVelocity = 0f; // Reset the falling velocity
        rotationAngle = MAX_UP_DEGREES;
    }

    public void floatingFalling() {
        y += FLOATING_SPEED;
    }
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        // Translate and rotate the graphics context around the bird's center coordinates
        g2d.translate(x + width / 2, y + height / 2);
        g2d.rotate(rotationAngle);

        // Draw the bird
        g2d.drawImage(BIRD, -width / 2, -height / 2, width, height, null);

    }
}
