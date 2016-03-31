package com.timehop.stickyheadersrecyclerview.caching;

import android.support.v7.widget.RecyclerView;
import android.view.View;

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
 * Implemented by objects that provide header views for decoration
 */
public interface HeaderProvider {

  /**
   * Will provide a header view for a given position in the RecyclerView
   *
   * @param recyclerView that will display the header
   * @param position     that will be headed by the header
   * @return a header view for the given position and list
   */
  public View getHeader(RecyclerView recyclerView, int position);

  /**
   * TODO: describe this functionality and its necessity
   */
  void invalidate();
}
