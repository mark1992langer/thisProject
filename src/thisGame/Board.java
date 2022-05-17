package thisGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;

class Board extends JPanel implements ActionListener, MouseListener { //расширяется от класса JPanel

    Data board;
    boolean gameInProgress; //метод нужен чтобы проверить, идет ли игра
    int currentPlayer; //отслеживает, чья очередь
    int selectedRow, selectedCol; //квадраты которых были выбраны
    movesMade[] legalMoves;
    JLabel title;
    JButton newGame;
    JButton howToPlay;
    JButton credits;
    JLabel message;
    String Player1;
    String Player2;
    Image img;

    //конструктор
    public Board() {

        addMouseListener(this); //реализует Mouse Listener

        //создание и отрисовка компонентов понели
        title = new JLabel("ЭТО вам не Это!");
        title.setFont(new Font("Italic", Font.BOLD, 40));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(Color.darkGray);
        howToPlay = new JButton("ЭТО?!");
        howToPlay.addActionListener(this);
        newGame = new JButton("Новая Игра");
        newGame.addActionListener(this);
        credits = new JButton("Инфа");
        credits.addActionListener(this);
        message = new JLabel("", JLabel.CENTER);
        message.setFont(new Font("Italic", Font.ITALIC, 16));
        message.setHorizontalAlignment(SwingConstants.CENTER);
        message.setForeground(Color.darkGray);

        board = new Data();
        getPlayersNames(); //запрос имени играков
        NewGame(); //запуск новой игры

    }

    //реализация Actions Listener
    //тут происходит реализация действий игрока на кнопках
    public void actionPerformed(ActionEvent evt) {


        Object src = evt.getSource();
        if (src == newGame)
            NewGame();
        else if (src == howToPlay)
            instructions();
        else if (src == credits)
            showCredits();
    }

    //создание новой игры
    void NewGame() {

        board.setUpBoard(); //отрисовка доски
        currentPlayer = Data.player1;
        legalMoves = board.getLegalMoves(Data.player1); //поиск возможных ходов игрока
        selectedRow = -1; //квадрат не выбран
        message.setText("Сейчас ходит " + Player1 + "';)."); //пишет чей ход
        gameInProgress = true; //пишем что игра продолжается
        newGame.setEnabled(true); //кнопка активна
        howToPlay.setEnabled(true); //кнопка активна
        credits.setEnabled(true); //кнопка активна
        repaint(); //перересовка доски

    }

