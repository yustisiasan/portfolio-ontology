/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pilpres_ontologi;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntTools;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.RDFS;
/**
 *
 * @author ACER
 */
public class OntologiPilpres {
    static String ONT_URL; 

    static String ONT_ENTITIES;// = "criminality_ontologyv2";

    public static String getONT_URL() {
        return ONT_URL;
    }

    public static void setONT_URL(String ONT_URL) {
        OntologiPilpres.ONT_URL = ONT_URL;
    }

    public static String getONT_ENTITIES() {
        return ONT_ENTITIES;
    }

    public static void setONT_ENTITIES(String ONT_ENTITIES) {
        OntologiPilpres.ONT_ENTITIES = ONT_ENTITIES;
    }

    static HashMap lexiconTableClasses = new HashMap();
    static HashMap lexiconTableInstances = new HashMap();

    public static OntModel getModel() throws FileNotFoundException {
        OntModel model = ModelFactory.createOntologyModel();
        model.read((new FileInputStream("E:\\SOC\\Semester 4\\Tugas Akhir\\TA2\\DataPilpres\\TAPilpres.owl")), null);
        return model;
    }

    public static String getNameSpace() throws FileNotFoundException {
        OntModel model = getModel();
        return model.getNsPrefixURI("TAPilpres");
    }

    public static String getNameSpaceOfEntity(String entity) throws FileNotFoundException {
        OntModel model = getModel();
        return model.getNsPrefixURI(entity);
    }

    private static boolean hasSubClassTransitive(OntClass parent, OntClass child) {
        return OntTools.findShortestPath(child.getOntModel(), child, parent,
        new OntTools.PredicatesFilter(RDFS.subClassOf)) != null;
    }

    public static List<OntClass> namedRootsOf(OntClass c) {
        List<OntClass> cRoots = new ArrayList<>();
        for (OntClass root : OntTools.namedHierarchyRoots(c.getOntModel())) {
            if (hasSubClassTransitive(root, c)) {
                cRoots.add(root);
            }
        }
        System.out.println(Arrays.deepToString(cRoots.toArray()));
        return cRoots;
    }

    public static void makeLexicon() throws FileNotFoundException {
        //create hashmap to contains the lexicon
        HashMap hmClass = new HashMap();
        HashMap hmInstances = new HashMap();

        //read model
        OntModel model = OntologiPilpres.getModel();

        //iterate the model and put the words to hashmap
        ExtendedIterator classes = model.listClasses();
        while (classes.hasNext()) {
            OntClass thisClass = (OntClass) classes.next();
            hmClass.put(thisClass.getLocalName(), thisClass.getLocalName());
            ExtendedIterator instances = thisClass.listInstances();
            while (instances.hasNext()) {
                Individual thisInstance = (Individual) instances.next();
                hmInstances.put(thisInstance.getLocalName(), thisInstance.getLocalName());
            }
        }
        //System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk"+hmInstances);

        lexiconTableClasses = hmClass;
        lexiconTableInstances = hmInstances;
    }

    /**
     *
     */
    static int maxDepth = 0;

    public static void compareDepth(int depthNow) {
        if (depthNow > maxDepth) {
            maxDepth = depthNow;
        }
    }

    public static void traverseStart(OntModel model, OntClass ontClass) {
        // if ontClass is specified we only traverse down that branch
        if (ontClass != null) {
            traverse(ontClass, new ArrayList<>(), 0);
            return;
        }

        // create an iterator over the root classes
        Iterator<OntClass> i = model.listHierarchyRootClasses();

        // traverse through all roots
        while (i.hasNext()) {
            OntClass tmp = i.next();
            traverse(tmp, new ArrayList<>(), 0);
        }
    }

    private static void traverse(OntClass oc, List<OntClass> occurs, int depth) {
        if (oc == null) {
            return;
        }

        // if end reached abort (Thing == root, Nothing == deadlock)
        if (oc.getLocalName() == null || oc.getLocalName().equals("Nothing")) {
            return;
        }

        // print depth times "\t" to retrieve a explorer tree like output
        for (int i = 0; i < depth; i++) {
            System.out.print("\t");
        }

        // print out the OntClass
        System.out.println(oc.toString());

        // check if we already visited this OntClass (avoid loops in graphs)
        if (oc.canAs(OntClass.class) && !occurs.contains(oc)) {
            // for every subClass, traverse down
            for (Iterator<OntClass> i = oc.listSubClasses(true); i.hasNext();) {
                OntClass subClass = i.next();

                // push this expression on the occurs list before we recurse to avoid loops
                occurs.add(oc);
                // traverse down and increase depth (used for logging tabs)
                int depthNow = depth + 1;
                traverse(subClass, occurs, depthNow);
                compareDepth(depthNow);
                // after traversing the path, remove from occurs list
                occurs.remove(oc);
            }
        }
    }

    public static int getDepth(String className) throws FileNotFoundException {
        int depthOfClass = 1;
        OntModel model = OntologiPilpres.getModel();
        String namespace = OntologiPilpres.getNameSpace();
        OntClass thisClass = model.getOntClass(namespace + className);
        boolean stillNotRoot = true;
        //repeating getting the superclass while keep incrementing depthOfClass until the class is hierarchy root
        while (stillNotRoot) {
            if (thisClass.isHierarchyRoot()) {
                stillNotRoot = false;
            } else {
                thisClass = thisClass.getSuperClass();
                depthOfClass++;
            }
        }
        return depthOfClass;
    }

    public static OntClass findRootClass(String className) throws FileNotFoundException {
        OntModel model = OntologiPilpres.getModel();
        String namespace = OntologiPilpres.getNameSpace();
        OntClass thisClass = model.getOntClass(namespace + className);
        List<OntClass> rootes = namedRootsOf(thisClass);
        return rootes.get(0);
    }
}
