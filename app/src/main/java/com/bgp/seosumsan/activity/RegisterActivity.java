package com.bgp.seosumsan.activity;

import android.app.ProgressDialog;
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

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends BasicAcitivty {
    private static String TAG = "Task";

    private EditText mEditTextID;
    private EditText mEditTextName;
    private EditText mEditTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
    }

    private void init() {
        mEditTextID = (EditText)findViewById(R.id.create_id);
        mEditTextName = (EditText)findViewById(R.id.create_nickname);
        mEditTextPassword = (EditText)findViewById(R.id.create_password);

        Button buttonLogin = (Button)findViewById(R.id.login_button);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkTask task = new NetworkTask();
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", mEditTextID.getText().toString());
                params.put("name", mEditTextName.getText().toString());
                params.put("password", mEditTextPassword.getText().toString());
                task.execute(params);
            }
        });

        TextView backButton = (TextView)findViewById(R.id.login_cancel);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public class NetworkTask extends AsyncTask<Map<String, String>, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(RegisterActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected String doInBackground(Map<String, String>... maps) {
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://" + HttpClient.Builder.getIP_ADDRESS() + "/competition/register.php");
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

            if (result.compareTo("0") == 0) {
                Toast.makeText(getBaseContext(), "회원가입이 완료되었습니다.",Toast.LENGTH_LONG).show();
                finish();
            }
            else if (result.compareTo("1") == 0) {
                Toast.makeText(getBaseContext(), "입력 정보를 다시 확인해 주세요..",Toast.LENGTH_LONG).show();
            }
            else if (result.compareTo("2") == 0) {
                Toast.makeText(getBaseContext(), "회원가입 실패...",Toast.LENGTH_LONG).show();
            }
            else if (result.compareTo("3") == 0) {
                Toast.makeText(getBaseContext(), "중복된 아이디가 존재합니다...",Toast.LENGTH_LONG).show();
            }
        }
    }
}
