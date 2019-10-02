package com.matadesigns.keyprapi;


import com.google.gson.annotations.SerializedName;
import com.matadesigns.keyprapi.environments.BasicApiEnvironment;
import com.matadesigns.keyprapi.models.PagedReservations;
import com.matadesigns.keyprapi.models.Reservation;
import com.matadesigns.keyprapi.models.ReservationTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import androidx.test.filters.SmallTest;
import androidx.test.runner.AndroidJUnit4;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class KeyprApiInstrumentedTest implements JWTGenerator {

    @Override
    public void jwtTokenNeeded(JWTNeeded handler) {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject keyprParams = new JSONObject();
            try {
                keyprParams.put("FirstName", "Nicholas");
                keyprParams.put("LastName", "Mata");
                keyprParams.put("EmailAddress", "nicholas+test@matadesigns.net");
                jsonObject.put("KeyPrParams", keyprParams);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SycuanJWT res = new Request<SycuanJWT>(Request.Method.Post,
                    new URL("https://dev.sicdevmobile.com/COGMobilegateway/api/player/keypr_jot_token"),
                    jsonObject.toString(), SycuanJWT.class, new HashMap<String, String>()
            {
                {
                    put("Client-ID", "ed5134bgf0113456-ios-normal-retina");
                }
            }).perform();
            System.out.println(res);
            handler.onComplete(res.jwt);
        } catch(Exception e) {
            e.printStackTrace();
            handler.onFailure(e);
        }
    }

    class SycuanJWT implements Serializable {
        @SerializedName("KEYPR_JOT")
        String jwt;
    }

    @Test
    public void keyprFederateAuth() {
        KeyprApi api = new KeyprApi(BasicApiEnvironment.staging, this);
        api.checkAuthentication();
        assertTrue(api.getAccessToken().isValid());
    }

    @Test
    public void reservations() {
        KeyprApi api = new KeyprApi(BasicApiEnvironment.staging, this);
        api.getReservations("/active?include=locations", new Handler<PagedReservations>() {
            @Override
            public void onSuccess(PagedReservations pagedReservations) {
                assertTrue(pagedReservations.data.size() > 0);
            }

            @Override
            public void onError(Exception err) {
                fail();
            }
        });
    }

    @Test
    public void checkIn() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        KeyprApi api = new KeyprApi(BasicApiEnvironment.staging, this);
        api.perform(KeyprAsyncTask.CHECK_IN, "ac30633f-2f1f-4853-b643-a6b0f60fbc6f", 5000, 1000, new Handler<ReservationTask>() {
            @Override
            public void onSuccess(ReservationTask value) {
                assertTrue(true);
                signal.countDown();
            }

            @Override
            public void onError(Exception err) {
                err.printStackTrace();
                assertTrue(false);
                signal.countDown();
            }
        });
        signal.await();
    }

    @Test
    public void checkOut() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        KeyprApi api = new KeyprApi(BasicApiEnvironment.staging, this);
        api.perform(KeyprAsyncTask.CHECK_OUT, "ac30633f-2f1f-4853-b643-a6b0f60fbc6f", 5000, 1000, new Handler<ReservationTask>() {
            @Override
            public void onSuccess(ReservationTask value) {
                assertTrue(true);
                signal.countDown();
            }

            @Override
            public void onError(Exception err) {
                err.printStackTrace();
                assertTrue(false);
                signal.countDown();
            }
        });
        signal.await();
    }
}