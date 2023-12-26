import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;
import java.util.Queue;
public class GamePanel extends JPanel implements Runnable {

    final static int GAME_WIDTH=650;
    final static int GAME_HEIGHT=740;
    final static Dimension SCREEN_SIZE= new Dimension(GAME_WIDTH,GAME_HEIGHT);

    Random random;
    Thread gameThread;
    Timer timer;
    Image image;
    Graphics graphics;
    Bird bird;
    Score score;
    static Queue<Pipes> pipesQueue2= new LinkedList<>();
    static Queue<Timer> timers;
    static boolean gameOver=false;

    final static int BIRD_DIAMETER=35;
    final static int BIRD_DIAMETER2=45;
    final static int MINIMUM_PIPE_HEIGHT=100;
    final static int PIPE_WIDTH=80;
    final static int SPACE_BETWEEN_PIPES=200;
    final static int PIPE_SPAWN_TIME=2000;

    GamePanel(){
        score= new Score(GAME_WIDTH,GAME_HEIGHT);
        newBird();
        newPipes();


        this.setPreferredSize(SCREEN_SIZE);
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                bird.keyPressed(e);
                createTimerForFlying();
            }
        });




        timers= new LinkedList<>();

        gameThread=new Thread(this);
        gameThread.start();

        timer = new Timer(PIPE_SPAWN_TIME, e -> {
            newPipes();
            repaint();
        });
        timer.start();

        this.setFocusable(true);
    }
    public void newBird(){
        bird= new Bird(200,(GAME_HEIGHT/2)-BIRD_DIAMETER,BIRD_DIAMETER2,BIRD_DIAMETER);
    }


    public void newPipes(){  // Fix formula later
        random= new Random();

        int blankSpaceTop=(random.nextInt(
                GAME_HEIGHT-SPACE_BETWEEN_PIPES-MINIMUM_PIPE_HEIGHT)+MINIMUM_PIPE_HEIGHT);

        int bottomBlank=blankSpaceTop+SPACE_BETWEEN_PIPES;

        Pipes upperPipe =new Pipes(GAME_WIDTH, // UpperPIPE
                0,
                PIPE_WIDTH,
                blankSpaceTop);

        Pipes lowerPipe=new Pipes(GAME_WIDTH,
                bottomBlank,
                PIPE_WIDTH,
                GAME_HEIGHT-bottomBlank);

        pipesQueue2.offer(upperPipe);
        pipesQueue2.offer(lowerPipe);

    }

    public void paint(Graphics g){

        BufferedImage backgroundImage = null;
        try {
            backgroundImage = ImageIO.read(new File("flappybirdbg.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        image = createImage(getWidth(),getHeight());
        graphics = image.getGraphics();


        if (backgroundImage != null) {
            graphics.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        draw(graphics);

        g.drawImage(image,0,0,this);
    }

    public void draw(Graphics g){
        int i=1;
        for(Pipes pipe:pipesQueue2){
            if(i%2==0){
                pipe.draw(g,false);
            }else{
                pipe.draw(g,true);
            }
            i++;
        }
        bird.draw(g);
        score.draw(g,!gameOver);
    }
    public void move(){
        for(Pipes pipe:pipesQueue2){
            pipe.move();
        }

        switch(bird.getStatus()){
            case "flying" -> bird.fly();
            case "floating" -> bird.floatt();
        }

        if(!pipesQueue2.isEmpty()&& bird.x == pipesQueue2.peek().x) {
            Score.points++;
        }
    }

    public void createTimerForFlying() {
        int flyingTime = 250;
        int floatingTime = flyingTime + 70;

        Timer[] timerss= new Timer[2];
        Timer flyingTimer = new Timer(flyingTime, e -> bird.setStatus("floating"));
        Timer floatingTimer = new Timer(floatingTime, e -> bird.setStatus("falling"));

        timerss[0]=flyingTimer;
        timerss[1]=floatingTimer;

        timers.forEach(Timer::stop);
        timers.clear();

        for(Timer timer:timerss){
            timers.offer(timer);
            timer.setRepeats(false);
            timer.start();
        }
    }



    public void checkBirdCollision(){
        if(bird.y<=0){ // So that the bird doesnt go
            bird.y=0;
        }
        for(Pipes pipes:pipesQueue2){
            if(bird.intersects(pipes)){
                gameOver();
            }
        }

        if(bird.y>=GAME_HEIGHT-BIRD_DIAMETER){
            gameOver();
        }
    }
    public void gameOver(){
        gameOver=true;
        gameThread.interrupt();
    }

    public void checkPipeOutOfBounds(){
        if (!pipesQueue2.isEmpty() && pipesQueue2.peek().x <= -PIPE_WIDTH) {
            pipesQueue2.poll(); // Upper pipe
            pipesQueue2.poll();// Lower pipe
        }
    }


    @Override
    public void run(){

            long lastTime = System.nanoTime();
            double amountOfTicks = 60.0;
            double ns = 1000000000 / amountOfTicks;
            double delta = 0;

            while (!gameOver) {
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;
                if (delta >= 1) {
                    move();
                    checkBirdCollision();
                    checkPipeOutOfBounds();
                    repaint();
                    delta--;

                    if (bird.getStatus().equals("falling")) {
                        bird.fall();
                    }
                }
            }
    }

}
