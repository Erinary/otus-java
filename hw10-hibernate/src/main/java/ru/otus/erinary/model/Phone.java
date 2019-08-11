package ru.otus.erinary.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "phones")
public class Phone {

    @Id
    private long id;

    @Column(name = "phone_number")
    private String number;

//    @ManyToOne
//    private User user;

}
