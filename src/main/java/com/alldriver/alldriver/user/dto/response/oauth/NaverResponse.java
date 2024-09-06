package com.alldriver.alldriver.user.dto.response.oauth;

import java.util.Map;

public class NaverResponse implements OAuth2Response {

    private Map<String, Object> attributes;

    public NaverResponse(Map<String, Object> attribute) {
        this.attributes = attribute;
    }
    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }

    @Override
    public String getProfileImage() {
        return null;
    }
}
