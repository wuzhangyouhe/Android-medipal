package sg.edu.nus.iss.se8.medipal.models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertTrue;

public class MedicinePrescriptionTest {
    private MedicinePrescription medicinePrescription;

    @Before
    public void setUp() throws Exception {
        Category category = new Category(null, "Supplement", "Ongoing intake", Category.ReminderApplicableOption.Y, "SUP");

        Medicine medicine = new Medicine(null, "Paracetamol", "General painkiller", category);

        medicinePrescription = new MedicinePrescription(
                null,
                medicine,
                100,
                2,
                3,
                new Date(),
                new Date(),
                true,
                3
        );
    }

    @After
    public void tearDown() throws Exception {
        medicinePrescription = null;
    }

//    @Test
//    public void getDepletionDate() {
//        int numDoses;
//        int numDays;
//        Calendar calendar;
//        Date expectedDepletionDate;
//
//       /* numDoses = medicinePrescription.getQuantityIssued() / medicinePrescription.getDoseQuantity();
//        numDays = numDoses / medicinePrescription.getDosesPerDay();
//
//        calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_YEAR, numDays);
//        expectedDepletionDate = calendar.getTime();
//
//        assertEquals("getDepletionDate not same", expectedDepletionDate, medicinePrescription.getDepletionDate());
//
//        numDoses = medicinePrescription2.getQuantityIssued() / medicinePrescription2.getDoseQuantity();
//        numDays = numDoses / medicinePrescription2.getDosesPerDay();
//*/
//        calendar = Calendar.getInstance();
//        // calendar.add(Calendar.DAY_OF_YEAR, numDays);
//        expectedDepletionDate = calendar.getTime();
//
//        assertEquals(expectedDepletionDate, medicinePrescription2.getDepletionDate());
//    }

    @Test
    public void getCurrentQuantityShouldReturnCorrectBalance() throws Exception {
        Category category = new Category(null, "Supplement", "Ongoing intake", Category.ReminderApplicableOption.Y, "SUP");
        Medicine medicine = new Medicine(null, "Paracetamol", "General painkiller", category);
        MedicinePrescription medicinePrescription = new MedicinePrescription(
                null,
                medicine,
                100,
                2,
                3,
                new Date(),
                new Date(),
                true,
                3
        );

        Consumption consumption1 = new Consumption(null, medicinePrescription, 10, new Date());
        Consumption consumption2 = new Consumption(null, medicinePrescription, 10, new Date());
        Consumption consumption3 = new Consumption(null, medicinePrescription, 10, new Date());

        medicinePrescription.addConsumption(consumption1);
        assertTrue("quantity not correct", 90 == medicinePrescription.getCurrentQuantity());

        medicinePrescription.addConsumption(consumption2);
        assertTrue("quantity not correct", 80 == medicinePrescription.getCurrentQuantity());

        medicinePrescription.addConsumption(consumption3);
        assertTrue("quantity not correct", 70 == medicinePrescription.getCurrentQuantity());
    }
}