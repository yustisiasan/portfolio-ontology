/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pilpres_ontologi;
import java.io.BufferedReader;
//import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.iterator.ExtendedIterator;
/**
 *
 * @author ACER
 */
public class Rule1 {
    static int maxDepth = 0;
    
    public static void compareDepth(int depthNow){
        if(depthNow>maxDepth){
            maxDepth=depthNow;
        }
    }

    public static void traverseStart( OntModel model, OntClass ontClass ) 
    {
    	// if ontClass is specified we only traverse down that branch
    	if( ontClass != null )
    	{
    		traverse(ontClass, new ArrayList<OntClass>(), 0);
    		return;
    	}
    	
        // create an iterator over the root classes
        Iterator<OntClass> i = 	model.listHierarchyRootClasses();
        
        // traverse through all roots
        while (i.hasNext()) 
        {
            OntClass tmp = i.next();
            traverse( tmp, new ArrayList<OntClass>(), 0 );
        }
    }

    private static void traverse( OntClass oc, List<OntClass> occurs, int depth )
    {
    	if( oc == null ) return;
 
    	// if end reached abort (Thing == root, Nothing == deadlock)
    	if( oc.getLocalName() == null || oc.getLocalName().equals( "Nothing" ) ) return;
    	
		// print depth times "\t" to retrieve a explorer tree like output
		for( int i = 0; i < depth; i++ ) { System.out.print("\t"); }
		
		// print out the OntClass
		System.out.println( oc.toString() );
		
        // check if we already visited this OntClass (avoid loops in graphs)
        if ( oc.canAs( OntClass.class ) && !occurs.contains( oc ) ) 
        {
        	// for every subClass, traverse down
            for ( Iterator<OntClass> i = oc.listSubClasses( true );  i.hasNext(); ) 
            {
                OntClass subClass = i.next();
                	                
                // push this expression on the occurs list before we recurse to avoid loops
                occurs.add( oc );
                // traverse down and increase depth (used for logging tabs)
                int depthNow = depth+1;
                traverse( subClass, occurs, depthNow );
                compareDepth(depthNow);
                // after traversing the path, remove from occurs list
                occurs.remove( oc );
            }
        }
    	
    }
    
    public static int getMaxDepth(String ONT_URL) throws FileNotFoundException{
        OntModel model = OntologiPilpres.getModel();
        traverseStart(model, null);
        return maxDepth;
    }
    
    public static int getDepth(String className) throws FileNotFoundException{
        int depthOfClass = 1;
        OntModel model = OntologiPilpres.getModel();
        String namespace= OntologiPilpres.getNameSpaceOfEntity("TAPilpres");
        System.out.println("Success");
        //String namespace= ontology.getNameSpace();
        System.out.println(namespace);
        OntClass thisClass = model.getOntClass(namespace+className);
        boolean stillNotRoot = true;
        while(stillNotRoot){
            if (thisClass.getSuperClass().isOntLanguageTerm()){
                stillNotRoot=false;
            } else {
                thisClass = thisClass.getSuperClass();
                depthOfClass++;
            }
        }
        System.out.println(depthOfClass);
        System.out.println("Bak");
        return depthOfClass;
    }
	
        public static ArrayList<String> individuPos() throws FileNotFoundException{
            ArrayList<String> individu = new ArrayList<>();
            OntModel model = OntologiPilpres.getModel();
                ExtendedIterator<OntClass> classes = model.listClasses();
                    while (classes.hasNext()){
                        OntClass thisClass = (OntClass) classes.next();
//                        System.out.println("Found class: " + thisClass.toString());
                        ExtendedIterator instances = thisClass.listInstances();
                        while (instances.hasNext()){
                            String kelas2 = thisClass.toString();
                            Individual thisInstance = (Individual) instances.next();
                            
                                if(kelas2.matches(".+Positif")){
                                    
//                                  System.out.println("  Found instance: " + thisInstance.toString());
                                    String[] hasilSplit2 = thisInstance.toString().split("#");
                                    individu.add(hasilSplit2[1]);
                                
                            }
//                            String kelas2 = thisClass.toString();
                            
                            
                        }

                    }

            return individu;
        }
        public static ArrayList<String> individuNeg() throws FileNotFoundException{
            ArrayList<String> individu = new ArrayList<>();
            OntModel model = OntologiPilpres.getModel();
                ExtendedIterator<OntClass> classes = model.listClasses();
                    while (classes.hasNext()){
                        OntClass thisClass = (OntClass) classes.next();
//                        System.out.println("Found class: " + thisClass.toString());
                        ExtendedIterator instances = thisClass.listInstances();
                        while (instances.hasNext()){
                            String kelas2 = thisClass.toString();
                            Individual thisInstance = (Individual) instances.next();
                            
                                if(kelas2.matches(".+Negatif")){
                                    
//                                  System.out.println("  Found instance: " + thisInstance.toString());
                                    String[] hasilSplit2 = thisInstance.toString().split("#");
                                    individu.add(hasilSplit2[1]);
                                
                            }
//                            String kelas2 = thisClass.toString();
                            
                            
                        }

                    }

            return individu;
        }
    
