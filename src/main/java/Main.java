import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
//            ColumnPositionMappingStrategy<Employee> clmn = new ColumnPositionMappingStrategy();
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
        Type listType = new TypeToken<List<T>>() { }.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(list, listType);
    }

    private static void writeString(String json, int nmbr) {

        try (FileWriter file = new FileWriter("data1"+Integer.toString(nmbr)+".json")) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

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

        writeString(json,1);



                     // *******
                     // 2ое задание   XML в JSON

        List<Employee> list2Empl = parseXML("data.xml");

                  //для наглядности...
        for (Employee empl : list2Empl) {
            empl.firstName = empl.firstName.replaceAll("XML", "JSON2");
        }

        String json2 = listToJson(list2Empl);
        writeString(json2,2);

    }

    private static List<Employee> parseXML(String s) throws ParserConfigurationException, IOException, SAXException {

        List<Employee> list1 = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

//        Document doc = builder.parse(new File("company.xml"));
        Document doc = builder.parse(new File("data.xml"));

        Node root = doc.getDocumentElement(); //получили корневой узел
        System.out.println("\n" + "Корневой элемент: " + root.getNodeName() + "\n");

        NodeList nodeList = root.getChildNodes();                                // получ список узлов из корневого

        for (int i = 0; i < nodeList.getLength(); i++) {                         // прошли по спсику узлов
            Node node1 = nodeList.item(i);                                       // узел

            if (Node.ELEMENT_NODE == node1.getNodeType()) {                      // и внтури в конце рекурсия
                System.out.println("Текущий узел: " + node1.getNodeName());      // ok

                NodeList nodeList2 = node1.getChildNodes();

                String[] mapEmp = new String[5];
                int cnt =0;

                for (int j = 0; j < nodeList2.getLength(); j++) {                                // прошли по спсику узлов
                    Node node2 = nodeList2.item(j);

                    if (Node.ELEMENT_NODE == node2.getNodeType()) {                             // и внтури в конце рекурсия
//                        System.out.println("Текущий узел21: " + node2.getNodeName());         // ok
//                        System.out.println("Текущий узел22: " + node2.getTextContent());      // ok
                        Element element = (Element) node2;                                      // элемент
                        String map = element.getTextContent() ;
//                        System.out.println(map);
                        mapEmp[cnt++]=map;
                    }
                }
//                System.out.println(Arrays.toString(mapEmp));
                list1.add(new Employee(Integer.parseInt(mapEmp[0]), mapEmp[1], mapEmp[2], mapEmp[3], Integer.parseInt(mapEmp[4])));
//                System.out.println("ls "+list1.size());
            }
        }
        return list1;
    }
}

