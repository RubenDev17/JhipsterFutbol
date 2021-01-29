package com.cev.futbol.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cev.futbol.web.rest.TestUtil;

public class LigaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Liga.class);
        Liga liga1 = new Liga();
        liga1.setId(1L);
        Liga liga2 = new Liga();
        liga2.setId(liga1.getId());
        assertThat(liga1).isEqualTo(liga2);
        liga2.setId(2L);
        assertThat(liga1).isNotEqualTo(liga2);
        liga1.setId(null);
        assertThat(liga1).isNotEqualTo(liga2);
    }
}
