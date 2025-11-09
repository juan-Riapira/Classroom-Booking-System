package co.edu.uptc.classroom.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassroomRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String code;

    @Positive
    private int capacity;

    private String location;

    private String state;
}
