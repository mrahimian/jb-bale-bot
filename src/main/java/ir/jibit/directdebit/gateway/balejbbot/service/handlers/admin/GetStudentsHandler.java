package ir.jibit.directdebit.gateway.balejbbot.service.handlers.admin;

import ir.jibit.directdebit.gateway.balejbbot.data.AdminRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.StudentRepository;
import ir.jibit.directdebit.gateway.balejbbot.service.handlers.AdminSupplierHandler;
import ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Role;
import ir.jibit.directdebit.gateway.balejbbot.service.models.students.Student;
import org.springframework.stereotype.Service;

import java.util.List;

import static ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Permission.FETCH_STUDENTS_LIST;

@Service
public class GetStudentsHandler implements AdminSupplierHandler<List<Student>> {
    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;

    public GetStudentsHandler(StudentRepository studentRepository, AdminRepository adminRepository) {
        this.studentRepository = studentRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public List<Student> get() {
        return studentRepository.findAll().stream().map(student -> {
            var teacher = adminRepository.findById(Long.valueOf(student.getTeacherId()));
            return new Student(student.getId(),
                    student.getUsername(), null, student.getFirstName(), student.getLastName(), student.getNationalCode(),
                    student.getBirthDate(), student.getPhoneNumber(), student.getFathersPhoneNumber(), student.getMothersPhoneNumber(),
                    !teacher.isEmpty() ? teacher.get().getLastName() : "", student.getScore());
        }).toList();
    }

    @Override
    public boolean isAllowed(Role role) {
        return role.getPermissions().stream().anyMatch(FETCH_STUDENTS_LIST::equals);
    }
}
