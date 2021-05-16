package com.okm1208.vacation.common;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
public class BigDecimalTests {

    @Test
    public void compareToBigDecimalTest(){
        BigDecimal one = BigDecimal.valueOf(1.0);
        BigDecimal zero = BigDecimal.valueOf(0);

        assertThat(one.compareTo(zero) , is(1));
        assertThat(zero.compareTo(one), is(-1));

        BigDecimal one2 = BigDecimal.ONE;

        assertThat(one.compareTo(one2), is(0));
        BigDecimal zero2 = BigDecimal.ZERO;
        assertThat(zero.compareTo(zero2), is(0));



    }
}
