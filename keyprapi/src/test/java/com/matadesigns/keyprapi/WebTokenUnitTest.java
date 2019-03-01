package com.matadesigns.keyprapi;

import org.junit.Test;

import java.util.Date;

import static java.lang.Thread.sleep;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertSame;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class WebTokenUnitTest {

    @Test
    public void webTokenValidation() {
        WebToken token = new WebToken();
        token.value = "QVZBTElEVE9LRU4=";
        Date expiration = new Date();
        expiration.setTime(expiration.getTime() + 10);
        token.expiration = expiration;
        assertTrue(token.isValid());
        token.value = null;
        assertFalse(token.isValid());
    }

    @Test
    public void webTokenExpiresIn() throws InterruptedException {
        WebToken token = new WebToken();
        token.expiresIn(5);
        sleep(1000);
        assertFalse(token.isExpired());
        sleep(5000);
        assertTrue(token.isExpired());
    }

    @Test
    public void webTokenExpiresOn() {
        WebToken token = new WebToken();
        token.expiresOn(1943470337);
        // Starting from 0
        assertEquals(7, token.expiration.getMonth());
        // Starting from 1900
        assertEquals(131, token.expiration.getYear());
    }
}
