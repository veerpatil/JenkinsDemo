package main.java;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExtentReport implements IReporter {


    public static String report;
    public List<String > testClassName = new ArrayList<>();
    private ExtentReports extent;
    @Override
    public void generateReport(List<XmlSuite> xmlSuites,
                               List<ISuite> suites,
                               String outputDirectory) {
        init(outputDirectory);
        for ( ISuite suite : suites) {
            Map<String, ISuiteResult> result = suite.getResults();
            for ( ISuiteResult r : result.values() ) {
                ITestContext context = r.getTestContext();
                buildTestNodes(context.getFailedTests(), Status.FAIL);
                buildTestNodes(context.getSkippedTests(), Status.SKIP);
                buildTestNodes(context.getPassedTests(), Status.PASS);
            }
        }
        for (String s : Reporter.getOutput()) {
            extent.setTestRunnerOutput(s);
        }
        extent.flush();
    }
    private void init(String outputDirectory) {
        ExtentSparkReporter htmlReporter =
                new ExtentSparkReporter (outputDirectory + File.separator
                        + "Extent_"+getTimeStamp()+".html");
     //htmlReporter.config().setTheme(Theme.DARK);
        report=  outputDirectory;
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setReportUsesManualConfiguration(true);
        extent.setSystemInfo("Operation System", "Windows 10");
        extent.setSystemInfo("Browser", "Chrome");
        extent.setSystemInfo("Machine Name" , "Home-Alone");
        extent.setSystemInfo("User Name:", "Veerkumar Patil");

    }
    String getTimeStamp() {
        DateFormat format = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        String timeStamp = format.format(new Date());
        return timeStamp;
    }
    private void buildTestNodes(IResultMap tests, Status status) {

        ExtentTest test;
        if ( tests.size() > 0 ) {
            for ( ITestResult result : tests.getAllResults() ) {
                test = extent.createTest(
                        result.getMethod().getMethodName());
                for ( String group : result.getMethod().getGroups() )
                    test.assignCategory(group);
                for (String message : Reporter.getOutput(result)) {
                    test.log(Status.PASS,message);
                }
                if ( result.getThrowable() != null ) {
                    test.log(status, result.getThrowable());
                    try {

                        String finalPath;
                        if(readProperties("env").equalsIgnoreCase("local"))
                        {
                            finalPath=Paths.get("").toAbsolutePath().toString()+ readProperties("localReportPath") + result.getName() + ".png";                        }
                        else
                        {
                            finalPath = readProperties( "remoteReportPath")+result.getName()+".png" ;
                        }

                        test.fail("Screenshot is as below:",  MediaEntityBuilder.createScreenCaptureFromPath(finalPath).build());
                    }
                    catch (Exception ex)
                    {
                        ex.getStackTrace();
                    }

                }

                else {
                    test.log(status,
                            "Test " +
                                    status.toString().toLowerCase() +
                                    "ed");
                }
                test.assignCategory(result.getTestContext().getName());
//                String  className = result.getInstanceName();
//                String []finalClassName =  className.split("[.]") ;
//                testClassName.add(finalClassName[finalClassName.length-1]);
//                test.assignCategory(finalClassName[finalClassName.length-1]);
                test.getModel().setStartTime(getTime(result.getStartMillis()));
                test.getModel().setEndTime(getTime(result.getEndMillis()));

            }
        }
    }
    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }

    public String readProperties(String name) throws IOException {
        FileReader reader=new FileReader("test.properties");
        Properties p=new Properties();
        p.load(reader);
//        System.out.println(p.get("env"));
//        System.out.println(p.get("localReportPath"));
//        System.out.println(p.get("remoteReportPath"));
        p.getProperty(name);
        return    p.getProperty(name);


    }


}
