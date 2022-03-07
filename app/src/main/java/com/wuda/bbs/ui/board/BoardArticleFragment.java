package com.wuda.bbs.ui.board;

import android.widget.Toast;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.BaseBoard;
import com.wuda.bbs.logic.bean.BriefArticle;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.utils.networkResponseHandler.TopicArticleHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardArticleFragment extends ArticleContainerFragment {

    BaseBoard board;

    @Override
    protected void requestArticleFromServer() {
        if (board == null) {
            article_srl.setRefreshing(false);
            return;
        }

        Map<String, String> form = new HashMap<>();
        int requestPage = mViewModel.articleResponse.getValue().getCurrentPage() + 1;
        form.put("page", Integer.toString(requestPage));
        form.put("board", board.getId());

        NetworkEntry.requestTopicArticle(form, new TopicArticleHandler() {
            @Override
            public void onResponseHandled(ContentResponse<List<BriefArticle>> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        article_srl.setRefreshing(false);
                    }
                });

                if (response.isSuccessful()) {
                    mViewModel.articleResponse.postValue(response);
                } else {
                    Toast.makeText(getContext(), response.getMassage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setBoard(BaseBoard baseBoard) {
        // 放到 mViewModel ???
        this.board = baseBoard;
    }
}