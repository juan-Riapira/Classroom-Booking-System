package co.edu.uptc.classroom.service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassroomResponse {
    private Long id;
    private String name;
    private int capacity; 
    private String location;
    private String state;
}
