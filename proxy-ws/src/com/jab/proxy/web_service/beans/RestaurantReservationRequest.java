package com.jab.proxy.web_service.beans;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class RestaurantReservationRequest extends ProxyRequest {
    private String dateTime;
    private Integer partySize;
    private String restaurant;

    public String getDateTime() {
        return this.dateTime;
    }

    public Integer getPartySize() {
        return this.partySize;
    }

    public String getRestaurant() {
        return this.restaurant;
    }

    public void setDateTime(final String dateTime) {
        this.dateTime = dateTime;
    }

    public void setPartySize(final Integer partySize) {
        this.partySize = partySize;
    }

    public void setRestaurant(final String restaurant) {
        this.restaurant = restaurant;
    }
}
