[1mdiff --git a/src/GamePanel.java b/src/GamePanel.java[m
[1mindex 75e9632..c48a7dc 100644[m
[1m--- a/src/GamePanel.java[m
[1m+++ b/src/GamePanel.java[m
[36m@@ -7,8 +7,8 @@[m [mimport java.util.Random;[m
 import java.util.Queue;[m
 public class GamePanel extends JPanel implements Runnable {[m
 [m
[31m-    final static int GAME_HEIGHT=1000;[m
[31m-    final static int GAME_WIDTH=(int)(GAME_HEIGHT*(0.888888));[m
[32m+[m[32m    final static int GAME_HEIGHT=700;[m
[32m+[m[32m    final static int GAME_WIDTH=(int)(GAME_HEIGHT*(1.1));[m
     final static Dimension SCREEN_SIZE= new Dimension(GAME_WIDTH,GAME_HEIGHT);[m
 [m
     Random random;[m
[36m@@ -22,7 +22,7 @@[m [mpublic class GamePanel extends JPanel implements Runnable {[m
     static Queue<Timer> timers;[m
     static boolean gameOver=false;[m
 [m
[31m-    final static int BIRD_DIAMETER=40;[m
[32m+[m[32m    final static int BIRD_DIAMETER=35;[m
     final static int MINIMUM_PIPE_HEIGHT=100;[m
     final static int PIPE_WIDTH=80;[m
     final static int SPACE_BETWEEN_PIPES=200;[m
[36m@@ -138,7 +138,7 @@[m [mpublic class GamePanel extends JPanel implements Runnable {[m
             }[m
         }[m
 [m
[31m-        if(bird.y>=GAME_HEIGHT){[m
[32m+[m[32m        if(bird.y>=GAME_HEIGHT-BIRD_DIAMETER){[m
             gameOver();[m
         }[m
     }[m
[36m@@ -150,7 +150,7 @@[m [mpublic class GamePanel extends JPanel implements Runnable {[m
     public void checkPipeOutOfBounds(){[m
         if (!pipesQueue2.isEmpty() && pipesQueue2.peek().x <= -PIPE_WIDTH) {[m
             pipesQueue2.poll(); // Upper pipe[m
[31m-            pipesQueue2.poll(); // Lower pipe[m
[32m+[m[32m            pipesQueue2.poll();// Lower pipe[m
         }[m
     }[m
 [m
[1mdiff --git a/src/Score.java b/src/Score.java[m
[1mindex f543c60..3855817 100644[m
[1m--- a/src/Score.java[m
[1m+++ b/src/Score.java[m
[36m@@ -13,11 +13,11 @@[m [mpublic class Score extends Rectangle {[m
         Score.GAME_WIDTH=GAME_WIDTH;[m
     }[m
 [m
[31m-    public void draw(Graphics g,boolean normal){[m
[32m+[m[32m    public void draw(Graphics g,boolean gameRunning){[m
         g.setColor(TEXT_COLOR);[m
         g.setFont(TEXT_FONT);[m
 [m
[31m-        if(normal){[m
[32m+[m[32m        if(gameRunning){[m
             g.drawString(String.valueOf(points),(GAME_WIDTH/2)-10,50);[m
         }else{[m
             g.drawString("GAME OVER",(GAME_WIDTH/2)-170,50);[m
