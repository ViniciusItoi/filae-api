package com.filae.api;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateHashes {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Passwords and their hashes
        String[] passwords = {
            "Password123!",      // Alice
            "Customer456!",      // Bob
            "Test@1234",         // Carol
            "Merchant789!",      // Tony
            "Shop@5678",         // Maria
            "Admin@123"          // Admin
        };

        String[] users = {
            "Alice Silva (alice@example.com)",
            "Bob Santos (bob@example.com)",
            "Carol Oliveira (carol@example.com)",
            "Tony Rossi (tony@tonysrestaurant.com)",
            "Maria Café (maria@mariacafe.com)",
            "System Admin (admin@filae.com)"
        };

        System.out.println("Generated BCrypt Hashes:\n");
        for (int i = 0; i < passwords.length; i++) {
            String hash = encoder.encode(passwords[i]);
            System.out.println(users[i]);
            System.out.println("Password: " + passwords[i]);
            System.out.println("Hash: " + hash);
            System.out.println();
        }
    }
}

