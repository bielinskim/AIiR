package com.bielinskim;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Document;

import java.time.LocalDate;
import java.time.Period;

public class Actor {

    int id;
    String firstName;
    String lastName;
    String gender;
    String birthDate;

    public Actor(int id, String firstName, String lastName, String gender, String birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "Actor " +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", birthDate='" + birthDate + '\'';
    }

    public Element getElement(Document doc) {
        Element actorElement = doc.createElement("aktor");

        Attr attr = doc.createAttribute("id");
        attr.setValue(String.valueOf(id));
        actorElement.setAttributeNode(attr);

        Element lastNameElement = doc.createElement("nazwisko");
        lastNameElement.appendChild(doc.createTextNode(lastName));

        Element firstNameElement = doc.createElement("imie");
        firstNameElement.appendChild(doc.createTextNode(firstName));

        Element genderElement = doc.createElement("plec");
        genderElement.appendChild(doc.createTextNode(gender));

        Element birthDateElement = doc.createElement("data_urodzenia");
        birthDateElement.appendChild(doc.createTextNode(birthDate));

        int yearsOld = getYearsOld(birthDate);
        Element yearsOldElement = doc.createElement("wiek");
        yearsOldElement.appendChild(doc.createTextNode(String.valueOf(yearsOld)));

        actorElement.appendChild(lastNameElement);
        actorElement.appendChild(firstNameElement);
        actorElement.appendChild(genderElement);
        actorElement.appendChild(birthDateElement);
        actorElement.appendChild(yearsOldElement);

        return actorElement;
    }

    public int getYearsOld(String stringBirthDate) {
        LocalDate birthDate = LocalDate.parse(stringBirthDate);
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }
}
