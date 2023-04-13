package com.wedevzone.digiparc.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.wedevzone.digiparc.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubscriberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Subscriber.class);
        Subscriber subscriber1 = new Subscriber();
        subscriber1.setId(1L);
        Subscriber subscriber2 = new Subscriber();
        subscriber2.setId(subscriber1.getId());
        assertThat(subscriber1).isEqualTo(subscriber2);
        subscriber2.setId(2L);
        assertThat(subscriber1).isNotEqualTo(subscriber2);
        subscriber1.setId(null);
        assertThat(subscriber1).isNotEqualTo(subscriber2);
    }
}
