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
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sourceforge.jFuzzyLogic.FIS;
/**
 *
 * @author Eda
 */
public class Hava_Kirlilik_Orani {
    private FIS fis;
    private double dogalgaz_kullanimi;
    private double mevsim;
    private double nufus;
    

    public Hava_Kirlilik_Orani(double dogalgaz_kullanimi, double mevsim, double nufus)throws URISyntaxException
    {
        this.dogalgaz_kullanimi=dogalgaz_kullanimi;
        this.mevsim=mevsim;
        this.nufus=nufus;
        //hata fırlattığınfan yakamak gerek
        File dosya=new File(getClass().getResource("Hava_Kirlilik_Orani.fcl").toURI());
        //2 . değişken eğer daha önce yüklenmişse onun üstüne yazmak için
        fis=FIS.load(dosya.getPath(),true);
        fis.setVariable("dogalgaz_kullanimi",dogalgaz_kullanimi );
        fis.setVariable("mevsim", mevsim);
        fis.setVariable("nufus", nufus);
        fis.evaluate();//hesaplandı  
    }

    
    @Override
    public String toString() {
        String cikti="Doğalgaz Kullanımı(0-100):"+dogalgaz_kullanimi+"\nMevsimler:(kis:1 ilkbahar:2 yaz:3 sonbahar:4)"+mevsim+"\n Nufus(0-100):"+nufus+"\nHava Kirlilik Oranı :"+fis.getVariable("hava_kirlilik_oran").getValue();
        return cikti;
    }
   public Hava_Kirlilik_Orani() throws URISyntaxException{
        File dosya=new File(getClass().getResource("Hava_Kirlilik_Orani.fcl").toURI());
        fis=FIS.load(dosya.getPath(),true);
    }
   FIS getModel() 
    {
        return fis;
    }
   
   public static void write() throws FileNotFoundException, IOException{
        BufferedWriter buffered_w = null;
        FileWriter file_w = null;
        
        Map<String, String> dogalgaz_kullanimi = new HashMap<String, String>();
        dogalgaz_kullanimi.put("az", "0 0");
        dogalgaz_kullanimi.put("orta", "0 1");
        dogalgaz_kullanimi.put("cok", "1 0");
        
        Map<String, String> mevsim = new HashMap<String, String>();
        mevsim.put("yaz", "0 0");
        mevsim.put("ilkbahar", "0 1");
        mevsim.put("sonbahar", "1 0");
        mevsim.put("kis", "1 1");
        
        Map<String, String> nufus = new HashMap<String, String>();
        nufus.put("az", "0 0");
        nufus.put("orta", "0 1");
        nufus.put("fazla", "1 0");
        
        Map<String, String> hava_k = new HashMap<String, String>();
        hava_k.put("temiz", "0 0 1");
        hava_k.put("normal", "0 1 0");
        hava_k.put("kirli", "1 0 0");
        
        
        String dogalgaz="", nuf="", mev="",hav="",write="";
        List<String> lines = new ArrayList<String>();
        List<String> wrt = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader("Data.txt"));
        String line = br.readLine();
        while (line != null)
        {
            dogalgaz=line.split(" ")[11].replace(")","");
            nuf=line.split(" ")[7].replace(")","");
            mev=line.split(" ")[3].replace(")","");
            hav=line.split(" ")[15];
            
            write=dogalgaz_kullanimi.get(dogalgaz)+" "+nufus.get(nuf)+" "+mevsim.get(mev)+" "+hava_k.get(hav)+"\n";
            wrt.add(write);
            line=br.readLine();
        }
        try {

			file_w = new FileWriter("Data4.txt");
			buffered_w = new BufferedWriter(file_w);
			for (int i=0;i<wrt.size();i++){
                            buffered_w.write(wrt.get(i));
                        }

			//System.out.println("Done");

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
}


