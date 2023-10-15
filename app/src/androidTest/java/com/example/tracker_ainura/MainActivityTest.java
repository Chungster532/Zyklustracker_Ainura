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
public class MainActivityTest {
    @Test
    public void test_isActivityInView() {
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.main)).check(matches(isDisplayed())); //looks for this id in a view in the displayed activity
    }

    @Test
    public void test_visibilityViews() {
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.button_ersterTag)).check(matches(isDisplayed()));
        onView(withId(R.id.calenderview_phasen)).check(matches(isDisplayed()));
        onView(withId(R.id.cardview_aktuell)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_infoWissen)).check(matches(isDisplayed()));
    }

    @Test
    public void test_btnText() {
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.button_ersterTag)).check(matches(withText("Ende der Periode?")));//Text ändern je nach Zyklusphase!
    }

    @Test
    public void test_navWissen() {
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.btn_infoWissen)).perform(click());
        onView(withId(R.id.wissen)).check(matches(isDisplayed()));
    }
}