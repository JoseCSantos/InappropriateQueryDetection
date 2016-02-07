package eval;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.List;
import java.util.LinkedList;

public class Eval {
  List<Integer> gold;
  List<Integer> pred;

  public Eval() {
    this.gold = new LinkedList<Integer>();
    this.pred = new LinkedList<Integer>();
  }
	public List<Integer> loadLabels(String file) throws IOException {
    List<Integer> labels = new LinkedList<Integer>();
    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
    String line = "";
    while((line = br.readLine())!=null) {
      String[] field = line.split(" ");
      int label = Integer.parseInt(field[0].trim());
      labels.add(label);
    }
    br.close();
    return labels;
  }

  public void printStats() {
    assert(this.gold.size()==this.pred.size()):System.out.printf("Labels do not correspond\n");
    double tp=0.0, fp=0.0, tn=0.0, fn=0.0;
    for(int i=0; i<this.gold.size(); i++) {
      if(this.gold.get(i)==1) {
        if(this.pred.get(i)==1) {
          tp++;
        }
        else {
          fn++;
        }
      }
      else {
        if(this.pred.get(i)==1) {
          fp++;
        }
        else {
          tn++;
        }
      }
    }

    double prec = ((tp+fp)>0)?tp/(tp+fp):0.0;
    double rec = ((tp+fn)>0)?tp/(tp+fn):0.0;
    double f1 = ((prec+rec)>0)?(2.0*prec*rec)/(prec+rec):0.0;
//    System.out.printf("tp = %d\n 
    System.out.printf("\nEval\n----\n\nprec\t= %.4f\nrec\t= %.4f\nf1\t= %.4f\n", prec, rec, f1);

    System.out.printf("\nTotal Pos = %.1f\nTotal Neg = %.1f\n", (tp+fn), (fp+tn));
  }

  public static void main(String[] args) throws IOException {

    if(args.length <2) {
      System.out.printf("\n\nUsage: sh run.sh eval.Eval <svm-test-gold> <svm-pred-output>\n\n");
      System.exit(0);
    }

    Eval eval = new Eval();
    eval.gold = eval.loadLabels(args[0]);
    System.out.printf("[log] Labels loaded.\n");
    eval.pred = eval.loadLabels(args[1]);
    System.out.printf("[log] Predictions loaded.\n");


    eval.printStats();
  }
}
