/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pilpres_ontologi;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
/**
 *
 * @author ACER
 */
public class Rule2 {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader("E:\\SOC\\Semester 4\\Tugas Akhir\\TA2\\DataPilpres\\praprosesPaslon1.xlsx");
        FileReader fr2 = new FileReader("E:\\SOC\\Semester 4\\Tugas Akhir\\TA2\\DataPilpres\\Hasil Validasi Paslon1.xlsx");
        //FileReader fr3 = new FileReader("E:\\SOC\\Semester 4\\Tugas Akhir\\TA2\\DataPilpres\\Hasil Klasifikasi Paslon1.xlsx");
        FileReader fr4 = new FileReader("E:\\SOC\\Semester 4\\Tugas Akhir\\TA2\\DataPilpres\\Testing Paslon1.xlsx");
        FileWriter fw=new FileWriter("E:\\SOC\\Semester 4\\Tugas Akhir\\TA2\\DataPilpres\\Hasil Klasifikasi Paslon1.xlsx");
        BufferedReader br = new BufferedReader(fr);
        BufferedReader br2 = new BufferedReader(fr2);
        //BufferedReader br3 = new BufferedReader(fr3);
        BufferedReader br4 = new BufferedReader(fr4);
        String sCurrentLine;
        String sCurrentLine2;
        String sCurrentLine3;
        String sCurrentLine4;
        double TP = 0.0,TN = 0.0,FP = 0.0,FN = 0.0;
        
        ArrayList<String> labelKalimat = new ArrayList<>();
        while ((sCurrentLine4 = br4.readLine())!=null){
            labelKalimat.add(sCurrentLine4);
        }
        
        
        
        ArrayList<String> stopword = new ArrayList<>();
        
        //Data hasil Rule ontology
        int panjang = 1;
        while ((sCurrentLine = br.readLine()) != null) {
            stopword.add(sCurrentLine);
        }
        
        
        
        while ((sCurrentLine2 = br2.readLine()) != null) {
            panjang++;
        }
        System.out.println(panjang);
        String[] indexKalimatOn = new String[panjang];
        String[] kataOn = new String[panjang];
        String[] aspekOn = new String[panjang];
        String[] sentimenOn = new String[panjang];
        int index = 0;
        while ((sCurrentLine3 = br4.readLine()) != null) {
//            System.out.println(index);
            String[] pecahData = sCurrentLine3.split(",");
//            System.out.println(pecahData.length);
            for(int h = 0;h<pecahData.length;h++){
                if(h==0){
                    indexKalimatOn[index] = pecahData[0];
                }else if(h==1){
                    kataOn[index] = pecahData[1];
                }else if(h==2){
                    aspekOn[index] = pecahData[2];
                }else if(h==3){
                    sentimenOn[index] = pecahData[3];
                }
            }
            
//            System.out.println(sCurrentLine2);
            index++;
        }
        
        
        //Data untuk klasifikasi file
        ArrayList<String> kata = new ArrayList<>();
        ArrayList<String[]> kalimat = new ArrayList<>();
        for(String yea:stopword){
            String[] cobs = yea.split(" ");
            for(String kats : cobs){
                kata.add(kats);
//                System.out.println(kats);|
            }
            kalimat.add(cobs);
//            System.out.println(yea);
        }
        //rules 5
        int kai = 1;
        String[] status = {"0","0","0","0","0","0","0","0","0","0","0","0"};
        for(int a = 0; a<kalimat.size();a++){
            System.out.println("kalimat ke : "+(a+1));
            String[] pkataa = kalimat.get(a);
            for(int b = 0; b<pkataa.length;b++){
//                System.out.println(kataa);
                if(!"dahulu".equals(pkataa[b])){
                } else {
                    for (int c = 0; c<pkataa.length;c++){
                        if(pkataa[c].equals("sekarang")){
                            pkataa[b] = "";
                            pkataa[b+1] = "";
                        }
                    }
                }
              
                
                
                for(int i = 0; i<kataOn.length;i++){
                    if(indexKalimatOn[i]!=null){
//                        System.out.println(indexKalimatOn[i]);
                        int inKal = Integer.parseInt(indexKalimatOn[i]);

                        if (kai==inKal){
                            if(pkataa[b].equals(kataOn[i])){
//                                System.out.println("Kalimat ke : "+indexKalimatOn[i]);
                                System.out.println(pkataa[b]);
                                System.out.println(aspekOn[i]);
                                System.out.println(sentimenOn[i]);
                                fw.write(indexKalimatOn[i]+","+pkataa[b]+","+aspekOn[i]+","+sentimenOn[i]);
                                fw.write("\n");
                                if (aspekOn[i].equals(".+Kompetensi") && sentimenOn[i].equals("Positif")){
                                    status[0] = "1";
                                }else if (".+Kompetensi".equals(aspekOn[i]) && "Negatif".equals(sentimenOn[i])){
                                    status[1] = "1";
                                }else if (".+Integritas".equals(aspekOn[i]) && "Positif".equals(sentimenOn[i])){
                                    status[2] = "1";
                                }else if (".+Integritas".equals(aspekOn[i]) && "Negatif".equals(sentimenOn[i])){
                                    status[3] = "1";
                                }else if (".+Empati".equals(aspekOn[i]) && "Positif".equals(sentimenOn[i])){
                                    status[4] = "1";
                                }else if (".+Empati".equals(aspekOn[i]) && "Negatif".equals(sentimenOn[i])){
                                    status[5] = "1";
                                }else if (".+RepresentasiSosial".equals(aspekOn[i]) && "Positif".equals(sentimenOn[i])){
                                    status[6] = "1";
                                }else if (".+RepresentasiSosial".equals(aspekOn[i]) && "Negatif".equals(sentimenOn[i])){
                                    status[7] = "1";
                                }
                           }
                            
                        }
                        
                    }
                    
                    
                }
                
            }
            System.out.println("");
            kai++;
            
            String[] h = labelKalimat.get(a).split(",");
            
//            for (int j = 0; j < 12; j++) {
//                System.out.println(status[j]);
//                System.out.println(h[j]);
                if (status[7].equals("1")){
                    if(status[7].equals(h[7])){
                        TP++;
                    }else{
                        FP++;
                    }
                }else{
                    if(status[7].equals(h[7])){
                        TN++;
                    }else{
                        FN++;
                    }
                }
//            }
            
            for (int j = 0; j < 12; j++) {
                        status[j]="0";
            }
            
        }
        fw.close();
//        ACCURACY
        double accuracy = 0.0;
        double precission = 0.0;
        double f1 = 0.0;
        double recall = 0.0;
        
        accuracy = (TP+TN)/(TP+TN+FP+FN);
        precission = (TP)/(TP+FP);
        recall = (TP)/(TP+FN);
        f1 = 2*((precission*recall)/(precission+recall));

        System.out.println("TP : "+TP);
        System.out.println("FP : "+FP);
        System.out.println("TN : "+TN);
        System.out.println("FN : "+FN);
        System.out.println("Akurasi : "+(accuracy*100)+"%");
        System.out.println("Precission : "+(precission*100)+"%");
        System.out.println("Recall : "+(recall*100)+"%");
        System.out.println("F1-measure : "+(f1*100)+"%");
        
    }
}
