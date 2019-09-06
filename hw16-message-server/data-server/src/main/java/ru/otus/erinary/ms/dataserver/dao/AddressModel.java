package ru.otus.erinary.ms.dataserver.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.erinary.ms.messageserver.model.Address;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "addresses")
public class AddressModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "street")
    private String street;

    public AddressModel(String street) {
        this.street = street;
    }

    public AddressModel(Address address) {
        this.street = address.getStreet();
    }

    public Address toAddress() {
        return new Address(street);
    }

}
