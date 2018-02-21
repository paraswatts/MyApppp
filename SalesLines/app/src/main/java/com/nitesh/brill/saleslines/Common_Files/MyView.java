package com.nitesh.brill.saleslines.Common_Files;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

/**
 * Created by Paras Android on 23-06-2017.
 */

public class MyView extends View {



    class Pt{



        float x, y;



        Pt(float _x, float _y){

            x = _x;

            y = _y;

        }

    }



    Pt[] myPath = { new Pt(0, 0),



            new Pt(400, 2000),

            new Pt(900, 2000),
            new Pt(1300, 0),

            new Pt(0, 0),

    };



    public MyView(Context context) {


        super(context);

        // TODO Auto-generated constructor stub

    }



    @Override

    protected void onDraw(Canvas canvas) {

        // TODO Auto-generated method stub

        super.onDraw(canvas);





        Paint paint = new Paint();

        paint.setColor(Color.BLACK);

        paint.setStrokeWidth(3);

        paint.setStyle(Paint.Style.FILL);

        Path path = new Path();



        path.moveTo(myPath[0].x, myPath[0].y);

        for (int i = 1; i < myPath.length; i++){

            path.lineTo(myPath[i].x, myPath[i].y);

        }

        canvas.drawPath(path, paint);



    }



}