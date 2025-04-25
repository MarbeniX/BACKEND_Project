package com.backend.projectbackend.util.token;

import java.security.SecureRandom;

public class TokenGenerator {

    private static final SecureRandom random = new SecureRandom();

    public static String generateToken() {
        int token = 100000 + random.nextInt(900000);  // genera número entre 100000 y 999999
        return String.valueOf(token);
    }
}
