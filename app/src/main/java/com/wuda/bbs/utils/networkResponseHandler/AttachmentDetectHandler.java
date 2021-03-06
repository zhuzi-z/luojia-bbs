package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AttachmentDetectHandler implements ContentResponseHandler<Boolean> {
    @Override
    public ContentResponse<Boolean> handleNetworkResponse(@NonNull byte[] data) {
        ContentResponse<Boolean> response;
        try {
            String html = new String(data, "GBK");
            // replyForm(board,reid,title,att,signum,sig,ano,outgo,lsave)
            // var o = new replyForm('PieFriends',0,' ',1,1,0,0,0,0);w(o.f());
            response = new ContentResponse<>();

            Pattern pattern = Pattern.compile("(?<=var o = new replyForm\\().*?(?=\\);w\\(o.f\\(\\)\\);)");
            Matcher matcher = pattern.matcher(html);
            if (matcher.find()) {
                String[] params = matcher.group().split(",");
                if (params.length != 9) {
                    response.setResultCode(ResultCode.UNMATCHED_CONTENT_ERR);
                    response.setMassage("未知数据错误");
                    response.setContent(false);
                } else {
                    response.setContent(params[3].equals("1"));
                }
            } else {
                response.setResultCode(ResultCode.PERMISSION_DENIED);
                response.setMassage("您无权在该版块发表文章（可能原因为您没有实名认证）");
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            response = new ContentResponse<>(ResultCode.HANDLE_DATA_ERR, e.getMessage());
            response.setContent(false);
        }

        return response;
    }
}
