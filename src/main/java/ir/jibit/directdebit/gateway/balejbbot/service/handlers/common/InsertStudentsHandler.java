package ir.jibit.directdebit.gateway.balejbbot.service.handlers.common;

import ir.jibit.directdebit.gateway.balejbbot.data.StudentRepository;
import ir.jibit.directdebit.gateway.balejbbot.service.handlers.AdminConsumerHandler;
import ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Role;
import ir.jibit.directdebit.gateway.balejbbot.service.models.students.Student;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;

import static ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Permission.INSERT_STUDENTS;

public class InsertStudentsHandler implements AdminConsumerHandler<List<Student>> {
    private final StudentRepository studentRepository;

    public InsertStudentsHandler(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void accept(List<Student> students) {
        students.forEach(student -> {
            var result = studentRepository.findById(Long.valueOf(student.getId()));
            if (result.isEmpty()) {
                var studentBuilder = ir.jibit.directdebit.gateway.balejbbot.data.entities.Student.builder()
                        .id(student.getId())
                        .teacherId(student.getTeacher())
                        .username(student.getUsername())
                        .password(DigestUtils.md5Hex(student.getPassword()))
                        .firstName(student.getFirstName())
                        .lastName(student.getLastName())
                        .nationalCode(student.getNationalCode())
                        .birthDate(student.getBirthDate().toString())
                        .phoneNumber(student.getPhoneNumber())
                        .fathersPhoneNumber(student.getFathersPhoneNumber())
                        .mothersPhoneNumber(student.getMothersPhoneNumber());
                studentRepository.save(studentBuilder.build());
            } else {
                var currentStudent = result.get();
                currentStudent.setFirstName(student.getFirstName());
                currentStudent.setLastName(student.getFirstName());
                currentStudent.setNationalCode(student.getNationalCode());
                currentStudent.setBirthDate(student.getBirthDate().toString());
                currentStudent.setPhoneNumber(student.getPhoneNumber());
                currentStudent.setFathersPhoneNumber(student.getFathersPhoneNumber());
                currentStudent.setMothersPhoneNumber(student.getMothersPhoneNumber());
                studentRepository.save(currentStudent);
            }
        });
    }

    @Override
    public boolean isAllowed(Role role) {
        return role.getPermissions().stream().anyMatch(INSERT_STUDENTS::equals);
    }
}
