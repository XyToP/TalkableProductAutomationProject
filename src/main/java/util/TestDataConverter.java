package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

@Deprecated
public class TestDataConverter {
    private static final String testDataFolderPath = "src/main/resources/testData/DDT.data/";     //"C:\\SeleniumTests\\SVN folder\\src\\test\\resources\\testData\\dataProvider.txt";
    private StringBuilder out = new StringBuilder();
    private ArrayList<String> stringList = new ArrayList<>();
    private ArrayList<ArrayList> rowList = new ArrayList<>();


    public TestDataConverter(String fileName) throws IOException {
        //** @fileName - name of file with test data.
        // file with test data should be located by the @testDataFolderPath*/

        FileInputStream inputStream = new FileInputStream(testDataFolderPath + fileName);
        int data = inputStream.read();
        char content = 0;

        while (data != -1) {
            content = (char) data;
            if (content != '\n') {
                if (content != ',') {
                    if(content != '\r') {
                        out.append(content);
                    }
                    else {System.out.println("Symbol is not added");}
                } else {
                    stringList.add(String.valueOf(out));
                    out = new StringBuilder();
                }
            } else {
                stringList.add(String.valueOf(out));
                rowList.add(stringList);
                out = new StringBuilder();
                stringList = new ArrayList();
            }


            data = inputStream.read();
        }



        System.out.print(rowList.size());

    }
///////////////////////////---------------------
//    public TestDataConverter(String fileName) throws IOException {
//        FileInputStream inputStream = new FileInputStream(testDataFolderPath + fileName);
//        int data = inputStream.read();
//        char content = 0;
//
//        while (data != -1) {
//            content = (char) data;
//            buildRow(content);
//            data = inputStream.read();
//        }
//
//        System.out.print(rowList.size());
//
//
//    }

    private void buildRow(char content){
        if (content != '\n') {
            buildString(content);
        } else {
            stringList.add(String.valueOf(out));
            addRowToRowList(stringList);
        }
    }

    private void addRowToRowList(ArrayList<String> list){
        rowList.add(list);
        out = new StringBuilder();
        stringList = new ArrayList<>();
    }

    private void buildString(char content){
        if (content != ',') {
            if(content != '\r') {
                out.append(content);
            }
            else {
                System.out.println("Symbol is not added");}
        } else {
            addStringToList(out);
        }
    }

    private void addStringToList(StringBuilder string){
        stringList.add(String.valueOf(string));
        out = new StringBuilder();
    }



    //**Converts test data from listArray into array**//
    //**First row(Header) in the file is skipped **//
    public String[][] getDataFromFile(){
        System.out.println("*********");
        System.out.println(rowList.toString());
        System.out.println("******");

        String[][] arr = new String[rowList.size()-1][rowList.get(0).size()];

        for(int i = 1; i < rowList.size(); i++){
//            verifyRowLength(i, rowList.get(i).size());
            for(int j = 0; j < rowList.get(i).size(); j++){
                arr[i-1][j] = rowList.get(i).get(j).toString();
            }

        }

        return arr;
    }



    private IllegalArgumentException verifyRowLength(int rowNumber, int rowSize){

        /**Compare rowSize */
        if(rowSize == rowList.get(0).size()){
            return null;
        }
        else{
            return new IllegalArgumentException("Incorrect count of columns in the row #" + rowNumber);
        }

    }




}







