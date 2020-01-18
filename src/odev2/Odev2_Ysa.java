/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package odev2;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;
/**
 *
 * @author Eda
 */
public class Odev2_Ysa {
    private static final File egitimdosya=new File(Odev2.class.getResource("Egitim.txt").getPath());
    private static final File testdosya=new File(Odev2.class.getResource("Test.txt").getPath());
    private static final File dosya=new File(Odev2.class.getResource("Data.txt").getPath());
    private double[] maksimumlar;
    private double[] minimumlar;
    private DataSet egitimVeriSeti;
    private DataSet testVeriSeti;
    private int araKatmanNoron;
    private MomentumBackpropagation bp;//a ile backpropagation
    private ArrayList<Integer> egitim=new ArrayList<Integer>();
    private ArrayList<Integer> test=new ArrayList<Integer>();
    
    public Odev2_Ysa(int araKatmanNoron,double momentum, double ogrenmeKatsayisi,double error, int epoch) throws FileNotFoundException, IOException
    {
        maksimumlar=new double[8];
        minimumlar=new double[8];
        for(int i=0;i<8;i++){
            minimumlar[i]=Double.MAX_VALUE;
            maksimumlar[i]=Double.MIN_VALUE;
            
        }
        
        dataOku();
        VeriSetiMaks(dosya);
        egitimVeriSeti=VeriSeti(egitim);
        testVeriSeti=VeriSeti(test);
        
        bp=new MomentumBackpropagation();
        bp.setMomentum(momentum);
        bp.setLearningRate(ogrenmeKatsayisi);
        bp.setMaxError(error);
        bp.setMaxIterations(epoch);
        this.araKatmanNoron=araKatmanNoron;
        
        
        
    }
    
    
    private void writeData(String name){
        FileWriter fw=null;
        BufferedWriter bw=null;
        try {

			

			//fw = new FileWriter("Data.txt",true);//append
                        fw = new FileWriter(name,true);
			bw = new BufferedWriter(fw);
                        if(name.contains("egitim")){
                            for(int i=0;i<egitim.size();i+=9)
                                bw.write(egitim.get(i+0)+" "+egitim.get(i+1)+" "+egitim.get(i+2)+" "+egitim.get(i+3)+" "+egitim.get(i+4)+" "+egitim.get(i+5)+" "+egitim.get(i+6)+" "+egitim.get(i+7)+" "+egitim.get(i+8)+"\n");
                        }else{
                            for(int i=0;i<test.size();i+=9)
                                bw.write(test.get(i+0)+" "+test.get(i+1)+" "+test.get(i+2)+" "+test.get(i+3)+" "+test.get(i+4)+" "+test.get(i+5)+" "+test.get(i+6)+" "+test.get(i+7)+" "+test.get(i+8)+"\n");
                        
                        }

			

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
    }
    private void dataOku() throws FileNotFoundException, IOException{
    try (BufferedReader br = new BufferedReader(new FileReader(dosya))) {
    String line;
    int i=0;
    while ((line = br.readLine()) != null) {
        if(i<6999){
            egitim.add(Integer.valueOf(line.split(" ")[0]));
            egitim.add(Integer.valueOf(line.split(" ")[1]));
            egitim.add(Integer.valueOf(line.split(" ")[2]));
            egitim.add(Integer.valueOf(line.split(" ")[3]));
            egitim.add(Integer.valueOf(line.split(" ")[4]));
            egitim.add(Integer.valueOf(line.split(" ")[5]));
            egitim.add(Integer.valueOf(line.split(" ")[6]));
            egitim.add(Integer.valueOf(line.split(" ")[7]));
            egitim.add(Integer.valueOf(line.split(" ")[8]));
        }else{
            test.add(Integer.valueOf(line.split(" ")[0]));
            test.add(Integer.valueOf(line.split(" ")[1]));
            test.add(Integer.valueOf(line.split(" ")[2]));
            test.add(Integer.valueOf(line.split(" ")[3]));
            test.add(Integer.valueOf(line.split(" ")[4]));
            test.add(Integer.valueOf(line.split(" ")[5]));
            test.add(Integer.valueOf(line.split(" ")[6]));
            test.add(Integer.valueOf(line.split(" ")[7]));
            test.add(Integer.valueOf(line.split(" ")[8]));
        }
        i++;
        
    }
   
    }
    }
    public double egitimHata(){
        return bp.getTotalNetworkError();
    
    }
    private String Sonuc(double[] outputs){
        int index=0;
        double maks=outputs[0];
        if(outputs[1]>maks){
            maks=outputs[1];
            index=1;
        }
        if(outputs[2]>maks){
            maks=outputs[2];
            index=2;
        }
        if(index==0) return "Kotu";
        else if (index==1) return "Normal";
        else return "Iyi";
    }
    public String tekTest(double[] inputs){
        for(int i=0;i<8;i++){
            inputs[i]=min_max(maksimumlar[i], minimumlar[i], inputs[i]);//girilen deger 0-1 arasi oldi
            
        }
        NeuralNetwork sinirselAg=NeuralNetwork.createFromFile("ogrenenAg.nnet");
        sinirselAg.setInput(inputs);
        sinirselAg.calculate();
        return Sonuc(sinirselAg.getOutput());
        
    }
    public double test()
    {
        NeuralNetwork sinirselAg=NeuralNetwork.createFromFile("ogrenenAg.nnet");
        double toplamHata=0;
        for(DataSetRow dr:testVeriSeti){
            sinirselAg.setInput(dr.getInput());
            sinirselAg.calculate();
            toplamHata+=mse(dr.getDesiredOutput(), sinirselAg.getOutput());
            
        }
        return toplamHata/testVeriSeti.size();    
    }
    private double mse(double[] beklenen,double[] cikti){
        double toplamHata=0;
        for(int i=0;i<3;i++){
            toplamHata+=Math.pow(beklenen[i]-cikti[i], 2);
        }
        return toplamHata/3;
    }
    
    public void egit()
    {
        MultiLayerPerceptron sinirselAg=new MultiLayerPerceptron(TransferFunctionType.SIGMOID,8,araKatmanNoron,3);
        sinirselAg.setLearningRule(bp);
        sinirselAg.learn(egitimVeriSeti);
        sinirselAg.save("ogrenenAg.nnet");
        System.out.println("Egitim tamamlandi.");
    }
    
    private void VeriSetiMaks(File ds) throws FileNotFoundException{
        Scanner oku=new Scanner(ds);
        while(oku.hasNextDouble()){
            
            for(int i=0;i<8;i++){
                double d=oku.nextDouble();//double d
                if(d>maksimumlar[i])
                    maksimumlar[i]=d;
                if(d<minimumlar[i])
                    minimumlar[i]=d;
            }
            oku.nextDouble();oku.nextDouble();oku.nextDouble();
            
        }
        oku.close();
    
    }
    private double min_max(double maks,double min,double x){
        return (x-min)/(maks-min);
    }
    private DataSet VeriSeti(File ds) throws FileNotFoundException{
        Scanner oku=new Scanner(ds);
        DataSet veriset=new DataSet(8,3);
        //double[] inputs=new double[8];
        //double d;
        while(oku.hasNextDouble()){
            double[] inputs=new double[8];
            
            for(int i=0;i<8;i++){
                double d=oku.nextDouble();
                //d=oku.nextDouble();
                inputs[i]=min_max(maksimumlar[i], minimumlar[i], d);
                
            }
            veriset.addRow(new DataSetRow(inputs,new double[]{oku.nextDouble(),oku.nextDouble(),oku.nextDouble()}));
            
            
        }
        oku.close();
        return veriset;
}

    private DataSet VeriSeti(ArrayList<Integer> test) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
