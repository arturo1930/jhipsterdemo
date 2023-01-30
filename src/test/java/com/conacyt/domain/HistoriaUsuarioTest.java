package com.conacyt.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.conacyt.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistoriaUsuarioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoriaUsuario.class);
        HistoriaUsuario historiaUsuario1 = new HistoriaUsuario();
        historiaUsuario1.setId(1L);
        HistoriaUsuario historiaUsuario2 = new HistoriaUsuario();
        historiaUsuario2.setId(historiaUsuario1.getId());
        assertThat(historiaUsuario1).isEqualTo(historiaUsuario2);
        historiaUsuario2.setId(2L);
        assertThat(historiaUsuario1).isNotEqualTo(historiaUsuario2);
        historiaUsuario1.setId(null);
        assertThat(historiaUsuario1).isNotEqualTo(historiaUsuario2);
    }
}
