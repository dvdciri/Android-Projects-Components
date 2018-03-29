[ ![Download](https://api.bintray.com/packages/dvd-ciri/maven/MultiChoiceRecyclerView/images/download.svg) ](https://bintray.com/dvd-ciri/maven/MultiChoiceRecyclerView/_latestVersion)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/cz.jirutka.rsql/rsql-parser/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/cz.jirutka.rsql/rsql-parser)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-MultiChoiceRecyclerView-green.svg?style=true)](https://android-arsenal.com/details/1/3755)
[![CircleCI](https://circleci.com/gh/dvdciri/MultiChoiceRecyclerView/tree/master.svg?style=shield)](https://circleci.com/gh/dvdciri/MultiChoiceRecyclerView/tree/master)

# Multichoice RecyclerView

## Sample
<a href='https://play.google.com/store/apps/details?id=com.davidecirillo.multichoicerecyclerview&hl=en_GB&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img width='150' alt='Get it on Google Play' src='https://play.google.com/intl/en_gb/badges/images/generic/en_badge_web_generic.png'/></a>

Donwload the sample app on the Google Play Store and check out all the features

<img src="https://raw.githubusercontent.com/dvdciri/MultiChoiceRecyclerView/master/example0.png" width="170"/> <img src="https://raw.githubusercontent.com/dvdciri/MultiChoiceRecyclerView/master/example1.png" width="170"/> <img src="https://raw.githubusercontent.com/dvdciri/MultiChoiceRecyclerView/master/example2.png" width="170"/> <img src="https://raw.githubusercontent.com/dvdciri/MultiChoiceRecyclerView/master/example4.png" width="170"/>

## Description
This library make life easier when you have to deal with a multi choice selection on recycler view.

## Developed by

**Davide Cirillo**
- Twitter @DvdCiri
- GitHub @dvdciri
- Mail dvd.ciri@gmail.com

If you want to contribute to the project feel free to Fork and create a Pull Request following the general rules for contribution: https://guides.github.com/activities/contributing-to-open-source/

### Thanks for the contribution!
- [Simon Hardt](https://github.com/hardysim)

## How to use
Add the library as dependency
```java
    repositories {
        jcenter()
    }
    ...

    dependencies {
        compile 'com.davidecirillo.multichoicerecyclerview:multichoicerecyclerview:2.3.0'
    }
```

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

## Features
[Sample features](https://github.com/dvdciri/MultiChoiceRecyclerView/blob/master/FEATURES.md)

## Changelog
[Changelog file](https://github.com/dvdciri/MultiChoiceRecyclerView/blob/master/CHANGELOG.md)

## License
Apache 2.0 © Davide Cirillo
