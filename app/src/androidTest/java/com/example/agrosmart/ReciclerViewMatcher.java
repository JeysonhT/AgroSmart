package com.example.agrosmart;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class ReciclerViewMatcher {
    public static Matcher<View> withItemCount(final int expectedCount) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("has " + expectedCount + " items in adapter");
            }

            @Override
            protected boolean matchesSafely(RecyclerView recyclerView) {
                if (recyclerView.getAdapter() == null) {
                    return false;
                }
                return recyclerView.getAdapter().getItemCount() == expectedCount;
            }
        };
    }
}
