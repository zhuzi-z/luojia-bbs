package com.wuda.bbs.ui.main.board;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.wuda.bbs.R;
import com.wuda.bbs.bean.Board;
import com.wuda.bbs.dao.AppDatabase;
import com.wuda.bbs.dao.BoardDao;
import com.wuda.bbs.utils.network.MobileService;
import com.wuda.bbs.utils.network.RootService;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardFragment extends Fragment {

    private BoardViewModel mViewModel;
    private TabLayout board_tl;
    private ViewPager2 board_vp2;

    public static BoardFragment newInstance() {
        return new BoardFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.board_fragment, container, false);
        board_tl = view.findViewById(R.id.board_tabLayout);
        board_vp2 = view.findViewById(R.id.board_viewPager2);
        requestFavouriteBoardsFromServer();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BoardViewModel.class);

//        board_vp2.setAdapter(new BoardViewPager2Adapter(getContext(), mViewModel.favoriteBoardList.getValue()));
        board_vp2.setAdapter(new FragmentStateAdapter(requireActivity().getSupportFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {

                BoardArticleFragment boardArticleFragment = new BoardArticleFragment();
                boardArticleFragment.setBoard(mViewModel.favoriteBoardList.getValue().get(position));
                return boardArticleFragment;
            }

            @Override
            public int getItemCount() {
                return mViewModel.favoriteBoardList.getValue().size();
            }
        });

        new TabLayoutMediator(board_tl, board_vp2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(mViewModel.favoriteBoardList.getValue().get(position).getName());
            }
        }).attach();

        eventBinding();

//        queryBoardsFromDB();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.board_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void eventBinding() {
        mViewModel.favoriteBoardList.observe(getViewLifecycleOwner(), boardList -> {
            if (board_vp2.getAdapter() != null) {
                board_vp2.getAdapter().notifyDataSetChanged();
            }
        });

        board_tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewModel.currentBoardIdx.setValue(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void queryBoardsFromDB() {
        if (getContext() == null)
            return;
        BoardDao boardDao = AppDatabase.getDatabase(getContext()).getBoardDao();
        List<Board> boardList = boardDao.loadAllBoards();
        if (boardList.isEmpty()) {
            requestAllBoardsFromServer();
        } else {
            List<Board> boards = new ArrayList<>();
            for (Board b: boardList) {
                if (!b.isFavourite()) {
                    boards.add(b);
                }
            }
            mViewModel.favoriteBoardList.postValue(boards);
        }
    }

    private void requestAllBoardsFromServer() {
        MobileService appBaseService = ServiceCreator.create(MobileService.class);
        appBaseService.request("boards", new HashMap<>()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    assert response.body() != null;
                    String text = response.body().string();
                    List<Board> boardList = XMLParser.parseBoard(text);
                    if (getContext() == null)
                        return;
                    BoardDao boardDao = AppDatabase.getDatabase(getContext()).getBoardDao();
                    boardDao.clearAll();
                    boardDao.insert(boardList);
                    queryBoardsFromDB();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void requestFavouriteBoardsFromServer() {
        RootService rootService = ServiceCreator.create(RootService.class);
        Map<String, String> form = new HashMap<>();
        form.put("select", "0");
        rootService.request("bbsfav.php", form).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String text = new String(response.body().bytes(), "GBK");
                    Log.d("fav", text);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}