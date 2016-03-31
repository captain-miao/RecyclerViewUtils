package com.timehop.stickyheadersrecyclerview.calculation;

import android.graphics.Rect;
import android.view.View;

import static android.view.ViewGroup.LayoutParams;
import static android.view.ViewGroup.MarginLayoutParams;

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
 * Helper to calculate various view dimensions
 */
public class DimensionCalculator {

  /**
   * Populates {@link Rect} with margins for any view.
   *
   *
   * @param margins rect to populate
   * @param view for which to get margins
   */
  public void initMargins(Rect margins, View view) {
    LayoutParams layoutParams = view.getLayoutParams();

    if (layoutParams instanceof MarginLayoutParams) {
      MarginLayoutParams marginLayoutParams = (MarginLayoutParams) layoutParams;
      initMarginRect(margins, marginLayoutParams);
    } else {
        margins.set(0, 0, 0, 0);
    }
  }

  /**
   * Converts {@link MarginLayoutParams} into a representative {@link Rect}.
   *
   * @param marginRect Rect to be initialized with margins coordinates, where
   * {@link MarginLayoutParams#leftMargin} is equivalent to {@link Rect#left}, etc.
   * @param marginLayoutParams margins to populate the Rect with
   */
  private void initMarginRect(Rect marginRect, MarginLayoutParams marginLayoutParams) {
      marginRect.set(
        marginLayoutParams.leftMargin,
        marginLayoutParams.topMargin,
        marginLayoutParams.rightMargin,
        marginLayoutParams.bottomMargin
    );
  }

}
