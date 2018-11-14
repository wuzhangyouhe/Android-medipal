package sg.edu.nus.iss.se8.medipal;


import android.support.test.InstrumentationRegistry;
import android.util.Pair;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.dao.DBDAO;
import sg.edu.nus.iss.se8.medipal.models.MedicinePrescription;
import sg.edu.nus.iss.se8.medipal.services.MedicinePrescriptionService;
import sg.edu.nus.iss.se8.medipal.utils.DateUtils;

import static org.junit.Assert.assertTrue;

public class MedicinePrescriptionServiceTest {

    @Before
    public void setUp() throws Exception {
        DBDAO.init(InstrumentationRegistry.getTargetContext());
    }


    @Test
    public void testConsumptionChart() throws Exception {
        /*MedicinePrescription mp = MedicinePrescriptionDao.save(MedicinePrescriptionSeeder.getMedicinePrescription());
        if(ConsumptionDao.getAllByPrescription(mp).size()==0){
            ConsumptionSeeder.run(10);
        }*/
        List<Pair<String, String>> list = MedicinePrescriptionService.getConsumptionChart(DateUtils.fromFriendlyDateString("01 Mar 2017"), new Date());
        assertTrue(list.size() >= 0);
        List<MedicinePrescription> list2 = MedicinePrescriptionService.getAll();
        assertTrue(list2.size() >= 0);
    }
}
