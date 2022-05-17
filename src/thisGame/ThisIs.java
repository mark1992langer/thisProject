package thisGame;

import java.awt.*;
import javax.swing.*;

public class ThisIs extends JFrame { //данный класс расширен JFrame

    public static void main (String [] args) { //точка входа программы

        JFrame game = new JFrame();

        //основные настройки окна
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.getContentPane();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        game.setBounds(dimension.width/2 -250,dimension.height/2 -250, 500, 500);
        game.setResizable(false);
        game.setLayout(null);
        game.setVisible(true);
        game.setBackground(new Color(255, 255, 255, 255));

        //создание доски и добавление на окно
        Board board = new Board();
        game.add(board);
        game.add(board.title);
        game.add(board.newGame);
        game.add(board.howToPlay);
        game.add(board.credits);
        game.add(board.message);

        //размещение компонентов окна
        board.setBounds(80,80,324,324);
        board.title.setBounds(80,0,324,50);
        board.newGame.setBounds(80, 50, 100, 30);
        board.howToPlay.setBounds(192, 50, 100, 30);
        board.credits.setBounds(304, 50, 100, 30);
        board.message.setBounds(80, 404, 324, 30);

    }

}
