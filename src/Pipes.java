import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Pipes extends Rectangle {

    private static final int SPEED=-3; // - left | + right
    private static final File UPPER_PIPE = new File("toppipe.png");
    private static final File BOTTOM_PIPE= new File("bottompipe.png");

    private static BufferedImage upperPipe;
    private static BufferedImage bottomPipe;

    static {
        try {
            upperPipe = ImageIO.read(UPPER_PIPE);
            bottomPipe = ImageIO.read(BOTTOM_PIPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    Pipes(int x, int y, int width, int height){
        super(x,y,width,height);
    }

    public void move(){
        x+=SPEED;
    }

    public void draw(Graphics g,boolean upper){
        BufferedImage pipe = upper ? upperPipe : bottomPipe;

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g2d.drawImage(pipe, x, y, width, height, null);

    }

}
