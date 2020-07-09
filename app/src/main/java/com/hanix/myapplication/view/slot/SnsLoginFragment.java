package com.hanix.myapplication.view.slot;

import android.media.MediaSession2;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.hanix.myapplication.R;
import com.hanix.myapplication.view.event.OnSingleClickListener;
import com.hanix.myapplication.view.slot.sns.SessionCallback;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import butterknife.BindView;

/**
 * https://lakue.tistory.com/40
 */
public class SnsLoginFragment extends Fragment {

    static ViewGroup rootView;
    Session session;
    SessionCallback sessionCallback;

    //ButterKnife
    @BindView(R.id.kakaoCustomLogin)
    Button kakaoCustomLogin;
    @BindView(R.id.kakaoCustomLogout)
    Button kakaoCustomLogout;
    @BindView(R.id.kakaoLayout)
    LinearLayout kakaoLayout;
    @BindView(R.id.snsLayout)
    ConstraintLayout snsLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sns_login, container, false);

        session = Session.getCurrentSession();
        session.addCallback(sessionCallback);

        kakaoCustomLogin.setOnClickListener(kakaoClick);
        kakaoCustomLogout.setOnClickListener(kakaoClick);

        return rootView;
    }

    private OnSingleClickListener kakaoClick = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.kakaoCustomLogin :
                    if(session != null)
                        session.open(AuthType.KAKAO_LOGIN_ALL, getActivity());
                    break;

                case R.id.kakaoCustomLogout :
                    UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                        @Override
                        public void onCompleteLogout() {
                            Toast.makeText(getContext(), "로그아웃 되었습니다.", Toast.LENGTH_LONG).show();
                        }
                    });
            }
        }
    };
}