package com.bgp.seosumsan.activity;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bgp.seosumsan.Adapter.PageAdapter;
import com.bgp.seosumsan.Adapter.PathAdapter;
import com.bgp.seosumsan.DTO.PathData;
import com.bgp.seosumsan.HttpClient;
import com.bgp.seosumsan.R;
import com.bgp.seosumsan.Adapter.ReviewAdapter;
import com.bgp.seosumsan.DTO.ReviewData;
import com.bgp.seosumsan.SaveSharedPreference;
import com.bgp.seosumsan.Adapter.SightsAdapter;
import com.bgp.seosumsan.DTO.SightsData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InfoActivity extends BasicAcitivty {
    private static String TAG = "Task";
    private String mid;
    private String mJsonString;

    private LinearLayout content1;
    private LinearLayout content2;
    private LinearLayout content3;
    private LinearLayout content4;
    private LinearLayout content5;

    private TabLayout tabLayout;
    private ArrayList<SightsData> mSightsArrayList;
    private SightsAdapter mSightsAdapter;
    private RecyclerView mGridView;

    private ArrayList<SightsData> mRestaurantArrayList;
    private SightsAdapter mRestaurantAdapter;
    private RecyclerView mListView;

    private TextView title;
    private TextView mountainName;
    private TextView mountainAddress;
    private TextView mountainDirections;
    private TextView mountainHeight;
    private TextView mountainInfo;

    private PathAdapter pPathAdapter;
    private ArrayList<PathData> pPathArrayList;
    private RecyclerView pListView;
    private NestedScrollView pScrollView;
    private TextView path_1;
    private TextView path_2;
    private TextView path_3;

    // dot + slider instance
    private PageAdapter pageAdapter;
    private ViewPager viewPager;
    private LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    private RecyclerView mReview;
    private ArrayList<ReviewData> mReviewArrayList;
    private ReviewAdapter mReviewAdapter;

    private ImageView buttonReview;
    private PopupWindow mReviewPopupWindow;

    private static RatingBar enroll_RatingBar;
    private static EditText enroll_EditText;

    private ReviewData reviewData;

    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        init();
        addNavigationView();
        addItem();
    }

    private void init() {
        content1 = (LinearLayout) findViewById(R.id.content1);
        content2 = (LinearLayout) findViewById(R.id.content2);
        content3 = (LinearLayout) findViewById(R.id.content3);
        content4 = (LinearLayout) findViewById(R.id.content4);
        content5 = (LinearLayout) findViewById(R.id.content5);

        title = (TextView) findViewById(R.id.title_text);
        mountainName = (TextView) findViewById(R.id.mountain_name);
        mountainDirections = (TextView) findViewById(R.id.mountain_subway);
        mountainHeight = (TextView) findViewById(R.id.mountain_height);
        mountainAddress = (TextView) findViewById(R.id.mountain_loca);
        mountainInfo = (TextView) findViewById(R.id.mountain_info);

        //경로 화면의 리사이클러뷰
        pListView = (RecyclerView)findViewById(R.id.path_listitem);
        LinearLayoutManager pLinearLayoutManager = new LinearLayoutManager(this);
        pListView.setLayoutManager(pLinearLayoutManager);
        pPathArrayList = new ArrayList<>();
        pPathAdapter = new PathAdapter(pPathArrayList);
        pListView.setAdapter(pPathAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(pListView.getContext(),
                pLinearLayoutManager.getOrientation());
        pListView.addItemDecoration(dividerItemDecoration);

        pScrollView = (NestedScrollView)findViewById(R.id.path_scroll);

        path_1 = (TextView)findViewById(R.id.first_path);
        path_1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Intent intent = new Intent(getBaseContext(), PopupActivity.class);
                //startActivity(intent);
                pScrollView.smoothScrollTo(0, 500);
                //scrollToView(test,pScrollView, 0);
            }
        });

        path_2 = (TextView)findViewById(R.id.second_path);
        path_2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                pScrollView.smoothScrollTo(0, 700);
            }
        });

        path_3 = (TextView)findViewById(R.id.third_path);
        path_3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                pScrollView.smoothScrollTo(0, 800);
            }
        });

        pPathAdapter.setOnItemClickListener(
                new PathAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int pos) {
                        Intent intent = new Intent(getBaseContext(), PopupActivity.class);
                        intent.putExtra("id", pPathArrayList.get(pos).getMember_id());
                        startActivity(intent);
                    }
                }
        );

        mGridView = (RecyclerView) findViewById(R.id.listView_sights_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mGridView.setLayoutManager(mLayoutManager);
        mSightsArrayList = new ArrayList<>();
        mSightsAdapter = new SightsAdapter(mSightsArrayList);
        mGridView.setAdapter(mSightsAdapter);

        mListView = (RecyclerView) findViewById(R.id.listView_restaurant_list);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this);
        mListView.setLayoutManager(mLayoutManager2);
        mRestaurantArrayList = new ArrayList<>();
        mRestaurantAdapter = new SightsAdapter(mRestaurantArrayList);
        mListView.setAdapter(mRestaurantAdapter);

        mReview = (RecyclerView) findViewById(R.id.listView_review_list);
        final RecyclerView.LayoutManager ReviewLayoutManager = new LinearLayoutManager(this);
        mReview.setLayoutManager(ReviewLayoutManager);
        mReviewArrayList = new ArrayList<>();
        mReviewAdapter = new ReviewAdapter(mReviewArrayList);
        mReview.setAdapter(mReviewAdapter);

        buttonReview = (ImageView) findViewById(R.id.review_popup_button);
        buttonReview.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                View popupView = getLayoutInflater().inflate(R.layout.review_write, null);
                mReviewPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                mReviewPopupWindow.setFocusable(true);

                mReviewPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                Button enroll_Button = (Button) popupView.findViewById(R.id.review_enroll_button);
                Button exit_Button=(Button) popupView.findViewById(R.id.review_enroll_exit);
                enroll_RatingBar = (RatingBar) popupView.findViewById(R.id.review_enroll_ratingBar);
                enroll_EditText = (EditText) popupView.findViewById(R.id.review_enroll_editText);
                reviewData = new ReviewData();
                enroll_RatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                        reviewData.setEvaluation((int)enroll_RatingBar.getRating());
                    }
                });
                enroll_Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reviewData.setReview(enroll_EditText.getText().toString());

                        Posting task = new Posting();
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("uid", SaveSharedPreference.getUserId(InfoActivity.this));
                        params.put("content", enroll_EditText.getText().toString());
                        params.put("rating", Integer.toString((int)enroll_RatingBar.getRating()));
                        params.put("mid", mid);
                        task.execute(params);

                        mReviewPopupWindow.dismiss();
                    }
                });
                exit_Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mReviewPopupWindow.dismiss();
                    }
                });
            }
        });

        // dot + slider
        viewPager = (ViewPager) findViewById(R.id.view);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for(int i = 0; i<dotscount;i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.non_active_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("개요"));
        tabLayout.addTab(tabLayout.newTab().setText("경로"));
        tabLayout.addTab(tabLayout.newTab().setText("명소"));
        tabLayout.addTab(tabLayout.newTab().setText("맛집"));
        tabLayout.addTab(tabLayout.newTab().setText("후기"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                changeView(pos);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // do nothing
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // do nothing
            }
        });

        backButton = (ImageView) findViewById(R.id.backbutton);
        backButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        mid = extras.getString("mid");
    }

    private void changeView(int index) {
        switch (index) {
            case 0 :
                content1.setVisibility(View.VISIBLE);
                content2.setVisibility(View.GONE);
                content3.setVisibility(View.GONE);
                content4.setVisibility(View.GONE);
                content5.setVisibility(View.GONE);
                break ;
            case 1 :
                content1.setVisibility(View.GONE);
                content2.setVisibility(View.VISIBLE);
                content3.setVisibility(View.GONE);
                content4.setVisibility(View.GONE);
                content5.setVisibility(View.GONE);
                break ;
            case 2 :
                content1.setVisibility(View.GONE);
                content2.setVisibility(View.GONE);
                content3.setVisibility(View.VISIBLE);
                content4.setVisibility(View.GONE);
                content5.setVisibility(View.GONE);
                break ;
            case 3 :
                content1.setVisibility(View.GONE);
                content2.setVisibility(View.GONE);
                content3.setVisibility(View.GONE);
                content4.setVisibility(View.VISIBLE);
                content5.setVisibility(View.GONE);
                break ;
            case 4 :
                content1.setVisibility(View.GONE);
                content2.setVisibility(View.GONE);
                content3.setVisibility(View.GONE);
                content4.setVisibility(View.GONE);
                content5.setVisibility(View.VISIBLE);
                break ;
        }
    }

    private void addItem() {
        NetworkTask task = new NetworkTask();
        Map<String, String> params = new HashMap<String, String>();
        params.put("mid", mid);
        task.execute(params);
    }

    private void addNavigationView(){
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

    public class NetworkTask extends AsyncTask<Map<String, String>, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(InfoActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected String doInBackground(Map<String, String>... maps) {
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://" + HttpClient.Builder.getIP_ADDRESS() + "/competition/get_minfo.php");
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
        String TAG_ADDRESS = "address";
        String TAG_HEIGHT = "height";
        String TAG_DIRECTIONS = "directions";
        String TAG_INFO = "info";
        String TAG_IMAGE = "images";
        String TAG_SIGHTS = "sights";
        String TAG_RESTAURANT = "restaurant";
        String TAG_REVIEW = "review";
        String TAG_PATH = "path";

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);

                String name = item.getString(TAG_NAME);
                String address = item.getString(TAG_ADDRESS);
                String height = item.getString(TAG_HEIGHT);
                String directions = item.getString(TAG_DIRECTIONS);
                String info = item.getString(TAG_INFO);

                ArrayList<Bitmap> bitmaps = new ArrayList<>();
                JSONArray images = item.getJSONArray(TAG_IMAGE);
                for(int j=0;j<images.length();j++){
                    JSONObject image = images.getJSONObject(j);
                    Bitmap bitmap = StringToBitMap(image.getString("image"));
                    bitmaps.add(bitmap);
                }

                JSONArray sights = item.getJSONArray(TAG_SIGHTS);
                for(int j=0;j<sights.length();j++) {
                    JSONObject image = sights.getJSONObject(j);
                    Bitmap bitmap = StringToBitMap(image.getString("image"));

                    SightsData sightsData = new SightsData();
                    sightsData.setMember_name(image.getString("name"));
                    sightsData.setMember_image(bitmap);
                    mSightsArrayList.add(sightsData);
                }

                title.setText(name);
                mountainName.setText(name);
                mountainAddress.setText("위치    " + address);
                mountainHeight.setText("높이    " + height);
                mountainDirections.setText("근처 지하철 위치    " + directions);
                mountainInfo.setText(info);

                pageAdapter = new PageAdapter(bitmaps, this);
                viewPager.setAdapter(pageAdapter);
                dotscount=pageAdapter.getCount();
                dots=new ImageView[dotscount];

                for(int j = 0; j<dotscount;j++){
                    dots[j] = new ImageView(this);
                    dots[j].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    params.setMargins(8,0,8,0);
                    sliderDotspanel.addView(dots[j], params);

                }

                dots[0].setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.active_dot));

                JSONArray review = item.getJSONArray(TAG_REVIEW);
                for(int j=0;j<review.length();j++) {
                    JSONObject r = review.getJSONObject(j);
                    reviewData = new ReviewData();
                    reviewData.setName(r.getString("name"));
                    reviewData.setEvaluation(Integer.parseInt(r.getString("rating")));
                    reviewData.setReview(r.getString("content"));
                    mReviewArrayList.add(reviewData);
                }

                JSONArray path = item.getJSONArray(TAG_PATH);
                for(int j=0;j<path.length();j++) {
                    JSONObject r = path.getJSONObject(j);
                    PathData pathData = new PathData();
                    pathData.setMember_id(r.getString("id"));
                    pathData.setMember_name(r.getString("name"));
                    pathData.setMember_start(r.getString("start"));
                    pathData.setMember_end(r.getString("end"));
                    pPathArrayList.add(pathData);
                }

                JSONArray restaurant = item.getJSONArray(TAG_RESTAURANT);
                for(int j=0;j<restaurant.length();j++) {
                    JSONObject image = restaurant.getJSONObject(j);
                    Bitmap bitmap = StringToBitMap(image.getString("image"));

                    SightsData sightsData = new SightsData();
                    sightsData.setMember_name(image.getString("name"));
                    sightsData.setMember_image(bitmap);
                    mRestaurantArrayList.add(sightsData);
                }
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

    public class Posting extends AsyncTask<Map<String, String>, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(InfoActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected String doInBackground(Map<String, String>... maps) {
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://" + HttpClient.Builder.getIP_ADDRESS() + "/competition/add_review.php");
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
            Log.d(TAG, "POST response  - " + result);

            if (result.length() > 0) {
                reviewData.setName(result);
                mReviewArrayList.add(reviewData);

                Toast.makeText(getBaseContext(), "리뷰를 작성했습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
