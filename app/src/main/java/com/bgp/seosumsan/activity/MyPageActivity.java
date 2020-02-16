package com.bgp.seosumsan.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bgp.seosumsan.Adapter.ReviewAdapter;
import com.bgp.seosumsan.DTO.ReviewData;
import com.bgp.seosumsan.HttpClient;
import com.bgp.seosumsan.R;
import com.bgp.seosumsan.SaveSharedPreference;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyPageActivity extends BasicAcitivty {
    private static String TAG = "Task";
    private String mJsonString;

    private TextView mypage_id;
    private TextView mypage_logout;
    private ImageView backButton;

    private RecyclerView mReview;
    private ArrayList<ReviewData> mReviewArrayList;
    private ReviewAdapter mReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);
        init();
        addItem();
    }

    private void init(){
        mypage_id = (TextView) findViewById(R.id.mypage_id);
        mypage_id.setText(SaveSharedPreference.getUserId(MyPageActivity.this));

        mypage_logout = (TextView) findViewById(R.id.mypage_logout);
        mypage_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSharedPreference.clearUserId(MyPageActivity.this);
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        mReview = (RecyclerView) findViewById(R.id.listView_review_list);
        final RecyclerView.LayoutManager ReviewLayoutManager = new LinearLayoutManager(this);
        mReview.setLayoutManager(ReviewLayoutManager);
        mReviewArrayList = new ArrayList<>();
        mReviewAdapter = new ReviewAdapter(mReviewArrayList);
        mReview.setAdapter(mReviewAdapter);

        backButton = (ImageView) findViewById(R.id.backbutton);
        backButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView_main_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menuitem_bottombar_1:
                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                return true;
                            case R.id.menuitem_bottombar_2:
                                intent = new Intent(getBaseContext(), WalkActivity.class);
                                startActivity(intent);
                                return true;
                            case R.id.menuitem_bottombar_3:
                                intent = new Intent(getBaseContext(), MyPageActivity.class);
                                startActivity(intent);
                                return true;
                        }
                        return false;
                    }
                });
    }

    private void addItem() {
        NetworkTask task = new NetworkTask();
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", SaveSharedPreference.getUserId(MyPageActivity.this));
        task.execute(params);
    }

    public class NetworkTask extends AsyncTask<Map<String, String>, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MyPageActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected String doInBackground(Map<String, String>... maps) {
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://" + HttpClient.Builder.getIP_ADDRESS() + "/competition/get_review.php");
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
        String TAG_NAME = "name";
        String TAG_CONTENT = "content";
        String TAG_RATING = "rating";

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);

                String name = item.getString(TAG_NAME);
                String content = item.getString(TAG_CONTENT);
                String rating = item.getString(TAG_RATING);

                ReviewData reviewData = new ReviewData();
                reviewData.setName(name);
                reviewData.setEvaluation(Integer.parseInt(rating));
                reviewData.setReview(content);
                mReviewArrayList.add(reviewData);
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}
