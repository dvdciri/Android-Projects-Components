/*
 * Copyright (c) 2014 Davide Cirillo
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *     Come on, don't tell me you read that.
 */

package com.davidecirillo.multichoicesample.sampleToolbar;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.davidecirillo.multichoicerecyclerview.MultiChoiceAdapter;
import com.davidecirillo.multichoicesample.R;

import java.util.ArrayList;
import java.util.List;

public class MySampleToolbarAdapter extends MultiChoiceAdapter<MySampleToolbarViewHolder> {

    private List<String> mList;
    private final Context mContext;

    MySampleToolbarAdapter(ArrayList<String> stringList, Context context) {
        this.mList = stringList;
        this.mContext = context;
    }

    @Override
    public MySampleToolbarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MySampleToolbarViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sample_toolbar, parent, false));
    }

    @Override
    public void onBindViewHolder(MySampleToolbarViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.mTextView.setText(mList.get(position));
    }

    /**
     * Override this method to implement a custom active/deactive state
     */
    @Override
    public void setActive(@NonNull View view, boolean state) {

        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.get_started_relative_layout);

        if(relativeLayout != null){
            if(state){
                relativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
            }else{
                relativeLayout.setBackgroundColor(Color.WHITE);
            }
        }
    }

    @Override
    protected View.OnClickListener defaultItemViewClickListener(MySampleToolbarViewHolder holder, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Click on item "+position, Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setThis(final ArrayList<String> newData) {
        mList = newData;
    }
}
