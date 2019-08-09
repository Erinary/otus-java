package ru.otus.erinary.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "phones")
public class Phone {

    @Column(name = "phone_number")
    private String number;

}
