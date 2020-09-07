/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package preprocessing;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author ACER
 */
public class preProcess {
    static HashMap phrases4 = new HashMap();
    static HashMap phrases3 = new HashMap();
    static HashMap phrases = new HashMap();
    static HashMap subs = new HashMap();
    static HashMap stopword = new HashMap();
    static String stopwordPath;
    static String phrasesPath;
    static String subsPath;

    public static ArrayList<String> dataInput = new ArrayList<>();

    public static void setStopwordPath(String stopwordPath) {
        preProcess.stopwordPath = stopwordPath;
    }

    public static void setPhrasesPath(String phrasesPath) {
        preProcess.phrasesPath = phrasesPath;
    }

    public static void setSubsPath(String subsPath) {
        preProcess.subsPath = subsPath;
    }
    
    public static void readPhrasesFromXLS(int sheetNum) throws IOException, InvalidFormatException {
        String filepath = "E:/SOC/Semester 4/Tugas Akhir/TA2/DataPilpres/prasv.xlsx";
        FileInputStream fis = new FileInputStream(new File(filepath));
        //File file = new File(filepath);
        //finds workbook
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        //return first sheet
        XSSFSheet sheet = wb.getSheetAt(sheetNum);
        //get iterator
        Iterator<Row> rowIterator = sheet.iterator();
        //traversing
        if (sheetNum == 0) {
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                //iterate columns
                Cell cell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                String first_Word;
                if (cell.getCellType() == CellType.NUMERIC) {
                    first_Word = cell.getNumericCellValue() + "";
                } else {
                    first_Word = cell.getStringCellValue();
                }
//                System.out.println("first_word : "+first_Word);
                cell = row.getCell(1);
                String second_Word = cell.getStringCellValue();
                if (!phrases.containsKey(first_Word)) {
                    phrases.put(first_Word, new ArrayList<>());
                    ArrayList matchingPhrases = (ArrayList) phrases.get(first_Word);
                    matchingPhrases.add(second_Word);
                } else {
                    ArrayList matchingPhrases = (ArrayList) phrases.get(first_Word);
                    matchingPhrases.add(second_Word);
                }
            }
        }
        else if (sheetNum == 1) {
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                //iterate columns
                Cell cell = row.getCell(0);
                String first_Word = cell.getStringCellValue();
                cell = row.getCell(1);
                String second_Word = cell.getStringCellValue();
                cell = row.getCell(2);
                String third_Word = cell.getStringCellValue();
                if (!phrases3.containsKey(first_Word)) {
                    phrases3.put(first_Word, new ArrayList<>());
                    String newW = second_Word + ">" + third_Word;
                    ArrayList matchingPhrases = (ArrayList) phrases3.get(first_Word);
                    matchingPhrases.add(newW);
                } else {
                    String newW = second_Word + ">" + third_Word;
                    ArrayList matchingPhrases = (ArrayList) phrases3.get(first_Word);
                    matchingPhrases.add(newW);
                }
            }
        }else if(sheetNum == 2){
            while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
                //iterate columns
                Cell cell = row.getCell(0);
                String first_Word = cell.getStringCellValue();
                cell = row.getCell(1);
                String second_Word = cell.getStringCellValue();
                cell = row.getCell(2);
                String third_Word = cell.getStringCellValue();
                cell = row.getCell(3);
                String fourth_Word = cell.getStringCellValue();
                if (!phrases4.containsKey(first_Word)) {
                    phrases4.put(first_Word, new ArrayList<>());
                    String newW = second_Word + ">" + third_Word + ">" + fourth_Word;
                    ArrayList matchingPhrases = (ArrayList) phrases4.get(first_Word);
                    matchingPhrases.add(newW);
                } else {
                    String newW = second_Word + ">" + third_Word + ">" + fourth_Word;
                    ArrayList matchingPhrases = (ArrayList) phrases4.get(first_Word);
                    matchingPhrases.add(newW);
                }
            }
        }
        fis.close();
    }

    public static void readSubsFromXLS(String filepath) throws IOException, InvalidFormatException {
        filepath = "E:\\SOC\\Semester 4\\Tugas Akhir\\TA2\\DataPilpres\\substitusi.xlsx";
        FileInputStream fis = new FileInputStream(new File(filepath));
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sheet = wb.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            Cell cell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            String first_Word;
            if (cell.getCellType() == CellType.NUMERIC) {
                first_Word = cell.getNumericCellValue() + "";
            } else {
                first_Word = cell.getStringCellValue();
            }

            cell = row.getCell(1);
            String second_Word = cell.getStringCellValue();
            if (!subs.containsKey(first_Word)) {
                subs.put(first_Word, second_Word);
            }
        }
        fis.close();
    }

    public static String lowerCase(String input) {
        return input.toLowerCase();
    }

    public static String removeSymbol(String input) {
        //return input.replaceAll("[()-+.^:,/!&*%$@=+#?]", " ");
        return input.replaceAll("[^a-zA-Z]", " ");
    }

    public static String removeLink(String input) {
        return input.replaceAll("[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)", "");
    }

    public static List<String> tokenizeFilterStemming(String input, boolean stopws) throws IOException {
        int k = 0;
        int p = 0;
        boolean check = false;
        ArrayList<String> wordlist = new ArrayList<String>();
        String sCurrentLine, pCurrentLine;
        ArrayList<String> st = new ArrayList<>();
        String[] stopword = new String[3000];
        String[] except = new String[3000];
//        System.out.println("input : " + input);
//        try {
            FileReader fr = new FileReader("E:/SOC/Semester 4/Tugas Akhir/TA2/DataPilpres/stopword.txt");

            BufferedReader br = new BufferedReader(fr);
//          BufferedReader bd = new BufferedReader(fd);

            while ((sCurrentLine = br.readLine()) != null) {
//                wordlist.add(sCurrentLine);
                stopword[k] = sCurrentLine;
//                System.out.println(stopword[k]);
                k++;
            }

//            while ((pCurrentLine = bd.readLine()) != null) {
////                wordlist.add(sCurrentLine);
//                except[p] = pCurrentLine;
////                System.out.println(stopword[k]);
//                p++;
//            }

            StringBuilder builder = new StringBuilder(input);
            String[] words = builder.toString().split("\\s");
            for (String wordss : words) {
//                System.out.println(wordss);
                wordlist.add(wordss);
                for (int i = 0; i < wordlist.size(); i++) {
                    for (int j = 0; j < k; j++) {
                        if (stopword[j].contains(wordlist.get(i))) {
                            for (int l = 0; l < p; l++) {
                                if (except[l].equals(wordlist.get(i))) {
                                    check = true;
                                }
                            }
                            if (!check) {
                                wordlist.remove(i);
                            } else {
                                check = false;
                            }
                            break;
                        }
//                        if (wordlist.get(i).equals(stopword[j])) {
////                            System.out.println("masuk");
//                            wordlist.remove(i);
//                            break;
//                        }
                    }
                }
//                System.out.println(wordss);
            }
            for (String str : wordlist) {
                st.add(str);
//                System.out.println(str);
            }
//        } catch (Exception e) {
//            System.out.println(e);
//        }
        return st;
    }

    public static List<String> tokenize(String input) throws IOException {
        input = lowerCase(input);
        StringReader sr = new StringReader(input);
        //String dat = lowerCase(input);
        Tokenizer tokenizer = new StandardTokenizer();
        tokenizer = new WhitespaceTokenizer();
        tokenizer.setReader(sr);
        CharTermAttribute cha = tokenizer.addAttribute(CharTermAttribute.class);
        TypeAttribute tp = tokenizer.getAttribute(TypeAttribute.class);
        OffsetAttribute oa = tokenizer.getAttribute(OffsetAttribute.class);
        List<String> tokens = new ArrayList<>();
        tokenizer.reset();
        while (tokenizer.incrementToken()) {
            tokens.add(cha.toString());
        }
        return tokens;
    }

    public static List<String> substituteWord(List<String> datas) throws IOException, InvalidFormatException {
        if (subs.isEmpty()) {
            readSubsFromXLS(subsPath);
        }
        List<String> output = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            if (subs.containsKey(datas.get(i))) {
                //System.out.println("yes");
                String subbed = (String) subs.get(datas.get(i));
                output.add(subbed);
            } else {
                output.add(datas.get(i));
            }
        }
        return output;
    }

    public static List<String> makePhrases(List<String> datas) throws IOException, InvalidFormatException {
        if (phrases.isEmpty()) {
            //System.out.println("read subs from xls");
            readPhrasesFromXLS(0);
            //ehrigProcessing.printHashMap(subs);
        }
        List<String> output = datas;
        for (int i = 0; i < output.size() - 1; i++) {
            if (phrases.containsKey(output.get(i))) {
                String fiWord = output.get(i);
                ArrayList secWords = (ArrayList) phrases.get(fiWord);
                if (secWords.contains(output.get(i + 1))) {
                    String secWord = output.get(i + 1);
                    output.set(i, fiWord + "_" + secWord);
                    output.remove(i + 1);
                }
            }
        }
        return output;
    }

    public static List<String> doStopword(List<String> data) throws IOException, InvalidFormatException {
        int k = 0;
        String input = "";
        ArrayList<String> wordlist = new ArrayList<String>();
        String sCurrentLine;
        ArrayList<String> st = new ArrayList<>();
        String[] stopword = new String[2000];
        FileReader fr = new FileReader("E:/SOC/Semester 4/Tugas Akhir/TA2/DataPilpres/stopword.txt");
        BufferedReader br = new BufferedReader(fr);
        while ((sCurrentLine = br.readLine()) != null) {
            stopword[k] = sCurrentLine;
            k++;
        }
        StringBuilder builder = new StringBuilder(input);
        String[] words = builder.toString().split("\\s");
        for (String wordss : words) {
            System.out.println(wordss);
            wordlist.add(wordss);
            for (int i = 0; i < wordlist.size(); i++) {
                for (int j = 0; j < k; j++) {
                    if (stopword[j].contains(wordlist.get(i))) {
                        wordlist.remove(i);
                        break;
                    }
                }
            }
        }
        for (String str : wordlist) {
            st.add(str);
        }
        return st;
    }

    public static List<String> makePhrases3(List<String> datas) throws IOException, InvalidFormatException {
        if (phrases3.isEmpty()) {
            //System.out.println("read subs from xls");
            readPhrasesFromXLS(1);
//            System.out.println("Phrases 3" + Arrays.asList(phrases3));
//            ehrigProcessing.printHashMap(subs);
        }
        List<String> output = datas;
        for (int i = 0; i < output.size() - 2; i++) {
            if (phrases3.containsKey(output.get(i))) {
                String fiWord = output.get(i);
                ArrayList secThirdWords = (ArrayList) phrases3.get(fiWord);
                // System.out.println(secThirdWords.size());
                ArrayList<String> secWords = new ArrayList<>();
                ArrayList<String> thirdWords = new ArrayList<>();
//                ArrayList<String> fourthWords = new ArrayList<>();
                
                for (Object s : secThirdWords) {
                    String stword = (String) s;
                    String[] words = stword.split(">");
//                    System.out.println("words : "+Arrays.toString(words));
                    secWords.add(words[0]);
                    thirdWords.add(words[1]);
//                    fourthWords.add(words[2]);
                }
                if (secWords.contains(output.get(i + 1))) {
                    String secWord = output.get(i + 1);
                    System.out.println("output + 2 : "+output.get(i + 2));
                    if (thirdWords.contains(output.get(i + 2))) {
                        String thirdWord = output.get(i + 2);
                        output.set(i, fiWord + "_" + secWord + "_" + thirdWord);
                        System.out.println("First : "+fiWord + " Second : "+secWord + " Third : "+thirdWord);
                        output.remove(i + 2);
                        output.remove(i + 1);
//                    }  if (fourthWords.contains(output.get(i + 3))){
//                        String fourthWord = output.get(i + 3);
//                        output.set(i, fiWord + "_" + secWord + "_" + thirdWord + "_" + fourthWord);
//                        System.out.println("first : "+fiWord + " Second : "+secWord + "Third : "+thirdWord + "Fourth : "+fourthWord);
//                        output.remove(i + 3);
//                        output.remove(i + 2);
//                        output.remove(i + 1);
//                    }   
                    }
                }
            }
        }
        return output;
    }
 public static List<String> makePhrases4(List<String> datas) throws IOException, InvalidFormatException {
        if (phrases4.isEmpty()) {
            //System.out.println("read subs from xls");
            readPhrasesFromXLS(2);
//            System.out.println("Phrases 3" + Arrays.asList(phrases3));
//            ehrigProcessing.printHashMap(subs);
        }
        List<String> output = datas;
        for (int i = 0; i < output.size()-3; i++) {
            
            if (phrases4.containsKey(output.get(i))) {
//                System.out.println("M4");
                String fiWord = output.get(i);
                ArrayList secThirdFWords = (ArrayList) phrases4.get(fiWord);
                // System.out.println(secThirdWords.size());
                ArrayList<String> secWords = new ArrayList<>();
                ArrayList<String> thirdWords = new ArrayList<>();
                ArrayList<String> fourthWords = new ArrayList<>();
                
                for (Object s : secThirdFWords) {
                    String stword = (String) s;
                    String[] words = stword.split(">");
//                    System.out.println("words : "+Arrays.toString(words));
                    secWords.add(words[0]);
                    thirdWords.add(words[1]);
                    fourthWords.add(words[2]);
//                    System.out.println(secWords);
                }
//                System.out.println(fourthWords);
                if (secWords.contains(output.get(i + 1))) {                    

                    String secWord = output.get(i + 1);
                    System.out.println("output + 2 : "+output.get(i + 2));
                    if (thirdWords.contains(output.get(i + 2))) {
                        String thirdWord = output.get(i + 2);
                        output.set(i, fiWord + "_" + secWord + "_" + thirdWord);
//                        System.out.println("first : "+fiWord + " Second : "+secWord + "Third : "+thirdWord);
//                        output.remove(i + 2);
//                        output.remove(i + 1);
                        if (fourthWords.contains(output.get(i + 3))){
                            String fourthWord = output.get(i + 3);
                            output.set(i, fiWord + "_" + secWord + "_" + thirdWord + "_" + fourthWord);
                            System.out.println("First : "+fiWord + " Second : "+secWord + "Third : "+thirdWord + "Fourth : "+fourthWord);
                            output.remove(i + 3);
                            output.remove(i + 2);
                            output.remove(i + 1);
                        }  
                    }
                }
            }
        }
        return output;
    }
 
    public static List<String> doPreprocessing(String input) throws IOException, InvalidFormatException {
        // String data = input;
        String data = lowerCase(input);
        data = removeLink(data);
        data = removeSymbol(data);
        List<String> result = tokenizeFilterStemming(data, true);
        result = substituteWord(result);
        result = makePhrases4(result);
        result = makePhrases3(result);
        result = makePhrases(result);
        
        return result;

    }

    public static void readDataFromXLS() throws IOException, InvalidFormatException {
        String filepath = "E:/SOC/Semester 4/Tugas Akhir/TA2/DataPilpres/Hasil Validasi Paslon1.xlsx";
        //String filepath = "E:/SOC/Semester 4/Tugas Akhir/TA2/DataPilpres/Hasil Validasi Paslon2.xlsx";
        FileInputStream fis = new FileInputStream(new File(filepath));
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sheet = wb.getSheetAt(0);
        int rows = sheet.getPhysicalNumberOfRows();
        for (int i = 2; i < rows; i++) {
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(2);
//            System.out.println(cell.getStringCellValue());
//            System.out.println("");
            dataInput.add(cell.getStringCellValue());
            
        }
        fis.close();
    }

    public static void writeDataFromXLS() throws IOException, InvalidFormatException {
        //Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        //Create a blank sheet
        XSSFSheet sheet = workbook.createSheet("DataInput");
        int rownum = 0;
        for (String data : dataInput) {
            String hasil = Arrays.toString(doPreprocessing(data).toArray());
            //String unpre = Arrays.toString(tokenize(data).toArray());
//            System.out.println("Hasil : "+hasil);
            Row row = sheet.createRow(rownum++);
            Cell cell = row.createCell(0);
            cell.setCellValue(hasil);
        }
        /*for (String data : dataInput) {
            
            Row row2 = sheet.createRow(rownum++);
            Cell cell2 = row2.createCell(1);
            cell2.setCellValue(unpre);
        }*/
        
        try {
            //Write the workbook in file system
            FileOutputStream out = new FileOutputStream(new File("E:/SOC/Semester 4/Tugas Akhir/TA2/DataPilpres/praprosesPaslon1.xlsx"));
            //FileOutputStream out = new FileOutputStream(new File("E:/SOC/Semester 4/Tugas Akhir/TA2/DataPilpres/praprosesPaslon2.xlsx"));
            workbook.write(out);
            out.close();
            System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InvalidFormatException {
//        String test = "Perjalanan ke Tangkuban Perahu tidak begitu jauh, saya menginap di Saung Balibu, perjalanan kesana memakan waktu sekitar 30 menit beruntung saya kesana tidak di jam sibuk atau jam macet biasanya bisa memakan waktu 1 jam lebih, itu kata driver uber yg ngantarin kita, dan sampai kesana kita disambut pemandangan yg eksotis nan indah, mata kita sangat dimanja akan keindahan dari sang pencipta";
//        System.out.println(Arrays.toString(doPreprocessing(test).toArray()));
        //System.out.println(ehrigProcessing.ehrigRelevanceComputation(doPreprocessing(test)));
        //fClassification.fClassify(doPreprocessing(test));
        readDataFromXLS();
        writeDataFromXLS();
    }
}
