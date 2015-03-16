package edu.harvard.we99.domain;

import edu.harvard.we99.util.JacksonUtil;
import org.junit.Test;

import static edu.harvard.we99.test.BaseFixture.load;
import static org.junit.Assert.assertEquals;

/**
 * @author mford
 */
public class ExtraPropertiesTest {
    @Test
    public void test() throws Exception {
        Compound c = JacksonUtil.fromString(
                load("/ExtraPropertiesTest/compound.json"), Compound.class);
        assertEquals("h20", c.getName());
        assertEquals(1234, c.getId().longValue());
    }
}
