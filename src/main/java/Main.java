import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> list = null;

        try {
            CSVReader empl = new CSVReader(new FileReader(fileName));

            ColumnPositionMappingStrategy<Employee> clmn = new ColumnPositionMappingStrategy<Employee>();
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
        Type listType = new TypeToken<List<T>>() {
        }.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(list, listType);
    }


    private static void writeString(String json, int nmbr) throws ParseException {

        try (FileWriter file = new FileWriter("data1" + Integer.toString(nmbr) + ".json")) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static List<Employee> parseXML(String s) throws ParserConfigurationException, IOException, SAXException {

        List<Employee> list1 = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File("data.xml"));
        Node root = doc.getDocumentElement(); //получили корневой узел
        NodeList nodeList = root.getChildNodes();                                // получ список узлов из корневого

        for (int i = 0; i < nodeList.getLength(); i++) {                         // прошли по спсику узлов
            Node node1 = nodeList.item(i);                                       // узел

            if (Node.ELEMENT_NODE == node1.getNodeType()) {                      // и внтури в конце рекурсия

                NodeList nodeList2 = node1.getChildNodes();

                String[] mapEmp = new String[5];
                int cnt = 0;

                for (int j = 0; j < nodeList2.getLength(); j++) {                                // прошли по спсику узлов
                    Node node2 = nodeList2.item(j);

                    if (Node.ELEMENT_NODE == node2.getNodeType()) {                             // и внтури в конце рекурсия
                        Element element = (Element) node2;                                      // элемент
                        String map = element.getTextContent();
                        mapEmp[cnt++] = map;
                    }
                }

                list1.add(new Employee(Integer.parseInt(mapEmp[0]), mapEmp[1], mapEmp[2], mapEmp[3], Integer.parseInt(mapEmp[4])));
            }
        }
        return list1;
    }


    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, ParseException {

        // *******
        // 1ое задание   CSV в JSON
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);

        //для наглядности...
        for (Employee empl : list) {
            empl.firstName = empl.firstName.replaceAll("CSV", "JSON");
        }
        String json = listToJson(list);

        writeString(json, 1);


        // *******
        // 2ое задание   XML в JSON

        List<Employee> list2Empl = parseXML("data.xml");
        //для наглядности...
        for (Employee empl : list2Empl) {
            empl.firstName = empl.firstName.replaceAll("XML", "JSON2");
        }

        String json2 = listToJson(list2Empl);
        writeString(json2, 2);


        // *******
        // 3ое задание  JSON парсер
        String json3 = readString("data12.json");
        List<Employee> list4 = jsonToList(json3);

    }

    private static List<Employee> jsonToList(String json) {
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        List<Employee> list1 = gson.fromJson(json, listType);
        System.out.println(list1);

        return list1;
    }

    private static String readString(String s) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(s));
        StringBuilder stringB1 = new StringBuilder();
        String str2;
        while ((str2 = reader.readLine()) != null) {
            stringB1.append(str2);
        }
        return stringB1.toString();
    }
}