    //получает имена игроков с помощю JTextField
    public void getPlayersNames() {

        JTextField player1Name = new JTextField("Это №1");
        JTextField player2Name = new JTextField("Это №2");

        JPanel getNames = new JPanel();
        getNames.setLayout(new BoxLayout(getNames, BoxLayout.PAGE_AXIS));
        getNames.add(player1Name);
        getNames.add(player2Name);

        //предлогаем игроку ввести имя
        int result = JOptionPane.showConfirmDialog(null, getNames, "Напиши имя!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);


        if (result == JOptionPane.OK_OPTION) { //если игрок написал имена то с ним игру и создаем
            Player1 = player1Name.getText();
            Player2 = player2Name.getText();
        } else {
            Player1 = "Это №1";
            Player2 = "Это №2";
        }

    }

    //метод кнопки что это
    void instructions() {

        String intro = "Это это это! Это что не понятно?!";

        JOptionPane.showMessageDialog(null, intro, "Хочешь знать что это? ", JOptionPane.PLAIN_MESSAGE);

    }

    //метод кнопки информации
    void showCredits() {

        String credits = "Ну как бы сам писал..\n" + "примерно процента на 3 :(\n" + ".................";
        JOptionPane.showMessageDialog(null, credits, "Инфа", JOptionPane.PLAIN_MESSAGE);

    }

    //в случае выйграша запускается этот метод
    void gameOver(String str) {

        message.setText(str); //пишем кто выйграл
        newGame.setEnabled(true); //активируем все кнопки и говорим параметру что игра закончена
        howToPlay.setEnabled(true);
        credits.setEnabled(true);
        gameInProgress = false;

    }

    //метод взаимодеиствия игрока с доской
    public void mousePressed(MouseEvent evt) {

        if (!gameInProgress) { //если игра не активна
            message.setText("а это всё начинай заново ;)"); //предлагаем начать новую игру
        } else { //в противном случае вычисляет, куда мы тычим
            int col = (evt.getX() - 2) / 40; //вычисление координаты x
            int row = (evt.getY() - 2) / 40; //вычисление координаты y
            if (col >= 0 && col < 8 && row >= 0 && row < 8) //если тычим в доску на доске
                ClickedSquare(row, col); //вызываем ClickedSquare
        }
    }

    //обработка возможных ходов
    void ClickedSquare(int row, int col) {

        for (int i = 0; i < legalMoves.length; i++) { //проходим по всем возможным ходам
            if (legalMoves[i].fromRow == row && legalMoves[i].fromCol == col) { //если такой ход возможен
                selectedRow = row;
                selectedCol = col;
                if (currentPlayer == Data.player1) //показываем чей сейчас ход
                    message.setText("Сейчас ходит " + Player1 + " :)");
                else
                    message.setText("Сейчас ходит " + Player2 + " :)");
                repaint(); //отрисовываем заного
                return;
            }
        }

        if (selectedRow < 0) { //если квадрат не выбран
            message.setText("Чем ходить будешь?."); //предлагаем выбрать фигуру
            return;
        }

        for (int i = 0; i < legalMoves.length; i++) { //проходит через все допустимые ходы
            if (legalMoves[i].fromRow == selectedRow && legalMoves[i].fromCol == selectedCol //если уже выбранная фигура может двигаться
                    && legalMoves[i].toRow == row && legalMoves[i].toCol == col) { //и пункт назначения выбранной фигуры возможен
                MakeMove(legalMoves[i]); //делаем ход
                return;
            }
        }

        //просим выбрать возможные варианты хода
        message.setText("это туда не ходят))");

    }

    //перемещает фигуру
    void MakeMove(movesMade move) {

        board.makeMove(move); //вызывает метод makeMove из класса Data

        if (move.isJump()) { //проверяем можно ли дальше ходить
            legalMoves = board.getLegalJumpsFrom(currentPlayer, move.toRow, move.toCol);
            if (legalMoves != null) { //если мы можем еще ходить
                if (currentPlayer == Data.player1)
                    message.setText(Player1 + " Это ест еще одно Это."); //сообщаем об этом
                else
                    message.setText(Player2 + " Это ест еще одно Это."); //сообщаем об этом
                selectedRow = move.toRow; //двигаем
                selectedCol = move.toCol; //двигаем
                repaint();
                return;
            }
        }

        if (currentPlayer == Data.player1) { //если это был ход игрока 1
            currentPlayer = Data.player2; //теперь ход игрока 2
            legalMoves = board.getLegalMoves(currentPlayer); //даем ходить игроку 2
            if (legalMoves == null) //если ходов нет, выигрывает игрок 1
                gameOver(Player1 + " это выйграло!");
            else if (legalMoves[0].isJump()) //если мы обязаны есть пишем об этом
                message.setText(Player2 + ", ешь это.");
            else //в противном случае это означает, что настала очередь игрока 2
                message.setText("Ходит " + Player2);
        } else { //и теперь все тоже для игрока 2
            currentPlayer = Data.player1;
            legalMoves = board.getLegalMoves(currentPlayer);
            if (legalMoves == null)
                gameOver(Player2 + " это выйграло!");
            else if (legalMoves[0].isJump())
                message.setText(Player1 + ", ешь это.");
            else
                message.setText("Ходит " + Player1);
        }

        selectedRow = -1; //ничего не выбрано

        if (legalMoves != null) { //если есть легальные ходы
            boolean sameFromSquare = true;
            for (int i = 1; i < legalMoves.length; i++) //проходит через все допустимые ходы
                if (legalMoves[i].fromRow != legalMoves[0].fromRow //есть ли допустимые ходы кроме выбранной строки
                        || legalMoves[i].fromCol != legalMoves[0].fromCol) { //и столбца
                    sameFromSquare = false;
                    break;
                }
            if (sameFromSquare) {
                selectedRow = legalMoves[0].fromRow;
                selectedCol = legalMoves[0].fromCol;
            }
        }

        repaint();

    }

    //ресуем доску
    public void paintComponent(Graphics g) {

        //граница вокруг игрового поля
        g.setColor(new Color(25, 70, 12));
        g.fillRect(0, 0, 324, 324);

        //создаем клетки
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                //ресуем квадраты
                if (row % 2 == col % 2)
                    g.setColor(new Color(122, 62, 6));
                else
                    g.setColor(new Color(10, 6, 6));
                g.fillRect(2 + col * 40, 2 + row * 40, 40, 40);

                //ресуем квадраты с это
                switch (board.pieceAt(row, col)) {
                    case Data.player1:
                        img = Toolkit.getDefaultToolkit().getImage("C:/Users/Марк/IdeaProjects/thisProject/это это1.jpg");
                        g.drawImage(img, 3 + col * 40, 3 + row * 40, this);
                        break;
                    case Data.player2:
                        img = Toolkit.getDefaultToolkit().getImage("C:/Users/Марк/IdeaProjects/thisProject/это это2.jpg");
                        g.drawImage(img, 3 + col * 40, 3 + row * 40, this);
                        break;
                    case Data.playerKing1:
                        img = Toolkit.getDefaultToolkit().getImage("C:/Users/Марк/IdeaProjects/thisProject/это это1.jpg");
                        g.drawImage(img, 3 + col * 40, 3 + row * 40, this);
                        g.drawString("K", 27 + col * 40, 36 + row * 40);
                        break;
                    case Data.playerKing2:
                        img = Toolkit.getDefaultToolkit().getImage("C:/Users/Марк/IdeaProjects/thisProject/это это2.jpg");
                        g.drawImage(img, 3 + col * 40, 3 + row * 40, this);
                        g.drawString("K", 27 + col * 40, 36 + row * 40);
                        break;
                }
            }
        }

