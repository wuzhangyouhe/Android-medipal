package sg.edu.nus.iss.se8.medipal.dao;

import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.models.Appointment;
import sg.edu.nus.iss.se8.medipal.models.Measurement;
import sg.edu.nus.iss.se8.medipal.utils.DateUtils;

import static org.junit.Assert.assertTrue;

public class MeasurementDaoTest {

    @Before
    public void setUp() throws Exception {
        DBDAO.init(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void testCRUD() throws Exception {
        List<Measurement> start = MeasurementDao.getAll();
        Measurement m  = new Measurement(null, 100,120,37.5,72,73.5,new Date(),139.0,66.0);
        MeasurementDao.save(m);
        Measurement m2 = new Measurement(m.getId(),m.getSystolic(),m.getDiastolic(),50000.0,m.getPulse(),m.getWeight(),m.getMeasurementDate(),m.getSugar(), m.getCholesterol());
        MeasurementDao.update(m2);
        List<Measurement> end = MeasurementDao.getAll();
        List<Measurement> listByDate = MeasurementDao.getAll(DateUtils.twentyFourHoursBefore(new Date()), new Date());
        MeasurementDao.delete(m2.getId());
    }
}