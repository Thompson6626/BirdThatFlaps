import java.awt.*;

public class Score extends Rectangle {

    static int GAME_HEIGHT;
    static int GAME_WIDTH;
    static double POINTS;
    final static  Color TEXT_COLOR= new Color(0, 0, 0);
    final static Font TEXT_FONT= new Font("Work Sans",Font.PLAIN,60);
    FontMetrics metrics;
    static String[] TEXT={"GAME OVER",
                                "Final Score",
                                String.valueOf(POINTS),
                                "Press r to restart"};

    Score(int GAME_WIDTH,int GAME_HEIGHT){
        Score.GAME_HEIGHT=GAME_HEIGHT;
        Score.GAME_WIDTH=GAME_WIDTH;
    }

    public void draw(Graphics g,boolean gameRunning){
        g.setColor(TEXT_COLOR);
        g.setFont(TEXT_FONT);

        metrics = g.getFontMetrics(g.getFont());


        int i=1;
        int textSize=metrics.getFont().getSize();

        String points = TEXT[2] = String.valueOf((int)POINTS);


        if(gameRunning){
            g.drawString(points,( (GAME_WIDTH - metrics.stringWidth(points)) /2 ),textSize);
        }else{
            for(String str:TEXT){
                g.drawString(str,
                        ( (GAME_WIDTH - metrics.stringWidth(str)) /2 ),
                        i < 4 ? textSize * i++ : GAME_HEIGHT - textSize);
            }
        }
    }


}