        if (gameInProgress) { //если игра идет

            g.setColor(new Color(55, 255, 0, 255));
            for (int i = 0; i < legalMoves.length; i++) { //проходит через все допустимые ходы
                //выделяем все возможные ходы
                g.drawRect(2 + legalMoves[i].fromCol * 40, 2 + legalMoves[i].fromRow * 40, 39, 39);
            }

            if (selectedRow >= 0) { //если выбран квадрат
                g.setColor(Color.white); //выделяем белым цветом
                g.drawRect(2 + selectedCol * 40, 2 + selectedRow * 40, 39, 39);
                g.drawRect(3 + selectedCol * 40, 3 + selectedRow * 40, 37, 37);
                g.setColor(Color.green);
                for (int i = 0; i < legalMoves.length; i++) { //его допустимые ходы затем выделяются другим цветом
                    if (legalMoves[i].fromCol == selectedCol && legalMoves[i].fromRow == selectedRow)
                        g.drawRect(2 + legalMoves[i].toCol * 40, 2 + legalMoves[i].toRow * 40, 39, 39);
                }
            }
        }
    }

    //реализует вход мыши, щелчок, отпускание и тд они обязательны
    public void mouseEntered(MouseEvent evt) {
    }

    public void mouseClicked(MouseEvent evt) {
    }

    public void mouseReleased(MouseEvent evt) {
    }

    public void mouseExited(MouseEvent evt) {
    }

}
