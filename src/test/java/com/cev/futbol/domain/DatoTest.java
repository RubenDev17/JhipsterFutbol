package com.cev.futbol.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cev.futbol.web.rest.TestUtil;

public class DatoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dato.class);
        Dato dato1 = new Dato();
        dato1.setId(1L);
        Dato dato2 = new Dato();
        dato2.setId(dato1.getId());
        assertThat(dato1).isEqualTo(dato2);
        dato2.setId(2L);
        assertThat(dato1).isNotEqualTo(dato2);
        dato1.setId(null);
        assertThat(dato1).isNotEqualTo(dato2);
    }
}
