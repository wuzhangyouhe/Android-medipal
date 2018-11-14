package sg.edu.nus.iss.se8.medipal.models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.iss.se8.medipal.utils.RandomUtils;

import static org.junit.Assert.assertEquals;

public class UserTest {
    private User user1;

   /* @Before
    public void setUp() throws Exception {
        user1 = fnew User(null, "Saiful Shahril Saini",
                RandomUtils.date(),
                "S8517809A",
                "Blk 866 Tampines St 83",
                "502866",
                "B+"
        );
    }

    @After
    public void tearDown() throws Exception {
        user1 = null;
    }

    @Test
    public void addHealthMeasurement() throws Exception {
        Measurement healthMeasurement = new Measurement(
                1,
                RandomUtils.numberBetween(80, 120),
                RandomUtils.numberBetween(125, 180),
                (double)RandomUtils.numberBetween(50, 90),
                RandomUtils.numberBetween(30, 40),
                (double) RandomUtils.numberBetween(150, 190),
                RandomUtils.date(), (double)RandomUtils.numberBetween(60, 80)
                );

        user1.addHealthMeasurement(healthMeasurement);

        Measurement retrieved = user1.getHealthMeasurements().get(0);

        assertEquals(healthMeasurement.getSystolic(), retrieved.getSystolic());
        assertEquals(healthMeasurement.getDiastolic(), retrieved.getDiastolic());
        assertEquals(healthMeasurement.getPulse(), retrieved.getPulse());
        assertEquals(healthMeasurement.getTemperature(), retrieved.getTemperature());
        assertEquals(healthMeasurement.getSugar(), retrieved.getSugar());
        assertEquals(healthMeasurement.getWeight(), retrieved.getWeight());
        assertEquals(healthMeasurement.getMeasurementDate(), retrieved.getMeasurementDate());
    }*/

}