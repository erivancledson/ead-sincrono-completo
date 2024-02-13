package com.ead.course.models;


import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_COURSES")
public class CourseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID courseId;
    @Column(nullable = false, length = 150)
    private String name;
    @Column(nullable = false, length = 250)
    private String description;
    @Column
    private String imageUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'") // Z é o UTC
    @Column(nullable = false)
    private LocalDateTime creationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime lastUpdateDate;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseStatus courseStatus;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseLevel courseLevel;
    @Column(nullable = false)
    private UUID userInstructor;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //somente escrita, não mostra no get
    //cascade = CascadeType.ALL, orphanRemoval = true remove em cascata e remove algum module que não tem vinculo com nenhum curso
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY ) //course vai ser a chave estrangeira
    @Fetch(FetchMode.SUBSELECT) //join as consulta vira eager, subselect e select ele vai utilizar o fetch lazy ou eager da forma que foi definida inicialmente
    @OnDelete(action = OnDeleteAction.CASCADE) //o banco de dados vai deletar os dados em cascata
    private Set<ModuleModel> modules; //set não é ordenado e não permite duplicada

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY) // 1 couse model para varios coursesUsers
    private Set<CourseUserModel> coursesUsers;

    public CourseUserModel convertCourseUserModel(UUID userID){
        return new CourseUserModel(null, userID, this); //id, a propria classe,  usuario
    }

    //eager ancioso carrega todos os dados
    //lazy carrega os dados somente se for necessario
}
