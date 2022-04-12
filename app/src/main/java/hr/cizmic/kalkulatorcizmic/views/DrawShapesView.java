package hr.cizmic.kalkulatorcizmic.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import hr.cizmic.kalkulatorcizmic.polygon.Polygon;
import hr.cizmic.kalkulatorcizmic.polygon.Vertex;

public class DrawShapesView extends View {
    private Paint paint;
    private Path path;
    private int viewWidth;
    private int viewHeight;
    private double viewRatio;


    public DrawShapesView(Context context) {
        super(context);
        init(null);
    }

    public DrawShapesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DrawShapesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public DrawShapesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        path = new Path();


        DrawShapesView _this = this;
        ViewTreeObserver observer = this.getViewTreeObserver();
        if (observer.isAlive()) {
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    _this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    viewWidth = _this.getWidth();
                    viewHeight = _this.getHeight();
                    viewRatio = (double)viewWidth/viewHeight;
                    Log.d("BANKA", viewWidth+"/"+viewHeight+"="+viewRatio);
                }
            });
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.LTGRAY);
        canvas.drawPath(path, paint);

    }

    public void setPath(Polygon poly) {
        path.reset();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        int dim = poly.getVertices().size();
        int[][] pts = normalizeVertices(poly.getVertices());



        for (int i=0; i<dim; i++) {
            if (i==0)
                path.moveTo(pts[0][0], pts[1][0]);
            else
                path.lineTo(pts[0][i], pts[1][i]);
        }
        path.close();

        postInvalidate();

    }

    public int[][] normalizeVertices(ArrayList<Vertex> verts) {
        int dim = verts.size();
        int[][] arr = new int[2][dim];

        double r_most = 0.0, b_most = 0.0;
        double l_most = Double.MAX_VALUE, t_most = Double.MAX_VALUE;

        for (Vertex v : verts) {
            l_most = Math.min(v.getX(), l_most);
            t_most = Math.min(v.getY(), t_most);
            r_most = Math.max(v.getX(), r_most);
            b_most = Math.max(v.getY(), b_most);
        }

        Log.d("BANKA", "nv   l t r b   "+l_most+" "+t_most+" "+r_most+" "+b_most);

        //  p = polygon
        double Wp = r_most - l_most;
        double Hp = b_most - t_most;
        double Rp = Wp / Hp;

        Log.d("BANKA", "poly w h r  "+Wp+" "+Hp+" "+Rp);

        //  s = scaled
        double Ws = viewRatio > Rp ? (Wp * viewHeight/Hp) : viewWidth;
        double Hs = viewRatio > Rp ? viewHeight : (Hp * viewWidth/Wp);

        final double L_most = l_most;
        final double T_most = t_most;
        verts.forEach(v -> {
            v.setX((v.getX() - L_most)*Ws/Wp);
            v.setY((v.getY() - T_most)*Hs/Hp);
        });
        for (int i=0; i<dim; i++) {
            arr[0][i]=(int)(verts.get(i).getX()+(viewWidth-Ws)/2);
            arr[1][i]=(int)(verts.get(i).getY()+(viewHeight-Hs)/2);
        }

        return arr;
    }
}
