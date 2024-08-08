package ir.jibit.directdebit.gateway.balejbbot.controller;

import ir.jibit.directdebit.gateway.balejbbot.data.AdminRepository;
import ir.jibit.directdebit.gateway.balejbbot.exceptions.BotException;
import ir.jibit.directdebit.gateway.balejbbot.service.CommonApplicationService;
import ir.jibit.directdebit.gateway.balejbbot.service.models.Award;
import ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Admin;
import ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Role;
import ir.jibit.directdebit.gateway.balejbbot.service.models.students.Student;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.List;

import static ir.jibit.directdebit.gateway.balejbbot.exceptions.Error.UNRECOGNIZED_USER;

@Controller
public class CommonController {
    @Value("${bot.bale.admins.token}")
    String adminsBotToken;

    @Value("${bot.bale.admins.host}")
    String adminsBotHost;

    @Value("${bot.bale.admins.port}")
    int adminsBotPort;

    private final CommonApplicationService commonApplicationService;
    private final AdminRepository adminRepository;

    public CommonController(CommonApplicationService commonApplicationService, AdminRepository adminRepository) {
        this.commonApplicationService = commonApplicationService;
        this.adminRepository = adminRepository;
    }

    public String login(String chatId, String username, String password, boolean isStudent) {
        var name = commonApplicationService.login(chatId, username, password, isStudent);
        return name + " " + "عزیز خوش آمدید ✅";
    }

    public String getAwards(String chatId, boolean isStudent) {
        var awardsString = new StringBuilder();
        commonApplicationService.getAwards(chatId, isStudent).forEach(award -> {
            awardsString.append(award.toString()).append("\n");
        });

        return awardsString.toString();
    }

    public String insertStudents(String chatId, String filePath) throws IOException {
        var admin = adminRepository.findAdminByChatId(chatId);
        if (admin != null) {
            var tempFilePath = downloadFile(filePath);
            File tempFile = tempFilePath.toFile();
            return commonApplicationService.insertStudents(parseExcelFile(tempFile, 0), admin.getRole());
        } else {
            throw new BotException(UNRECOGNIZED_USER);
        }
    }

    public String insertAdmins(String chatId, String filePath) throws IOException {
        var admin = adminRepository.findAdminByChatId(chatId);
        if (admin != null) {
            var tempFilePath = downloadFile(filePath);
            File tempFile = tempFilePath.toFile();
            return commonApplicationService.insertAdmins(parseExcelFile(tempFile, 1), admin.getRole());
        } else {
            throw new BotException(UNRECOGNIZED_USER);
        }
    }

    public String insertAwards(String chatId, String filePath) throws IOException {
        var admin = adminRepository.findAdminByChatId(chatId);
        if (admin != null) {
            var tempFilePath = downloadFile(filePath);
            File tempFile = tempFilePath.toFile();
            return commonApplicationService.insertAwards(parseExcelFile(tempFile, 2), admin.getRole());
        } else {
            throw new BotException(UNRECOGNIZED_USER);
        }
    }

    private Path downloadFile(String filePath) throws IOException {
        Path tempFile = Files.createTempFile("downloaded", ".xlsx");
        try (InputStream in = new URL("https://" + adminsBotHost + "/file/" + adminsBotToken + "/" + filePath).openStream()) {
            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }
        return tempFile;
    }

    private List parseExcelFile(File file, int type) {
        try (InputStream excelFile = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(excelFile)) {

            Sheet sheet = workbook.getSheetAt(0);
            if (type == 0) {
                return getStudents(sheet);
            } else if (type == 1) {
                return getAdmins(sheet);
            } else {
                return getAwards(sheet);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private List getStudents(Sheet sheet) {
        List<Student> students = new LinkedList<>();
        int counter = 0;

        for (Row row : sheet) {
            counter++;
            if (counter == sheet.getPhysicalNumberOfRows() + 1) break;
            if (counter == 1) continue;
            if (row.getCell(0) == null) continue;

            var id = row.getCell(0) != null ? row.getCell(0).getNumericCellValue() : 0;
            var username = row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "";
            var password = row.getCell(2) != null ? row.getCell(2).getStringCellValue() : "";
            var fName = row.getCell(3) != null ? row.getCell(3).getStringCellValue() : "";
            var lName = row.getCell(4) != null ? row.getCell(4).getStringCellValue() : "";
            var nationalCode = row.getCell(5) != null ? row.getCell(5).getStringCellValue() : "";
            var birthDate = row.getCell(6) != null ? row.getCell(6).getStringCellValue() : "";
            var phoneNumber = row.getCell(7) != null ? row.getCell(7).getStringCellValue() : "";
            var fathersPhoneNumber = row.getCell(8) != null ? row.getCell(8).getStringCellValue() : "";
            var mothersPhoneNumber = row.getCell(9) != null ? row.getCell(9).getStringCellValue() : "";
            var teachersId = row.getCell(10) != null ? row.getCell(10).getNumericCellValue() : 0;

            students.add(new Student(String.valueOf(Double.valueOf(id).longValue()), username, password, fName, lName, nationalCode, birthDate,
                    phoneNumber, fathersPhoneNumber, mothersPhoneNumber, String.valueOf((int) teachersId)));
        }
        return students;
    }

    private List getAdmins(Sheet sheet) {
        List<Admin> admins = new LinkedList<>();
        int counter = 0;
        for (Row row : sheet) {
            counter++;
            if (counter == sheet.getPhysicalNumberOfRows() + 1) break;
            if (counter == 1) continue;
            if (row.getCell(0) == null) continue;

            var id = row.getCell(0) != null ? row.getCell(0).getNumericCellValue() : 0;
            var username = row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "";
            var password = row.getCell(2) != null ? row.getCell(2).getStringCellValue() : "";
            var fName = row.getCell(3) != null ? row.getCell(3).getStringCellValue() : "";
            var lName = row.getCell(4) != null ? row.getCell(4).getStringCellValue() : "";
            var nationalCode = row.getCell(5) != null ? row.getCell(5).getStringCellValue() : "";
            var birthDate = row.getCell(6) != null ? row.getCell(6).getStringCellValue() : "";
            var phoneNumber = row.getCell(7) != null ? row.getCell(7).getStringCellValue() : "";
            var role = row.getCell(8) != null ? row.getCell(8).getStringCellValue() : "";
            var groupNumber = row.getCell(9) != null ? row.getCell(9).getNumericCellValue() : 0;

            admins.add(new Admin(String.valueOf(Double.valueOf(id).longValue()), username, password, fName, lName, nationalCode,
                    birthDate, phoneNumber, Role.valueOf(role.toUpperCase()), (int) groupNumber));
        }
        return admins;
    }

    private List getAwards(Sheet sheet) {
        List<Award> awards = new LinkedList<>();
        int counter = 0;
        for (Row row : sheet) {
            counter++;
            if (counter == sheet.getPhysicalNumberOfRows() + 1) break;
            if (counter == 1) continue;
            if (row.getCell(0) == null || row.getCell(0).getStringCellValue().isBlank()) continue;

            var name = row.getCell(0) != null ? row.getCell(0).getStringCellValue() : "";
            var code = row.getCell(1) != null ? row.getCell(1).getNumericCellValue() : 0;
            var description = row.getCell(2) != null ? row.getCell(2).getStringCellValue() : "";
            var requiredScore = row.getCell(3) != null ? row.getCell(3).getNumericCellValue() : 0;

            awards.add(new Award(name, (int) code, description, (int) requiredScore));
        }
        return awards;
    }
}
