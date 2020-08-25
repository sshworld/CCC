package com.example.cloudcenter_v2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NoticeActivity extends AppCompatActivity {

    LinearLayout drawer4;

    Button btn_goHome, btn_my_booking, btn_my_page, btn_logout;

    ImageButton btn_open, btn_close;

    Animation tranlateLeftAnim, tranlateRightAnim;

    TextView myName4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        Intent goMyPage = getIntent();
        final String userID = getIntent().getStringExtra("userID");
        final String userName = getIntent().getStringExtra("userName");

        btn_open = findViewById(R.id.btn_open4);
        btn_close = findViewById(R.id.btn_close4);
        btn_goHome = findViewById(R.id.btn_goHome4);
        btn_my_booking = findViewById(R.id.btn_my_booking4);
        btn_my_page = findViewById(R.id.btn_my_page4);
        btn_logout = findViewById(R.id.btn_logout4);

        myName4 = findViewById(R.id.myName4);
        myName4.setText(userName + "님 안녕하세요.");


        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer4 = findViewById(R.id.drawer4);
                //anim 폴더의 애니메이션을 가져와서 준비
                tranlateLeftAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_left);

                //페이지 슬라이딩 이벤트가 발생했을때 애니메이션이 시작 됐는지 종료 됐는지 감지할 수 있다.
                NoticeActivity.SlidingPageAnimationListener animListener = new NoticeActivity.SlidingPageAnimationListener();
                tranlateLeftAnim.setAnimationListener(animListener);

                drawer4.setVisibility(View.VISIBLE);
                drawer4.startAnimation(tranlateLeftAnim);

                btn_open.setVisibility(View.INVISIBLE);
                btn_close.setVisibility(View.VISIBLE);

            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer4 = findViewById(R.id.drawer4);
                //anim 폴더의 애니메이션을 가져와서 준비
                tranlateRightAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_right);

                //페이지 슬라이딩 이벤트가 발생했을때 애니메이션이 시작 됐는지 종료 됐는지 감지할 수 있다.
                NoticeActivity.SlidingPageAnimationListener animListener = new NoticeActivity.SlidingPageAnimationListener();
                tranlateRightAnim.setAnimationListener(animListener);


                drawer4.setVisibility(View.INVISIBLE);
                drawer4.startAnimation(tranlateRightAnim);
                btn_open.setVisibility(View.VISIBLE);
                btn_close.setVisibility(View.INVISIBLE);

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
}