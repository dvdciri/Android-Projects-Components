# Features

## Multi Choice Toolbar
Activate and customise the multi choice toolbar provided by the library (only if using setSupportActionBar with Toolbar)
```java
        MultiChoiceToolbar multiChoiceToolbar =
                    new MultiChoiceToolbar.Builder(SampleToolbarActivity.this, toolbar)
                            .setTitles(toolbarTitle(), "item selected")
                            .setMultiChoiceColours(R.color.colorPrimaryMulti, R.color.colorPrimaryDarkMulti)
                            .setDefaultIcon(R.drawable.ic_arrow_back_white_24dp, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    onBackPressed();
                                }
                            })
                            .build();

        mAdapter.setMultiChoiceToolbar(multiChoiceToolbar);
```

## Single Click Mode
Use always single click mode (by default, to activate the multi selection mode you need a long click on the first item)
```java
    /*Setting single click mode true, the user will be able to select the first item just with a single click*/
    mAdapter.setSingleClickMode(true);
```

## Event Callbacks
Use the MultiChoiceAdapter.Listener in order to have a callback on action taken to the items
```java
    mAdapter.setMultiChoiceSelectionListener(new MultiChoiceAdapter.Listener() {
            @Override
            public void OnItemSelected(int selectedPosition, int itemSelectedCount, int allItemCount) {

            }

            @Override
            public void OnItemDeselected(int deselectedPosition, int itemSelectedCount, int allItemCount) {

            }

            @Override
            public void OnSelectAll(int itemSelectedCount, int allItemCount) {

            }

            @Override
            public void OnDeselectAll(int itemSelectedCount, int allItemCount) {

            }
    });
```

## Save Activity Instance
In order to not lose your current selection when the activity configurations change, you can use those method to let the library handle the `savedInstanceState` for you:
```java
        // Override those method from your Activity

        @Override
        protected void onSaveInstanceState(Bundle outState) {
            mAdapter.onSaveInstanceState(outState);
            super.onSaveInstanceState(outState);
        }

        @Override
        protected void onRestoreInstanceState(Bundle savedInstanceState) {
            mAdapter.onRestoreInstanceState(savedInstanceState);
            super.onRestoreInstanceState(savedInstanceState);
        }
```
It will automatically keep the selection in place when orientation and other config change

## MultiChoiceAdapter useful methods
```java
        // Selection/Deselection
        void selectAll()

        void deselectAll()

        boolean select(int position)

        boolean deselect(int position)

        List<Integer> getSelectedItemList()

        // Mode
        boolean isInSingleClickMode()

        // Extra
        void notifyAdapterDataSetChanged()
```