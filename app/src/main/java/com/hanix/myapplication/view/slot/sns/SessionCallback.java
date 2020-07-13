package com.hanix.myapplication.view.slot.sns;

import com.hanix.myapplication.common.app.GLog;
import com.hanix.myapplication.view.slot.SnsLoginActivity;
import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;

public class SessionCallback implements ISessionCallback {

    // 로그인에 성공한 상태
    @Override
    public void onSessionOpened() {
        requestMe();
    }

    // 로그인에 실패한 상태
    @Override
    public void onSessionOpenFailed(KakaoException exception) {
        GLog.e(exception.getMessage(), exception);
    }

    public void requestMe() {
        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                GLog.e("KAKAO_API : 세션이 닫혀 있음 : " + errorResult);
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                GLog.e("KAKAO_API : 사용자 정보 요청 실패: " + errorResult);
            }

            @Override
            public void onSuccess(MeV2Response result) {
                GLog.d("KAKAO_API : 사용자 아이디: " + result.getId());

                UserAccount account = result.getKakaoAccount();
                if(account != null) {

                    SnsLoginActivity.setKakaoLogoutButtonVisible();
                    String email = account.getEmail();

                    if(email != null) {
                        GLog.d("사용자 이메일 : " + email);
                    } else if(account.emailNeedsAgreement() == OptionalBoolean.TRUE) {
                        // 동의 요청 후 이메일 획득 가능
                        // 단, 선택 동의로 설정되어있다면 서비스 이용 시나리오 상에서 반드시 필요한 경우에만 요청해야 합니다.
                   } else {
                        // 이메일 획득 불가
                    }

                    Profile profile = account.getProfile();

                    if(profile != null) {
                        GLog.d("KAKAO_API nickname: " + profile.getNickname());
                        GLog.d("KAKAO_API profile image: " + profile.getProfileImageUrl());
                        GLog.d("KAKAO_API thumbnail image: " + profile.getThumbnailImageUrl());
                    } else if (account.profileNeedsAgreement() == OptionalBoolean.TRUE) {
                        // 동의 요청 후 프로필 정보 획득 가능
                    } else {
                        // 프로필 획득 불가
                    }

                }
            }
        });
    }
}
