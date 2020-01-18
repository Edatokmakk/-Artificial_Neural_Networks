/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package odev2;

//import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import net.sourceforge.jFuzzyLogic.rule.Rule;
/**
 *
 * @author Eda
 */
public class Odev2  {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, URISyntaxException {
        Scanner in = new Scanner(System.in);
        System.out.println("Data.txt Olusturulsun mu? e/h");
        String cevap = in.next();
        if(cevap.contains("E")||cevap.contains("e"))
        {
            Random r1;
            r1=new Random();
            ArrayList<String> liste=new ArrayList<String>();
            FileWriter file_w = null;
            
            BufferedWriter buffered_w = null;
            
            for (int i=0;i<1000;i++){
                
                
                System.out.print("Dogalgaz Kullanimi(0-100):");
                
                int dogalgaz_kullanimi=r1.nextInt(100);
                
                System.out.print("Mevsim(Ilkbahar=1 Yaz=2 Sonbahar=3 Kis=4):");
                
                Map<Integer, String> mevsm = new HashMap<>();
                
                mevsm.put(1, "0 0");
                mevsm.put(2, "0 1");
                mevsm.put(3, "1 0");
                mevsm.put(4, "1 1");
                
                Map<String, String> havakirlilik = new HashMap<String, String>();
                 havakirlilik.put("temiz", "0 0 1");
                 havakirlilik.put("normal", "0 1 0");
                 havakirlilik.put("kirli", "1 0 0");
                
                int mevsim=r1.nextInt(4)+1;
                
                System.out.print("Nufus (0-100):");
                
                int nufus=r1.nextInt(100)+1;
                Hava_Kirlilik_Orani hava=new Hava_Kirlilik_Orani(dogalgaz_kullanimi,mevsim,nufus);
                String s=dogalgaz_kullanimi+" "+nufus+" "+mevsm.get(mevsim)+" ";
                String ss="";
                double cont=0.0;
                for(Rule r: hava.getModel().getFunctionBlock("Hava_Kirlilik_Orani").getFuzzyRuleBlock("kurallarblok1").getRules()){
                    
                    if(cont<r.getDegreeOfSupport()){
                        ss=r.toString().split("hava_kirlilik_oran")[1].split(" ")[2];
                    }
                    
                }
                s+=havakirlilik.get(ss);
                System.out.println();
                System.out.println(s +" "+ss);
                liste.add(s);
            }
            try {

                            file_w = new FileWriter("Data.txt");
                            
                            buffered_w = new BufferedWriter(file_w);
                            for(int j=0;j<liste.size();j++){
                                buffered_w.write(liste.get(j)+"\n");
                            }



                    } catch (IOException e) {

                            e.printStackTrace();

                    } finally {

                            try {

                                    if (buffered_w != null)
                                            buffered_w.close();

                                    if (file_w != null)
                                            file_w.close();

                            } catch (IOException ex) {

                                    ex.printStackTrace();

                            }

                    }
            
        }
        double error=0.0001;
        int arakatmanNoron=15;
        double momentum=0.8;
        double ogrenmeKatsayisi=0.2;       
        int epoch=3500;
        int sec=0;
        Odev2_Ysa ysa=null;
        
        do
        {
            
            System.out.println("1. Egitim Ve Test");
            System.out.println("2. Tekli Test");
            System.out.println("3. Cikis");
            System.out.println("=>");
            sec=in.nextInt();
            switch(sec){
                case 1:
                    System.out.println("Ara Katman Noron Sayisi:");
                    arakatmanNoron=in.nextInt();
                    System.out.println("Momentum:");
                    momentum=in.nextDouble();
                    System.out.println("Ogrenme Katsayisi:");
                    ogrenmeKatsayisi=in.nextDouble();
                    System.out.println("Min Hata: ");
                    error=in.nextDouble();
                    System.out.println("Epoch");
                    epoch=in.nextInt();
                    ysa=new Odev2_Ysa(arakatmanNoron,momentum,ogrenmeKatsayisi,error,epoch);
                    ysa.egit();
                    System.out.println("Egitimden elde edilen minimum hata: "+ysa.egitimHata());
                    System.out.println("Testte elde edilen minimum hata: "+ysa.test());
                    break;
                case 2:
                    if(ysa==null) 
                    {
                        
                        System.out.println("Egitme Basarisiz");
                        System.in.read();
                        break;
                    }
                    double[] inputs=new double[8];
                    System.out.println("dogalgaz_kullanimi");
                    inputs[0]=in.nextDouble();
                    System.out.println("mevsim");
                    inputs[1]=in.nextDouble();
                    System.out.println("nufus");
                    inputs[2]=in.nextDouble();
                    
                    
                    System.out.println("Agirilik:");
                    inputs[3]=in.nextDouble();
                    System.out.println("Hizlanma:");
                    inputs[4]=in.nextDouble();
                    System.out.println("Model:");
                    inputs[5]=in.nextDouble();
                    System.out.println("Ulke 0:");
                    inputs[6]=in.nextDouble();
                    System.out.println("Ulke 1:");
                    inputs[7]=in.nextDouble();
                    System.out.println("Yakit: "+ysa.tekTest(inputs));
                    System.in.read();
                    break;
            }
            
        
        }
        while(sec!=3);
        // TODO code application logic here
    }
    
}
