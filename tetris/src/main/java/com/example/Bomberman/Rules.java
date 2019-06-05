package com.example.tetris;

public class Rules {


    /**
     * 棋盘(9,8),当前的位置(x,y),下一步的位置(x,y)
     * return 0为超出棋盘外  1走的是原地  2 下一步走的是自己的棋子上 3 移动不符合规则
     * 4 可移动到下一步
     */

    public static class Config {
        public static int chessWidth = 0;   //棋子宽度设置
    }

    public static int[][] chessBoard =  //棋盘棋子的分布 {9}[8]    10横9列
            {
                    {1, 2, 3, 4, 5, 4, 3, 2, 1},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 6, 0, 0, 0, 0, 0, 6, 0},
                    {7, 0, 7, 0, 7, 0, 7, 0, 7},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},  //红方棋子分布    //不该有红黑之分,如果有换棋功能,可能双方会换颜色,但是这个棋盘值不该变,不然会有系列问题
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},  //黑方棋子分布
                    {8, 0, 8, 0, 8, 0, 8, 0, 8},
                    {0, 9, 0, 0, 0, 0, 0, 9, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {14, 13, 12, 11, 10, 11, 12, 13, 14},
            };
    public static int non_win = 0;
    public static int up_win = 1;       //不应该有红,黑之分  应该只有上和下之分要好点
    public static int down_win = 2;

    public static int win() {   //判断输赢
        int x = 0, y = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 9; j++) {
                if (chessBoard[i][j] == 5) {
                    x = 1;
                } else if (chessBoard[i][j] == 10) {
                    y = 1;
                }
            }
        }
        if (x == y) {
            return non_win;
        } else if (x == 1 && y == 0) {
            return up_win;
        } else {
            return down_win;
        }
    }

    //   序列
