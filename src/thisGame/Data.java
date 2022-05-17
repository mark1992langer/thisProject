package thisGame;

import java.util.ArrayList;

class Data {

    public static final int //объявляет конечные целые числа, представляющие состояние квадрата
            blank = 0,
            player1 = 1,
            playerKing1 = 2,
            player2 = 3,
            playerKing2 = 4;

    private int[][] board;

    public Data() { //конструктор Data

        board = new int[8][8]; //создание массива
        setUpBoard(); //вызов setUpBoard

    }

    public void setUpBoard() { //обновление доски

        for (int row = 0; row < 8; row++) {

            for (int col = 0; col < 8; col++) {

                if ( row % 2 == col % 2 ) { //для темных квадратов

                    if (row < 3) //если в верхних 3 рядах
                        board[row][col] = player2; //квадраты игрока 2
                    else if (row > 4) //если в нижних 3 рядах
                        board[row][col] = player1; //квадраты игрока 1
                    else //в противном случае пусто
                        board[row][col] = blank;

                } else //для всех светлых квадратов
                    board[row][col] = blank;

            }

        }

    }

    public int pieceAt(int row, int col) { //метод, который возвращает расположение это

        return board[row][col];

    }

    public void makeMove(movesMade move) { //метод, который принимает тип moveMade и делает ход

        makeMove(move.fromRow, move.fromCol, move.toRow, move.toCol);

    }

    public void makeMove(int fromRow, int fromCol, int toRow, int toCol) { //сам ход

        board[toRow][toCol] = board[fromRow][fromCol]; //часть, которая была в исходном квадрате, теперь находится в новом квадрате
        board[fromRow][fromCol] = blank; //исходный квадрат теперь пуст

        if (fromRow - toRow == 2 || fromRow - toRow == -2){ //если ход - это прыжок

            //игрок прыгает
            int jumpRow = (fromRow + toRow) / 2;
            int jumpCol = (fromCol + toCol) / 2;
            board[jumpRow][jumpCol] = blank; //исходный квадрат не пустой

        }

        if (toRow == 0 && board[toRow][toCol] == player1){ //если игрок 1 достиг верхнего ряда
            board[toRow][toCol] = playerKing1;
        }

        if (toRow == 7 && board[toRow][toCol] == player2){ //если игрок 2 достиг нижнего ряда
            board[toRow][toCol] = playerKing2;
        }
    }

    public movesMade[] getLegalMoves(int player) { //определяет допустимые ходы для игрока

        if (player != player1 && player != player2) //если метод вызван не игроком
            return null;

        int playerKing;


        if (player == player1){
            playerKing = playerKing1;
        } else {
            playerKing = playerKing2;
        }

        ArrayList moves = new ArrayList(); //создает новый массив для записи допустимых ходов

        for (int row = 0; row < 8; row++){ //просматривает все квадраты доски

            for (int col = 0; col < 8; col++){

                if (board[row][col] == player || board[row][col] == playerKing){ //если квадрат принадлежит игроку

                    //проверить все возможные прыжки вокруг фигуры - если они найдены, игрок должен прыгнуть
                    if (canJump(player, row, col, row+1, col+1, row+2, col+2))
                        moves.add(new movesMade(row, col, row+2, col+2));
                    if (canJump(player, row, col, row-1, col+1, row-2, col+2))
                        moves.add(new movesMade(row, col, row-2, col+2));
                    if (canJump(player, row, col, row+1, col-1, row+2, col-2))
                        moves.add(new movesMade(row, col, row+2, col-2));
                    if (canJump(player, row, col, row-1, col-1, row-2, col-2))
                        moves.add(new movesMade(row, col, row-2, col-2));

                }

            }

        }

        if (moves.size() == 0){ //если прыжков нет

            for (int row = 0; row < 8; row++){ //смотрим квадраты еще раз

                for (int col = 0; col < 8; col++){

                    if (board[row][col] == player || board[row][col] == playerKing){ //если квадрат принадлежит игроку

                        //проверить все возможные прыжки вокруг фигуры - если они найдены, игрок должен прыгнуть
                        if (canMove(player,row,col,row+1,col+1))
                            moves.add(new movesMade(row,col,row+1,col+1));
                        if (canMove(player,row,col,row-1,col+1))
                            moves.add(new movesMade(row,col,row-1,col+1));
                        if (canMove(player,row,col,row+1,col-1))
                            moves.add(new movesMade(row,col,row+1,col-1));
                        if (canMove(player,row,col,row-1,col-1))
                            moves.add(new movesMade(row,col,row-1,col-1));

                    }

                }

            }

        }

        if (moves.size() == 0){ //если нет  ходов
            return null; //игрок ходить не может
        }else { //в противном случае создается массив для хранения всех возможных ходов
            movesMade[] moveArray = new movesMade[moves.size()];
            for (int i = 0; i < moves.size(); i++){
                moveArray[i] = (movesMade)moves.get(i);
            }
            return moveArray;
        }

    }

