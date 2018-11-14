package sg.edu.nus.iss.se8.medipal.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import sg.edu.nus.iss.se8.medipal.utils.faker.Faker;

import static org.junit.Assert.assertTrue;

public class FakerTest {
    private Faker faker;
    private SimpleDateFormat dateFormatter;

    @Before
    public void setUp() throws Exception {
        faker = new Faker();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
    }

    @After
    public void tearDown() throws Exception {
        faker = null;
        dateFormatter = null;
    }

    @Test
    public void numberBetween() throws Exception {
        long before = 1;
        long after = 100;

        long newLong = faker.numberBetween(before, after);

        assertTrue(newLong > before);
        assertTrue(newLong < after);
    }

    @Test
    public void dateBetween() throws Exception {
        Date startDate = dateFormatter.parse("2017-01-01");
        Date endDate = dateFormatter.parse("2017-01-20");
        Date receivedDate = faker.dateBetween(startDate, endDate);

        assertTrue(receivedDate.toString(), receivedDate.compareTo(startDate) > 0);
        assertTrue(receivedDate.toString(), receivedDate.compareTo(endDate) < 0);
    }

    @Test
    public void dateBeforeToday() throws Exception {
        Date receivedDate = faker.dateBeforeToday();
        Date today = new Date();

        assertTrue(receivedDate.compareTo(today) < 0);
    }
}