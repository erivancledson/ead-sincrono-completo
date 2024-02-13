package com.ead.course.services.impl;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.services.CourseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CourseUserServiceImpl implements CourseUserService {

    @Autowired
    CourseUserRepository courseUserRepository;

    @Autowired
    AuthUserClient authUserClient;

    @Override
    public boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId) {
        return courseUserRepository.existsByCourseAndUserId(courseModel, userId);
    }

    @Override
    public CourseUserModel save(CourseUserModel courseUserModel) {
        return courseUserRepository.save(courseUserModel);
    }
    @Transactional//se algo de errado ele cancela a transacao
    @Override
    public CourseUserModel saveAndSendSubscruptionUserInCourse(CourseUserModel courseUserModel) {
        //salva e envia para a api de usuário salvar através do outro endpoint em sua base de dados
        courseUserModel = courseUserRepository.save(courseUserModel); //salvo os dados

        authUserClient.postSubscriptionUserInCourse(courseUserModel.getCourse().getCourseId(), courseUserModel.getUserId()); //envia

        return courseUserModel;
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return courseUserRepository.existsById(userId);
    }

    @Transactional
    @Override
    public void deleteCourseUserByUser(UUID userId) {
        courseUserRepository.deleteAllByUserId(userId);
    }
}
