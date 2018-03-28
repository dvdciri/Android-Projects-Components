[ ![Download](https://api.bintray.com/packages/dvd-ciri/maven/MultiChoiceRecyclerView/images/download.svg) ](https://bintray.com/dvd-ciri/maven/MultiChoiceRecyclerView/_latestVersion)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/cz.jirutka.rsql/rsql-parser/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/cz.jirutka.rsql/rsql-parser)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-MultiChoiceRecyclerView-green.svg?style=true)](https://android-arsenal.com/details/1/3755)
[![CircleCI](https://circleci.com/gh/dvdciri/MultiChoiceRecyclerView/tree/master.svg?style=shield)](https://circleci.com/gh/dvdciri/MultiChoiceRecyclerView/tree/master)

#Multichoice RecyclerView

###Sample
<a href='https://play.google.com/store/apps/details?id=com.davidecirillo.multichoicerecyclerview&hl=en_GB&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img width='150' alt='Get it on Google Play' src='https://play.google.com/intl/en_gb/badges/images/generic/en_badge_web_generic.png'/></a>

Donwload the sample app on the Google Play Store and check out all the features

<img src="https://raw.githubusercontent.com/dvdciri/MultiChoiceRecyclerView/master/example0.png" width="180">
<img src="https://raw.githubusercontent.com/dvdciri/MultiChoiceRecyclerView/master/example1.png" width="180">
<img src="https://raw.githubusercontent.com/dvdciri/MultiChoiceRecyclerView/master/example2.png" width="180">
<img src="https://raw.githubusercontent.com/dvdciri/MultiChoiceRecyclerView/master/example4.png" width="180">

*Are you using MultiChoiceRecyclerView? If you want to be featured on this page drop me a line.*

##** IMPORTANT ** MAJOR RELEASE **
There will be a major release **v2.0.0** in which will be removed the custom **MultiChoiceRecyclerView** class and all the logic is moved inside the **MultiChoiceAdapter**, that will be the only class needed for the implemetation of the library.
Check out the documentation before upgrading.

###Description
This library make life easier when you have to deal with a multi choice selection on recycler view.

#Implementation
Extend your adapter to the MultiChoiceAdapter and add it to the RecyclerView as per normal usage
```java
    public class MyAdapter extends MultiChoiceAdapter<MyViewHolder> {

        public MyAdapter(ArrayList<String> stringList, Context context) {
            this.mList = stringList;
            this.mContext = context;
        }

         @Override
         public void onBindViewHolder(MySampleToolbarViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);

            /*
                Don't use View.OnClickListener on the "holder.itemView", override defaultItemViewClickListener(...) instead
            */
            ...
         }
    }
```

```java
    MyAdapter myAdapter = new MyAdapter(mList, context);
    mMultiChoiceRecyclerView.setAdapter(myAdapter);
```

**N.B.**
- Do not forget to call **super.onBindViewHolder(holder, position);** when binding the view holder

Customize the activation or deactivation just overriding the setActive(View rootView, boolean state) method of the MultiChoiceAdapter
```java
    @Override
    public void setActive(View rootView, boolean state) {

        //Use View.findViewById(int id) to look for your view in the rootView

        if(state){
            //Apply your changes
        }else{
            //Reset your changes
        }
    }
```

<br>

#Fetures
- **Multi Choice Toolbar**
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
<img src="https://raw.githubusercontent.com/dvdciri/MultiChoiceRecyclerView/master/example_toolbar.png" width="300">

You can also set the following:
- Default toolbar primary color
- Default toolbar primary color dark

<br>
- **Single Click Mode**
Use always single click mode (by default, to activate the multi selection mode you need a long click on the first item)
```java
    /*Setting single click mode true, the user will be able to select the first item just with a single click*/
    mAdapter.setSingleClickMode(true);
```

<br>
- **Event Callbacks**
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

<br>

#Changelog
[Changelog file](https://github.com/dvdciri/MultiChoiceRecyclerView/blob/master/CHANGELOG.md)


#Gradle Dependency
```java
    repositories {
        jcenter()
    }
    ...

    dependencies {
        compile 'com.davidecirillo.multichoicerecyclerview:multichoicerecyclerview:2.0.0'
    }
```

#Developed by

**Davide Cirillo**
- Twitter @DvdCiri
- GitHub @dvdciri
- Mail dvd.ciri@gmail.com

If you want to contribute to the project feel free to Fork and create a Pull Request following the general rules for contribution: https://guides.github.com/activities/contributing-to-open-source/

# License

Apache 2.0 © Davide Cirillo