	public static void main(String[] args) throws FileNotFoundException, IOException 
	{
		// create OntModel
            FileWriter fw=new FileWriter("E:\\SOC\\Semester 4\\Tugas Akhir\\TA2\\DataPilpres\\Hasil Klasifikasi Paslon1.txt");
            //FileWriter fw=new FileWriter("E:\\SOC\\Semester 4\\Tugas Akhir\\TA2\\DataPilpres\\Hasil Klasifikasi Paslon2.txt");
            ArrayList<String> kelas = new ArrayList<>();
            ArrayList<String> individu = new ArrayList<>();
//            String regex = "http://www.semanticweb.org/acer/ontologies/2019/4/TAPilpres#";
            String[] arrayKelas = {".+Kompetensi", ".+Integritas", ".+Empati", ".+RepresentasiSosial"};
		OntModel model = OntologiPilpres.getModel();
                ExtendedIterator<OntClass> classes = model.listClasses();
                    while (classes.hasNext()){
                        OntClass thisClass = (OntClass) classes.next();
//                        System.out.println("Found class: " + thisClass.toString());
                        ExtendedIterator instances = thisClass.listInstances();
                        while (instances.hasNext()){
                            String kelas2 = thisClass.toString();
                            Individual thisInstance = (Individual) instances.next();
                            for (String klass : arrayKelas){
                                if(kelas2.matches(klass)){
                                    kelas.add(klass);
                                    
//                                  System.out.println("  Found instance: " + thisInstance.toString());
                                    String[] hasilSplit2 = thisInstance.toString().split("#");
                                    individu.add(hasilSplit2[1]);
                                    
                                }
                            }
//                            String kelas2 = thisClass.toString();
                            
                            
                        }

                    }

                ArrayList<String> neg = individuNeg();
                ArrayList<String> pos = individuPos();
                ArrayList<String> kalimat = Rule1.bacaFile();
                int kk = 1;
                for(String kalim : kalimat){

                    System.out.println("Kalimat ke : "+kk);
                    kalim = kalim.substring(2,kalim.length()-2);
                    String[] pecahKalimat = kalim.split(", ");
                    
                    for (String pecahKalimat1 : pecahKalimat) {

                        // Load data per positif dan negatif
                        ArrayList<String> positPecah = new ArrayList<>();
                        ArrayList<String> positKelas = new ArrayList<>();
                        ArrayList<String> negatPecah = new ArrayList<>();
                        ArrayList<String> negatKelas = new ArrayList<>();
                        for (int i = 0; i < kelas.size(); i++) {
                            if (pecahKalimat1.equals(individu.get(i))) {
//                                System.out.println("=== Ketemu ===");
                                for (int j = 0; j < neg.size(); j++) {
                                    String a = ".+"+neg.get(j);
                                    String b = neg.get(j)+".+";
                                    String c = ".+"+neg.get(j)+".+";
                                    String d = neg.get(j); 
                                    if(pecahKalimat1.matches(a) || pecahKalimat1.matches(b) || pecahKalimat1.matches(c) || pecahKalimat1.matches(d)){
                                      
                                            negatPecah.add(pecahKalimat1);
                                            negatKelas.add(kelas.get(i));
                                            //System.out.println("sfdfdfd"+negatKelas);
                                            //System.out.println("sfdfdfd"+negatPecah);
//                                            System.out.println("Sentimen : Negatif");
//                                            System.out.println(""); 
                                        
                                    }
                                }
                                for (int j = 0; j < pos.size(); j++){
                                    String a = ".+"+pos.get(j);
                                    String b = pos.get(j)+".+";
                                    String c = ".+"+pos.get(j)+".+";
                                    String d = pos.get(j);
                                    if (pecahKalimat1.matches(a) || pecahKalimat1.matches(b) || pecahKalimat1.matches(c) ||pecahKalimat1.matches(d)){
                                            positPecah.add(pecahKalimat1);
                                            positKelas.add(kelas.get(i));
                                        
                                    }
                                }
                                
                            }
                        }
                        
                        
                            
                         //RULE 1 (Kerja Ambisius)
                        ArrayList<String> positPecahNew1 = new ArrayList<>();
                        ArrayList<String> positKelasNew1 = new ArrayList<>();
                        ArrayList<String> negatPecahNew1 = new ArrayList<>();
                        ArrayList<String> negatKelasNew1 = new ArrayList<>();
                         for(int j=0;j<negatPecah.size();j++){
                            if(negatPecah.get(j).matches("kerja_ambisius") || negatPecah.get(j).matches(".+kerja_ambisius")|| negatPecah.get(j).matches(".+kerja_ambisius.+")|| negatPecah.get(j).matches("kerja_ambisius.+")){
                                negatPecah.remove(negatPecah.get(j));
                                negatKelas.remove(negatKelas.get(j));
                               
                            }else{
                                negatPecahNew1.add(negatPecah.get(j));
                                negatKelasNew1.add(negatKelas.get(j));
                            }
                            
                        }
                         //RULE 1 (Kerja Santai)
                        for(int j=0;j<positPecah.size();j++){
                            if(positPecah.get(j).matches("kerja_santai") || positPecah.get(j).matches(".+kerja_santai")|| positPecah.get(j).matches(".+kerja_santai.+")|| positPecah.get(j).matches("kerja_santai.+")){
                                positPecah.remove(positPecah.get(j));
                                positKelas.remove(positKelas.get(j));
                            }else{
                                positPecahNew1.add(positPecah.get(j));
                                positKelasNew1.add(positKelas.get(j));
                            }
                        }
                        
                        
                        //RULE 2 (TIDAK_blabla)
                        
                        for(int j=0;j<positPecahNew1.size();j++){
                            for (String pos1 : pos) {
                                String a = "tidak_" + pos1;
                                String b = ".+tidak_" + pos1;
                                String c = "tidak_" +".+"+ pos1;
                                if(positPecahNew1.get(j).matches(c)||positPecahNew1.get(j).matches(a) || positPecahNew1.get(j).matches(b)){
                                    negatPecahNew1.add(positPecahNew1.get(j));
                                    negatKelasNew1.add(positKelasNew1.get(j));
                                    positPecahNew1.remove(positPecahNew1.get(j));
                                    positKelasNew1.remove(positKelasNew1.get(j));
                                    break;
                                }
                            
                            }
                        }
                         
          //RULE 4 (Positif + Negatif = Negatif)
                        
                            for (String neg1 : neg) {
                                    String b = ".+_" + neg1;
                                    String c = neg1+"_.+";
                                    String e = ".+_"+neg1+"_.+";
                                    String d = "tidak_" + neg1;
                                    String f = ".+tidak_" + neg1;        
                                for(int j=0;j<positPecahNew1.size();j++){
                                    if (positPecahNew1.get(j).matches(b)||
                                            positPecahNew1.get(j).matches(c)||
                                            positPecahNew1.get(j).matches(e)|| 
                                            positPecahNew1.get(j).matches(d)||
                                            positPecahNew1.get(j).matches(f)){
                                        negatPecahNew1.add(positPecahNew1.get(j));
                                        negatKelasNew1.add(positKelasNew1.get(j));
//                                        positPecahNew1.add(negatPecahNew1.get(j));
//                                        positKelasNew1.add(negatKelasNew1.get(j));
                                        positPecahNew1.remove(positPecahNew1.get(j));
                                        positKelasNew1.remove(positKelasNew1.get(j));
                                        negatPecahNew1.remove(negatPecahNew1.get(j));
                                        negatKelasNew1.remove(negatKelasNew1.get(j));
                                     break;  
                                }
                                
                            }
                            
                        }
                        for(int j=0;j<positPecahNew1.size();j++){
                            System.out.println("Kata : "+positPecahNew1.get(j));
                            System.out.println("Kelas : "+positKelasNew1.get(j));
                            System.out.println("Sentimen : Positif");
                            fw.write(kk+","+positPecahNew1.get(j)+","+positKelasNew1.get(j)+",Positif\n");
                        }
                        for(int j=0;j<negatPecahNew1.size();j++){
                            System.out.println("Kata : "+negatPecahNew1.get(j));
                            System.out.println("Kelas : "+negatKelasNew1.get(j));
                            System.out.println("Sentimen : Negatif");
                            fw.write(kk+","+negatPecahNew1.get(j)+","+negatKelasNew1.get(j)+",Negatif\n");
                        }
                        
                        
                    }
                    System.out.println("");
                    System.out.println("----Pemisah kalimat----");
                    System.out.println("");
                    kk++;
                    
                }
                fw.close(); 
        /*catch(Exception e){System.out.println(e);}*/
                
	}
        
        public static ArrayList<String> bacaFile() throws FileNotFoundException, IOException{
            String fileName = "E:\\SOC\\Semester 4\\Tugas Akhir\\TA2\\DataPilpres\\praprosesPaslon1.txt";
            //String fileName = "E:\\SOC\\Semester 4\\Tugas Akhir\\TA2\\DataPilpres\\praprosesPaslon2.txt";
            FileReader fileReader = new FileReader(fileName);
            ArrayList<String> kalimat = new ArrayList<>();
            int i=0;
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String line;
                while((line = bufferedReader.readLine()) != null) {
                    //System.out.println(line);
                    kalimat.add(line);
                    i++;
                }
            }
            return kalimat;
        }
}
