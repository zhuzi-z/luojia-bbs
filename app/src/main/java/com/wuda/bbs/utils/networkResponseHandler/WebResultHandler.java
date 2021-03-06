package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.bbs.WebResult;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;

public abstract class WebResultHandler implements ContentResponseHandler<WebResult>{
    @Override
    public ContentResponse<WebResult> handleNetworkResponse(@NonNull byte[] data) {
        ContentResponse<WebResult> response =new ContentResponse<>();
        try {
            String html = new String(data, "GBK");
            Document doc = Jsoup.parse(html);
            Elements tables = doc.getElementsByClass("TableBody1");
            if (!tables.isEmpty()) {
                Element tb = tables.get(tables.size()-1);
                String text = tb.text();
                if (text.contains("错误")) {
                    response.setResultCode(ResultCode.SERVER_HANDLE_ERR);
                    response.setMassage(text);
                } else{
                    response.setContent(new WebResult(tb.text()));
                }
            } else {
                tables = doc.getElementsByClass("TableBody2");
                if (!tables.isEmpty()) {
                    Element tb = tables.get(tables.size()-1);
                    response.setContent(new WebResult(tb.text()));
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            response.setResultCode(ResultCode.HANDLE_DATA_ERR);
            response.setException(e);
        }

        return response;
    }
}
