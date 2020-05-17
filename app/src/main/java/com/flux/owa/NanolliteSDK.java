package com.flux.owa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.drawable.Animatable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

final class NanolliteSDK {


    static void initializeFrescoImage(Activity fx, Context cx, final SimpleDraweeView image, String asset){
        image.setVisibility(View.VISIBLE);

        final DisplayMetrics displayMetrics = new DisplayMetrics();
        fx.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        final float dpi = cx.getResources().getDisplayMetrics().density;

        ControllerListener<ImageInfo> listener = new BaseControllerListener<ImageInfo>(){
            @Override
            public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
                if (imageInfo != null){
                    image.getLayoutParams().width = imageInfo.getWidth() * Math.round(dpi);
                    image.getLayoutParams().height = imageInfo.getHeight()  * Math.round(dpi);
                    image.setAspectRatio((float) imageInfo.getWidth() / imageInfo.getHeight());
                }
            }

            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                if (imageInfo != null){
                    image.getLayoutParams().width = imageInfo.getWidth() * Math.round(dpi);
                    image.getLayoutParams().height = imageInfo.getHeight() * Math.round(dpi);
                    image.setAspectRatio((float) imageInfo.getWidth() / imageInfo.getHeight());
                }
            }
        };
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(asset)
                .setControllerListener(listener)
                .build();
        image.setController(controller);
    }
    public static class gifView extends View{

        private static final int DEFAULT_DURATION = 1000;

        private Movie mMovie;

        private long mMovieStart = 0;
        private int mCurrentAnimationTime = 0;

        @SuppressLint("NewApi")
        public gifView(Context context, AttributeSet attrs) {
            super(context, attrs);
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        public void setImageResource(int mvId){
            mMovie = Movie.decodeStream(getResources().openRawResource(mvId));
            requestLayout();
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            if(mMovie != null){
                setMeasuredDimension(mMovie.width(), mMovie.height());
            }else{
                setMeasuredDimension(getSuggestedMinimumWidth(), getSuggestedMinimumHeight());
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (mMovie != null){
                updateAnimtionTime();
                drawGif(canvas);
                invalidate();
            } else{
                drawGif(canvas);
            }
        }

        private void updateAnimtionTime() {
            long now = android.os.SystemClock.uptimeMillis();

            if (mMovieStart == 0) {
                mMovieStart = now;
            }
            int dur = mMovie.duration();
            if (dur == 0) {
                dur = DEFAULT_DURATION;
            }
            mCurrentAnimationTime = (int) ((now - mMovieStart) % dur);
        }

        private void drawGif(Canvas canvas) {
            mMovie.setTime(mCurrentAnimationTime);
            mMovie.draw(canvas, 0, 0);
            canvas.restore();
        }
    }

    static void sendMail(final Context cx, String senderMail, String senderName, String receiver, String message){
        final String sendInBlue = "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";

        try {
            String url = "https://api.sendinblue.com/v3/smtp/email";

            String foo = "{  \n" +
                    "      \"name\":\"Balogun Silver\",\n" +
                    "      \"email\":\"connect@nanollite.com\"\n" +
                    "   },\n" +
                    "   \"to\":[  \n" +
                    "      {  \n" +
                    "         \"email\":\"silverg33k@gmail.com\",\n" +
                    "         \"name\":\"John Doe\"\n" +
                    "      }\n" +
                    "   ],\n" +
                    "   \"subject\":\"Hello world\",\n" +
                    "   \"htmlContent\":\"<html><head></head><body><p>Hello,</p>This is my first transactional email sent from Sendinblue.</p></body></html>\"\n" +
                    "}";
            JSONObject json = new JSONObject(foo);


            JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.POST, url, json, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("silvr", "--" + response);
                    Toast.makeText(cx, "ok", Toast.LENGTH_SHORT).show();
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("silvr", "--" + error.networkResponse.toString());
                    Log.e("silvr", "-----" + error.getMessage());
                    Toast.makeText(cx, "Error", Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> header = new HashMap<>();
                    header.put("accept", "application/json");
                    header.put("content-type", "application/json");
                    header.put("api-key", sendInBlue);
                    return header;
                }
            };
            Volley.newRequestQueue(cx).add(request);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

}
