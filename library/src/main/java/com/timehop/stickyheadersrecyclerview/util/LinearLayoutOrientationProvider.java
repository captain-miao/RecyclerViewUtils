package com.timehop.stickyheadersrecyclerview.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/*
 *  Copyright 2016 https://github.com/jacobtabak
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 *    from https://github.com/timehop/sticky-headers-recyclerview
 */

/**
 * OrientationProvider for ReyclerViews who use a LinearLayoutManager
 */
public class LinearLayoutOrientationProvider implements OrientationProvider {

  @Override
  public int getOrientation(RecyclerView recyclerView) {
    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
    throwIfNotLinearLayoutManager(layoutManager);
    return ((LinearLayoutManager) layoutManager).getOrientation();
  }

  @Override
  public boolean isReverseLayout(RecyclerView recyclerView) {
    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
    throwIfNotLinearLayoutManager(layoutManager);
    return ((LinearLayoutManager) layoutManager).getReverseLayout();
  }

  private void throwIfNotLinearLayoutManager(RecyclerView.LayoutManager layoutManager){
    if (!(layoutManager instanceof LinearLayoutManager)) {
      throw new IllegalStateException("StickyListHeadersDecoration can only be used with a " +
          "LinearLayoutManager.");
    }
  }
}
