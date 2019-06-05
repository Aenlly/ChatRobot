package com.example.tetris;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tetris.Rules;

public class ChessView extends View {
    // TODO 画棋盘
    // TODO 画棋子
    // TODO 触摸交互 --> 旗子走动   --->可移动规则
    // TODO 判断输赢
    private int chessWidth = Rules.Config.chessWidth;    //棋子宽度和高度是一样
    private Bitmap boardBitmap;     //棋盘bitmap
    private int red = 0; //红色棋子
    private int black = 1; //黑色棋子
    private int currentChessMove = red; //当前走棋的棋子
    private boolean chooseStatus; //状态 是否选中棋子
    //白方:1车 2马 3相 4士 5帅 6炮 7兵
    //黑方:14车 13马 12相 11士 10帅 9炮 8兵
    private int[] currentPosition = new int[2]; //用来记录上一步棋子的x,y
    Paint paint = new Paint();
    Paint redPaint = new Paint();
    Paint blackPaint = new Paint();

    public ChessView(Context context) {
        this(context, null);
    }

    public ChessView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChessView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //初始化一些东西
    private void init() {
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        //设置去锯齿
        paint.setAntiAlias(true);
        paint.setTextSize(45);
        paint.setStrokeWidth(3);

        redPaint.setColor(Color.RED);
        redPaint.setAntiAlias(true);
        redPaint.setTextSize(60);
        redPaint.setStyle(Paint.Style.FILL);

        blackPaint.setStyle(Paint.Style.FILL);
        blackPaint.setColor(Color.BLACK);
        blackPaint.setAntiAlias(true);
        blackPaint.setTextSize(60);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { //自己设置宽和高相等
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (chessWidth == 0)
            chessWidth = width / 10;
        Log.d("tag", "onMeasure:width=" + chessWidth);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(width + chessWidth, MeasureSpec.EXACTLY);
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    float touchX, touchY;     //用于记录触摸的点

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("tag", "onTouchEvent:x=" + event.getX() + "   y=" + event.getY() + "  10 * chessWidth=" + 10 * chessWidth);
        if (event.getY() > 10.5 * chessWidth||Rules.win()!=Rules.non_win) {    //超出棋盘范围的点不需要 -- 因为有10行,棋子也占半行
            return false;   //有人赢了也不允许移动
        }
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                touchX = event.getX();
                touchY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                Log.d("tag", "移动棋子:x=" + event.getX() + "   y=" + event.getY() + "  chessWidth=" + chessWidth + "  touchX=" + touchX + "  touchY=" + touchY);
                if (Math.round(event.getX() - touchX) > chessWidth || Math.round(event.getY() - touchY) > chessWidth) {
                    Log.d("tag", "移动棋子不可跨度太大");
                    return super.onTouchEvent(event);   //想要移动的棋子不可确认 --- down和up坐标大
                } else {
                    int x = (int) (event.getX() / chessWidth - 0.5f);
                    int y = (int) (event.getY() / chessWidth - 0.5f);
                    Log.d("tag", "移动棋子:x=" + x + "   y=" + y);
                    if (y > 9 || x > 8) {
                        return super.onTouchEvent(event);
                    }
                    if (currentChessMove == red) {    //红棋走
                        if (chooseStatus == false) {    //没选中棋子 -- 开始选
                            if (Rules.chessValue(x, y) > 0 && Rules.chessValue(x, y) < 8) {
                                chooseStatus = true;
                                currentPosition[0] = x;
                                currentPosition[1] = y;
                                invalidate();   //重新draw
                            }
                        } else {  //已经选中棋子 --- 移动
                            if (Rules.canMove(currentPosition[0], currentPosition[1], x, y)) {//可以移动棋子
                                chooseStatus = false;
                                Rules.moveChess(currentPosition[0], currentPosition[1], x, y);
                                currentChessMove = black;
                                invalidate();   //重新draw
                            } else if (Rules.chessValue(x, y) > 0 && Rules.chessValue(x, y) < 8) {
                                currentPosition[0] = x;   //选中别的棋子
                                currentPosition[1] = y;
                                invalidate();
                            } else {
                                Toast.makeText(getContext(), "不符合规则", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else { //黑棋走
                        if (chooseStatus == false) {    //没选中棋子
                            if (Rules.chessValue(x, y) > 7 && Rules.chessValue(x, y) < 15) {
                                chooseStatus = true;
                                currentPosition[0] = x;
                                currentPosition[1] = y;
                                invalidate();   //重新draw
                            }
                        } else {  //已经选中棋子
                            if (Rules.canMove(currentPosition[0], currentPosition[1], x, y)) {//可以移动棋子
                                chooseStatus = false;
                                Rules.moveChess(currentPosition[0], currentPosition[1], x, y);
                                currentChessMove = red;
                                invalidate();
                            } else if (Rules.chessValue(x, y) > 7 && Rules.chessValue(x, y) < 15) {
                                currentPosition[0] = x;   //选中别的棋子
                                currentPosition[1] = y;
                                invalidate();
                            } else {
                                Toast.makeText(getContext(), "不符合规则", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                break;

        }
        return true;
    }

    TextView tv;

    public void setText(TextView tv) {  //用来提示信息  -- 显示谁赢等
        this.tv = tv;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) { //这个也可以获取宽高
        super.onSizeChanged(w, h, oldw, oldh);
        if (chessWidth == 0)
            chessWidth = getWidth() / 10;
        Log.d("tag", "onSizeChanged:width=" + chessWidth);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        long time = System.currentTimeMillis();
        canvas.drawColor(Color.parseColor("#5500ff00"));  //画出白色背景
        canvasBoard(canvas);    //画棋盘   --- 可以优化一下,保存一张bitmap,直接drawBitmap
        if (chooseStatus) {
            paint.setColor(Color.BLUE);
            canvas.drawCircle(chessWidth * (currentPosition[0] + 1), chessWidth * (currentPosition[1] + 1), chessWidth / 2 + 5, paint);
        }
        canvasChess(canvas);    //画棋子
        int win = Rules.win();
        if (win != Rules.non_win) {
            if (tv == null) {
                return;
            }
            if (win == Rules.up_win) {
                tv.setText("红方赢");
            } else {
                tv.setText("黑方赢");
            }
            getParent().requestDisallowInterceptTouchEvent(false);  //
        }
        Log.d("tag", "用时:" + (System.currentTimeMillis() - time) ); //一般不超过20ms
    }

    int widthCenter = 1;    //楚河汉界中间是空白的,需要调整一些不能让棋盘不好看
    int space = chessWidth / 10;    //一开始为0,获取不到chessWidth
    //    int line = 40;
    int line = chessWidth / 3;

    private void canvasBoard(Canvas canvas) {
        if (boardBitmap == null) {
            space = chessWidth / 10;  //确定线的间隔
            line = chessWidth / 3;    //线的长度
            boardBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Log.d("tag", "boardBitmap==null" + getWidth() + "height:" + getHeight());
            Canvas bb = new Canvas(boardBitmap);
            paint.setColor(Color.BLACK);
            for (int i = 0; i < 10; i++) {  //画线
                bb.drawLine(chessWidth, chessWidth * (i + 1), chessWidth * 9, chessWidth * (i + 1), paint);//画出横线  -- 9行
            }
            for (int i = 0; i < 9; i++) {
                bb.drawLine(chessWidth * (i + 1), chessWidth, chessWidth * (i + 1), chessWidth * 10, paint);//画出竖线    -- 10列
            }
            //画士的地方斜线
            bb.drawLine(4 * chessWidth, 1 * chessWidth, 6 * chessWidth, chessWidth * 3, paint);
            bb.drawLine(4 * chessWidth, 3 * chessWidth, 6 * chessWidth, chessWidth * 1, paint);
            bb.drawLine(4 * chessWidth, 8 * chessWidth, 6 * chessWidth, chessWidth * 10, paint);
            bb.drawLine(4 * chessWidth, 10 * chessWidth, 6 * chessWidth, chessWidth * 8, paint);
            //画兵线 --  炮线    ---- 算的代码写的杂,也可以忽略
            drawDetails(bb);

            //画楚河汉界     ---- 这里只是简单的画下
            paint.setColor(Color.WHITE);
            bb.drawRect(chessWidth + widthCenter, 5 * chessWidth + widthCenter, 9 * chessWidth - widthCenter, 6 * chessWidth - widthCenter, paint);
            paint.setColor(Color.parseColor("#D1BD92"));
            String text = "楚河                              汉界";
            Rect rect = new Rect();
            paint.getTextBounds(text, 0, text.length(), rect);
            bb.drawText(text, 6 * chessWidth - chessWidth - rect.width() / 2, 6 * chessWidth - chessWidth / 2 + rect.height() / 2 - 5, paint);
            bb.save();
            bb.restore();
            canvas.drawBitmap(boardBitmap, 0, 0, paint);

        } else {
            Log.d("tag", "boardBitmap不为空");
            canvas.drawBitmap(boardBitmap, 0, 0, paint);
        }
    }

    private void drawDetails(Canvas bb) {
        int x1, x2, y1, y2;
        for (int i = 0; i < 4; i++) {
            x1 = chessWidth + space;
            x2 = chessWidth + space;
            y1 = 4 * chessWidth - line - space;
            y2 = 4 * chessWidth - space;
            bb.drawLine(i * 2 * chessWidth + x1, y1, i * 2 * chessWidth + x2, y2, paint);   //竖线
            bb.drawLine(i * 2 * chessWidth + x2, y2, i * 2 * chessWidth + x2 + line, y2, paint);  //横线
            bb.drawLine(i * 2 * chessWidth + x2, y2 + 2 * space, i * 2 * chessWidth + x2 + line, y2 + 2 * space, paint);  //横线
            bb.drawLine(i * 2 * chessWidth + x1, y2 + 2 * space, i * 2 * chessWidth + x2, y2 + 2 * space + line, paint);   //竖线

            bb.drawLine(i * 2 * chessWidth + x1, y1 + 3 * chessWidth, i * 2 * chessWidth + x2, y2 + 3 * chessWidth, paint);//上面向下平移3*chessWidth Y轴加平移值
            bb.drawLine(i * 2 * chessWidth + x2, y2 + 3 * chessWidth, i * 2 * chessWidth + x2 + line, y2 + 3 * chessWidth, paint);
            bb.drawLine(i * 2 * chessWidth + x2, y2 + 2 * space + 3 * chessWidth, i * 2 * chessWidth + x2 + line, y2 + 2 * space + 3 * chessWidth, paint);
            bb.drawLine(i * 2 * chessWidth + x1, y2 + 2 * space + 3 * chessWidth, i * 2 * chessWidth + x2, y2 + 2 * space + line + 3 * chessWidth, paint);

            x1 = 3 * chessWidth - space;
            x2 = 3 * chessWidth - space;
            y1 = 4 * chessWidth - line - space;
            y2 = 4 * chessWidth - space;
            bb.drawLine(i * 2 * chessWidth + x1, y1, i * 2 * chessWidth + x2, y2, paint);   //竖线
            bb.drawLine(i * 2 * chessWidth + x2, y2, i * 2 * chessWidth + x2 - line, y2, paint);  //横线
            bb.drawLine(i * 2 * chessWidth + x2, y2 + 2 * space, i * 2 * chessWidth + x2 - line, y2 + 2 * space, paint);  //横线
            bb.drawLine(i * 2 * chessWidth + x1, y2 + 2 * space, i * 2 * chessWidth + x2, y2 + 2 * space + line, paint);   //竖线

            bb.drawLine(i * 2 * chessWidth + x1, y1 + 3 * chessWidth, i * 2 * chessWidth + x2, y2 + 3 * chessWidth, paint);   //竖线
            bb.drawLine(i * 2 * chessWidth + x2, y2 + 3 * chessWidth, i * 2 * chessWidth + x2 - line, y2 + 3 * chessWidth, paint);  //横线
            bb.drawLine(i * 2 * chessWidth + x2, y2 + 2 * space + 3 * chessWidth, i * 2 * chessWidth + x2 - line, y2 + 2 * space + 3 * chessWidth, paint);  //横线
            bb.drawLine(i * 2 * chessWidth + x1, y2 + 2 * space + 3 * chessWidth, i * 2 * chessWidth + x2, y2 + 2 * space + line + 3 * chessWidth, paint);   //竖线

        }
        //画炮线
        x1 = 2 * chessWidth + space;
        x2 = 2 * chessWidth + space;
        y1 = 3 * chessWidth - line - space;
        y2 = 3 * chessWidth - space;
        bb.drawLine(x1, y1, x2, y2, paint);   //竖线
        bb.drawLine(x2, y2, x2 + line, y2, paint);  //横线
        bb.drawLine(x2, y2 + 2 * space, x2 + line, y2 + 2 * space, paint);  //横线
        bb.drawLine(x1, y2 + 2 * space, x2, y2 + 2 * space + line, paint);   //竖线
        bb.drawLine(x1 - 2 * space, y1, x2 - 2 * space, y2, paint);//竖线
        bb.drawLine(x2 - 2 * space - line, y2, x2 + line - 2 * space - line, y2, paint);  //横线
        bb.drawLine(x2 - 2 * space - line, y2 + 2 * space, x2 + line - 2 * space - line, y2 + 2 * space, paint);  //横线
        bb.drawLine(x1 - 2 * space, y2 + 2 * space, x2 - 2 * space, y2 + 2 * space + line, paint);   //竖线

        bb.drawLine(x1, y1 + 5 * chessWidth, x2, y2 + 5 * chessWidth, paint);   //竖线      ----统一向下移动5*chessWidth
        bb.drawLine(x2, y2 + 5 * chessWidth, x2 + line, y2 + 5 * chessWidth, paint);  //横线
        bb.drawLine(x2, y2 + 2 * space + 5 * chessWidth, x2 + line, y2 + 2 * space + 5 * chessWidth, paint);  //横线
        bb.drawLine(x1, y2 + 2 * space + 5 * chessWidth, x2, y2 + 2 * space + line + 5 * chessWidth, paint);   //竖线
        bb.drawLine(x1 - 2 * space, y1 + 5 * chessWidth, x2 - 2 * space, y2 + 5 * chessWidth, paint);//竖线
        bb.drawLine(x2 - 2 * space - line, y2 + 5 * chessWidth, x2 + line - 2 * space - line, y2 + 5 * chessWidth, paint);  //横线
        bb.drawLine(x2 - 2 * space - line, y2 + 2 * space + 5 * chessWidth, x2 + line - 2 * space - line, y2 + 2 * space + 5 * chessWidth, paint);  //横线
        bb.drawLine(x1 - 2 * space, y2 + 2 * space + 5 * chessWidth, x2 - 2 * space, y2 + 2 * space + line + 5 * chessWidth, paint);   //竖线

        bb.drawLine(6 * chessWidth + x1, y1, 6 * chessWidth + x2, y2, paint);   //竖线      ---- 统一向右移动6*chessWidth+
        bb.drawLine(6 * chessWidth + x2, y2, 6 * chessWidth + x2 + line, y2, paint);  //横线
        bb.drawLine(6 * chessWidth + x2, y2 + 2 * space, 6 * chessWidth + x2 + line, y2 + 2 * space, paint);  //横线
        bb.drawLine(6 * chessWidth + x1, y2 + 2 * space, 6 * chessWidth + x2, y2 + 2 * space + line, paint);   //竖线
        bb.drawLine(6 * chessWidth + x1 - 2 * space, y1, 6 * chessWidth + x2 - 2 * space, y2, paint);//竖线
        bb.drawLine(6 * chessWidth + x2 - 2 * space - line, y2, 6 * chessWidth + x2 + line - 2 * space - line, y2, paint);  //横线
        bb.drawLine(6 * chessWidth + x2 - 2 * space - line, y2 + 2 * space, 6 * chessWidth + x2 + line - 2 * space - line, y2 + 2 * space, paint);  //横线
        bb.drawLine(6 * chessWidth + x1 - 2 * space, y2 + 2 * space, 6 * chessWidth + x2 - 2 * space, y2 + 2 * space + line, paint);   //竖线

        bb.drawLine(6 * chessWidth + x1, y1 + 5 * chessWidth, 6 * chessWidth + x2, y2 + 5 * chessWidth, paint);   //竖线      ----统一向右移动6*chessWidth
        bb.drawLine(6 * chessWidth + x2, y2 + 5 * chessWidth, 6 * chessWidth + x2 + line, y2 + 5 * chessWidth, paint);  //横线
        bb.drawLine(6 * chessWidth + x2, y2 + 2 * space + 5 * chessWidth, 6 * chessWidth + x2 + line, y2 + 2 * space + 5 * chessWidth, paint);  //横线
        bb.drawLine(6 * chessWidth + x1, y2 + 2 * space + 5 * chessWidth, 6 * chessWidth + x2, y2 + 2 * space + line + 5 * chessWidth, paint);   //竖线
        bb.drawLine(6 * chessWidth + x1 - 2 * space, y1 + 5 * chessWidth, 6 * chessWidth + x2 - 2 * space, y2 + 5 * chessWidth, paint);//竖线
        bb.drawLine(6 * chessWidth + x2 - 2 * space - line, y2 + 5 * chessWidth, 6 * chessWidth + x2 + line - 2 * space - line, y2 + 5 * chessWidth, paint);  //横线
        bb.drawLine(6 * chessWidth + x2 - 2 * space - line, y2 + 2 * space + 5 * chessWidth, 6 * chessWidth + x2 + line - 2 * space - line, y2 + 2 * space + 5 * chessWidth, paint);  //横线
        bb.drawLine(6 * chessWidth + x1 - 2 * space, y2 + 2 * space + 5 * chessWidth, 6 * chessWidth + x2 - 2 * space, y2 + 2 * space + line + 5 * chessWidth, paint);   //竖线
    }

    private void canvasChess(Canvas canvas) {
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 9; x++) {
                if (Rules.chessValue(x, y) <= 0 || Rules.chessValue(x, y) > 14) {
                    if (Rules.chessValue(x, y) < 0)
                        Log.e("tag", "hava a bug");
                } else {
                    drawChess(canvas, x, y);
                }
            }
        }
    }

    int textWidth;
    int textHeight;

    private void drawChess(Canvas canvas, int x, int y) {   //这里是画棋子的地方   --- 自己可以改动,例如:相 换-->象
        //红方:1车  2马  3相  4士  5帅  6炮 7兵
        //黑方:14车 13马 12相 11士 10帅 9炮 8兵
        String text = "车";
        Paint chessPaint = null;
        if (Rules.chessValue(x, y) < 8) {//红方
            switch (Rules.chessValue(x, y)) {
                case 1:
                    text = "车";
                    break;
                case 2:
                    text = "马";
                    break;
                case 3:
                    text = "相";
                    break;
                case 4:
                    text = "士";
                    break;
                case 5:
                    text = "帅";
                    break;
                case 6:
                    text = "炮";
                    break;
                case 7:
                    text = "兵";
                    break;
            }
            chessPaint = redPaint;
        } else {
            switch (15 - Rules.chessValue(x, y)) {    //黑方
                case 1:
                    text = "车";
                    break;
                case 2:
                    text = "马";
                    break;
                case 3:
                    text = "相";
                    break;
                case 4:
                    text = "士";
                    break;
                case 5:
                    text = "帅";
                    break;
                case 6:
                    text = "炮";
                    break;
                case 7:
                    text = "兵";
                    break;
            }
            chessPaint = blackPaint;
        }
        if (textHeight == 0 || textWidth == 0) {
            Rect rect = new Rect();
            redPaint.getTextBounds(text, 0, text.length(), rect);
            textWidth = rect.width();//文字宽
            textHeight = rect.height() - 10;//文字高 --- 高度相对来说有一定的对不上,需要进行小调整
        }

        x += 1;
        y += 1;
        paint.setColor(Color.parseColor("#D1BD92"));
        canvas.drawCircle(chessWidth * x, chessWidth * y, chessWidth / 2, paint);
//        canvas.drawRect(chessWidth*j-textWidth/2,chessWidth*i-textHeight/2,chessWidth*j+textWidth/2,chessWidth*i+textHeight/2,paint);
        canvas.drawText(text, chessWidth * x - textWidth / 2, chessWidth * y + textHeight / 2, chessPaint);
    }

}