//    {1,   2,  3,  4,  5,  4,  3,  2,  1},
//    {0,   0,  0,  0,  0,  0,  0,  0,  0},
//    {0,   6,  0,  0,  0,  0,  0,  6,  0},
//    {7,   0,  7,  0,  7,  0,  7,  0,  7},
//    {0,   0,  0,  0,  0,  0,  0,  0,  0},
//    {0,   0,  0,  0,  0,  0,  0,  0,  0},
//    {8,   0,  8,  0,  8,  0,  8,  0,  8},
//    {0,   9,  0,  0,  0,  0,  0,  9,  0},
//    {0,   0,  0,  0,  0,  0,  0,  0,  0},
//    {14, 13, 12, 11, 10, 11, 12, 13, 14},
    public static int chessValue(int positionX, int positionY) {    //坐标转数组值
        return chessBoard[positionY][positionX];
    }
    public static void moveChess(int fromX, int fromY, int toX, int toY) {
        chessBoard[toY][toX] = chessValue(fromX,fromY);
        chessBoard[fromY][fromX] = 0;
    }
    public static boolean canMove(int fromX, int fromY, int toX, int toY) {//0,3 0,4
//TODO 起始位置到目的位置fromX,fromY, toX, toY  这个为坐标位置 ----注意转换为棋子对应的数组要注意 例如:坐标位x,y --> 棋子=chessBoard[y][x]
        if (toX > 8 || toY > 9 || toX < 0 || toY < 0) {    //超出棋盘范围
            return false;
        }
        if (fromX == toX && fromY == toY) {     //走的是原地
            return false;
        }
        if (chessBoard[fromY][fromX] > 7) {    //一方棋子走的位置是自己棋子的位置
            if (chessBoard[toY][toX] > 7) {
                return false;
            }
        } else if (chessBoard[fromY][fromX] > 0) {
            if (chessBoard[toY][toX] < 8 &&chessValue(toX,toY)!=0) {
                return false;
            }
        }
        switch (chessBoard[fromY][fromX]) {
            case 1: //车
            case 14:
                if (Math.abs(toY - fromY) > 0 && Math.abs(toX - fromX) == 0) {//走的竖线
                    if (toY > fromY) {
                        for (int i = fromY + 1; i < toY; i++) {
                            if (chessBoard[i][fromX] != 0) {
                                return false;
                            }
                        }
                    } else {
                        for (int i = toY + 1; i < fromY; i++) {
                            if (chessBoard[i][fromX] != 0) {
                                return false;
                            }
                        }
                    }
                    return true;
                } else if (Math.abs(toX - fromX) > 0 && Math.abs(toY - fromY) == 0) { //走的横线
                    if (toX > fromX) {
                        for (int i = fromX + 1; i < toX; i++) {
                            if (chessBoard[fromY][i] != 0) {
                                return false;
                            }
                        }
                    } else {
                        for (int i = toX + 1; i < fromX; i++) {
                            if (chessBoard[fromY][i] != 0) {
                                return false;
                            }
                        }
                    }
                    return true;
                }
                break;
            case 2: //马
            case 13:
                if (Math.abs(toY - fromY) == 2 && Math.abs(toX - fromX) == 1) {
                    int centerY = (toY + fromY) / 2;
                    if (chessBoard[centerY][fromX] != 0) {//马蹄处有棋子
                        return false;
                    }
                    return true;
                } else if (Math.abs(toY - fromY) == 1 && Math.abs(toX - fromX) == 2) {
                    int centerX = (toX + fromX) / 2;
                    if (chessBoard[fromY][centerX] != 0) {//马蹄处有棋子
                        return false;
                    }
                    return true;
                }
                break;
            case 3: //相
                if (toY > 4) {//过河了
                    return false;
                } else if (Math.abs(toY - fromY) == 2 && Math.abs(toX - fromX) == 2) {  //走"田"字
                    int centerY = (toY + fromY) / 2;
                    int centerX = (toX + fromX) / 2;
                    if (chessBoard[centerY][centerX] != 0) {// 象眼处有棋子
                        return false;
                    }
                    return true;
                }
                break;
            case 12:
                if (toY < 5) {//过河了
                    return false;
                } else if (Math.abs(toY - fromY) == 2 && Math.abs(toX - fromX) == 2) {  //走"田"字
                    int centerY = (toY + fromY) / 2;
                    int centerX = (toX + fromX) / 2;
                    if (chessBoard[centerY][centerX] != 0) {// 象眼处有棋子
                        return false;
                    }
                    return true;
                }
                break;
            case 4: //士
                if (toY > 2 || toX < 3 || toX > 5) { //出了九宫格
                    return false;
                } else if (Math.abs(toX - fromX) == 1 && Math.abs(toY - fromY) == 1) {//走斜线，直走一格
                    return true;
                }
                break;
            case 11:
                if (toY < 7 || toX < 3 || toX > 5) { //出了九宫格
                    return false;
                } else if (Math.abs(toX - fromX) == 1 && Math.abs(toY - fromY) == 1) {//走斜线，直走一格
                    return true;
                }
                break;
            //帅
            case 5:
                if (toY > 2 || toX < 3 || toX > 5) {//出了九宫格
                    return false;
                } else if ((Math.abs(toX - fromX) + Math.abs(toY - fromY)) == 1) {//只能走一格
                    return true;
                }
                break;
            case 10:
                if (toY < 7 || toX < 3 || toX > 5) {//出了九宫格
                    return false;
                } else if ((Math.abs(toX - fromX) + Math.abs(toY - fromY)) == 1) {//只能走一格
                    return true;
                }
                break;
            case 6: //炮
            case 9:
                int count = 0;
                if (chessBoard[toY][toX] == 0) {    //到的位置是空位置
                    if (Math.abs(toY - fromY) > 0 && (toX - fromX) == 0) {//走的竖线
                        if (toY > fromY) {
                            for (int i = fromY + 1; i < toY; i++) {
                                if (chessBoard[i][fromX] != 0) {
                                    return false;
                                }
                            }
                        } else {
                            for (int i = toY + 1; i < fromY; i++) {
                                if (chessBoard[i][fromX] != 0) {
                                    return false;
                                }
                            }
                        }
                    } else if (Math.abs(toX - fromX) > 0 && Math.abs(toY - fromY) == 0) {//走的横线
                        if (toX > fromX) {
                            for (int i = fromX + 1; i < toX; i++) {
                                if (chessBoard[fromY][i] != 0) {
                                    return false;
                                }
                            }
                        } else {
                            for (int i = toX + 1; i < fromX; i++) {
                                if (chessBoard[fromY][i] != 0) {
                                    return false;
                                }
                            }
                        }
                    }
                    return true;
                }else { //到的位置是有子的
                    if (Math.abs(toY - fromY) > 0 && (toX - fromX) == 0) {//走的竖线
                        if (toY > fromY) {
                            for (int i = fromY + 1; i < toY; i++) {
                                if (chessBoard[i][fromX] != 0) {
                                    count++;
                                }
                            }
                        } else {
                            for (int i = toY + 1; i < fromY; i++) {
                                if (chessBoard[i][fromX] != 0) {
                                    count++;
                                }
                            }
                        }
                    } else if (Math.abs(toX - fromX) > 0 && Math.abs(toY - fromY) == 0) {//走的横线
                        if (toX > fromX) {
                            for (int i = fromX + 1; i < toX; i++) {
                                if (chessBoard[fromY][i] != 0) {
                                    count++;
                                }
                            }
                        } else {
                            for (int i = toX - 1; i > fromX; i--) {
                                if (chessBoard[fromY][i] != 0) {
                                    count++;
                                }
                            }
                        }
                    }
                    if (count == 1) {  //如果中间只有一个棋子间隔就可以
                        return true;
                    }
                }
                break;
            case 7: //兵
                if ((toY - fromY) < 0) {//后退
                    return false;
                } else if (fromY > 4) {//过了河
                    if ((Math.abs(toX - fromX) + Math.abs(toY - fromY)) == 1) { //只能走一格
                        return true;
                    }
                } else {//没过河
                    if (Math.abs(toY - fromY) == 1 && fromX == toX) {    //只能往前走
                        return true;
                    }
                }
                break;
            case 8:
                if ((toY - fromY) > 0) {//后退
                    return false;
                } else if (fromY <= 4) {//过了河
                    if ((Math.abs(toX - fromX) + Math.abs(toY - fromY)) == 1) { //只能走一格
                        return true;
                    }
                } else {//没过河
                    if (Math.abs(toY - fromY) == 1 && fromX == toX) {    //只能往前走
                        return true;
                    }
                }
                break;
            default:    //如果为其他值是不允许移动
                break;
        }

        return false;
    }
}
