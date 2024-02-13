package com.ead.authuser.specifications;

import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import java.util.UUID;

public class SpecificationTemplate {
    @And({
            @Spec(path = "userType", spec = Equal.class), //busca pelo userType, Equal ele vai identificar o valor exato que passei na busca
            @Spec(path = "userStatus", spec = Equal.class),
            @Spec(path = "email", spec = Like.class), //busca por email, utilizando Like
            @Spec(path = "fullName", spec = Like.class)
    })
    public interface UserSpec extends Specification<UserModel>{}

    //quando for passado o courseId passar vai ser chamado este metodo
    public static Specification<UserModel> userCourseId(final UUID courseId){
        return(root, query, cb) -> {
            query.distinct(true); //apenas dados que não se repetem
            Join<UserModel, UserCourseModel> userProd = root.join("usersCourses"); //fazer um join entre duas entidades, passando a relação entre elas usersCourses
            return cb.equal(userProd.get("courseId"), courseId);
        };
    }
}
