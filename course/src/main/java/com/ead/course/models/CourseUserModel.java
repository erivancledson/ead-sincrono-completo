package com.ead.course.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor //tras todos os construtores com parametros
@NoArgsConstructor //vai ter lugares que não vai precisar de construtor com parametros
@JsonInclude(JsonInclude.Include.NON_NULL) //oculta todos os atributos que estão com valores nulos
@Entity
@Table(name = "TB_COURSES_USERS")
public class CourseUserModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) //varios users_courses model para um unico curso
    private CourseModel course;

}
