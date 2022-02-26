package com.wuda.bbs.logic;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.Account;
import com.wuda.bbs.logic.bean.response.AccountResponse;
import com.wuda.bbs.logic.bean.response.BaseResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;
import com.wuda.bbs.utils.network.BBSCallback2;
import com.wuda.bbs.utils.network.BBSCallback22;
import com.wuda.bbs.utils.network.BBSCallback3;
import com.wuda.bbs.utils.network.MobileService;
import com.wuda.bbs.utils.network.RootService;
import com.wuda.bbs.utils.network.RootServiceGBKWrapper;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.networkResponseHandler.AccountResponseHandler;
import com.wuda.bbs.utils.networkResponseHandler.BaseResponseHandler;
import com.wuda.bbs.utils.networkResponseHandler.DetailBoardHandler;
import com.wuda.bbs.utils.networkResponseHandler.FavBoardHandler;
import com.wuda.bbs.utils.networkResponseHandler.FindPasswordResponseHandler;
import com.wuda.bbs.utils.networkResponseHandler.FriendResponseHandler;
import com.wuda.bbs.utils.networkResponseHandler.HotArticleHandler;
import com.wuda.bbs.utils.networkResponseHandler.MailContentHandler;
import com.wuda.bbs.utils.networkResponseHandler.MailListHandler;
import com.wuda.bbs.utils.networkResponseHandler.TodayNewArticleHandler;
import com.wuda.bbs.utils.networkResponseHandler.RecommendArticleHandler;
import com.wuda.bbs.utils.networkResponseHandler.RegisterResponseHandler;
import com.wuda.bbs.utils.networkResponseHandler.SetPasswordResponseHandler;
import com.wuda.bbs.utils.networkResponseHandler.TopicArticleHandler;
import com.wuda.bbs.utils.networkResponseHandler.UserInfoResponseHandler;
import com.wuda.bbs.utils.networkResponseHandler.WebResultHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkEntry {

    static RootService mRootService = ServiceCreator.create(RootService.class);
    static RootServiceGBKWrapper mRootServiceGBKWrapper = new RootServiceGBKWrapper(mRootService);
    static MobileService mMobileService = ServiceCreator.create(MobileService.class);


    // *******************************
    // Account
    // *******************************


    public static void login(Account account, AccountResponseHandler responseHandler) {

//        Map<String, String> form = new HashMap<>();
//        form.put("userId", account.getId());

//        mMobileService.get("userInfo", form).enqueue(new BBSCallback3(responseHandler));
        _login(account, responseHandler);
    }

    private static void _login(Account account, AccountResponseHandler responseHandler) {

        Map<String, String> form = new HashMap<>();
        form.put("app", "login");
        form.put("id", account.getId());
        form.put("passwd", account.getPasswd());

        // results in cookies
        mMobileService.post(form).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                List<String> cookies = response.headers().values("Set-Cookie");

                Map<String, String> cookiesFilter = new HashMap<>();

                for (String cookie : cookies) {
                    String[] tmp = cookie.split(";");
                    if (tmp.length > 0) {
                        String[] name_value = tmp[0].split("=");
                        if (name_value.length == 2) {
                            cookiesFilter.put(name_value[0], name_value[1]);
                        }
                    }
                }

                AccountResponse accountResponse;

                // 是否成功登录
                String userId = cookiesFilter.get("UTMPUSERID");
                if (userId == null)
                    userId = "guest";
                if (userId.equals("guest") || userId.equals("deleted")) {
                    accountResponse = new AccountResponse();
                    accountResponse.setResultCode(ResultCode.LOGIN_ERR);
                    accountResponse.setMassage("登录失败，请检查帐号和密码！");
                    responseHandler.onResponseHandled(accountResponse);
                    return;
                }

                responseHandler.onResponseHandled(new AccountResponse(account));
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                AccountResponse accountResponse = new AccountResponse();
                accountResponse.setResultCode(ResultCode.CONNECT_ERR);
                responseHandler.onResponseHandled(accountResponse);
            }
        });
    }

    public static void logout(BaseResponseHandler responseHandler) {
        mRootService.get("bbslogout.php").enqueue(new BBSCallback2(responseHandler));
    }

    public static void register(Map<String, String> form, RegisterResponseHandler responseHandler) {
        mRootServiceGBKWrapper.post("bbsreg.php", form).enqueue(new BBSCallback3(responseHandler));
    }

    public static void setPassword(Map<String, String> form, SetPasswordResponseHandler responseHandler) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("do", "");

        mRootService.post("bbspwd.php", queryMap, form).enqueue(new BBSCallback2(responseHandler));
    }

    public static void findPassword(Map<String, String> form, BaseResponseHandler responseHandler) {

        mRootService.post("r/doreset.php", form).enqueue(new BBSCallback3(new FindPasswordResponseHandler() {
            @Override
            public void onResponseHandled(BaseResponse baseResponse) {
                responseHandler.onResponseHandled(baseResponse);
            }
        }));
    }

    // *******************************
    // User / Friend
    // *******************************

    public static void requestUserInfo(String userId, UserInfoResponseHandler responseHandler) {
        Map<String, String> form = new HashMap<>();
        form.put("userId", userId);

        mMobileService.get("userInfo", form).enqueue(new BBSCallback3(responseHandler));
    }


    public static void requestFriend(FriendResponseHandler responseHandler) {
        Map<String, String> form = new HashMap<>();
        form.put("list", "all");

        mMobileService.get("friend", form).enqueue(new BBSCallback2(responseHandler));
    }

    // *******************************
    // Article
    // *******************************
    public static void requestRecommendArticle(RecommendArticleHandler responseHandler) {
        mMobileService.get("recomm").enqueue(new BBSCallback3(responseHandler));
    }

    public static void requestTodayNewArticle(TodayNewArticleHandler responseHandler) {
        mRootService.get("wForum/newtopic.php").enqueue(new BBSCallback3(responseHandler));
    }

    public static void requestHotArticle(HotArticleHandler responseHandler) {
        mMobileService.get("hot").enqueue(new BBSCallback3(responseHandler));
    }

    public static void requestTopicArticle(Map<String, String> form, TopicArticleHandler responseHandler) {
        mMobileService.get("topics", form).enqueue(new BBSCallback3(responseHandler));
    }

    // *******************************
    // Board
    // *******************************

    public static void requestDetailBoard(DetailBoardHandler responseHandler) {
        mMobileService.get("boards").enqueue(new BBSCallback3(responseHandler));
    }

    public static void requestFavBoard(FavBoardHandler responseHandler) {
        mMobileService.get("favor").enqueue(new BBSCallback2(responseHandler));
    }

    // *******************************
    // Mail
    // *******************************



    public static void requestMailList1(Map<String, String> form, MailListHandler responseHandler) {
        mMobileService.get("mail", form).enqueue(new BBSCallback22<>(responseHandler));
    }


    public static void requestMailContent(Map<String, String> form, MailContentHandler responseHandler) {
        mMobileService.get("mail", form).enqueue(new BBSCallback22<>(responseHandler));
    }

    public static void sendMail(Map<String, String> form, WebResultHandler responseHandler) {
        mRootServiceGBKWrapper.post("wForum/dosendmail.php", form).enqueue(new BBSCallback22<>(responseHandler));
    }

    // *******************************
    // Article
    // *******************************

    // *******************************
    // Article
    // *******************************

    // *******************************
    // Article
    // *******************************

}
