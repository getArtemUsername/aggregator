package ru.one.more.util;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * Created by aboba on 28.01.17.
 */
public class StrUtilsTest {
    @Test
    public void testIsWord() {
        assertFalse(StrUtils.isWord("  "));
        assertTrue(StrUtils.isWord("test"));
    }

    @Test
    public void testGetStackTraceText() {
        StrUtils.getStackTraceText(new Throwable("test"));
    }

}
