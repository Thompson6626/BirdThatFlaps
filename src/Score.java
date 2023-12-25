import java.awt.*;

public class Score extends Rectangle {

    static int GAME_HEIGHT;
    static int GAME_WIDTH;
    static int points;
    final static  Color TEXT_COLOR= new Color(0, 0, 0);
    final static Font TEXT_FONT= new Font("EB Garamond",Font.PLAIN,60);

    Score(int GAME_WIDTH,int GAME_HEIGHT){
        Score.GAME_HEIGHT=GAME_HEIGHT;
        Score.GAME_WIDTH=GAME_WIDTH;
    }

    public void draw(Graphics g,boolean normal){
        g.setColor(TEXT_COLOR);
        g.setFont(TEXT_FONT);

        if(normal){
            g.drawString(String.valueOf(points),(GAME_WIDTH/2)-10,50);
        }else{
            g.drawString("GAME OVER",(GAME_WIDTH/2)-170,50);
            g.drawString("Final Score",(GAME_WIDTH/2)-130,120);
            g.drawString(String.valueOf(points),(GAME_WIDTH/2)-20,190);
        }


    }


}
