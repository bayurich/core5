package ru.netology;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.netology.Main.parseCSV;

public class MyTests {

    final static String TEST_PATH = "src/test/resources/";
    final static String TEST_JSON_FILE = "data_test.json";

    @After
    public void aftInit(){
        clear(TEST_PATH + TEST_JSON_FILE);
    }

    private static void clear(String fileName) {
        File file = new File(fileName);
        if (file.exists()){
            file.delete();
        }
    }

    @Test
    public void testClassEmployee(){
        final long expectedId = 1;
        final String expectedFirstName = "John";
        final String expectedLastName = "Smith";
        final String expectedCountry = "USA";
        final int expectedAge = 25;
        final String expectedString = "Employee{id=1, firstName='John', lastName='Smith', country='USA', age=25}";

        Employee employee = new Employee(expectedId,expectedFirstName,expectedLastName,expectedCountry,expectedAge);

        Assertions.assertEquals(expectedId, employee.getId());
        Assertions.assertEquals(expectedFirstName, employee.getFirstName());
        Assertions.assertEquals(expectedLastName, employee.getLastName());
        Assertions.assertEquals(expectedCountry, employee.getCountry());
        Assertions.assertEquals(expectedAge, employee.getAge());
        Assertions.assertEquals(expectedString, employee.toString());
    }

    @Test
    public void testMethodParseCSV(){
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = TEST_PATH + "data_test.csv";
        List<Employee> expectedList = Arrays.asList(
                new Employee(1,"Name1","Surname1","Country1",10),
                new Employee(2,"Name2","Surname2","Country2",20));

        List<Employee> result = parseCSV(columnMapping, fileName);

        Assertions.assertEquals(expectedList.toString(), result.toString());
        Assertions.assertTrue(expectedList.toString().equals(result.toString()));


        String[] columnMappingIncorrect = {"id", "lastName", "firstName", "country", "age"};
        result = parseCSV(columnMappingIncorrect, fileName);
        Assertions.assertFalse(expectedList.toString().equals(result.toString()));
    }

    @Test
    public void testMethodParseCSV_errorMapping_1(){
        String[] columnMappingIncorrect = {"id", "lastName", "firstName", "country", "age"};
        String fileName = TEST_PATH + "data_test.csv";
        List<Employee> expectedList = Arrays.asList(
                new Employee(1,"Name1","Surname1","Country1",10),
                new Employee(2,"Name2","Surname2","Country2",20));

        List<Employee> result = parseCSV(columnMappingIncorrect, fileName);
        Assertions.assertFalse(expectedList.toString().equals(result.toString()));
    }

    @Test
    public void testMethodParseCSV_errorMapping_2(){
        String[] columnMappingIncorrect = {"lastName", "firstName", "country", "age", "id"};
        String fileName = TEST_PATH + "data_test.csv";

        Assertions.assertThrows(RuntimeException.class, () -> {
            List<Employee> result = parseCSV(columnMappingIncorrect, fileName);
                });
    }

    @Test
    public void testMethodJsonToList()
    {
        String inputString = "[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\",\"country\":\"USA\",\"age\":25},{\"id\":2,\"firstName\":\"Inav\",\"lastName\":\"Petrov\",\"country\":\"RU\",\"age\":23}]";
        List<Employee> expectedList = Arrays.asList(
                new Employee(1, "John", "Smith", "USA", 25),
                new Employee(2, "Inav", "Petrov", "RU", 23));

        List<Employee> resultList = Main.jsonToList(inputString);

        Assert.assertThat(resultList, Matchers.hasSize(2));
        Assertions.assertEquals(expectedList.size(), resultList.size());
        Assertions.assertTrue(expectedList.toString().equals(resultList.toString()));
    }

    @Test
    public void testMethodListToJson(){
        List<Employee> inputList = Arrays.asList(
                new Employee(1, "John", "Smith", "USA", 25),
                new Employee(2, "Inav", "Petrov", "RU", 23));
        final String expectedString = "[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\",\"country\":\"USA\",\"age\":25},{\"id\":2,\"firstName\":\"Inav\",\"lastName\":\"Petrov\",\"country\":\"RU\",\"age\":23}]";

        String resultString = Main.listToJson(inputList);

        Assertions.assertEquals(expectedString, resultString);
    }

    @Test
    public void testMethodParseXML(){
        List<Employee> expectedList = Arrays.asList(
                new Employee(1,"Name1","Surname1","Country1",10),
                new Employee(2,"Name2","Surname2","Country2",20));

        String fileName = TEST_PATH + "data_test.xml";
        List<Employee> resultList = Main.parseXML(fileName);

        Assert.assertThat(resultList, Matchers.hasSize(2));
        Assertions.assertEquals(expectedList.size(), resultList.size());
        System.out.println(resultList.toString());
        Assertions.assertTrue(expectedList.toString().equals(resultList.toString()));
    }

    @Test
    public void testMethodWriteString_ReadString(){
        String inputString = "[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\",\"country\":\"USA\",\"age\":25},{\"id\":2,\"firstName\":\"Inav\",\"lastName\":\"Petrov\",\"country\":\"RU\",\"age\":23}]";
        String outputFile = TEST_PATH + TEST_JSON_FILE;

        Main.writeString(inputString, outputFile);

        File file = new File(outputFile);
        Assertions.assertTrue(file.exists());

        String resultString = Main.readString(outputFile);
        Assertions.assertEquals(inputString, resultString);
    }

}
