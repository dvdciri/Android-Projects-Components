package com.davidecirillo.multichoicesample.sampleCustomView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.davidecirillo.multichoicerecyclerview.MultiChoiceAdapter;
import com.davidecirillo.multichoicerecyclerview.MultiChoiceToolbar;
import com.davidecirillo.multichoicesample.BaseActivity;
import com.davidecirillo.multichoicesample.R;

import java.util.ArrayList;

import butterknife.BindView;

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ((MultiChoiceAdapter) multiChoiceRecyclerView.getAdapter()).onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        ((MultiChoiceAdapter) multiChoiceRecyclerView.getAdapter()).onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void setUpMultiChoiceRecyclerView() {

        multiChoiceRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

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
                getSampleMessageList(),
                SampleCustomActivity.this
        );
        adapter.setMultiChoiceToolbar(multiChoiceToolbar);
        multiChoiceRecyclerView.setAdapter(adapter);

    }

    private ArrayList<MessageV0> getSampleMessageList() {
        ArrayList<MessageV0> sampleList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            sampleList.add(new MessageV0("Title message number " + i, "Lorem ipsum dolor " + i + " sit amet, consectetur adipiscing" +
                    " elit. Donec id mi pharetra, porta felis sed, aliquam urna. Curabitur porta dolor lobortis semper dictum. " +
                    "Vestibulum posuere velit nisl, at porta lectus condimentum vel. Duis pharetra auctor tempor. Proin feugiat turpis " +
                    "vel tincidunt molestie. Pellentesque tincidunt felis vitae leo pharetra aliquam. Donec sapien ante, feugiat at " +
                    "eleifend vel, laoreet vitae neque. Donec eu erat et diam fermentum congue a sed libero. Vivamus dapibus nunc nec " +
                    "posuere elementum. Nunc eget mi sed est finibus ultricies. Nam ut sapien feugiat neque tempus feugiat."));
        }
        return sampleList;
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
