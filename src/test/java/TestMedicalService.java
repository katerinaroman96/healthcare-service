import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TestMedicalService {
    PatientInfoRepository repository = Mockito.mock(PatientInfoRepository.class);
    SendAlertService service = Mockito.mock(SendAlertService.class);
    MedicalService medicalService;

    @BeforeAll
    public static void startedAll() {
        System.out.println("Tests started");
    }

    @AfterAll
    public static void finishedAll() {
        System.out.println("Tests completed");
    }

    @BeforeEach
    public void started() {
        System.out.println("test started");
        medicalService = new MedicalServiceImpl(repository, service);
    }

    @AfterEach
    public void finished() {
        System.out.println("test completed");
    }

    @Test
    public void checkBloodPressure() {
        Mockito.when(repository.getById("id1")).thenReturn(new PatientInfo("Иван", "Петров",
                LocalDate.of(1980, 11, 26),
                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));

        medicalService.checkBloodPressure("id1", new BloodPressure(12, 13));
        Mockito.verify(service, Mockito.times(1)).send("Warning, patient id1 need help");
    }

    @Test
    public void checkTemperature() {
        Mockito.when(repository.getById("id1")).thenReturn(new PatientInfo("Иван", "Петров",
                LocalDate.of(1980, 11, 26),
                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));

        medicalService.checkTemperature("id1", new BigDecimal("35.1"));
        Mockito.verify(service, Mockito.times(1)).send("Warning, patient id1 need help");
    }

    @Test
    public void checkEmptyMessage() {
        Mockito.when(repository.getById("id1")).thenReturn(new PatientInfo("Иван", "Петров",
                LocalDate.of(1980, 11, 26),
                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));

        medicalService.checkBloodPressure("id1", new BloodPressure(120, 80));
        Mockito.verify(service, Mockito.times(0)).send("Warning, patient id1 need help");
    }

}
