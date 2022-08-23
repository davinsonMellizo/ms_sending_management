package karate;//package karate;
//
//
//import com.intuit.karate.Results;
//import com.intuit.karate.Runner;
//import karate.nonapi.io.github.classgraph.utils.FileUtils;
//import net.masterthought.cucumber.Configuration;
//import net.masterthought.cucumber.ReportBuilder;
//import org.apache.commons.io.FileUtils;
//import org.junit.jupiter.api.Test;
//
//import java.io.File;
//import java.lang.module.Configuration;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//
//public class ManagementAcceptanceTest {
//
//    @Test
//    void testAll(){
//
//        Results result = Runner.path("classpath:karate")
//                .outputCucumberJson(true)
//                .tags("~@ignore")
//                .parallel(1);
//
//
//        generateReport(result.getReportDir());
//
//    }
//
//    public static <ReportBuilder> void generateReport(String karateOutputPath) {
//        Collection<File> jsonFiles = FileUtils.listFiles(new File(karateOutputPath), new String[]{"json"}, true);
//        List<String> jsonPaths = new ArrayList<>(jsonFiles.size());
//        jsonFiles.forEach(file -> jsonPaths.add(file.getAbsolutePath()));
//        Configuration config = new Configuration(new File("build"), "KarateNico");
//        ReportBuilder reportBuilder = new ReportBuilder(jsonPaths, config);
//        reportBuilder.generateReports();
//    }
//
//
//}
