package ru.netology;

import com.google.gson.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        //task1
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        //printListOfEmployee(list);

        String json = listToJson(list);
        //System.out.println(json);

        writeString(json, "data.json");

        //task2
        list = parseXML("data.xml");
        //printListOfEmployee(list);

        json = listToJson(list);
        //System.out.println(json);

        writeString(json, "data2.json");

        //task3
        json = readString("data.json");
        //System.out.println(json);

        list = jsonToList(json);
        printListOfEmployee(list);
    }

    private static List<Employee> jsonToList(String json) {
        List<Employee> employees = new ArrayList<>();

        Gson gson = new GsonBuilder().create();
        JSONParser jsonParser = new JSONParser();
        try {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(json);
            for (Object object : jsonArray){
                employees.add(gson.fromJson(object.toString(), Employee.class));
            }
        } catch (ParseException e) {
            System.out.println("Error while jsonToList: " + e.getMessage());
        }

        return employees;
    }

    private static String readString(String fileName) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))){
            String s;
            while ((s = br.readLine()) != null){
                sb.append(s);
            }
        }
        catch (Exception e){
            System.out.println("Error while readFile: " + fileName + ": " + e.getMessage());
        }

        return sb.toString();
    }

    private static List<Employee> parseXML(String fileName) {
        List<Employee> employees = new ArrayList<>();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(fileName);

            Node rootNode = document.getDocumentElement();
            NodeList nodeList = rootNode.getChildNodes();
            if (nodeList != null){
                for (int i = 0; i < nodeList.getLength(); i++){
                    Node node = nodeList.item(i);
                    if (Node.ELEMENT_NODE == node.getNodeType()) {
                        Element element = (Element) node;

                        long id = Long.parseLong(element.getElementsByTagName("id").item(0).getTextContent());
                        String firstName = element.getElementsByTagName("firstName").item(0).getTextContent();
                        String lastName = element.getElementsByTagName("firstName").item(0).getTextContent();
                        String country = element.getElementsByTagName("firstName").item(0).getTextContent();
                        int age = Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent());

                        employees.add(new Employee(id, firstName, lastName, country, age));
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error while parseXML: " + e.getMessage());
        }

        return employees;
    }

    private static void writeString(String json, String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName)){
            fileWriter.write(json);
            fileWriter.flush();
            System.out.println("Успешно записано в " + fileName);

        } catch (IOException e) {
            System.out.println("Error while writeToFile: " + fileName + ": " + e.getMessage());
        }
    }

    private static String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        Gson gson = new GsonBuilder().create();

        return gson.toJson(list, listType);
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> employees = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))){

            ColumnPositionMappingStrategy columnPositionMappingStrategy = new ColumnPositionMappingStrategy();
            columnPositionMappingStrategy.setColumnMapping(columnMapping);
            columnPositionMappingStrategy.setType(Employee.class);

            CsvToBeanBuilder csvToBeanBuilder = new CsvToBeanBuilder(csvReader)
                    .withMappingStrategy(columnPositionMappingStrategy);
            CsvToBean csvToBean = csvToBeanBuilder.build();

            employees = csvToBean.parse();
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error while parseCSV: " + e.getMessage());
        }

        return employees;
    }

    private static void printListOfEmployee(List<Employee> list) {
        for(Employee employee : list){
            System.out.println(employee.toString());
        }
    }
}
