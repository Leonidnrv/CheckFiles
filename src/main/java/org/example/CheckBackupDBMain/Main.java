package org.example.CheckBackupDBMain;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Main {
    public static void main(String[] args) {
        String backupDir = "C:\\Users\\Leonid Pintea\\Documents\\CheckBackupDB_TEST";
        File[] folderContent = new File(backupDir).listFiles();//stocam fisierele din folder
        for(File f : folderContent) {
            if(f.isFile()){
                if(findDiffDaysFromToday(f) > 14){
                    System.out.println(f.getName());
                    //deleteFile(f);
                }
            }
        }
    }

    public static long findDiffDaysFromToday(File currentFile){//Aceasta functie primeste un fisier si ne spune cat de vechi este (in zile) fata de ziua curenta
        //ToDo de verificat daca fisierul respecta sau are formatul de data in el, altfel arunca exceptie
        //seteaza formatul de data yyyyMMdd
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        //taie numele de la caracterul _ pana la . si stocheaza doar "20250801"
        //String datePart = currentFile.getName().substring(currentFile.getName().indexOf("_") + 1, currentFile.getName().indexOf("."));
        //LocalDate fileDate = LocalDate.parse(datePart, formatter); // fileDate = data fisierului in tip obiect LocalDate
        LocalDate todayDate = LocalDate.now(); // todayDate = data curenta in tip obiect LocalDate. Aceste doua variabile sunt create pentru a fi folosite in ChronoUnit


        String[] datePartFinal = currentFile.getName().split("\\.");
        LocalDate fileDateFinal = LocalDate.parse(datePartFinal[2], formatter);

        //return ChronoUnit.DAYS.between(fileDate, todayDate);
        return ChronoUnit.DAYS.between(fileDateFinal, todayDate);
    }


    public static boolean deleteFile(File file){
        //Stergem fisierul daca exista
        if (file.exists()) {
            System.out.println("Stergem fisierul " + file.getName());
            return file.delete();
        }
        return false;
    }

    public static boolean checkFileName(File file){
        //ToDo verificam daca fisierul are formatul prestabilit. Pentur fiecare client probabil vom avea fisiere cu formate diferite
        return true;
    }
}
