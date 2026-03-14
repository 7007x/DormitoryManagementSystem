package com.example.springboot;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateBCryptHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String encoded = encoder.encode("asdfasdf");
        System.out.println(encoded);

        boolean b = encoder.matches("asdfasdf", encoded);
        System.out.println(b);

    }

}
