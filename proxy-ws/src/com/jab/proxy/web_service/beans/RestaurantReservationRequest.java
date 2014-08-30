package com.jab.proxy.web_service.beans;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class RestaurantReservationRequest extends ProxyRequest {
    private String created;
    private String dateTime;
    private String id;
    private Integer partySize;
    private String restaurant;

    public RestaurantReservationRequest() {
        super.setIntent(Intent.RESTAURANT_RESERVATION);
    }

    public String getCreated() {
        return this.created;
    }

    public String getDateTime() {
        return this.dateTime;
    }

    public String getId() {
        return this.id;
    }

    public Integer getPartySize() {
        return this.partySize;
    }

    public String getRestaurant() {
        return this.restaurant;
    }

    public void setCreated(final String created) {
        this.created = created;
    }

    public void setDateTime(final String dateTime) {
        this.dateTime = dateTime;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public void setPartySize(final Integer partySize) {
        this.partySize = partySize;
    }

    public void setRestaurant(final String restaurant) {
        this.restaurant = restaurant;
    }
}
