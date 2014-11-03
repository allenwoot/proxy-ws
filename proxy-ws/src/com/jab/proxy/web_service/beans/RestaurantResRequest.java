package com.jab.proxy.web_service.beans;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class RestaurantResRequest extends ProxyRequest {

    public static String getConfirmation() {
        return String.format("We'll make a reservation at $%s for $%s people at $%s.",
                AllTranslationFields.RESTAURANT.getName(),
                AllTranslationFields.PARTY_SIZE.getName(),
                AllTranslationFields.DATE_TIME.getName());
    }

    private String created;
    private String dateTime;
    private Integer partySize;

    private String restaurant;

    public RestaurantResRequest() {
        super.setIntent(Intent.RESTAURANT_RESERVATION);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        } else if (this == obj) {
            return true;
        }

        final String objId = ((RestaurantResRequest) obj).getId();
        return objId != null && objId.equals(super.getId());
    }

    public String getCreated() {
        return this.created;
    }

    public String getDateTime() {
        return this.dateTime;
    }

    public Integer getPartySize() {
        return this.partySize;
    }

    public String getRestaurant() {
        return this.restaurant;
    }

    @Override
    public int hashCode() {
        return super.getId().hashCode();
    }

    public void setCreated(final String created) {
        this.created = created;
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
