package org.falldetectives.falldetector;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String name;
    private String age;
    private String blood_type;
    private  int emergency_contact;
    private int ID;

    //constructors

    public UserModel(int ID, String name, String age, String blood_type, int emergency_contact) {
        this.name = name;
        this.age = age;
        this.blood_type = blood_type;
        this.emergency_contact = emergency_contact;
        this.ID=ID;
    }
    // Empty Constructor
    public UserModel() {
    }
    // To String

    @Override
    public String toString() {
        return
                "User:  " + name + " / " + age + " / " + blood_type + " / " + emergency_contact  ;
    }

    // getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBlood_type() {
        return blood_type;
    }

    public void setBlood_type(String blood_type) {
        this.blood_type = blood_type;
    }

    public int getEmergency_contact() {
        return emergency_contact;
    }

    public void setEmergency_contact(int emergency_contact) {
        this.emergency_contact = emergency_contact;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
