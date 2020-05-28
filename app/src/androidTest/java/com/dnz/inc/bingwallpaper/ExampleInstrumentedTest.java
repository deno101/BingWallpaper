package com.dnz.inc.bingwallpaper;

import android.content.Context;
import android.util.Log;

import com.dnz.inc.bingwallpaper.utils.TimeUtils;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.dnz.inc.bingwallpaper", appContext.getPackageName());
    }

    @Test
    public void testDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                0,
                0,
                0
        );
        calendar.set(Calendar.MILLISECOND, 0);

        Date date1 = TimeUtils.getDate(TimeUtils.PATTERN_JSON_DB, "20200524");

        long difference = calendar.getTimeInMillis() - date1.getTime();
        int days = (int) (difference / TimeUtils.ONE_DAY);
        assertEquals(days, 4);
    }
}
