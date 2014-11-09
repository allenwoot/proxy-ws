package com.jab.proxy.web_service.core;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.glassfish.grizzly.http.util.HttpStatus;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.jab.proxy.web_service.beans.AllTranslationFields;
import com.jab.proxy.web_service.beans.Intent;
import com.jab.proxy.web_service.beans.RestaurantResRequest;
import com.jab.proxy.web_service.beans.TranslateResult;
import com.jab.proxy.web_service.beans.TranslationField;
import com.jab.proxy.web_service.beans.wit.WitResponse;
import com.jab.proxy.web_service.exceptions.ProxyException;
import com.jab.proxy.web_service.utilities.ProxyUtils;

/**
 * Provides a service to integrate with wit.ai
 */
public class WitService {

    /**
     * Classifies the specified request string using wit
     */
    public TranslateResult translate(final String requestString) throws ProxyException {
        final WitResponse witResponse = issueWitCall(requestString);
        return mapWitResponseToTranslateResult(witResponse);
    }

    private WitResponse issueWitCall(final String requestString) throws ProxyException {
        CloseableHttpResponse response = null;

        try {
            final StringBuilder uriSB = new StringBuilder("https://api.wit.ai/message");
            uriSB.append("?v=20140830");
            uriSB.append("&q=").append(URLEncoder.encode(requestString, "UTF-8").replace("+", "%20"));

            final HttpGet httpGet = new HttpGet(uriSB.toString());
            httpGet.addHeader("Authorization", "Bearer KZ7E4SEPKYPGTSA5GDGBEBTJHATROFIQ");

            final CloseableHttpClient httpclient = HttpClients.createDefault();
            response = httpclient.execute(httpGet);
            final String entityString = EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new ProxyException(entityString, HttpStatus.getHttpStatus(response.getStatusLine().getStatusCode()));
            }

            return new Gson().fromJson(entityString, WitResponse.class);
        } catch (final IOException e) {
            throw new ProxyException("Failed to read wit response", HttpStatus.getHttpStatus(response.getStatusLine().getStatusCode()));
        } finally {
            try {
                response.close();
            } catch (final IOException e) {
                // Do nothing
            }
        }

    }

    private TranslateResult mapToRestaurantReservation(final WitResponse witResponse) {
        final TranslateResult translateResult = new TranslateResult();
        final RestaurantResRequest restaurantReservationRequest = new RestaurantResRequest();
        final List<TranslationField> missingFields = new ArrayList<TranslationField>();
        final List<TranslationField> validatedFields = new ArrayList<TranslationField>();
        final HashMap<String, JsonElement> entitiesMap = witResponse.getOutcomes().get(0).get_entities();

        // Set party size
        if (entitiesMap.containsKey("party")) {
            restaurantReservationRequest.setPartySize(entitiesMap.get("party").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsInt());
            validatedFields.add(AllTranslationFields.PARTY_SIZE.getTranslationField());
        } else {
            missingFields.add(AllTranslationFields.PARTY_SIZE.getTranslationField());
        }

        // Set restaurant name
        if (entitiesMap.containsKey("restaurant")) {
            restaurantReservationRequest.setRestaurant(entitiesMap.get("restaurant").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString());
            validatedFields.add(AllTranslationFields.RESTAURANT.getTranslationField());
        } else {
            missingFields.add(AllTranslationFields.RESTAURANT.getTranslationField());
        }

        // Set date time
        if (entitiesMap.containsKey("datetime")) {
            final String dateTime = entitiesMap.get("datetime").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsJsonObject().get("from").getAsString();
            final String iso86901Time = ProxyUtils.toIso8601Time(dateTime);
            restaurantReservationRequest.setDateTime(iso86901Time);
            validatedFields.add(AllTranslationFields.DATE_TIME.getTranslationField());
        } else {
            missingFields.add(AllTranslationFields.DATE_TIME.getTranslationField());
        }

        // Set request
        translateResult.setRequest(restaurantReservationRequest);
        if (!missingFields.isEmpty()) {
            translateResult.setMissingFields(missingFields);
        }
        if (!validatedFields.isEmpty()) {
            translateResult.setValidatedFields(validatedFields);
        }
        translateResult.setConfirmation(RestaurantResRequest.getConfirmation());

        return translateResult;
    }

    private TranslateResult mapWitResponseToTranslateResult(final WitResponse witResponse) throws ProxyException {
        final Intent intent = Intent.fromSchemaName(witResponse.getOutcomes().get(0).get_intent());
        if (intent == null) {
            throw new ProxyException("Could not map request \"" + witResponse.get_text() + "\" to an intent", HttpStatus.BAD_REQUEST_400);
        }

        if (intent != Intent.RESTAURANT_RESERVATION) {
            throw new ProxyException("Only restaurant reservations currently supported", HttpStatus.BAD_REQUEST_400);
        }

        switch (intent) {
        case RESTAURANT_RESERVATION:
            return mapToRestaurantReservation(witResponse);
        }

        throw new IllegalStateException("Should not reach this point");
    }
}
