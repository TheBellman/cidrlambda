package net.parttimepolymath;

import net.parttimepolymath.iplib.Ranges;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RangeFactoryTest {
    @Test
    public void testGet() throws IOException, InterruptedException {
        Ranges result = RangeFactory.getRanges();
        assertNotNull(result);
        assertSame(result, RangeFactory.getRanges());
    }
}