package br.gov.sp.cps.demo.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CpfValidadorTest {

    @Test
    void aceitaCpfValido() {
        assertTrue(CpfValidador.valido("52998224725"));
    }

    @Test
    void recusaDigitoVerificadorErrado() {
        assertFalse(CpfValidador.valido("52998224726"));
    }

    @Test
    void recusaTodosDigitosIguais() {
        assertFalse(CpfValidador.valido("11111111111"));
    }

    @Test
    void recusaTamanhoErrado() {
        assertFalse(CpfValidador.valido("123"));
    }
}
