package io;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.PrintStream;
//import java.io.IOException;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;


/** Formats the data to be processed with different libraries.
*/
public class FormatData {
  Set<String> vocab;
  Map<Integer, String> featureMap;
  Map<String, Integer> revFeatureMap;
  public static String LABEL_TYPE = "string";

  public FormatData() {
    this.vocab = new HashSet<String>();
    this.featureMap = new HashMap<Integer, String>();
    this.revFeatureMap = new HashMap<String, Integer>();
  }

  // Loads vocabulary from the data.
  public void loadVocab(String dataFile) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile), "UTF-8"));
    String line = "";
    while((line = br.readLine())!=null) {
      String[] field = line.split("\t");
      String query = field[0].trim();
      String[] terms = field[0].toLowerCase().trim().split(" ");
      for(String term: terms) {
        this.vocab.add(term);
      }
    }
    br.close();
    System.out.printf("Size of Vocab = %d\n", this.vocab.size());
  }

  // Applies char 3-gram word hashing
  public void wordHashFeatures() {
    int id=0;
    for(String term: this.vocab) {
      term = "$"+term+"$";
      List<String> grams = nGrams(term, 3);
      for(String gram: grams) {
        if(!this.revFeatureMap.containsKey(gram)) {
          this.revFeatureMap.put(gram, id);
          this.featureMap.put(id, gram);
          id++;
        }
      }
    }
    System.out.printf("Size of features = %d\n", this.featureMap.size());
  }

  // Loads pre-stored features from a file
  public void loadFeatures(String featureFile) throws IOException {
    this.featureMap = new HashMap();
    this.revFeatureMap = new HashMap();
    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(featureFile), "UTF-8"));
    String line = "";
    while((line = br.readLine())!=null) {
      String[] field = line.split("\t");
      featureMap.put(Integer.parseInt(field[0].trim()), field[1].trim());
      revFeatureMap.put(field[1].trim(), Integer.parseInt(field[0].trim()));
    }
    br.close();
  }

  // stores feature map for future use
  public void writeFeatureMap(String outFile) throws IOException {
    FileOutputStream fos = new FileOutputStream(outFile);
    PrintStream p = new PrintStream(fos, false, "UTF-8");

    for(int i=0; i<this.featureMap.size(); i++) {
      p.printf("%d\t%s\n", i, featureMap.get(i));
    }
    p.close();
    fos.close();
  }
  
  // obtains a list of char n-grams
  public static List<String> nGrams(String str, int n) {
    List<String> grams = new LinkedList<String>();
    if(n>str.length())
      return grams;
    else if(n==str.length()) {
      grams.add(str);
      return grams;
    }
    char[] chars = str.toCharArray();
    for(int i=0; i<chars.length-n; i++) {
      StringBuffer sb = new StringBuffer();
      for(int j=0; j<n; j++) {
        sb.append(chars[i+j]);
      }
      grams.add(sb.toString());
    }
    return grams;
  }

  // converts the DSSM data into svm format with classes 1 and 0
  public void svmFormat(String dataFile, String svmDataFile) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile), "UTF-8"));

    FileOutputStream fos = new FileOutputStream(svmDataFile);
    PrintStream p = new PrintStream(fos);

    String line = "";
    while((line = br.readLine())!=null) {
      String[] field = line.split("\t");
      String query = field[0].trim();
      String label = "";
      if(LABEL_TYPE!="string")
        label = (Double.parseDouble(field[1].trim()))>=0.2?"1":"0";
      else
        label = (field[1].trim().equals("NonOffensive"))?"0":"1";
      String[] terms = field[0].toLowerCase().trim().split(" ");
      Map<Integer, Double> countVector = new HashMap<Integer, Double>();
      for(String term: terms) {
        List<String> grams = nGrams("$"+term+"$", 3);
        for(String gram: grams) {
	        int id = (this.revFeatureMap.containsKey(gram))?this.revFeatureMap.get(gram):-1;
          if(countVector.containsKey(id) && id!=-1) {
            countVector.put(id, countVector.get(id)+1.0);
          }
          else if(id!=-1){
            countVector.put(id, 1.0);
          }
        }
      }
      List<Integer> ids = new ArrayList<Integer>(countVector.keySet());
      Collections.sort(ids);

      if(ids.size()>0) {
        p.printf("%s ", label);
        for(int feature: ids) {
            p.printf("%d:%.2f ", feature, countVector.get(feature));
        }
        p.printf("\n");
      }
    }
    br.close();

    p.close();
    fos.close();
  }
  
  public void svmFormatDSSM(String dataFile, String svmDataFile) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile), "UTF-8"));

    FileOutputStream fos = new FileOutputStream(svmDataFile);
    PrintStream p = new PrintStream(fos);

    String line = "";
    while((line = br.readLine())!=null) {
      String[] field = line.split("\t");
      String query = field[0].trim();
      String partition = field[1].trim();
      String label = "";

      
      if(LABEL_TYPE!="string")
        label = (Double.parseDouble(field[2].trim()))>=0.2?"1":"0";
      else
        label = (field[2].trim().equals("NonOffensive"))?"0":"1";
      String[] terms = field[0].toLowerCase().trim().split(" ");
      Map<Integer, Double> countVector = new HashMap<Integer, Double>();
      for(String term: terms) {
        List<String> grams = nGrams("$"+term+"$", 3);
        for(String gram: grams) {
	        int id = (this.revFeatureMap.containsKey(gram))?this.revFeatureMap.get(gram):-1;
          if(countVector.containsKey(id) && id!=-1) {
            countVector.put(id, countVector.get(id)+1.0);
          }
          else if(id!=-1){
            countVector.put(id, 1.0);
          }
        }
      }
      List<Integer> ids = new ArrayList<Integer>(countVector.keySet());
      Collections.sort(ids);

      if(ids.size()>0) {
        p.printf("%s %s ", partition, label);
        for(int i=3; i< field.length; i++) {
          p.printf("%d:%s ", i-3, field[i]);
        }
        for(int feature: ids) {
            p.printf("%d:%.2f ", feature+field.length-2, countVector.get(feature));
        }
        p.printf("\n");
      }
    }
    br.close();

    p.close();
    fos.close();
  }

  public static void main(String[] args) throws IOException {
    FormatData data = new FormatData();
    String operation = System.getProperty("operation");
    if(operation==null || (!operation.trim().equals("extractFeatures") && !operation.trim().equals("svmFormat"))) {
        System.out.printf("\n\nUsage: sh run.sh -Doperation=extractFeatures io.FormatData <input-data> <feature-map-output>\n\nOR");
        System.out.printf("\n\nUsage: sh run.sh -Doperation=svmFormat io.FormatData <input-feature-map> <input-data> <svm-data-output>\n\n");
        System.exit(0);
    }
    if(operation.trim().equals("extractFeatures")) {
      if(args.length!=2) {
        System.out.printf("\n\nUsage: sh run.sh -Doperation=extractFeatures io.FormatData <input-data> <feature-map-output>\n\n");
        System.exit(0);
      }

      data.loadVocab(args[0]);
      data.wordHashFeatures();
      data.writeFeatureMap(args[1]);
      System.out.printf("Feature Map is outputted to: %s\n", args[1]);
    }
    else if(operation.equals("svmFormat")) {
      if(args.length!=3) {
        System.out.printf("\n\nUsage: sh run.sh -Doperation=svmFormat io.FormatData <input-feature-map> <input-data> <svm-data-output>\n\n");
        System.exit(0);
      }
      data.loadFeatures(args[0]);
      data.svmFormatDSSM(args[1], args[2]);
      System.out.printf("The data is converted to SVM format and stored in: %s\n", args[2]);
    }

  }

}
