RecyclerView Utils

### Gradle
Get library from  [oss.sonatype.org.io](https://oss.sonatype.org/content/repositories/snapshots)
```java

repositories {

    maven { url 'https://oss.sonatype.org/content/repositories/releases' }
    maven { url "https://oss.sonatype.org/content/repositories/snapshots" }

}

dependencies {
    compile compile 'com.github.captain-miao:recyclerviewutils:1.2.3'

    
    
    //comment follows:
    //compile 'in.srain.cube:ultra-ptr:1.0.11'
    //compile 'com.timehop.stickyheadersrecyclerview:library:[latest.version.number]@aar'
    //compile 'com.bignerdranch.android:expandablerecyclerview:2.1.1'
    //compile 'com.camnter.easyrecyclerviewsidebar:easyrecyclerviewsidebar:1.3'
}

```
<br/>
### RefreshRecyclerView  
![load_more_screenshot](https://raw.githubusercontent.com/captain-miao/me.github.com/master/screenshot/refresh_and_load_more.gif "refresh_and_load_more")

### RecyclerView addHeaderView and addFooterView
![add_header_view](https://raw.githubusercontent.com/captain-miao/me.github.com/master/screenshot/add_header_view.gif "add_header_view")

### RecyclerView With Header ViewPage
![recycle_view_whith_header_view_page](https://raw.githubusercontent.com/captain-miao/me.github.com/master/screenshot/recycle_view_whith_header_view_page.gif "recycle_view_whith_header_view_page")

### StickyRecyclerHeaderView  
![sticky_header_view](https://raw.githubusercontent.com/captain-miao/me.github.com/master/screenshot/sticky_header_view.gif "sticky_header_view")

### ExpandableRecyclerHeaderView  
![expandable_recycler_view](https://raw.githubusercontent.com/captain-miao/me.github.com/master/screenshot/expandable_recycler_view.gif "expandable_recycler_view")

### IndexRecyclerHeaderView  
![index_recycler_view](https://raw.githubusercontent.com/captain-miao/me.github.com/master/screenshot/index_recycler_view.gif "index_recycler_view")

<br/>

### Tips:
1. work with ViewPager: disableWhenHorizontalMove()
//2. work with LongPressed: setInterceptEventWhileWorking()
3. StickyRecyclerHeaderView work with addItemDecoration:
```
divider color is the same as header backgroud.
first Add decoration for dividers between list items
    mRecyclerView.addItemDecoration(new DividerItemDecoration(this));
then:
    final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
    mRecyclerView.addItemDecoration(headersDecor);
```

QQ  Group:436275452(Q&A)
# Thanks
## import Pull to Refresh
Ultra Pull to Refresh for Android:https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh.

## import sticky-headers-recyclerview
Sticky Headers decorator for Android's RecyclerView:https://github.com/timehop/sticky-headers-recyclerview

## import expandable-recycler-view
A custom RecyclerView which allows for an expandable view to be attached to each ViewHolder:https://github.com/bignerdranch/expandable-recycler-view

## Easy sidebar for RecyclerView
Easy sidebar for RecyclerView: https://github.com/CaMnter/EasyRecyclerViewSidebar

## License

    Copyright 2014, 2015, 2016 captain_miao

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
