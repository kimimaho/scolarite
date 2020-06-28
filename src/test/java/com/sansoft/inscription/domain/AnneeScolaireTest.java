package com.sansoft.inscription.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sansoft.inscription.web.rest.TestUtil;

public class AnneeScolaireTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnneeScolaire.class);
        AnneeScolaire anneeScolaire1 = new AnneeScolaire();
        anneeScolaire1.setId(1L);
        AnneeScolaire anneeScolaire2 = new AnneeScolaire();
        anneeScolaire2.setId(anneeScolaire1.getId());
        assertThat(anneeScolaire1).isEqualTo(anneeScolaire2);
        anneeScolaire2.setId(2L);
        assertThat(anneeScolaire1).isNotEqualTo(anneeScolaire2);
        anneeScolaire1.setId(null);
        assertThat(anneeScolaire1).isNotEqualTo(anneeScolaire2);
    }
}
