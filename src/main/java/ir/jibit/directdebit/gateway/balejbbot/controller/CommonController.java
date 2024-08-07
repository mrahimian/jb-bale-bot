package ir.jibit.directdebit.gateway.balejbbot.controller;

import ir.jibit.directdebit.gateway.balejbbot.service.CommonApplicationService;
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

@Controller
public class CommonController {
    @Value("${bot.bale.admins.token}")
    String adminsBotToken;

    @Value("${bot.bale.admins.host}")
    String adminsBotHost;

    @Value("${bot.bale.admins.port}")
    int adminsBotPort;

    private final CommonApplicationService commonApplicationService;

    public CommonController(CommonApplicationService commonApplicationService) {
        this.commonApplicationService = commonApplicationService;
    }

    public String login(String chatId, String username, String password, boolean isStudent) {
        var name = commonApplicationService.login(chatId, username, password, isStudent);
        return name + " " + "اطلاعات شما با موفقیت ثبت شد ✅";
    }

    public String insertStudents(String chatId, String filePath) throws IOException {
        var tempFilePath = downloadFile(filePath);
        File tempFile = tempFilePath.toFile();
        return commonApplicationService.insertStudents(parseExcelFile(tempFile));
    }

    private Path downloadFile(String filePath) throws IOException {
        Path tempFile = Files.createTempFile("downloaded", ".xlsx");
        try (InputStream in = new URL("https://" + adminsBotHost + "/file/" + adminsBotToken + "/" + filePath).openStream()) {
            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }
        return tempFile;
    }

    private List<Student> parseExcelFile(File file) {
        List<Student> students = new LinkedList<>();
        try (InputStream excelFile = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(excelFile)) {

            Sheet sheet = workbook.getSheetAt(0);
            int counter = 1;
            for (Row row : sheet) {
                if (counter == sheet.getPhysicalNumberOfRows() - 1) break;
                if (row.getCell(0).getCellType().equals(CellType.STRING)) continue;

                var id = row.getCell(0).getNumericCellValue();
                var username = row.getCell(1).getStringCellValue();
                var password = row.getCell(2).getStringCellValue();
                var fName = row.getCell(3).getStringCellValue();
                var lName = row.getCell(4).getStringCellValue();
                var nationalCode = row.getCell(5).getStringCellValue();
                var birthDate = row.getCell(6).getStringCellValue();
                var phoneNumber = row.getCell(7).getStringCellValue();
                var fathersPhoneNumber = row.getCell(8).getStringCellValue();
                var mothersPhoneNumber = row.getCell(9).getStringCellValue();
                var teachersId = row.getCell(10).getNumericCellValue();

                students.add(new Student(String.valueOf(Double.valueOf(id).longValue()), username, password, fName, lName, nationalCode, birthDate,
                        phoneNumber, fathersPhoneNumber, mothersPhoneNumber, String.valueOf(teachersId)));
                counter++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return students;
    }
}
