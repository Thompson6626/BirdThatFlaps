import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;
import java.util.Queue;
public class GamePanel extends JPanel implements Runnable {

    final static int GAME_HEIGHT=1000;
    final static int GAME_WIDTH=(int)(GAME_HEIGHT*(0.888888));
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

    final static int BIRD_DIAMETER=40;
    final static int MINIMUM_PIPE_HEIGHT=100;
    final static int PIPE_WIDTH=80;
    final static int SPACE_BETWEEN_PIPES=200;

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

        timer = new Timer(2500, e -> {
            newPipes();
            repaint();
        });
        timer.start();

        this.setFocusable(true);
    }
    public void newBird(){
        bird= new Bird(200,(GAME_HEIGHT/2)-BIRD_DIAMETER,BIRD_DIAMETER,BIRD_DIAMETER);
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

        image = createImage(getWidth(),getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image,0,0,this);
    }

    public void draw(Graphics g){
        for(Pipes pipe:pipesQueue2){
            pipe.draw(g);
        }
        bird.draw(g);
        score.draw(g,!gameOver);
    }
    public void move(){
        for(Pipes pipe:pipesQueue2){
            pipe.move();
        }
        if(bird.isFalling()){
            bird.fall();
        }else{
            bird.fly();
        }
        if(!pipesQueue2.isEmpty()&& bird.x == pipesQueue2.peek().x) {
            Score.points++;
        }
    }

    public void createTimerForFlying(){
        Timer timer = new Timer(400, e ->
                bird.keepFalling()
        );
        if(!timers.isEmpty()){
            timers.poll().stop();
        }
        timers.offer(timer);
        timer.setRepeats(false); // Repeats only once
        timer.start();
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

        if(bird.y>=GAME_HEIGHT){
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
            pipesQueue2.poll(); // Lower pipe
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
                }
            }
    }

}
