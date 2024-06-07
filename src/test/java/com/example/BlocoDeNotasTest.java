package test.java.com.example;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import main.java.com.example.BlocoDeNotas;

public class BlocoDeNotasTest {
    
    @Test
    public void testApp() {
        BlocoDeNotas blocoDeNotas = new BlocoDeNotas();
        assertNotNull(blocoDeNotas);
    }
}
