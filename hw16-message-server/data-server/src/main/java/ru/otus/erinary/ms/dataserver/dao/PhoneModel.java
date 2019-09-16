package ru.otus.erinary.ms.dataserver.dao;

import lombok.*;
import ru.otus.erinary.ms.messageserver.model.Phone;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "phones")
public class PhoneModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "number")
    private String number;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    public PhoneModel(Phone phone) {
        this.number = phone.getNumber();
    }

    public PhoneModel(long id, String number) {
        this.id = id;
        this.number = number;
    }

    public Phone toPhone() {
        return new Phone(number);
    }

}
