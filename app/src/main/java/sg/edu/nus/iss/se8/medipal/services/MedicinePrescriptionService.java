package sg.edu.nus.iss.se8.medipal.services;

import android.content.Context;
import android.database.SQLException;
import android.util.Pair;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.dao.ConsumptionDao;
import sg.edu.nus.iss.se8.medipal.dao.MedicinePrescriptionDao;
import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.models.Consumption;
import sg.edu.nus.iss.se8.medipal.models.MedicinePrescription;
import sg.edu.nus.iss.se8.medipal.utils.DateUtils;

public class MedicinePrescriptionService extends MedicinePrescriptionDao {
    private Context context;

    public MedicinePrescriptionService(Context context) {
        this.context = context;
    }

    public void createMedicinePrescription(MedicinePrescription medicinePrescription) throws MedipalException {
        MedicinePrescription savedMedicinePrescription;
        try {
            savedMedicinePrescription = MedicinePrescriptionDao.save(medicinePrescription);
        } catch (MedipalException me) {
            throw me;
        } catch (SQLException se) {
            throw new MedipalException("", MedipalException.DB_ERROR, MedipalException.Level.SEVERE, se);
        } catch (Exception e) {
            throw new MedipalException("", MedipalException.ERROR, MedipalException.Level.SEVERE, e);
        }

        ReminderService reminderService = new ReminderService(context);
        reminderService.createRemindersFor(savedMedicinePrescription);
    }

    public static MedicinePrescription update(MedicinePrescription medicinePrescription) {
        return MedicinePrescriptionDao.update(medicinePrescription);
    }

    public void deleteMedicinePrescription(Integer medicinePrescriptionId) {
        MedicinePrescription medicinePrescription = MedicinePrescriptionService.get(medicinePrescriptionId);

        ReminderService reminderService = new ReminderService(context);
        reminderService.deleteRemindersFor(medicinePrescription);

        MedicinePrescriptionDao.delete(medicinePrescriptionId);
    }

    public static List<MedicinePrescription> getAll() {
        List<MedicinePrescription> list = MedicinePrescriptionDao.getAll();
        for (MedicinePrescription mp : list) {
            mp.setConsumptions(ConsumptionDao.getAllByPrescription(mp));
        }
        return list;
    }

    public static MedicinePrescription get(Integer id) {
        MedicinePrescription mp = MedicinePrescriptionDao.get(id);
        mp.setConsumptions(ConsumptionDao.getAllByPrescription(mp));
        return mp;
    }

    public static List<Pair<String, String>> getConsumptionChart(Date fromDate, Date toDate) throws ParseException {
        List<Pair<String, String>> list = new ArrayList<Pair<String, String>>();

        //Add all charts
        for (MedicinePrescription mp : MedicinePrescriptionDao.getAll()) {
            list.addAll(getConsumptionChart(mp));
        }
        //Sort charts by date and filter extra entries
        Collections.sort(list, new Comparator<Pair<String, String>>() {
            @Override
            public int compare(Pair<String, String> o1, Pair<String, String> o2) {
                try {
                    return (int) ((DateUtils.fromFriendlyDateString(o2.first).getTime() - DateUtils.fromFriendlyDateString(o1.first).getTime()) / 1000);
                } catch (Exception e) {
                    return 0;
                }
            }
        });
        Iterator<Pair<String, String>> itr = list.iterator();
        while (itr.hasNext()) {
            long time = DateUtils.fromFriendlyDateString(itr.next().first).getTime();
            if (time < fromDate.getTime() || time > DateUtils.twentyFourHoursAfter(toDate).getTime()) {
                itr.remove();
            }
        }
        return list;
    }

    private static List<Pair<String, String>> getConsumptionChart(MedicinePrescription mp) throws ParseException {
        List<Pair<String, String>> list = new ArrayList<Pair<String, String>>();

        List<Consumption> realConsumptions = ConsumptionDao.getAllByPrescription(mp);

        int noOfDays = 0;
        noOfDays = DateUtils.numberOfDays(new Date(), mp.getIssueDate());

        HashMap<String, Integer> counts = new HashMap<String, Integer>();
        String dateString = null;
        Integer count = null;
        for (Consumption c : realConsumptions) {
            dateString = DateUtils.toFriendlyDateString(c.getConsumptionDate());
            count = counts.get(dateString);
            if (count == null) {
                count = 0;
            }
            count += c.getQuantity();
            counts.put(dateString, count);
        }

        Iterator<Consumption> iterator = realConsumptions.iterator();
        Consumption c = null;
        String date = DateUtils.toFriendlyDateString(mp.getIssueDate()); // Starting date for chart
        if (iterator.hasNext()) {
            c = iterator.next(); // Move the pointer to first consumption
        }
        for (int i = 0; i < noOfDays; i++) { // Per day
            if (!counts.containsKey(date)) { // No Consumption on this day and medicine schedule.
                list.add(new Pair<String, String>(date, "MISSED: " + mp.getMedicine().getName() + ", Consumed: 0"));
            } else {
                while (c != null && DateUtils.toFriendlyDateString(c.getConsumptionDate()).equals(date)) {
                    //Show real consumptions for the day
                    list.add(new Pair<String, String>(DateUtils.toFriendlyDateTimeString(c.getConsumptionDate()), "APPROPRIATE: " + mp.getMedicine().getName() + ", Consumed: " + mp.getDoseQuantity()));
                    /*if (c.getQuantity() == mp.getDoseQuantity()) {
                        list.add(new Pair<String, String>(DateUtils.toFriendlyDateTimeString(c.getConsumptionDate()), "APPROPRIATE: "+mp.getMedicine().getName()+", Consumption Correct"));
                    } else if (c.getQuantity() < mp.getDoseQuantity()) {
                        list.add(new Pair<String, String>(DateUtils.toFriendlyDateTimeString(c.getConsumptionDate()), "UNDERDOSE: "+mp.getMedicine().getName()+", Consumed: " + c.getQuantity() + " vs " + mp.getDoseQuantity()));
                    } else {
                        list.add(new Pair<String, String>(DateUtils.toFriendlyDateTimeString(c.getConsumptionDate()), "OVERDOSE: "+mp.getMedicine().getName()+", Consumed: " + c.getQuantity() + " vs " + mp.getDoseQuantity()));
                    }*/
                    if (iterator.hasNext()) {
                        c = iterator.next(); // Move the pointer to next consumption
                    } else {
                        c = null;
                    }
                }
                //Calculate Differences
                int diff = mp.getDoseQuantity() * mp.getDoseFrequency() - counts.get(date);
                if (diff > 0) {
                    list.add(new Pair<String, String>(date, "TOTAL UNDERDOSE: " + mp.getMedicine().getName() + ", by: " + diff));
                } else if (diff < 0) {
                    list.add(new Pair<String, String>(date, "TOTAL OVERDOSE: " + mp.getMedicine().getName() + ", by: " + (diff * -1)));
                }
            }
            try {
                date = DateUtils.nextFriendlyDateString(date);
            } catch (Exception me) {
                date = null;
                break;
            }
        }

        return list;
    }
}
