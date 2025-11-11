package com.example.agrosmart;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.agrosmart.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class HomeFragmentTest {

    @Before
    public void setUp() {
        ActivityScenario.launch(new Intent(ApplicationProvider.getApplicationContext(), TestActivity.class));
    }

    @Test
    public void test_RecyclerViewForCrops_IsDisplayed() {
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()));
    }

    @Test
    public void test_ButtonsForDeficienciesAndFertilizers_AreDisplayed() {
        onView(withId(R.id.buttonDeficiencies)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonFertilizers)).check(matches(isDisplayed()));
    }

    @Test
    public void test_RecyclerViewForNews_IsDisplayed() {
        onView(withId(R.id.noticeRecyclerView)).check(matches(isDisplayed()));
    }
}
