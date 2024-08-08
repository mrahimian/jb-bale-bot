package ir.jibit.directdebit.gateway.balejbbot.service.handlers.common;

import ir.jibit.directdebit.gateway.balejbbot.data.AdminRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.StudentRepository;
import ir.jibit.directdebit.gateway.balejbbot.service.handlers.AdminConsumerHandler;
import ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Admin;
import ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Role;
import ir.jibit.directdebit.gateway.balejbbot.service.models.students.Student;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;

import static ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Permission.INSERT_ADMINS;
import static ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Permission.INSERT_STUDENTS;

public class InsertAdminHandler implements AdminConsumerHandler<List<Admin>> {
    private final AdminRepository adminRepository;

    public InsertAdminHandler(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public void accept(List<Admin> admins) {
        admins.stream().parallel().forEach(admin -> {
            var result = adminRepository.findById(Long.valueOf(admin.getId()));
            if (result.isEmpty()) {
                var adminBuilder = ir.jibit.directdebit.gateway.balejbbot.data.entities.Admin.builder()
                        .id(admin.getId())
                        .username(admin.getUsername())
                        .password(DigestUtils.md5Hex(admin.getPassword()))
                        .firstName(admin.getFirstName())
                        .lastName(admin.getLastName())
                        .nationalCode(admin.getNationalCode())
                        .birthDate(admin.getBirthDate())
                        .phoneNumber(admin.getPhoneNumber())
                        .role(admin.getRole())
                        .groupNumber(admin.getGroupNumber());
                adminRepository.save(adminBuilder.build());
            } else {
                var currentAdmin = result.get();
                currentAdmin.setFirstName(admin.getFirstName());
                currentAdmin.setLastName(admin.getLastName());
                currentAdmin.setNationalCode(admin.getNationalCode());
                currentAdmin.setBirthDate(admin.getBirthDate());
                currentAdmin.setPhoneNumber(admin.getPhoneNumber());
                currentAdmin.setRole(admin.getRole());
                currentAdmin.setGroupNumber(admin.getGroupNumber());
                adminRepository.save(currentAdmin);
            }
        });

    }

    @Override
    public boolean isAllowed(Role role) {
        return role.getPermissions().stream().anyMatch(INSERT_ADMINS::equals);
    }
}
