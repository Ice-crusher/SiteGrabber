package com.company.ice.sitegrabber.View;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.ice.sitegrabber.Interfaces.CustomRVItemTouchListener;
import com.company.ice.sitegrabber.Interfaces.IContentControl;
import com.company.ice.sitegrabber.Interfaces.RecyclerViewItemClickListener;
import com.company.ice.sitegrabber.Adapter.ArticlesAdapter;
import com.company.ice.sitegrabber.Model.DataAboutShortArticle;
import com.company.ice.sitegrabber.Presenter.MainPresenter;
import com.company.ice.sitegrabber.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticlesListFragment extends Fragment implements IContentControl {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private MainPresenter mainPresenter;

    private ArticlesAdapter mAdapter;

    private FloatingActionButton mFloatingActionButton;


    private boolean isLoadingMore;

    public ArticlesListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_articles_list, container, false);
        rootView.setTag("ArticlesListFragment");
        init(rootView);
        return rootView;
    }

    private void init(View rootView){
        setHasOptionsMenu(true);
        isLoadingMore = false;
        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mainPresenter.refreshList();
            }
        });
        mFloatingActionButton = rootView.findViewById(R.id.fab);
        mFloatingActionButton.hide();
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLayoutManager.findFirstCompletelyVisibleItemPosition()  < 10) {
                    mRecyclerView.smoothScrollToPosition(0);
                } else {
                    mRecyclerView.scrollToPosition(0);
                }
            }
        });

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerview_articles);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItemPosition < 3) {
                    mFloatingActionButton.hide();
                } else if (firstVisibleItemPosition > 3) {
                    mFloatingActionButton.show();
                }

//                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
//                        && firstVisibleItemPosition >= 0
//                        && totalItemCount >= PAGE_SIZE) {
                if (dy > 0) //check for scroll down
                    if (!isLoadingMore)
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                            isLoadingMore = true;
                            Log.d("Main", "We need to load more!");
                            loadMore();
                        }

            }
        });
        mRecyclerView.addOnItemTouchListener(new CustomRVItemTouchListener(getContext(), mRecyclerView,
                new RecyclerViewItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        mainPresenter.clickItem(position);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ArticlesAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);

        mainPresenter = new MainPresenter(rootView.getContext());
        mainPresenter.attachView(this);
        mainPresenter.viewIsReady();
    }



    public void loadMore(){
        mainPresenter.loadMore();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainPresenter.detachView();
    }


    @Override
    public void showArticles(List<DataAboutShortArticle> dataList) {
        isLoadingMore = false;
        mAdapter.setData(dataList);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void deleteAll() {
        mAdapter.removeAll();
    }

    @Override
    public void loadArticle(int position, String articleReference) {
        Intent intent = new Intent(getActivity(), ArticleFullActivity.class);
        intent.putExtra("article_reference", articleReference);
        ((MainActivity)getActivity()).startActivity(intent);
    }


    @Override
    public void updateArticles() {

    }
}
