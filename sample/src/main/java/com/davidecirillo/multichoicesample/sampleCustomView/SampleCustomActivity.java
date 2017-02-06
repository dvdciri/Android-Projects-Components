package com.davidecirillo.multichoicesample.sampleCustomView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.davidecirillo.multichoicerecyclerview.MultiChoiceToolbar;
import com.davidecirillo.multichoicesample.BaseActivity;
import com.davidecirillo.multichoicesample.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SampleCustomActivity extends BaseActivity {

    @BindView(R.id.multiChoiceRecyclerView)
    public RecyclerView multiChoiceRecyclerView;

    @Override
    protected int setActivityIdentifier() {
        return R.layout.activity_sample_custom_view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpMultiChoiceRecyclerView();
    }

    private void setUpMultiChoiceRecyclerView() {

        multiChoiceRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        getSampleMessageList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<List<MessageV0>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<MessageV0> messageV0s) {
                        setUpAdapter(messageV0s);
                    }
                });
    }

    private void setUpAdapter(List<MessageV0> messageV0s) {
        MultiChoiceToolbar multiChoiceToolbar =
                new MultiChoiceToolbar.Builder(SampleCustomActivity.this, toolbar)
                        .setTitles(getString(toolbarTitle()
                        ), "")
                        .setMultiChoiceColours(R.color.colorPrimaryMulti, R.color.colorPrimaryDarkMulti)
                        .setDefaultIcon(R.drawable.ic_arrow_back_white_24dp, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onBackPressed();
                            }
                        })
                        .build();

        SampleCustomViewAdapter adapter = new SampleCustomViewAdapter(
                new ArrayList<>(messageV0s),
                SampleCustomActivity.this
        );
        adapter.setMultiChoiceToolbar(multiChoiceToolbar);
        multiChoiceRecyclerView.setAdapter(adapter);
    }

    private Observable<List<MessageV0>> getSampleMessageList() {
        return Observable
                .range(0, 30)
                .map(new Func1<Integer, MessageV0>() {
                    @Override
                    public MessageV0 call(Integer integer) {
                        return new MessageV0("Title message number " + integer, "Lorem ipsum dolor " + integer + " sit amet, consectetur adipiscing" +
                                " elit. Donec id mi pharetra, porta felis sed, aliquam urna. Curabitur porta dolor lobortis semper dictum. " +
                                "Vestibulum posuere velit nisl, at porta lectus condimentum vel. Duis pharetra auctor tempor. Proin feugiat turpis " +
                                "vel tincidunt molestie. Pellentesque tincidunt felis vitae leo pharetra aliquam. Donec sapien ante, feugiat at " +
                                "eleifend vel, laoreet vitae neque. Donec eu erat et diam fermentum congue a sed libero. Vivamus dapibus nunc nec " +
                                "posuere elementum. Nunc eget mi sed est finibus ultricies. Nam ut sapien feugiat neque tempus feugiat.");
                    }
                })
                .toList();
    }

    @Override
    protected int toolbarTitle() {
        return R.string.custom_selection_view;
    }

    @Override
    protected boolean showBackHomeAsUpIndicator() {
        return true;
    }
}
