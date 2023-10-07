package com.facebook.dto.appuser;

import lombok.Data;

import java.util.Set;

@Data
public class UserSubscriptionsDto {
    private Long userId;
    private Set<Long> subscriptionIds;
}
