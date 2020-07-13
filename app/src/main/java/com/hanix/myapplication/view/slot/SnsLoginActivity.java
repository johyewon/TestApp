package com.hanix.myapplication.view.slot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.hanix.myapplication.R;
import com.hanix.myapplication.common.app.GLog;
import com.hanix.myapplication.view.MainActivity;
import com.hanix.myapplication.view.event.OnSingleClickListener;
import com.hanix.myapplication.view.slot.sns.SessionCallback;
import com.kakao.auth.AuthType;
import com.kakao.auth.Session;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

public class SnsLoginActivity extends AppCompatActivity {

    // kakao
    Session session;
    SessionCallback sessionCallback = new SessionCallback();

    static Button kakaoCustomLogin;
    static Button kakaoCustomLogout;
    LinearLayout kakaoLayout;

    // Google
    SignInButton googleLoginButton;
    Button googleLogoutButton;
    private FirebaseAuth mAuth;
    private GoogleSignInClient signInClient;
    private static final int GOOGLE_SIGN_IN = 9001;
    GoogleSignInOptions gso;


    ConstraintLayout snsLayout;
    ImageView backView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sns_login);

        backView = findViewById(R.id.backView);
        snsLayout = findViewById(R.id.snsLayout);

        backView.setOnClickListener(snsClick);

        // kakao
        session = Session.getCurrentSession();
        session.addCallback(sessionCallback);
        kakaoCustomLogin = findViewById(R.id.kakaoCustomLogin);
        kakaoCustomLogout = findViewById(R.id.kakaoCustomLogout);
        kakaoLayout = findViewById(R.id.kakaoLayout);
        kakaoCustomLogin.setOnClickListener(kakaoClick);
        kakaoCustomLogout.setOnClickListener(kakaoClick);
        kakaoCustomLogout.setVisibility(View.GONE);

        // google
        googleLoginButton = findViewById(R.id.googleLoginButton);
        googleLogoutButton = findViewById(R.id.googleLogoutButton);
        mAuth = FirebaseAuth.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
        googleLogoutButton.setOnClickListener(googleClick);
        googleLoginButton.setOnClickListener(googleClick);
        googleLogoutButton.setVisibility(View.GONE);
        // 로그인 하고 다음에 다시 로그인 하게 하지 않으려면 이 코드를 넣으면 됨
//        if(mAuth.getCurrentUser() != null) {
//            Intent intent = new Intent(getApplication(), 넘어갈액티비티.class);
//            startActivity(intent);
//            finish();
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GOOGLE_SIGN_IN) {
            GLog.d("here i am");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                getData(account);

            } catch (ApiException e) {
                GLog.e(e.getMessage(), e);
            }
        }
    }

    View.OnClickListener snsClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.backView) {
                Intent intent = new Intent(SnsLoginActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in_activity, R.anim.hold_activity);
                finish();
            }
        }
    };

    private void getData(GoogleSignInAccount acc) {
        if(acc != null && !acc.getEmail().equals("")) {
            googleLoginButton.setVisibility(View.VISIBLE);
            googleLogoutButton.setVisibility(View.VISIBLE);
            GLog.d("user email is " + acc.getEmail());
            GLog.d("user uid is " + acc.getId());
            GLog.d("user display name is " + acc.getDisplayName());
            GLog.d("user photoUrl is " + acc.getPhotoUrl());

        }
    }


    private void signIn() {
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }


    // Google login
    OnSingleClickListener googleClick = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.googleLoginButton :
                    GLog.d("googleLoginButton");
                    signIn();
                    break;

                case R.id.googleLogoutButton :
                    GLog.d("googleLogoutButton");
                    googleLoginButton.setVisibility(View.VISIBLE);
                    googleLogoutButton.setVisibility(View.GONE);
                    FirebaseAuth.getInstance().signOut();
                    break;
            }
        }
    };


    // kakao login
    OnSingleClickListener kakaoClick = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.kakaoCustomLogin :
                    GLog.d("kakaoCustomLogin");
                    if(session != null)
                        session.open(AuthType.KAKAO_LOGIN_ALL, SnsLoginActivity.this);
                    break;


                case R.id.kakaoCustomLogout :
                    GLog.d("kakaoCustomLogout");
                    kakaoCustomLogin.setVisibility(View.VISIBLE);
                    kakaoCustomLogout.setVisibility(View.GONE);
                    UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                        @Override
                        public void onCompleteLogout() {
                            Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_LONG).show();
                        }
                    });
                    break;
            }
        }
    };

    public static void setKakaoLogoutButtonVisible() {
        kakaoCustomLogout.setVisibility(View.VISIBLE);
        kakaoCustomLogin.setVisibility(View.GONE);
    }
}