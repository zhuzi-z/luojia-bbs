package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;
import com.wuda.bbs.utils.parser.HtmlParser;

import java.io.UnsupportedEncodingException;

public abstract class SetPasswordResponseHandler implements ContentResponseHandler<String> {

    @Override
    public ContentResponse<String> handleNetworkResponse(@NonNull byte[] data) {
        ContentResponse<String> response;
        try {
            response = HtmlParser.parseSetPasswordResponse(new String(data, "GBK"));
        } catch (UnsupportedEncodingException e) {
            response =new ContentResponse<>(ResultCode.HANDLE_DATA_ERR, e);
            e.printStackTrace();
        }
        return response;
    }
}
