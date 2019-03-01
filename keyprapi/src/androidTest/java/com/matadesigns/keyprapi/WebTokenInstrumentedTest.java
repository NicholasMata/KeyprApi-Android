package com.matadesigns.keyprapi;

import android.content.Context;

import junit.framework.TestCase;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class WebTokenInstrumentedTest {
    @Test
    public void webTokenValiad() {
        String jwt = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2p3dC1pZHAuZXhhbXBsZS5jb20iLCJzdWIiOiJtYWlsdG86bWlrZUBleGFtcGxlLmNvbSIsIm5iZiI6MTU1MTIxMDE5MCwiZXhwIjoxOTQzNDcwMzM3LCJpYXQiOjE1NTEyMTAxOTAsImp0aSI6ImlkMTIzNDU2IiwidHlwIjoiaHR0cHM6Ly9leGFtcGxlLmNvbS9yZWdpc3RlciJ9.ddIi59HA83HOTqROaPG_UFjY22Xak7njuOz3z-AJ7vQk34sOzprvzLoQTBHkPSwcQ3BOqDhq7p1zX-v2TdhOz8W7FdVPC5XfrhytY39XzKZTNyeJEK2-E7rJAeL_GkugzHSjkXDeU14YXuTOeFTkd56WJBSpZ0RIB5-Eoe4qvF1tjzagjOmQEXviLVIuejYIlXf_1H0CDcgtu51jxWV4AYGS1vxAREcv7HQ68WqPh2sz3e9lNyZb0I2XX6mgVEOfY7dEhTX7EtjclbO91SCSpI9RQa_oQztVHj8J1iYFK2-XOlU9uQ8X-OVwSp3601kGyNfU0DhkfaUrTbyuV09-Fw";
        WebToken token = new WebToken();
        try {
            token.fill(jwt);
            TestCase.assertEquals(token.value, jwt);
            TestCase.assertEquals(new Date((long)(1943470337.0 * 1000)), token.expiration);
        } catch (JWTExceptions.JWTInvalidStructureException e) {
            e.printStackTrace();
        } catch (JWTExceptions.JWTInvalidPayloadException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
