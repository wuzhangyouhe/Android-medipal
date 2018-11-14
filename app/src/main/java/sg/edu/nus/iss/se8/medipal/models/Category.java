package sg.edu.nus.iss.se8.medipal.models;

import android.widget.ArrayAdapter;

import java.io.Serializable;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.dao.CategoryDao;
import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;

public class Category implements Serializable {
    private Integer id;
    private String name;
    private String code;
    private ReminderApplicableOption reminderApplicable;
    private String description;

    public enum ReminderApplicableOption {
        Y("Yes"), N("No"), OY("Optional (Default: Yes)"), ON("Optional (Default: No)");
        String description;

        ReminderApplicableOption(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    public Category(Integer id, String name, String code, ReminderApplicableOption reminderApplicable, String description) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.reminderApplicable = reminderApplicable;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ReminderApplicableOption isReminderApplicable() {
        return reminderApplicable;
    }

    public void setReminderApplicable(ReminderApplicableOption reminderApplicable) {
        this.reminderApplicable = reminderApplicable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static List<Category> getAll() {
        return CategoryDao.getAll();
    }

    public static Category update(Category category) {
        return CategoryDao.update(category);
    }

    public static void delete(Integer categoryId) {
        CategoryDao.delete(categoryId);
    }

    public boolean equals(Object category) {
        if (category instanceof Category) {
            Category cat = (Category) category;
            if (this.getName().equals(cat.getName())&&this.getDescription().equals(cat.getDescription())&&this.getCode().equals(cat.getCode()))
                return true;
        }
        return false;
    }


    public Boolean dataSanityCheck() throws MedipalException {
        if (name == null || name.trim().equalsIgnoreCase("")) {
            throw new MedipalException("Name is mandatory", MedipalException.BAD_INPUT, MedipalException.Level.MAJOR, null);
        }
        if (code == null || code.trim().equalsIgnoreCase("")) {
            throw new MedipalException("Code is mandatory", MedipalException.BAD_INPUT, MedipalException.Level.MAJOR, null);
        }
        if (description == null || description.trim().equalsIgnoreCase("")) {
            throw new MedipalException("Description is mandatory", MedipalException.BAD_INPUT, MedipalException.Level.MAJOR, null);
        }
        return true;
    }
    @Override
    public String toString() {
        return name;
    }
}
