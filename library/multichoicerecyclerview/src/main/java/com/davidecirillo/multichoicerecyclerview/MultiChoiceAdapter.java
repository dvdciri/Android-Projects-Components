package com.davidecirillo.multichoicerecyclerview;

import android.Manifest;
import android.content.Context;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public abstract class MultiChoiceAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements MultiChoiceToolbar.Listener {

    private static final float DESELECTED_ALPHA = 1f;
    static final float SELECTED_ALPHA = 0.25f;

    boolean mIsInMultiChoiceMode;
    boolean mIsInSingleClickMode;

    private LinkedHashMap<Integer, State> mItemList = new LinkedHashMap<>();
    private Listener mListener = null;
    private MultiChoiceToolbarHelper mMultiChoiceToolbarHelper;
    private RecyclerView mRecyclerView;

    //region Public methods

    /**
     * Override this method to customize the active item
     *
     * @param view  the view to customize
     * @param state true if the state is active/selected
     */
    public void setActive(@NonNull View view, boolean state) {
        if (state) {
            view.setAlpha(SELECTED_ALPHA);
        } else {
            view.setAlpha(DESELECTED_ALPHA);
        }
    }

    /**
     * Provide the default behaviour for the item click with multi choice mode disabled
     *
     * @return the onClick action to perform when multi choice selection is off
     */
    protected View.OnClickListener defaultItemViewClickListener(VH holder, int position) {
        return null;
    }

    protected boolean isSelectableInMultiChoiceMode(int position) {
        return true;
    }

    /**
     * Deselect all the selected items in the adapter
     */
    public void deselectAll() {
        performAll(Action.DESELECT);
    }

    /**
     * Select all the view in the adapter
     */
    public void selectAll() {
        performAll(Action.SELECT);
    }


    /**
     * Select a view from position in the adapter only if is the multi choice mode or single click choice mode is activated
     *
     * @param position the position of the view in the adapter
     * @return true if has been selected false otherwise
     */
    public boolean select(int position) {
        if (mIsInMultiChoiceMode || mIsInSingleClickMode && mItemList.containsKey(position)) {
            perform(Action.SELECT, position, true, true);
            return true;
        }
        return false;
    }


    /**
     * Set the selection of the RecyclerView to always single click (instead of first long click and then single click)
     *
     * @param set true if single click sctivated
     */
    public void setSingleClickMode(boolean set) {
        mIsInSingleClickMode = set;
        processNotifyDataSetChanged();
    }


    /**
     * Method to get the number of selected items
     *
     * @return number of selected items
     */
    public int getSelectedItemCount() {
        return getSelectedItemListInternal().size();
    }

    /**
     * @return true if any item is currently selected; false if not
     */
    public boolean isSelectionActive() {
        return (getSelectedItemListInternal().size() > 0);
    }

    /**
     * Get the list of selected item
     *
     * @return Collection of all the selected position in the adapter
     */
    public List<Integer> getSelectedItemList() {
        return getSelectedItemListInternal();
    }

    public void setMultiChoiceSelectionListener(Listener listener) {
        this.mListener = listener;
    }

    public void setMultiChoiceToolbar(MultiChoiceToolbar multiChoiceToolbar) {
        multiChoiceToolbar.setToolbarListener(this);
        mMultiChoiceToolbarHelper = new MultiChoiceToolbarHelper(multiChoiceToolbar);
    }

    /**
     * @return true if the single click mode is active
     */
    public boolean isInSingleClickMode() {
        return mIsInSingleClickMode;
    }

    //endregion

    //region Private methods
    List<Integer> getSelectedItemListInternal() {
        List<Integer> selectedList = new ArrayList<>();
        for (Map.Entry<Integer, State> item : mItemList.entrySet()) {
            if (item.getValue().equals(State.ACTIVE)) {
                selectedList.add(item.getKey());
            }
        }
        return selectedList;
    }

    private void processSingleClick(int position) {
        if (mIsInMultiChoiceMode || mIsInSingleClickMode) {
            processClick(position);
        }
    }

    private void processLongClick(int position) {
        if (!mIsInMultiChoiceMode && !mIsInSingleClickMode) {
            processClick(position);
        }
    }

    private void processUpdate(View view, int position) {
        if (mItemList.containsKey(position)) {
            if (mItemList.get(position).equals(State.ACTIVE)) {
                setActive(view, true);
            } else {
                setActive(view, false);
            }
        }
    }

    private void processClick(int position) {
        if (mItemList.containsKey(position)) {
            if (mItemList.get(position).equals(State.ACTIVE)) {
                perform(Action.DESELECT, position, true, true);
            } else {
                perform(Action.SELECT, position, true, true);
            }
        }
    }

    /**
     * Remember to call this method before selecting or deselection something otherwise it won't vibrate
     */
    private void performVibrate() {
        if (!mIsInMultiChoiceMode && !mIsInSingleClickMode && mRecyclerView != null) {
            if (ContextCompat.checkSelfPermission(mRecyclerView.getContext(), Manifest.permission.VIBRATE) == PermissionChecker.PERMISSION_GRANTED) {
                Vibrator v = (Vibrator) mRecyclerView.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(10);
            }
        }
    }

    private void perform(Action action, int position, boolean withCallback, boolean withVibration) {
        if (withVibration) {
            performVibrate();
        }

        if (action == Action.SELECT) {
            mItemList.put(position, State.ACTIVE);
        } else {
            mItemList.put(position, State.INACTIVE);
        }

        int selectedListSize = getSelectedItemListInternal().size();

        updateToolbarIfNeeded(selectedListSize);

        updateMultiChoiceMode(selectedListSize);

        processNotifyDataSetChanged();

        if (mListener != null && withCallback) {
            if (action == Action.SELECT) {
                mListener.OnItemSelected(position, selectedListSize, mItemList.size());
            } else {
                mListener.OnItemDeselected(position, selectedListSize, mItemList.size());
            }
        }
    }

    private void processNotifyDataSetChanged() {
        if (mRecyclerView != null) {
            notifyDataSetChanged();
        }
    }

    private void updateToolbarIfNeeded(int selectedListSize) {
        if ((mIsInMultiChoiceMode || mIsInSingleClickMode || selectedListSize > 0) && mMultiChoiceToolbarHelper != null) {
            mMultiChoiceToolbarHelper.updateToolbar(selectedListSize);
        }
    }

    private void updateMultiChoiceMode(int selectedListSize) {
        boolean somethingSelected = selectedListSize > 0;
        if (mIsInMultiChoiceMode != somethingSelected) {
            mIsInMultiChoiceMode = somethingSelected;
            processNotifyDataSetChanged();
        }
    }

    private void performAll(Action action) {
        performVibrate();

        int selectedItems;
        State state;
        if (action == Action.SELECT) {
            selectedItems = mItemList.size();
            state = State.ACTIVE;
        } else {
            selectedItems = 0;
            state = State.INACTIVE;
        }

        for (Integer i : mItemList.keySet()) {
            mItemList.put(i, state);
        }

        updateToolbarIfNeeded(selectedItems);
        updateMultiChoiceMode(selectedItems);

        processNotifyDataSetChanged();

        if (mListener != null) {
            if (action == Action.SELECT) {
                mListener.OnSelectAll(getSelectedItemListInternal().size(), mItemList.size());
            } else {
                mListener.OnDeselectAll(getSelectedItemListInternal().size(), mItemList.size());
            }
        }
    }

    @Override
    public void onClearButtonPressed() {
        performAll(Action.DESELECT);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;

        for (int i = 0; i < getItemCount(); i++) {
            mItemList.put(i, State.INACTIVE);
        }
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        View mCurrentView = holder.itemView;

        if ((mIsInMultiChoiceMode || mIsInSingleClickMode) && isSelectableInMultiChoiceMode(position)) {
            mCurrentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    processSingleClick(holder.getAdapterPosition());
                }
            });
        } else if (defaultItemViewClickListener(holder, position) != null) {
            mCurrentView.setOnClickListener(defaultItemViewClickListener(holder, position));
        }

        mCurrentView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                processLongClick(holder.getAdapterPosition());
                return true;
            }
        });

        processUpdate(mCurrentView, holder.getAdapterPosition());
    }

    //endregion

    //region Package-Protected methods

    @VisibleForTesting
    void setItemList(LinkedHashMap<Integer, State> itemList) {
        mItemList = itemList;
    }

    //endregion

    private enum Action {
        SELECT,
        DESELECT
    }

    enum State {
        ACTIVE,
        INACTIVE
    }

    public interface Listener {

        void OnItemSelected(int selectedPosition, int itemSelectedCount, int allItemCount);

        void OnItemDeselected(int deselectedPosition, int itemSelectedCount, int allItemCount);

        void OnSelectAll(int itemSelectedCount, int allItemCount);

        void OnDeselectAll(int itemSelectedCount, int allItemCount);
    }
}
