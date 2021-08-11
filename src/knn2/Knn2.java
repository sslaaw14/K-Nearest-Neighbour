/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knn2;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author ASUS
 */
public class Knn2 {
    static int[][] setData = new int[75][3];
    static int[][] trainingData = new int[60][3];
    static int[][] testingData = new int[15][3];
    static int[] trainingLabel = new int[60];
    static int[] testingLabel = new int[15];
    static int urutTraining = 0;
    static int urutTesting = 0;
    static double[] allDistance = new double[1000];
    static double[][] hasilsort= new double[60][2];
    static int k;
    static int[] hasilknn = new int[15];
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        Scanner inputk = new Scanner(System.in);
        //System.out.println("Input Nilai K :");
        //k = inputk.nextInt();
         try 
        { 
            FileInputStream fstream_school = new FileInputStream("F:\\Kuliah\\Semester 5\\Machine Learning\\ruspini.txt"); 
            DataInputStream data_input = new DataInputStream(fstream_school); 
            BufferedReader buffer = new BufferedReader(new InputStreamReader(data_input)); 
            String str_line; 
            
            int baris=0;
            int kolom=0;
            String[] split = null;
            while ((str_line = buffer.readLine()) != null) 
            { 
                split = str_line.split(",");
                for (kolom=0; kolom<split.length;kolom++) {
                    int data = Integer.parseInt(split[kolom]);
                    setData[baris][kolom] = data;
                }
                baris++;
            }     
        } catch(IOException ex) {}
        
        /*for(int i=0;i<75;i++){
            for(int j=0;j<3;j++){
                System.out.println(setData[i][j]);
            } 
        }*/
        splitData(0,20,16);
        splitData(20,37,14); 
        splitData(37,60,18); 
        splitData(60,75,12);  
        
        /*for(int i=0;i<60;i++){
            for(int j=0;j<3;j++){
                System.out.println(trainingData[i][j]);
            }
        }*/
        
        /*for(int i=0;i<15;i++){
            for(int j=0;j<3;j++){
                System.out.println(testingData[i][j]);
            }
        }*/
        
        for(int i=0;i<testingData.length;i++){
            for(int j=0;j<trainingData.length;j++){
                allDistance[j] = getDistance(i,j,trainingData,testingData);
                System.out.println(" " + j + "\t" + allDistance[j] + "\n");
            }
            System.out.println("-------------------------------------------");
            hasilsort = sortedData(allDistance);
            hasilknn[i] = classification(k, hasilsort, trainingLabel);
        }
        
        double hasil = getError(testingLabel, hasilknn);
        
        System.out.println("Error = " +hasil+ "%");
    }
    
    public static void splitData(int nilaiAwal, int jumlah, int Data) {
        int count=0;

        while(nilaiAwal < jumlah) {
            if(count<Data) {
                trainingData[urutTraining] = setData[nilaiAwal];
                count++;
                urutTraining++;
            }else {
                testingData[urutTesting] = setData[nilaiAwal];
                urutTesting++;
            }
            nilaiAwal++;
        } 
    }
    
    public static void ReadTrainingLabel() {
        for(int baris=0;baris<trainingData.length;baris++) {
            for(int kolom=0;kolom<trainingData[baris].length;kolom++) {
                trainingLabel[baris] = trainingData[baris][2];
            }
        }
    
    }
    public static void ReadTestingLabel() {
        for(int baris=0;baris<testingData.length;baris++) {
            for(int kolom=0;kolom<testingData[baris].length;kolom++) {
                testingLabel[baris] = testingData[baris][2];
            }
        }
    }
    
    public static double getDistance(int i, int j, int[][] trainingdata, int[][] testingdata){
        double totalDistance = 0;
        double jarakx = 0;
        double jaraky = 0;
        
        jarakx = Math.pow(testingdata[i][0] - trainingdata[j][0], 2);
        jaraky = Math.pow(testingdata[i][1] - trainingdata[j][1], 2);
        
        totalDistance = Math.sqrt(jarakx+jaraky);
        
        return totalDistance;
    }
    
    
    public static double[][] sortedData(double[] distance) {
        double[][] sortdata = new double[60][2];
        for(int i=0;i<trainingData.length;i++){
            sortdata[i][0] = i;
            sortdata[i][1] = allDistance[i];
        }
        
        for(int i=0;i<trainingData.length;i++){
            for(int j=0;j<trainingData.length-1;j++) {
                if(sortdata[j][1] > sortdata[j+1][1]) {
                    double[] pindah = sortdata[j];
                    sortdata[j] = sortdata[j+1];
                    sortdata[j+1] = pindah;
                }
            }
        }
        
        System.out.println("Jarak\t\tIndex");
        for(int i=0;i<sortdata.length;i++) {
            System.out.println(sortdata[i][1]+"\t"+sortdata[i][0]);
        }
        return sortdata;
    }
    
    public static int classification(int k, double[][] sorting, int[] traininglabel) {
        int label1=0;
        int label2=0;
        int label3=0;
        int label4=0;
        int hasil=0;
        
        for(int i=0;i<k;i++) {
            if (traininglabel[(int) sorting[i][1]] == 1) {
                label1++;
            }else if(traininglabel[(int) sorting[i][1]] == 2) {
                label2++;
            }else if(traininglabel[(int) sorting[i][1]] == 3){
                label3++;
            }else if(traininglabel[(int) sorting[i][1]] == 4) {
                label4++;
            }
        }
        
        if(label1 >= label2 && label1 >= label3 && label1 >= label4) {
            hasil=1;
        }else if(label2>=label1&&label2>=label3&&label2>=label4){
            hasil=2;
        }else if(label3>=label1&&label3>=label2&&label3>=label4){
            hasil = 3;
        }else if(label4>=label1&&label4>=label2&&label4>=label3){
            hasil=4;
        }
        
        return hasil;
    }
    
    public static double getError(int[] testinglabel,int[] knnresult) {
        int error=0;
        double hasil = 0;
        int total = testinglabel.length;
        for(int i=0;i<total;i++){
            if(testinglabel[i]!=knnresult[i]) {
                error++;
            }
        }
        
        hasil = ((double) error) * 100 / ((double) total);
        return hasil;
    }
}
