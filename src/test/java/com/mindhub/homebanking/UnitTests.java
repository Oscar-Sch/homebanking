package com.mindhub.homebanking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.mindhub.homebanking.utils.Utilities.cardCvvGenerator;
import static com.mindhub.homebanking.utils.Utilities.cardNumberGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class UnitTests {

    @Test
    public void cardNumberLenght(){
        String number=cardNumberGenerator();
        assertThat(number,hasLength(19));
    }
    @Test
    public void cardCvvLenght(){
        String number=cardCvvGenerator();
        assertThat(number,hasLength(3));
    }
}
