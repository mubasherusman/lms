package org.mubasherusman.cms.data;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;

@Entity
public class Client extends AbstractEntity {

    private String firstName;
    private String lastName;
    private String fatherName;
    private String nic;
    private String cast;
    private String occupation;
    @Email
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String address;

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
    public String getFatherName() {
        return fatherName;
    }
    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }
    public String getNic() {
        return nic;
    }
    public void setNic(String nic) {
        this.nic = nic;
    }
    public String getCast() {
        return cast;
    }
    public void setCast(String cast) {
        this.cast = cast;
    }
    public String getOccupation() {
        return occupation;
    }
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

}
