SwipeRefreshAndLoadMore of (Android 5.0)

### Gradle
Get library from  [oss.sonatype.org.io](https://oss.sonatype.org/content/repositories/snapshots)
```javascript

repositories {
    
    maven { url "https://oss.sonatype.org/content/repositories/snapshots" }

}

dependencies {
    compile compile 'com.github.captain-miao:recyclerviewutils:1.1.6-SNAPSHOT'
}

```
<br/>
###RefreshRecyclerView
```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:background="@color/light_grey">

    <com.github.captain_miao.recyclerviewutils.RefreshRecyclerView
        android:id="@+id/recycler_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

    </com.github.captain_miao.recyclerviewutils.RefreshRecyclerView>
</RelativeLayout>
```
<br/>
###RefreshRecyclerViewListener
```
        mRefreshRecyclerView = (RefreshRecyclerView) findViewById(R.id.recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRefreshRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new SimpleAdapter(new ArrayList<String>());

        mRefreshRecyclerView.setAdapter(mAdapter);

        mRefreshRecyclerView.setRecyclerViewListener(new RefreshRecyclerViewListener() {
            @Override
            public void onRefresh() {
                
            }

            @Override
            public void onLoadMore(int pagination, int pageSize) {

            }
        });
        mRefreshRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mRefreshRecyclerView.autoRefresh();
            }
        });
```
<br/>
usage(Chinese):https://yanlu.me/recyclerview_pull_up_load_more/

QQ  Group:436275452

![load_more_screenshot](/screenshot/load_more_screenshot.jpg?raw=true "load_more_screenshot")

## import Pull to Refresh
Ultra Pull to Refresh for Android:https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh.

## import sticky-headers-recyclerview
Sticky Headers decorator for Android's RecyclerView:https://github.com/timehop/sticky-headers-recyclerview

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
