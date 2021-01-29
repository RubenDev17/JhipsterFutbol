package com.cev.futbol.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cev.futbol.web.rest.TestUtil;

public class PresidenteTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Presidente.class);
        Presidente presidente1 = new Presidente();
        presidente1.setId(1L);
        Presidente presidente2 = new Presidente();
        presidente2.setId(presidente1.getId());
        assertThat(presidente1).isEqualTo(presidente2);
        presidente2.setId(2L);
        assertThat(presidente1).isNotEqualTo(presidente2);
        presidente1.setId(null);
        assertThat(presidente1).isNotEqualTo(presidente2);
    }
}
