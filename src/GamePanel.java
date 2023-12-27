import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {

    final static int GAME_WIDTH=650;
    final static int GAME_HEIGHT=740;
    static Queue<Pipes> PIPESQUEUE2 = new LinkedList<>();
    static Queue<Timer> TIMERS;
    static boolean GAMEOVER =false;
    final static int BIRD_DIAMETER=35;
    final static int BIRD_DIAMETER2=45;
    final static int MINIMUM_PIPE_HEIGHT=100;
    final static int PIPE_WIDTH=80;
    final static int SPACE_BETWEEN_PIPES=200;
    final static int PIPE_SPAWN_TIME=1600;
    final static int[] BIRD_XY_COORD_STARTING_POS = {200,(GAME_HEIGHT/2)-BIRD_DIAMETER};

    final static Dimension SCREEN_SIZE= new Dimension(GAME_WIDTH,GAME_HEIGHT);
    Random random;
    Thread gameThread;
    Timer pipeSpawnerTimer = new Timer(PIPE_SPAWN_TIME, e -> newPipes());
    Image image;
    Graphics graphics;
    Bird bird;
    Score score;

    private static final File BACKGROUND= new File("flappybirdbg.png");

    private static BufferedImage BACKGROUND_IMAGE;

    static{
        try{
            BACKGROUND_IMAGE = ImageIO.read(BACKGROUND);
        }catch (IOException e){
            e.printStackTrace();
        }
    }



    GamePanel(){
        newScore();
        newBird();
        newPipes();

        this.setFocusable(true);
        this.setPreferredSize(SCREEN_SIZE);
        this.addKeyListener(new AL());

        TIMERS = new LinkedList<>();

        gameThread = new Thread(this);
        gameThread.start();

        pipeSpawnerTimer.start();

    }
    public void newBird(){
        bird= new Bird(BIRD_XY_COORD_STARTING_POS[0],
                BIRD_XY_COORD_STARTING_POS[1],
                BIRD_DIAMETER2,
                BIRD_DIAMETER);
    }

    public void newScore(){
        score= new Score(GAME_WIDTH,GAME_HEIGHT);
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

        PIPESQUEUE2.offer(upperPipe);
        PIPESQUEUE2.offer(lowerPipe);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();

        image = createImage(width, height);
        graphics = image.getGraphics();

        if (BACKGROUND_IMAGE != null) {
            graphics.drawImage(BACKGROUND_IMAGE, 0, 0, width, height, this);
        }

        draw(graphics);

        g.drawImage(image, 0, 0, this);
    }

    public void draw(Graphics g){
        int i=1;
        for(Pipes pipe: PIPESQUEUE2){
            pipe.draw(g, i % 2 != 0);
            i++;
        }
        bird.draw(g);
        score.draw(g,!GAMEOVER);
    }
    public void move(){
        for(Pipes pipe: PIPESQUEUE2){
            pipe.move();
        }

        switch(bird.getStatus()){
            case "flying" -> bird.fly();
            case "falling" -> bird.fall();
            case "floating" -> bird.floatt();
        }

        if(!PIPESQUEUE2.isEmpty() ) {
            int pipex=PIPESQUEUE2.peek().x;
            if(bird.x==pipex){
                Score.POINTS++;
            }
        }
    }

    public void createTimerForFlying() {
        int flyingTime = 250;
        int floatingTime = flyingTime + 70;

        Timer[] timers= new Timer[2];
        Timer flyingTimer = new Timer(flyingTime, e -> bird.setStatus("floating"));
        Timer floatingTimer = new Timer(floatingTime, e -> bird.setStatus("falling"));

        timers[0]=flyingTimer;
        timers[1]=floatingTimer;

        while(!TIMERS.isEmpty()){
            TIMERS.poll().stop();
        }

        for(Timer timer:timers){
            TIMERS.offer(timer);
            timer.setRepeats(false);
            timer.start();
        }
    }



    public void checkBirdCollision(){
        if(bird.y<=0){ // So that the bird doesnt go way up
            bird.y=0;
        }

        for(Pipes pipes: PIPESQUEUE2){
            if(bird.intersects(pipes)){
                gameOver();
            }
        }

        if(bird.y>=GAME_HEIGHT-BIRD_DIAMETER){
            gameOver();
        }
    }
    public void gameOver(){
        GAMEOVER =true;
        gameThread.interrupt();


    }


    public void checkPipeOutOfBounds(){
        if (!PIPESQUEUE2.isEmpty() && PIPESQUEUE2.peek().x <= -PIPE_WIDTH) {
            PIPESQUEUE2.poll(); // Upper pipe
            PIPESQUEUE2.poll();// Lower pipe
        }
    }


    @Override
    public void run(){

            long lastTime = System.nanoTime();
            double amountOfTicks = 55;
            double ns = 1000000000 / amountOfTicks;
            double delta = 0;

            while (!GAMEOVER) {
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

    private class AL extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            bird.keyPressed(e);
            createTimerForFlying();

            if(GAMEOVER && e.getKeyCode()==KeyEvent.VK_R ){
                restartAll();
            }
        }


    }
    private void restartAll(){
        bird.x = BIRD_XY_COORD_STARTING_POS[0];
        bird.y = BIRD_XY_COORD_STARTING_POS[1];

        while(!TIMERS.isEmpty()){
            TIMERS.poll().stop();
        }

        PIPESQUEUE2.clear();

        Score.POINTS=0;

        GAMEOVER = false;

        bird.setStatus("falling");

        gameThread= new Thread(this);
        gameThread.start();
    }

}
