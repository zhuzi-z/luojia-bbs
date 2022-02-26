package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.response.AccountResponse;
import com.wuda.bbs.logic.bean.response.BaseResponse;
import com.wuda.bbs.logic.bean.response.ContentResponse;

public abstract class AccountResponseHandler implements BaseResponseHandler {
    @Override
    public BaseResponse handleNetworkResponse(@NonNull byte[] data) {
        return new AccountResponse();
    }
}
