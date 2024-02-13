package com.ead.course.services.impl;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    CourseUserRepository courseUserRepository;

    @Autowired
    AuthUserClient authUserClient;

    @Transactional //se tudo der errado ele volta ao estado anterior
    @Override //deleção em cascata
    public void delete(CourseModel courseModel) {
        boolean deleteCourseUserInAuthUser = false;
        List<ModuleModel> moduleModelList = moduleRepository.findAllLModulesIntoCourse(courseModel.getCourseId());
        if (!moduleModelList.isEmpty()){
            //se a lista estiver diferente de vazia, vai ser deletado em cascata
            for(ModuleModel module : moduleModelList){
                List<LessonModel> lessonModelList = lessonRepository.findAllLessonsIntoModule(module.getModuleId()); //retorna todas as lições vinculadas a um modulo
                if (!lessonModelList.isEmpty()){
                    lessonRepository.deleteAll(lessonModelList);  //deleta todas as lições do banco de dados
                }
            }
            moduleRepository.deleteAll(moduleModelList);
        }
        //para deletar a relação entre curso e usuario em cascata
        List<CourseUserModel> courseUserModelList = courseUserRepository.findAllCourseUserIntoCourse(courseModel.getCourseId());
        if(!courseUserModelList.isEmpty()){
            courseUserRepository.deleteAll(courseUserModelList);
            deleteCourseUserInAuthUser = true;
        }
        courseRepository.delete(courseModel);
        if(deleteCourseUserInAuthUser){
            //se estiver true eu envio para o endpoin de authuser: deleteUserCourseByCourse para realizar a deleção no relacionamento
            authUserClient.deleteCourseInAuthUser(courseModel.getCourseId());
        }

    }

    @Override
    public CourseModel save(CourseModel courseModel) {
        return courseRepository.save(courseModel);
    }

    @Override
    public Optional<CourseModel> findById(UUID courseId) {
        return courseRepository.findById(courseId);
    }

    @Override
    public Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable) {
        return courseRepository.findAll(spec, pageable);
    }
}
