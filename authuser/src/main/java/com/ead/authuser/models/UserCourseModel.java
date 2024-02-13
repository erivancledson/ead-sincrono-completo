package com.ead.authuser.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data //classe para relação entre microserviços. API Composition Pattern
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) //oculta todos os atributos que estão com valores nulos
@Entity
@Table(name = "TB_USERS_COURSES")
public class UserCourseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) //varios users_courses model para um unico usuario. Um usuario pode está cadastro em varios cursos
    private UserModel user;
    @Column(nullable = false)
    private UUID courseId;


}
