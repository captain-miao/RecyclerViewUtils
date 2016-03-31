package com.timehop.stickyheadersrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

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

public interface StickyRecyclerHeadersAdapter<VH extends RecyclerView.ViewHolder> {
  /**
   * Get the ID of the header associated with this item.  For example, if your headers group
   * items by their first letter, you could return the character representation of the first letter.
   * Return a value &lt; 0 if the view should not have a header (like, a header view or footer view)
   *
   * @param position the position of the view to get the header ID of
   * @return the header ID
   */
  long getHeaderId(int position);

  /**
   * Creates a new ViewHolder for a header.  This works the same way onCreateViewHolder in
   * Recycler.Adapter, ViewHolders can be reused for different views.  This is usually a good place
   * to inflate the layout for the header.
   *
   * @param parent the view to create a header view holder for
   * @return the view holder
   */
  VH onCreateHeaderViewHolder(ViewGroup parent);

  /**
   * Binds an existing ViewHolder to the specified adapter position.
   *
   * @param holder the view holder
   * @param position the adapter position
   */
  void onBindHeaderViewHolder(VH holder, int position);

  /**
   * @return the number of views in the adapter
   */
  int getItemCount();
}
