package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.sun.jdi.Type;
import org.json.simple.JSONArray;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //создайдии массив строчек columnMapping, содержащий информацию о предназначении колонок в CVS файле
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};

        //определим имя для считываемого CSV файла
        String fileName = "data.csv";

        //получим список сотрудников, вызвав метод parseCSV()
        List<Employee> list = parseCSV(columnMapping, fileName);

        //преобразуем полученный список  в строчку в формате JSON
        String json = listToJson(list);
        System.out.println(json);

        //создадим переменную с именем json-файла
        String json_out= "data.json";
        //Запишем строку в файл:
        writeString(json,json_out);

    }

    //метод записи json-строки в файл
    public static void writeString(String json_string, String json_file){

        //создадим объект типа FileWriter в блоке try с ресурсами,
        // передав ему имя файла в параметр конструктора
        try (FileWriter file = new
                FileWriter(json_file)) {
            //вызовем метод write, передав в него json-строку
            file.write(json_string);            //
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String listToJson(List<Employee> list) {
        //Создаём экземпляр класса GsonBuilder и с помощью билдера создаём объект gson
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        //вызываем у объекта gson метод для формирования строки из списка сотрудников
        return gson.toJson(list);
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        //создадим пустой список сотрудников:
        List<Employee> staff = new ArrayList<>();
        //создадим экземпляр класса CSVReader, передав в его конструктор файловый ридер
        // FileReader файла fileName, заключив его в блок try с ресурсами
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            //Создадим объект класса ColumnPositionMappingStrategy для класса Employee
            ColumnPositionMappingStrategy<Employee> strategy =
                    new ColumnPositionMappingStrategy<>();
            //установим тип
            strategy.setType(Employee.class);
            //передадим стратегию маппинга
            strategy.setColumnMapping(columnMapping);
            //Создадим экземпляр CsvToBean с использованием билдера CsvToBeanBuilder, передав в него
            //стратегию
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            //запишем сотрудников в ранее созданный список:
            staff = csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //вернём считанный из CSV-файла список сотрудников
        return staff;
    }
}