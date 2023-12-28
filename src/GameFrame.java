import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    GamePanel panel;
    GameFrame(){
        panel= new GamePanel();
        this.add(panel);
        this.setTitle("Generic flying species game");
        this.setResizable(false);
        this.setBackground(new Color(30, 176, 195));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }



}
