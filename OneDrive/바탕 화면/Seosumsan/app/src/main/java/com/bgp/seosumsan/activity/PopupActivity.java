package com.bgp.seosumsan.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bgp.seosumsan.HttpClient;
import com.bgp.seosumsan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PopupActivity extends AppCompatActivity {
    private static String TAG = "Task";
    private String id;
    private String mJsonString;

    Button okBtn;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 제거
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //팝업이 올라오면 배경 블러처리
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        //레이아웃 설정
        setContentView(R.layout.popup_activity);

        okBtn = (Button)findViewById(R.id.okBtn);
        imageView = (ImageView)findViewById(R.id.detail_image);
        //액티비티 바깥화면이 클릭되어도 종료되지 않게 설정하기
        this.setFinishOnTouchOutside(false);

        Bundle extras = getIntent().getExtras();
        id = extras.getString("id");

        Log.d(TAG, id);

        NetworkTask task = new NetworkTask();
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        task.execute(params);
    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

    public class NetworkTask extends AsyncTask<Map<String, String>, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(PopupActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected String doInBackground(Map<String, String>... maps) {
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://" + HttpClient.Builder.getIP_ADDRESS() + "/competition/get_path.php");
            http.addAllParameters(maps[0]);

            HttpClient post = http.create();
            post.request();

            int statusCode = post.getHttpStatusCode();
            Log.d(TAG, "response code - " + statusCode);
            String body = post.getBody();

            return body;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);

            mJsonString = result;
            showResult();
        }
    }

    private void showResult(){
        String TAG_JSON="mountain";
        String TAG_IMAGE = "image";

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);

                Bitmap image = StringToBitMap(item.getString(TAG_IMAGE));
                imageView.setImageBitmap(image);
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    public static Bitmap StringToBitMap(String image){
        Log.e("StringToBitMap","StringToBitMap");
        try{
            byte [] encodeByte= Base64.decode(image,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            Log.e("StringToBitMap","good");
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}