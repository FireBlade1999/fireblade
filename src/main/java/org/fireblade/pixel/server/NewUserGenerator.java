package org.fireblade.pixel.server;

import java.util.UUID;

/**
 * Generate a new user.
 * 
 * @author swilliams
 */
public class NewUserGenerator {
    
    public User generate() {
        return new User(UUID.randomUUID());
        
    }

}
