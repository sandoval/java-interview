package com.vingcard.athos.interview.dto.response;

import java.time.Instant;
import java.util.List;

public record JwtAuthenticatedUserInfo(String sub,
                                       Instant issuedAt,
                                       Instant expiresAt,
                                       List<String> roles) {
}
