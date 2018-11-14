package sg.edu.nus.iss.se8.medipal.models;

import java.io.Serializable;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.dao.MedicineDao;
import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;

public class Medicine implements Serializable {
    private Integer id;
    private String name;
    private String description;
    private Category category;

    public Medicine(Integer id, String name, String description, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public static List<Medicine> getAll() {
        return MedicineDao.getAll();
    }

    public static Medicine update(Medicine medicine) {
        return MedicineDao.update(medicine);
    }

    public Boolean dataSanityCheck() throws MedipalException {
        if (name == null || name.trim().equalsIgnoreCase("")) {
            throw new MedipalException("Name is mandatory", MedipalException.BAD_INPUT, MedipalException.Level.MAJOR, null);
        }
        if (description == null || description.trim().equalsIgnoreCase("")) {
            throw new MedipalException("Description is mandatory", MedipalException.BAD_INPUT, MedipalException.Level.MAJOR, null);
        }
        return true;
    }

    @Override
    public String toString() {
        return getName();
    }
}
