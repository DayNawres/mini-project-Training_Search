package com.wedevzone.digiparc.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.wedevzone.digiparc.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmplacementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Emplacement.class);
        Emplacement emplacement1 = new Emplacement();
        emplacement1.setId(1L);
        Emplacement emplacement2 = new Emplacement();
        emplacement2.setId(emplacement1.getId());
        assertThat(emplacement1).isEqualTo(emplacement2);
        emplacement2.setId(2L);
        assertThat(emplacement1).isNotEqualTo(emplacement2);
        emplacement1.setId(null);
        assertThat(emplacement1).isNotEqualTo(emplacement2);
    }
}
