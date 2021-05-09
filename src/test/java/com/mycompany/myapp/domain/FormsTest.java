package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Forms.class);
        Forms forms1 = new Forms();
        forms1.setId(1L);
        Forms forms2 = new Forms();
        forms2.setId(forms1.getId());
        assertThat(forms1).isEqualTo(forms2);
        forms2.setId(2L);
        assertThat(forms1).isNotEqualTo(forms2);
        forms1.setId(null);
        assertThat(forms1).isNotEqualTo(forms2);
    }
}
