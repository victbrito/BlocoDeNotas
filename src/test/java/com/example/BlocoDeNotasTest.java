package com.example;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class BlocoDeNotasTest {
    
    @Test
    public void testApp() {
        BlocoDeNotas blocoDeNotas = new BlocoDeNotas();
        assertNotNull(blocoDeNotas);
    }
}
