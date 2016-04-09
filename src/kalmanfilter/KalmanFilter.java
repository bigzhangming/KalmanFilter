/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kalmanfilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author Iraklis Athanasakis
 */
public class KalmanFilter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        File file = new File("measurements.txt");
       try {
           Scanner sc = new Scanner(file);
           //Ο πίνακας x[200] θα περιέχει τις εκτιμήσεις του φίλτρου Kalman.
           double[] x = new double[200]; 
           //Ο πίνακας real[200] περιέχει τις πραγματικές τιμές από το αρχείο measurements.txt.
           double[] real = new double [200];
           //Ο πίνακας z[200] περιέχει τις μετρηθείσες τιμές από το αρχείο measurements.txt.
           double[] z = new double [200];
           //Ανάγνωση τιμών από το αρχείο και αποθήκευση στους πίνακες.
           for (int i=0; i < 200; i++){
               real[i]= Double.parseDouble(sc.next());
               z[i]= Double.parseDouble(sc.next());
           }
           
           //Είσοδος από το χρήστη των φ, σ1, σ2
           Scanner user_input = new Scanner(System.in);
     
           System.out.print("Δώσε την παράμετυρο φ: ");
           double fi = Double.parseDouble(user_input.next());  // φ
     
           System.out.print("Δώσε την παράμετρο σ1: ");
           double sigma1 = Double.parseDouble(user_input.next()); // σ1
            
           System.out.print("Δώσε την παράμετρο σ2: ");
           double sigma2 = Double.parseDouble(user_input.next()); // σ2
     
           user_input.close(); 
           sc.close();    
           
            
           // Υλοποίηση Kalman Filter
           // Από τα δεδομένα της άσκησης έχουμε Η=1 και φ=0.9
           double Q = Math.pow(sigma1, 2);  // Υπολογισμός Q ως σ1^2
           double R = Math.pow(sigma2, 2);  // Υπολογισμός R ως σ2^2
           
           // Αρχικοποίηση
           double x0 = 111.544624; // αρχική τιμή x0 ο μέσος όρος των 2 πρώτων μετρηθεισών τιμών
           double p0 = 1;          // αρχική τιμή του P
           
           System.out.println("\t K \t\t\t P");
           // Prediction phase για i=0           
           double xPr = fi * x0;  // εξίσωση 9.12 
           double pPr = fi * p0 * fi + Q;   // εξίσωση 9.13
           
           // Correction phase για i=0
           double K = pPr / (pPr + R);  // Kalman Gain - εξίσωση 9.14
           System.out.print(K+"\t");
           x[0] = xPr + K * (z[0] - xPr);  // εξίσωση 9.15
           double P = (1 - K) * pPr;  // State noise P - εξίσωση 9.16
           System.out.println(P);
           
           // Επαναληπτική διαδικασία για τις υπόλοιπες μετρήσεις
           for (int i = 1; i < 200; i++) {
               // Predition phase
               xPr = fi * x[i-1];  
               pPr = fi * P * fi + Q;
               
               // Correction phase
               K = pPr / (pPr + R); 
               System.out.print(K+"\t");
               x[i] = xPr + K * (z[i] - xPr);
               P = (1 - K) * pPr;  
               System.out.println(P);
           }
          
            //Μέση ευκλείδεια απόσταση
            double mean_a = 0.0;
            double mean_b = 0.0;
            for (int i=0; i < 200; i++){
                //Υπολογίζεται η ευκλείδεια απόσταση μεταξύ πραγματικής τιμής και μετρηθείσας τιμής και προστίθεται στη μεταβλητή mean_a
                mean_a+=Math.abs(real[i]-z[i]);
                //Υπολογίζεται η ευκλείδεια απόσταση μεταξύ πραγματικής τιμής και τιμής εξόδου φίλτρου Kalman και προστίθεται στη μεταβλητή mean_b 
                mean_b+=Math.abs(real[i]-x[i]);
            }
            //Οι τιμές mean_a και mean_b διαιρούνται με το 200 προκειμένου να υπολογιστεί η μέση ευκλείδεια απόσταση.
            mean_a=mean_a/200;
            mean_b=mean_b/200;
            
            System.out.println("Η μέση απόκλιση μεταξύ πραγματικών τιμών και μετρούμενων τιμών είναι: " +mean_a);
            System.out.println("Η μέση απόκλιση μεταξύ πραγματικών τιμών και τιμών εξόδου του φίλτρου Kalman είναι: "+mean_b);
       }
       catch (FileNotFoundException e) {
           System.err.format("File does not exist\n");
       }
    }
}
