package com.sansoft.inscription.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sansoft.inscription.web.rest.TestUtil;

public class ScolariteTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Scolarite.class);
        Scolarite scolarite1 = new Scolarite();
        scolarite1.setId(1L);
        Scolarite scolarite2 = new Scolarite();
        scolarite2.setId(scolarite1.getId());
        assertThat(scolarite1).isEqualTo(scolarite2);
        scolarite2.setId(2L);
        assertThat(scolarite1).isNotEqualTo(scolarite2);
        scolarite1.setId(null);
        assertThat(scolarite1).isNotEqualTo(scolarite2);
    }
}
