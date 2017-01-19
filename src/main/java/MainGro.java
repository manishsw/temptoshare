import org.jsontocsv.parser.JSONFlattener;
import org.jsontocsv.writer.CSVWriter;

import java.io.File;
import java.util.*;

/**
 * Created by user on 18/01/17.
 */
public class MainGro {

    public static void main(String[] args) throws Exception {
        File file = new File("files/gro/");


        System.out.println("Loaded files: " + file.getName());
        if (file.isDirectory()) {
            File[] listOfFiles = file.listFiles();

            Set<String> systemNumberList  = new LinkedHashSet<String>();

            Set<String> stringList = new LinkedHashSet<String>();

            Set<String>  totalHeaders = new LinkedHashSet<String>();

            for(File tempFileObj :listOfFiles ) {
                if (tempFileObj.getName().contains("json")) {
                    List<Map<String, String>> flatJson =
                            JSONFlattener.parseJson(tempFileObj, "UTF-8");

                    Set<String> setHeaders = CSVWriter.getCSVHeaders(flatJson, ";");
                    totalHeaders.addAll(setHeaders);
                }
            }
            System.out.println(totalHeaders);
            boolean isAdded = false;
           for(File tempFileObj :listOfFiles ) {
                if (tempFileObj.getName().contains("json")) {
                    List<Map<String, String>> flatJson =
                            JSONFlattener.parseJson(tempFileObj, "UTF-8");
                    boolean isExist = isRecordExists (flatJson, systemNumberList);
                    if(!isExist) {
                        String csvString = CSVWriter.getCSV(flatJson, ";", totalHeaders, isAdded);
                        isAdded = true;

                        stringList.add(csvString);
                    }
                }
            }


            String filePath = "files/out/gro-all-data.csv";
            File toWriteFile  = new File(filePath);
            if(toWriteFile.exists()){
                toWriteFile.delete();
            }
            toWriteFile.createNewFile();
            for(String string : stringList) {
                System.out.println("Writing: " + string);
                CSVWriter.writeToFile(string, filePath);
            }



        }

    }

    private static boolean isRecordExists(List<Map<String, String>> flatJson, Set<String> systemNumberList) {
        boolean isAlreadyAdded = false;

        for (Map<String, String> map : flatJson) {
           String value = map.get("systemNumber");

            if(systemNumberList.contains(value)){
                System.out.println("is already Added "+ value);
                isAlreadyAdded = true;

                break;
            }else {
                systemNumberList.add(value);
            }
        }

       return isAlreadyAdded;
    }
}
