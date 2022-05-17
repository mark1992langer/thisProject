package thisGame;

class movesMade {

    int fromRow, fromCol; //объявляет из строки и столбца как общедоступные целые числа
    int toRow, toCol; //объявляет строку и столбец как общедоступные целые числа

    movesMade(int r1, int c1, int r2, int c2) { //movesMade конструктор берет выбранные квадраты и присваивает их общедоступным целым числам

        fromRow = r1;
        fromCol = c1;
        toRow = r2;
        toCol = c2;

    }

    //проверяет, является ли перемещение прыжком
    boolean isJump() {

        return (fromRow - toRow == 2 || fromRow - toRow == -2);

    }

}