    public movesMade[] getLegalJumpsFrom(int player, int row, int col){ //определяет разрешенные прыжки для игрока

        if (player != player1 && player != player2)
            return null;

        int playerKing;


        if (player == player1){
            playerKing = playerKing1;
        }else {
            playerKing = playerKing2;
        }

        ArrayList moves = new ArrayList();

        if (board[row][col] == player || board[row][col] == playerKing){


            if (canJump(player, row, col, row+1, col+1, row+2, col+2))
                moves.add(new movesMade(row, col, row+2, col+2));
            if (canJump(player, row, col, row-1, col+1, row-2, col+2))
                moves.add(new movesMade(row, col, row-2, col+2));
            if (canJump(player, row, col, row+1, col-1, row+2, col-2))
                moves.add(new movesMade(row, col, row+2, col-2));
            if (canJump(player, row, col, row-1, col-1, row-2, col-2))
                moves.add(new movesMade(row, col, row-2, col-2));

        }

        if (moves.size() == 0){
            return null;
        }else {
            movesMade[] moveArray = new movesMade[moves.size()];
            for (int i = 0; i < moves.size(); i++){
                moveArray[i] = (movesMade)moves.get(i);
            }
            return moveArray;
        }
    }

    private boolean canJump(int player, int r1, int c1, int r2, int c2, int r3, int c3){ //метод проверяет возможные прыжки

        if (r3 < 0 || r3 >= 8 || c3 < 0 || c3 >= 8) //если целевая строка или столбец находятся за пределами доски
            return false; //прыжка нет, так как пункт назначения не существует

        if (board[r3][c3] != blank) //если пункт назначения не пуст
            return false; //нет прыжка, так как пункт назначения занят

        if (player == player1) { //в случае игрока 1
            if (board[r1][c1] == player1 && r3 > r1) //если целевая строка больше исходной
                return false; //прыжка нет, так как игрок 1 может двигаться только вверх
            if (board[r2][c2] != player2 && board[r2][c2] != playerKing2) //если средняя фигура не принадлежит игроку 2
                return false; //прыжка нет, так как игрок 1 не может прыгать через свои фигуры
            return true; //в противном случае прыжок разрешен
        }else { //в случае игрока 2
            if (board[r1][c1] == player2 && r3 < r1) //если целевая строка больше исходной
                return false; //прыжка нет, так как игрок 2 может двигаться только в низ
            if (board[r2][c2] != player1 && board[r2][c2] != playerKing1) //если средняя фигура не принадлежит игроку 1
                return false; //прыжка нет, так как игрок 1 не может прыгать через свои фигуры
            return true; //в противном случае прыжок разрешен
        }

    }

    private boolean canMove(int player, int r1, int c1, int r2, int c2){ //метод проверяет возможные нормальные ходы

        if (r2 < 0 || r2 >= 8 || c2 < 0 || c2 >= 8) //если целевая строка или столбец находятся за пределами доски
            return false; //нет хода, так как пункт назначения не существует

        if (board[r2][c2] != blank) //если пункт назначения не пуст
            return false; //нет хода, так как пункт назначения взят

        if (player == player1) { //в случае игрока 1
            if (board[r1][c1] == player1 && r2 > r1) //если целевая строка больше исходной
                return false; //нет хода, так как игрок 1 может двигаться только вверх
            return true; //в противном случае ход разрешен
        }else { //в случае игрока 2
            if (board[r1][c1] == player2 && r2 < r1) //если целевая строка больше исходной
                return false; //нет хода, так как игрок 2 может двигаться только в низ
            return true; //в противном случае ход разрешен
        }

    }

}


