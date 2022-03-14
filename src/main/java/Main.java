import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class Main {

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> list = null;

        try {
            CSVReader empl = new CSVReader(new FileReader(fileName));

            ColumnPositionMappingStrategy<Employee> clmn = new ColumnPositionMappingStrategy();
            clmn.setType(Employee.class);
            clmn.setColumnMapping(columnMapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(empl).withMappingStrategy(clmn).build();

            list = csv.parse();

            empl.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static <T> String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<T>>(){}.getType();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
//        System.out.println(gson.toJson(list.get(0)));

        return gson.toJson(list, listType);
    }


    private static void writeString(String json) {

        try (FileWriter file = new FileWriter("data.json")) {
//            file.write(obj.toJSONString());
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void main(String[] args) {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

        List<Employee> list = parseCSV(columnMapping, fileName);
//       System.out.println(list.get(0).firstName);

        String json = listToJson(list);   // типо туСтринга в строку для созадния CSV
//       System.out.println(json);

        writeString(json);
    }
}
