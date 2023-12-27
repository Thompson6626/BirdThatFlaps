import java.awt.*;

public class Score extends Rectangle {

    static int GAME_HEIGHT;
    static int GAME_WIDTH;
    static int POINTS;
    final static  Color TEXT_COLOR= new Color(0, 0, 0);
    final static Font TEXT_FONT= new Font("EB Garamond",Font.PLAIN,60);
    FontMetrics metrics;
    final static String[] TEXT={"GAME OVER",
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
        if(gameRunning){
            g.drawString(String.valueOf(POINTS),(GAME_WIDTH/2)-10,50);
        }else{
            int i=1;
            int textSize=metrics.getFont().getSize();
            for(String str:TEXT){
                if(i!=4){
                    g.drawString(str,((GAME_WIDTH-metrics.stringWidth(str))/2),textSize * i++);
                }else{
                    g.drawString(str,((GAME_WIDTH-metrics.stringWidth(str))/2),GAME_HEIGHT-textSize);
                }
            }
        }
    }


}
