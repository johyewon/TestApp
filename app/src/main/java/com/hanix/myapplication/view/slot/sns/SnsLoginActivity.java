package com.hanix.myapplication.view.slot.sns;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.hanix.myapplication.R;
import com.hanix.myapplication.common.app.GLog;
import com.hanix.myapplication.view.MainActivity;
import com.hanix.myapplication.view.event.OnSingleClickListener;
import com.kakao.auth.AuthType;
import com.kakao.auth.Session;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import java.util.Objects;

public class SnsLoginActivity extends AppCompatActivity {

    // kakao
    Session session;
    SessionCallback sessionCallback = new SessionCallback();

    @SuppressLint("StaticFieldLeak")
    static Button kakaoCustomLogin;
    @SuppressLint("StaticFieldLeak")
    static Button kakaoCustomLogout;
    LinearLayout kakaoLayout;

    // Google
    SignInButton googleLoginButton;
    Button googleLogoutButton;
    private FirebaseAuth mAuth;
    private GoogleSignInClient signInClient;
    private static final int GOOGLE_SIGN_IN = 9001;
    GoogleSignInOptions gso;

    // naver
    OAuthLogin mOAuthLoginModule;
    OAuthLoginButton naverLoginButton;

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
        kakaoCustomLogin.setOnClickListener(loginButton);
        kakaoCustomLogout.setOnClickListener(loginButton);
        kakaoCustomLogout.setVisibility(View.GONE);

        // google
        googleLoginButton = findViewById(R.id.googleLoginButton);
        googleLogoutButton = findViewById(R.id.googleLogoutButton);
        mAuth = FirebaseAuth.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
        googleLogoutButton.setOnClickListener(loginButton);
        googleLoginButton.setOnClickListener(loginButton);
        googleLogoutButton.setVisibility(View.GONE);
/*
 - 로그인을 한 상태라면 재로그인 하게 해줌
        if(mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplication(), 넘어갈액티비티.class);
            startActivity(intent);
            finish();
        }
*/

        // naver
        mOAuthLoginModule = OAuthLogin.getInstance();
        naverLoginButton = findViewById(R.id.naverLoginButton);
        //  TODO : 검수 후에 다시 해야 함
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) {
            GLog.d("here i am");

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    GLog.d("firebaseAuthWithGoogle : " + account.getId());
                    getData(account);
                }
            } catch (ApiException e) {
                GLog.e(e.getMessage(), e);
            }
        }
    }

    View.OnClickListener snsClick = (view) -> {
        if (view.getId() == R.id.backView) {
            startActivity(new Intent(SnsLoginActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.fade_in_activity, R.anim.hold_activity);
            finish();
        }
    };

    private void getData(GoogleSignInAccount acc) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acc.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, (task) -> {
                    if (task.isSuccessful()) {
                        if (!Objects.equals(acc.getEmail(), "")) {
                            googleLoginButton.setVisibility(View.VISIBLE);
                            googleLogoutButton.setVisibility(View.VISIBLE);
                            GLog.d("user email is " + acc.getEmail());
                            GLog.d("user uid is " + acc.getId());
                            GLog.d("user display name is " + acc.getDisplayName());
                            GLog.d("user photoUrl is " + acc.getPhotoUrl());
                        } else {
                            GLog.d("is null");
                        }
                    } else {
                        GLog.d("user is null");
                    }
                });

    }


    private void signIn() {
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }


    OnSingleClickListener loginButton = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            int id = v.getId();
            if (id == R.id.googleLoginButton) {
                GLog.d("googleLoginButton");
                signIn();
            } else if (id == R.id.googleLogoutButton) {
                GLog.d("googleLogoutButton");
                googleLoginButton.setVisibility(View.VISIBLE);
                googleLogoutButton.setVisibility(View.GONE);
                FirebaseAuth.getInstance().signOut();
            } else if (id == R.id.kakaoCustomLogin) {
                GLog.d("kakaoCustomLogin");
                if (session != null)
                    session.open(AuthType.KAKAO_LOGIN_ALL, SnsLoginActivity.this);
            } else if (id == R.id.kakaoCustomLogout) {
                GLog.d("kakaoCustomLogout");
                kakaoCustomLogin.setVisibility(View.VISIBLE);
                kakaoCustomLogout.setVisibility(View.GONE);
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_LONG).show();
                    }
                });
            } else if (id == R.id.naverLoginButton) {
            } else if (id == R.id.naverLogoutButton) {
            }
        }
    };

    public static void setKakaoLogoutButtonVisible() {
        kakaoCustomLogout.setVisibility(View.VISIBLE);
        kakaoCustomLogin.setVisibility(View.GONE);
    }
}