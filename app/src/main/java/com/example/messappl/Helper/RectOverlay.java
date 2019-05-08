package com.example.messappl.Helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class RectOverlay extends GraphicOverlay.Graphic {

    private int RECT_COLOR=Color.RED;
    private float STROKE_WIDTH=4.0f;
    private Paint Rect_paint;
    private GraphicOverlay graphicOverlay;
    private Rect rect;
    public RectOverlay(GraphicOverlay graphicOverlay, Rect rect)
    {
        super(graphicOverlay);
        Rect_paint = new Paint();
        Rect_paint.setColor(RECT_COLOR);
        Rect_paint.setStyle(Paint.Style.STROKE);
        Rect_paint.setStrokeWidth(STROKE_WIDTH);
        this.graphicOverlay=graphicOverlay;
        this.rect=rect;
        postInvalidate();
    }
    @Override
    public void draw(Canvas canvas)
    {
        RectF rectF=new RectF(rect);
        rectF.left=translateX(rectF.left);
        rectF.right=translateX(rectF.right);
        rectF.top=translateY(rectF.top);
        rectF.bottom=translateY(rectF.bottom);
        canvas.drawRect(rectF,Rect_paint);
    }
}
