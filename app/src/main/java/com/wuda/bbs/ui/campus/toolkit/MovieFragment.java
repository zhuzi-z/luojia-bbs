package com.wuda.bbs.ui.campus.toolkit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.campus.InfoBaseBean;
import com.wuda.bbs.logic.bean.campus.ToolBean;
import com.wuda.bbs.ui.adapter.AdapterItemListener;
import com.wuda.bbs.ui.adapter.InfoAdapter;
import com.wuda.bbs.ui.campus.CampusActivity;
import com.wuda.bbs.ui.campus.ToolFragment;
import com.wuda.bbs.ui.campus.detial.MovieDetailFragment;
import com.wuda.bbs.utils.campus.HttpUtil;
import com.wuda.bbs.utils.campus.InfoResponseParser;
import com.wuda.bbs.utils.campus.ServerURL;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MovieFragment extends ToolFragment {

    RecyclerView recyclerView;
    List<InfoBaseBean> infoList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tool = new ToolBean(R.drawable.ic_movie, "#986764", "梅操电影", ServerURL.MOVIE, MovieFragment.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = view.findViewById(R.id.list);

        if (infoList != null) {
            showInfo();
        } else {
            requestFromServer();
        }

        return view;
    }

    private void requestFromServer() {

        showProgressBar();

        HttpUtil.sendOkHttpRequest(ServerURL.MOVIE_JSON, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressBar();
                        }
                    });
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                infoList = InfoResponseParser.handleMovieResponse(response.body().string());
                showInfo();
            }
        });
    }

    private void showInfo() {
        InfoAdapter infoAdapter = new InfoAdapter(getContext(), infoList);
        infoAdapter.setAdapterItemListener(new AdapterItemListener<InfoBaseBean>() {
            @Override
            public void onItemClick(InfoBaseBean data, int position) {
                ((CampusActivity) requireActivity()).navigationTo(
                        MovieDetailFragment.newInstance(data),
                        true
                );
            }

            @Override
            public void onItemLongClick(InfoBaseBean data, int position) {

            }
        });
        if (getActivity() == null)
            return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeProgressBar();
                recyclerView.setAdapter(infoAdapter);
            }
        });
    }
}
