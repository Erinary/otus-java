package ru.otus.erinary.ms.dataserver.dao;

import lombok.*;

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

    public PhoneModel(String number) {
        this.number = number;
    }

    public PhoneModel(long id, String number) {
        this.id = id;
        this.number = number;
    }

}
