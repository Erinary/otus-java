package ru.otus.erinary.model;

import com.google.gson.annotations.Expose;
import lombok.*;

import javax.persistence.*;

@SuppressWarnings("JpaDataSourceORMInspection")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "phones")
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Expose
    private long id;

    @Column(name = "number")
    @Expose
    private String number;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Phone(String number) {
        this.number = number;
    }

    public Phone(long id, String number) {
        this.id = id;
        this.number = number;
    }
}
