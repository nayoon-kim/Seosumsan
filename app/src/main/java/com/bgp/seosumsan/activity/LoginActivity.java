package com.bgp.seosumsan.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bgp.seosumsan.HttpClient;
import com.bgp.seosumsan.R;
import com.bgp.seosumsan.SaveSharedPreference;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BasicAcitivty {
    private static String TAG = "Task";

    private EditText mEditTextID;
    private EditText mEditTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        mEditTextID = (EditText)findViewById(R.id.login_id);
        mEditTextPassword = (EditText)findViewById(R.id.login_password);

        Button buttonLogin = (Button)findViewById(R.id.login_button);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkTask task = new NetworkTask();
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", mEditTextID.getText().toString());
                params.put("password", mEditTextPassword.getText().toString());
                task.execute(params);

                mEditTextID.setText("");
                mEditTextPassword.setText("");
            }
        });

        TextView buttonRegister = (TextView)findViewById(R.id.login_create);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public class NetworkTask extends AsyncTask<Map<String, String>, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(LoginActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected String doInBackground(Map<String, String>... maps) {
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://" + HttpClient.Builder.getIP_ADDRESS() + "/competition/login.php");
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

            if (result.length() > 0){
                SaveSharedPreference.setUserId(LoginActivity.this, result);
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(getBaseContext(), "로그인 성공.",Toast.LENGTH_LONG).show();
                finish();
            }
            else {
                Toast.makeText(getBaseContext(), "아이디나 비밀번호를 다시 확인해 주세요.",Toast.LENGTH_LONG).show();
            }
        }
    }
}
