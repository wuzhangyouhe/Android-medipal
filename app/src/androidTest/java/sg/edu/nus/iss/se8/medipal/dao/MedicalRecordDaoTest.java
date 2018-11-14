package sg.edu.nus.iss.se8.medipal.dao;

import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import sg.edu.nus.iss.se8.medipal.models.MedicalRecord;

import static org.junit.Assert.assertEquals;

public class MedicalRecordDaoTest {

    @Before
    public void setUp() throws Exception {
        DBDAO.init(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void save() throws Exception {
        //create a new medical record
        //save the medical record - get a handle on the saved record
        //get the record using the ID from the handle
        //compare the values

        Date today = new Date();
        MedicalRecord medicalRecord = new MedicalRecord(null, "Fever", today, MedicalRecord.Type.A);
        MedicalRecord savedMedicalRecord = MedicalRecordDao.save(medicalRecord);
        medicalRecord.setId(null);
        //MedicalRecordDao dao = new MedicalRecordDao(InstrumentationRegistry.getTargetContext());
        savedMedicalRecord = MedicalRecordDao.save(medicalRecord);

        assertEquals("Fever", savedMedicalRecord.getNameOfAilment());
    }

    @Test
    public void testCRUD() throws Exception {
        MedicalRecordDao.save(new MedicalRecord(null, "new condition name",new Date() , MedicalRecord.Type.valueOf("C")));
        MedicalRecordDao.update(new MedicalRecord(4, "new updated condition name",new Date() , MedicalRecord.Type.valueOf("C")));
        MedicalRecordDao.delete(4);
    }
}