package com.ead.authuser.models;

import com.ead.authuser.enums.USerStatus;
import com.ead.authuser.enums.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) //oculta todos os atributos que estão com valores nulos
@Entity
@Table(name = "TB_USERS")
public class UserModel extends RepresentationModel<UserModel> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId;
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    @Column(nullable = false, unique = true, length = 50)
    private String email;
    @Column(nullable = false, length = 255)
    @JsonIgnore //não exibe no json da api
    private String password;
    @Column(nullable = false, length = 255)
    private String fullName;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING) //salva como string no banco de dados
    private USerStatus userStatus;
    @Enumerated(EnumType.STRING)
    private UserType userType;
    @Column(length = 20)
    private String phoneNumber;
    @Column(length = 20)
    private String cpf;
    @Column
    private String imageUrl;
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'") //retorna a data no formato desejado
    private LocalDateTime creationDate;
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'") //retorna a data no formato desejado
    private LocalDateTime lastUpdateDate;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //um uduario tem vario courseModel
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY) //chave estrangeira do outro lado mappedBy
    private Set<UserCourseModel> usersCourses;

    public UserCourseModel convertToUserCourseModel(UUID courseId){
        return new UserCourseModel(null, this, courseId );
    }
}
