package com.davidecirillo.multichoicerecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import static com.davidecirillo.multichoicerecyclerview.MultiChoiceAdapter.SELECTED_ALPHA;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MultiChoiceAdapterTest {

    private TestViewHolder mViewHolder;
    private MultiChoiceAdapter<TestViewHolder> mMultiChoiceAdapter;

    @Mock
    private LinkedHashMap<Integer, MultiChoiceAdapter.State> mMockItemList;

    @Mock
    private View mMockItemView;

    @Mock
    private MultiChoiceAdapter.Listener mMockMultiChoiceListener;

    private LinkedHashMap<Integer, MultiChoiceAdapter.State> mRealItemList;
    private int mTestPosition;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mViewHolder = new TestViewHolder(mMockItemView);

        mMultiChoiceAdapter = new TestAdapter();
        mMultiChoiceAdapter.setItemList(mMockItemList);
        mMultiChoiceAdapter.setMultiChoiceSelectionListener(mMockMultiChoiceListener);

        mRealItemList = new LinkedHashMap<>();

        mTestPosition = 0;
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGivenOneActiveItemInTheItemListWhenGetSelectedItemListInternalThenListWithOneItemIsReturned() throws Exception {
        // Given
        addInactiveTestItems(3);
        mRealItemList.put(2, MultiChoiceAdapter.State.ACTIVE);
        mMultiChoiceAdapter.setItemList(mRealItemList);

        // When
        List<Integer> selectedItemListInternal = mMultiChoiceAdapter.getSelectedItemListInternal();

        // Then
        assertEquals("Size of the list should be 1", 1, selectedItemListInternal.size());
    }

    @Test
    public void testAdapterShouldCallOnUpdateItemListener() throws Exception {
        // Given
        when(mMockItemList.containsKey(RecyclerView.NO_POSITION)).thenReturn(true);
        when(mMockItemList.get(RecyclerView.NO_POSITION)).thenReturn(MultiChoiceAdapter.State.ACTIVE);

        // When
        mMultiChoiceAdapter.onBindViewHolder(mViewHolder, mTestPosition);

        // Then
        verify(mMockItemList, times(1)).get(RecyclerView.NO_POSITION);
        verify(mMockItemList, times(1)).containsKey(RecyclerView.NO_POSITION);
        verify(mMockItemView, times(1)).setAlpha(SELECTED_ALPHA);
    }

    @Test
    public void testGivenIsNotInMultiChoiceModeAndItemIsInactiveWhenOnBindViewHolderAndPerformClickThenNoActivationOnTheItem() throws Exception {
        // Given
        mMultiChoiceAdapter.mIsInMultiChoiceMode = false;
        mMultiChoiceAdapter.mIsInSingleClickMode = false;
        when(mMockItemList.containsKey(RecyclerView.NO_POSITION)).thenReturn(true);
        when(mMockItemList.get(RecyclerView.NO_POSITION)).thenReturn(MultiChoiceAdapter.State.INACTIVE);

        // When
        mMultiChoiceAdapter.onBindViewHolder(mViewHolder, RecyclerView.NO_POSITION);
        mViewHolder.itemView.callOnClick();

        // Then
        verify(mMockItemList, times(0)).put(RecyclerView.NO_POSITION, MultiChoiceAdapter.State.ACTIVE);
        verify(mMockItemView, times(0)).setAlpha(SELECTED_ALPHA);
    }

    @Test
    public void testGivenIsInSingleClickModeAndThemItemIsInactiveWhenOnBindThenClickListenerSetAndItemActive() throws Exception {
        // Given
        ArgumentCaptor<View.OnClickListener> clickListenerCapture = ArgumentCaptor.forClass(View.OnClickListener.class);
        when(mMockItemList.containsKey(RecyclerView.NO_POSITION)).thenReturn(true);
        when(mMockItemList.get(RecyclerView.NO_POSITION)).thenReturn(MultiChoiceAdapter.State.INACTIVE);
        mMultiChoiceAdapter.mIsInSingleClickMode = true;
        mMultiChoiceAdapter.mIsInMultiChoiceMode = false;

        // When
        mMultiChoiceAdapter.onBindViewHolder(mViewHolder, mTestPosition);

        //Then
        verify(mMockItemView, times(1)).setOnClickListener(clickListenerCapture.capture());
        clickListenerCapture.getValue().onClick(mMockItemView);

        verify(mMockItemList, times(1)).put(RecyclerView.NO_POSITION, MultiChoiceAdapter.State.ACTIVE);
    }

    @Test
    public void testGivenIsInMultiChoiceModeAndThemItemIsInactiveWhenOnBindThenClickListenerSetAndItemActive() throws Exception {
        // Given
        ArgumentCaptor<View.OnClickListener> clickListenerCapture = ArgumentCaptor.forClass(View.OnClickListener.class);
        when(mMockItemList.containsKey(RecyclerView.NO_POSITION)).thenReturn(true);
        when(mMockItemList.get(RecyclerView.NO_POSITION)).thenReturn(MultiChoiceAdapter.State.INACTIVE);
        mMultiChoiceAdapter.mIsInSingleClickMode = false;
        mMultiChoiceAdapter.mIsInMultiChoiceMode = true;

        // When
        mMultiChoiceAdapter.onBindViewHolder(mViewHolder, mTestPosition);

        //Then
        verify(mMockItemView, times(1)).setOnClickListener(clickListenerCapture.capture());
        clickListenerCapture.getValue().onClick(mMockItemView);

        verify(mMockItemList, times(1)).put(RecyclerView.NO_POSITION, MultiChoiceAdapter.State.ACTIVE);
    }

    @Test
    public void testGivenIsNotSelectableInMultiChoiceModeAndIsInMultiChoiceModeWhenOnBindThenNoClickListenerSet() throws Exception {
        // Given
        mMultiChoiceAdapter = new TestAdapter() {
            @Override
            protected boolean isSelectableInMultiChoiceMode(int position) {
                return false;
            }

            @Override
            protected View.OnClickListener defaultItemViewClickListener(TestViewHolder holder, int position) {
                return null;
            }
        };

        mMultiChoiceAdapter.mIsInMultiChoiceMode = false;
        mMultiChoiceAdapter.mIsInSingleClickMode = true;

        // When
        mMultiChoiceAdapter.onBindViewHolder(mViewHolder, mTestPosition);

        // Then
        verify(mViewHolder.itemView, times(0)).setOnClickListener(any(View.OnClickListener.class));
    }

    @Test
    public void testDisableItemShouldNotBeSelectedInMultiChoiceModeAndShouldHaveDefaultClickListenerIfNotNull() throws Exception {
        // Given
        ArgumentCaptor<View.OnClickListener> clickListenerCapture = ArgumentCaptor.forClass(View.OnClickListener.class);
        mMultiChoiceAdapter = new TestAdapter() {
            @Override
            protected boolean isSelectableInMultiChoiceMode(int position) {
                return false;
            }
        };
        mMultiChoiceAdapter.mIsInMultiChoiceMode = false;
        mMultiChoiceAdapter.mIsInSingleClickMode = true;

        // When
        mMultiChoiceAdapter.onBindViewHolder(mViewHolder, mTestPosition);

        // Then
        verify(mViewHolder.itemView, times(1)).setOnClickListener(clickListenerCapture.capture());
        assertEquals("The setOnClickListener doesn't have the correct onClickListener object", ((TestAdapter) mMultiChoiceAdapter)
                .getOnClickListener(), clickListenerCapture.getValue());
    }

    @Test
    public void testAdapterIsUsingTheDefaultClickListenerIfNotInMultiAndSingleClickModeAndTheItemIsSelectable() throws Exception {
        // Given
        ArgumentCaptor<View.OnClickListener> clickListenerCapture = ArgumentCaptor.forClass(View.OnClickListener.class);
        mMultiChoiceAdapter.mIsInMultiChoiceMode = false;
        mMultiChoiceAdapter.mIsInSingleClickMode = false;
        when(mMockItemList.containsKey(RecyclerView.NO_POSITION)).thenReturn(true);
        when(mMockItemList.get(RecyclerView.NO_POSITION)).thenReturn(MultiChoiceAdapter.State.ACTIVE);

        // When
        mMultiChoiceAdapter.onBindViewHolder(mViewHolder, mTestPosition);

        // Then
        verify(mViewHolder.itemView, times(1)).setOnClickListener(clickListenerCapture.capture());

        assertEquals("The setOnClickListener doesn't have the correct onClickListener object", ((TestAdapter) mMultiChoiceAdapter)
                .getOnClickListener(), clickListenerCapture.getValue());
    }

    @Test
    public void testAdapterIsUsingNotTheDefaultClickListenerIfNotInMultiAndSingleClickModeAndTheItemIsSelectableAndTheDefaultClickIsNull() throws
            Exception {
        // Given
        mMultiChoiceAdapter = new TestAdapter() {
            @Override
            protected View.OnClickListener defaultItemViewClickListener(TestViewHolder holder, int position) {
                return null;
            }
        };
        mMultiChoiceAdapter.mIsInMultiChoiceMode = false;
        mMultiChoiceAdapter.mIsInSingleClickMode = false;

        // When
        mMultiChoiceAdapter.onBindViewHolder(mViewHolder, 1);

        // Then
        verify(mViewHolder.itemView, times(0)).setOnClickListener(any(View.OnClickListener.class));
    }

    @Test
    public void testGivenOneActiveItemOutOfThreeWhenSelectAllThenThreeItemSelected() throws Exception {
        // Given
        addInactiveTestItems(3);
        mRealItemList.put(2, MultiChoiceAdapter.State.ACTIVE);
        mMultiChoiceAdapter.setItemList(mRealItemList);

        // When
        mMultiChoiceAdapter.selectAll();

        // Then
        assertEquals("All 3 items should be selected", 3, mMultiChoiceAdapter.getSelectedItemListInternal().size());
    }

    @Test
    public void testGivenAllItemAcgiveWhenSelectAllThenZeroItemSelected() throws Exception {
        // Given
        addInactiveTestItems(3);
        mRealItemList.put(0, MultiChoiceAdapter.State.ACTIVE);
        mRealItemList.put(1, MultiChoiceAdapter.State.ACTIVE);
        mRealItemList.put(2, MultiChoiceAdapter.State.ACTIVE);
        mMultiChoiceAdapter.setItemList(mRealItemList);

        // When
        mMultiChoiceAdapter.deselectAll();

        // Then
        assertEquals("No one items should be selected", 0, mMultiChoiceAdapter.getSelectedItemListInternal().size());
    }

    @Test
    public void givenItemInactiveWhenSelectThenSelect() throws Exception {
        // Given

        when(mMockItemList.containsKey(mTestPosition)).thenReturn(true);
        when(mMockItemList.get(mTestPosition)).thenReturn(MultiChoiceAdapter.State.INACTIVE);

        // When
        mMultiChoiceAdapter.select(mTestPosition);

        // Then
        verify(mMockItemList, times(1)).put(mTestPosition, MultiChoiceAdapter.State.ACTIVE);
    }

    @Test
    public void givenItemActiveWhenSelectThenNothing() throws Exception {
        // Given

        when(mMockItemList.containsKey(mTestPosition)).thenReturn(true);
        when(mMockItemList.get(mTestPosition)).thenReturn(MultiChoiceAdapter.State.ACTIVE);

        // When
        mMultiChoiceAdapter.select(mTestPosition);

        // Then
        verify(mMockItemList, times(0)).put(anyInt(), any(MultiChoiceAdapter.State.class));
    }

    @Test
    public void givenItemActiveWhenDeselectThenDeselect() throws Exception {
        // Given

        when(mMockItemList.containsKey(mTestPosition)).thenReturn(true);
        when(mMockItemList.get(mTestPosition)).thenReturn(MultiChoiceAdapter.State.ACTIVE);

        // When
        mMultiChoiceAdapter.deselect(mTestPosition);

        // Then
        verify(mMockItemList, times(1)).put(mTestPosition, MultiChoiceAdapter.State.INACTIVE);
    }

    @Test
    public void givenItemInactiveWhenDeselectThenNothing() throws Exception {
        // Given
        when(mMockItemList.containsKey(mTestPosition)).thenReturn(true);
        when(mMockItemList.get(mTestPosition)).thenReturn(MultiChoiceAdapter.State.INACTIVE);

        // When
        mMultiChoiceAdapter.deselect(mTestPosition);

        // Then
        verify(mMockItemList, times(0)).put(anyInt(), any(MultiChoiceAdapter.State.class));
    }

    @Test
    public void givenThreeItemWhenSelectAllThenAllSelected() throws Exception {
        // Given
        int expectedCount = 3;
        when(mMockItemList.size()).thenReturn(expectedCount);
        HashSet<Integer> value = new HashSet<>();
        value.add(0);
        value.add(1);
        value.add(2);
        when(mMockItemList.keySet()).thenReturn(value);

        // When
        mMultiChoiceAdapter.selectAll();

        // Then
        verify(mMockItemList, times(expectedCount)).put(anyInt(), eq(MultiChoiceAdapter.State.ACTIVE));
    }

    @Test
    public void givenThreeItemWhenDeselectAllThenAllDeselected() throws Exception {
        // Given
        int expectedCount = 3;
        when(mMockItemList.size()).thenReturn(expectedCount);
        HashSet<Integer> value = new HashSet<>();
        value.add(0);
        value.add(1);
        value.add(2);
        when(mMockItemList.keySet()).thenReturn(value);

        // When
        mMultiChoiceAdapter.deselectAll();

        // Then
        verify(mMockItemList, times(expectedCount)).put(anyInt(), eq(MultiChoiceAdapter.State.INACTIVE));
    }

    private void addInactiveTestItems(int count) {
        for (int i = 0; i < count; i++) {
            mRealItemList.put(i, MultiChoiceAdapter.State.INACTIVE);
        }
    }

    private class TestViewHolder extends RecyclerView.ViewHolder {
        private TestViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class TestAdapter extends MultiChoiceAdapter<TestViewHolder> {

        private View.OnClickListener mOnClickListener;

        @Override
        public TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public int getItemCount() {
            return 0;
        }

        @Override
        protected View.OnClickListener defaultItemViewClickListener(TestViewHolder holder, int position) {
            mOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            };
            return mOnClickListener;
        }

        public View.OnClickListener getOnClickListener() {
            return mOnClickListener;
        }
    }

}