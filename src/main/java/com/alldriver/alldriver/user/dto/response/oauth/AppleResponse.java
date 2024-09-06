package com.alldriver.alldriver.user.dto.response.oauth;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class AppleResponse implements OAuth2Response{
    private final Map<String, Object> attribute;
    @Override
    public String getProvider() {
        return "apple";
    }

    @Override
    public String getProviderId() {
        return null;
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getProfileImage() {
        return null;
    }
}
