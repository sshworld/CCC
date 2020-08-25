package com.example.cloudcenter_v2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyPageActivity extends AppCompatActivity {

    ArrayList<JSONObject> arrayJson = new ArrayList<JSONObject>();

    LinearLayout drawer3;

    Button btn_goHome, btn_my_booking, btn_notice, btn_logout, btn_updateValue, btn_cancleValue;

    ImageButton btn_open, btn_close;

    Animation tranlateLeftAnim, tranlateRightAnim;

    TextView nowRoadValue, nowAdrValue, myName3;

    EditText newRoadValue, newAdrValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        Intent goMyPage = getIntent();
        final String userID = getIntent().getStringExtra("userID");
        final String userName = getIntent().getStringExtra("userName");

        btn_open = findViewById(R.id.btn_open3);
        btn_close = findViewById(R.id.btn_close3);
        btn_goHome = findViewById(R.id.btn_goHome3);
        btn_my_booking = findViewById(R.id.btn_my_booking3);
        btn_notice = findViewById(R.id.btn_notice3);
        btn_logout = findViewById(R.id.btn_logout3);

        nowRoadValue = findViewById(R.id.nowRoadValue);
        nowAdrValue = findViewById(R.id.nowAdrValue);
        newRoadValue = findViewById(R.id.newRoadValue);
        newAdrValue = findViewById(R.id.newAdrValue);
        btn_updateValue = findViewById(R.id.btn_updateValue);
        btn_cancleValue = findViewById(R.id.btn_cancleValue);

        myName3 = findViewById(R.id.myName3);
        myName3.setText(userName + "님 안녕하세요.");

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("response", "실행");
                    JSONObject jsonObject = new JSONObject(response);
                    //배열 생성
                    JSONArray jsonArray = new JSONArray();
                    //배열 입력
                    jsonArray.put(jsonObject);

                    for(int k = 0; k < jsonArray.length(); k++){
                        JSONObject tempJson = jsonArray.getJSONObject(k);
                        arrayJson.add(tempJson);
                    }

                    JSONObject[] jsons = new JSONObject[arrayJson.size()];
                    arrayJson.toArray(jsons);

                    nowRoadValue.setText(arrayJson.get(0).getString("userLoad"));
                    nowAdrValue.setText(arrayJson.get(0).getString("userAddr"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        MyPageActivity.MyAddrRequest myAddrRequest = new MyPageActivity.MyAddrRequest(userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(MyPageActivity.this);
        queue.add(myAddrRequest);

        btn_updateValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userLoad = newRoadValue.getText().toString();
                String userAddr = newAdrValue.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                Toast.makeText(getApplicationContext(), "주소등록에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MyPageActivity.this, MainActivity.class);
                                intent.putExtra("userID", userID);
                                startActivity(intent);
                                finish();
                            } else { // 회원등록에 실패할 경우
                                Toast.makeText(getApplicationContext(), "주소등록에 실패하셨습니다.", Toast.LENGTH_LONG).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                // 서버로 Volley를 이용해서 요청을 함
                MyAddrUpdateRequest myAddrUpdateRequest = new MyAddrUpdateRequest(userID, userLoad, userAddr, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MyPageActivity.this);
                queue.add(myAddrUpdateRequest);

            }
        });


        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer3 = findViewById(R.id.drawer3);
                //anim 폴더의 애니메이션을 가져와서 준비
                tranlateLeftAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_left);

                //페이지 슬라이딩 이벤트가 발생했을때 애니메이션이 시작 됐는지 종료 됐는지 감지할 수 있다.
                MyPageActivity.SlidingPageAnimationListener animListener = new MyPageActivity.SlidingPageAnimationListener();
                tranlateLeftAnim.setAnimationListener(animListener);

                drawer3.setVisibility(View.VISIBLE);
                drawer3.startAnimation(tranlateLeftAnim);

                btn_open.setVisibility(View.INVISIBLE);
                btn_close.setVisibility(View.VISIBLE);

                btn_updateValue.setVisibility(View.INVISIBLE);
                btn_cancleValue.setVisibility(View.INVISIBLE);

            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer3 = findViewById(R.id.drawer3);
                //anim 폴더의 애니메이션을 가져와서 준비
                tranlateRightAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_right);

                //페이지 슬라이딩 이벤트가 발생했을때 애니메이션이 시작 됐는지 종료 됐는지 감지할 수 있다.
                MyPageActivity.SlidingPageAnimationListener animListener = new MyPageActivity.SlidingPageAnimationListener();
                tranlateRightAnim.setAnimationListener(animListener);


                drawer3.setVisibility(View.INVISIBLE);
                drawer3.startAnimation(tranlateRightAnim);
                btn_open.setVisibility(View.VISIBLE);
                btn_close.setVisibility(View.INVISIBLE);

                btn_updateValue.setVisibility(View.VISIBLE);
                btn_cancleValue.setVisibility(View.VISIBLE);

            }
        });

        btn_goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goMain = new Intent(getApplicationContext(), MainActivity.class);
                goMain.putExtra("userID", userID);
                startActivity(goMain);
                finish();
            }
        });

        btn_my_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goMyBooking = new Intent(getApplicationContext(), MyBookingActivity.class);
                goMyBooking.putExtra("userID", userID);
                goMyBooking.putExtra("userName", userName);
                startActivity(goMyBooking);
                finish();
            }
        });

        btn_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goNotice = new Intent(getApplicationContext(), NoticeActivity.class);
                goNotice.putExtra("userID", userID);
                goNotice.putExtra("userName", userName);
                startActivity(goNotice);
                finish();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(goLogin);
                finish();
            }
        });
    }

    private class SlidingPageAnimationListener implements Animation.AnimationListener{
        @Override public void onAnimationStart(Animation animation) {

        }

        public void onAnimationEnd(Animation animation){

        }

        @Override public void onAnimationRepeat(Animation animation) {

        }
    }

    public class MyAddrRequest extends StringRequest {

        // 서버 URL 실행
        final static private String URL = "http://ssh9754.dothome.co.kr/selectAddr.php";
        private Map<String, String> map;

        public MyAddrRequest(String userID, Response.Listener<String> listener){
            super(Method.POST, URL, listener, null);

            map = new HashMap<>();
            map.put("userID", userID);

        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }

    public class MyAddrUpdateRequest extends StringRequest {

        // 서버 URL 실행
        final static private String URL = "http://ssh9754.dothome.co.kr/UpdateUser.php";
        private Map<String, String> map;

        public MyAddrUpdateRequest(String userID, String userLoad, String userAddr, Response.Listener<String> listener){
            super(Method.POST, URL, listener, null);

            map = new HashMap<>();
            map.put("userID", userID);
            map.put("userLoad", userLoad);
            map.put("userAddr", userAddr);

        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }
}