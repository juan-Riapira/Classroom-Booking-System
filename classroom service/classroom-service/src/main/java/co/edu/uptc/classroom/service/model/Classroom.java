package co.edu.uptc.classroom.service.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "classrooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String state;
    private int capacity;
    private String location;

}
