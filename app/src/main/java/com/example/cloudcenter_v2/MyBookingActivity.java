package com.example.cloudcenter_v2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.List;
import java.util.Map;

public class MyBookingActivity extends AppCompatActivity {

    TextView myName2;

    LinearLayout drawer2;

    Button btn_goHome, btn_my_page, btn_notice, btn_logout, noBooking;

    ImageButton btn_open, btn_close;

    Animation tranlateLeftAnim, tranlateRightAnim;

    ArrayList<JSONObject> arrayJson = new ArrayList<JSONObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_booking);

        Intent goMyBooking = getIntent();
        final String userID = getIntent().getStringExtra("userID");
        final String userName = getIntent().getStringExtra("userName");

        btn_open = findViewById(R.id.btn_open2);
        btn_close = findViewById(R.id.btn_close2);
        btn_goHome = findViewById(R.id.btn_goHome2);
        btn_my_page = findViewById(R.id.btn_my_page2);
        btn_notice = findViewById(R.id.btn_notice2);
        btn_logout = findViewById(R.id.btn_logout2);
        noBooking = (Button) findViewById(R.id.noBooking);
        myName2 = findViewById(R.id.myName2);
        myName2.setText(userName + "님 안녕하세요.");


        final List<String> myBookinglist = new ArrayList<>();
        final List<String> deleteBooking = new ArrayList<>();

        final ListView listView = findViewById(R.id.MyBookingList);

        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("response", "실행");
                    //JSONObject jsonObject = new JSONObject(response);
                    //배열 생성
                    JSONArray jsonArray = new JSONArray(response);
                    //배열 입력
                    //jsonArray.put(jsonObject);

                    for (int k = 0; k < jsonArray.length(); k++) {
                        JSONObject tempJson = jsonArray.getJSONObject(k);
                        arrayJson.add(tempJson);
                    }

                    JSONObject[] jsons = new JSONObject[arrayJson.size()];
                    arrayJson.toArray(jsons);

                    for (int i = 0; i < arrayJson.size(); i++) {
                        Log.d(arrayJson.size() + "", "arrayJson 사이즈");
                        myBookinglist.add(arrayJson.get(i).getString("academyName") + "/" + arrayJson.get(i).getString("bookingDate") + "/" + arrayJson.get(i).getString("bookingTime"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        MyBookingActivity.SelectBookingRequest selectBookingRequest = new MyBookingActivity.SelectBookingRequest(userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(MyBookingActivity.this);
        queue.add(selectBookingRequest);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, myBookinglist);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listView.isItemChecked(position) == true) {
                    deleteBooking.add(myBookinglist.get(position));

                } else {
                    deleteBooking.remove(myBookinglist.get(position));
                }
            }
        });

        noBooking.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
                int count = adapter.getCount();

                for (int i = count - 1; i >= 0; i--) {
                    if (checkedItems.get(i)) {
                        String[] array = myBookinglist.get(i).split("/");
                        Log.d(array[0], "학원 명");
                        MyBookingActivity.DeleteBookingRequest deleteBookingRequest = new MyBookingActivity.DeleteBookingRequest(userID, array[0], responseListener);
                        RequestQueue queue1 = Volley.newRequestQueue(MyBookingActivity.this);
                        queue1.add(deleteBookingRequest);
                        myBookinglist.remove(i);
                    }
                }

                // 모든 선택 상태 초기화.
                listView.clearChoices();

                adapter.notifyDataSetChanged();
            }
        });


        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer2 = findViewById(R.id.drawer2);
                //anim 폴더의 애니메이션을 가져와서 준비
                tranlateLeftAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_left);

                //페이지 슬라이딩 이벤트가 발생했을때 애니메이션이 시작 됐는지 종료 됐는지 감지할 수 있다.
                MyBookingActivity.SlidingPageAnimationListener animListener = new MyBookingActivity.SlidingPageAnimationListener();
                tranlateLeftAnim.setAnimationListener(animListener);

                drawer2.setVisibility(View.VISIBLE);
                drawer2.startAnimation(tranlateLeftAnim);

                btn_open.setVisibility(View.INVISIBLE);
                btn_close.setVisibility(View.VISIBLE);

                noBooking.setVisibility(View.INVISIBLE);

            }
        });
        /*-------------------------------사이드바 닫기-------------------------------------*/
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer2 = findViewById(R.id.drawer2);
                //anim 폴더의 애니메이션을 가져와서 준비
                tranlateRightAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_right);

                //페이지 슬라이딩 이벤트가 발생했을때 애니메이션이 시작 됐는지 종료 됐는지 감지할 수 있다.
                MyBookingActivity.SlidingPageAnimationListener animListener = new MyBookingActivity.SlidingPageAnimationListener();
                tranlateRightAnim.setAnimationListener(animListener);


                drawer2.setVisibility(View.INVISIBLE);
                drawer2.startAnimation(tranlateRightAnim);
                btn_open.setVisibility(View.VISIBLE);
                btn_close.setVisibility(View.INVISIBLE);

                noBooking.setVisibility(View.VISIBLE);

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

        btn_my_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goMyPage = new Intent(getApplicationContext(), MyPageActivity.class);
                goMyPage.putExtra("userID", userID);
                goMyPage.putExtra("userName", userName);
                startActivity(goMyPage);
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

    private class SlidingPageAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    public class SelectBookingRequest extends StringRequest {

        // 서버 URL 실행
        final static private String URL = "http://ssh9754.dothome.co.kr/selectBooking.php";
        private Map<String, String> map;

        public SelectBookingRequest(String userID, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);

            map = new HashMap<>();
            map.put("userID", userID);

        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }

    public class DeleteBookingRequest extends StringRequest {

        // 서버 URL 실행
        final static private String URL = "http://ssh9754.dothome.co.kr/deleteBooking.php";
        private Map<String, String> map;

        public DeleteBookingRequest(String userID, String academyName, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);

            map = new HashMap<>();
            map.put("userID", userID);
            map.put("academyName", academyName);

        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }
}