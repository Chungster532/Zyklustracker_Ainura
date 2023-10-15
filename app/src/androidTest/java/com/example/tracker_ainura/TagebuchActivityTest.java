package com.example.tracker_ainura;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4ClassRunner.class)

public class TagebuchActivityTest {
    @Test
    public void test_navNotiz() {
        ActivityScenario activityScenario = ActivityScenario.launch(TagebuchActivity.class);
        onView(withId(R.id.button_notiz_erstellen)).perform(click());
        onView(withId(R.id.notiz)).check(matches(isDisplayed()));
    }
}