package org.example.CheckBackupDBMain;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

public class Main {
    private static int countNumberOfFiles = 0;
    private static Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {
        String backupDir = "C:\\Users\\Leonid Pintea\\Documents\\CheckBackupDB_TEST";
        File[] folderContent = new File(backupDir).listFiles();//stocam fisierele din folder
        for(File f : folderContent) {
            if(f.isFile()){
                if(findDiffDaysFromToday(f) > 14){
                    System.out.println(f.getName());
                    countNumberOfFiles++;
                    //deleteFile(f);
                    logger.info("S-a sters fisierul " + f.getName());
                }
            }
        }
        if (countNumberOfFiles != 9) {
            logger.severe("Job-ul a esuat!");
        }else {
            logger.info("Succes!");
        }
        countNumberOfFiles = 0;
    }

    public static long findDiffDaysFromToday(File currentFile){//Aceasta functie primeste un fisier si ne spune cat de vechi este (in zile) fata de ziua curenta
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
            //System.out.println("Stergem fisierul " + file.getName());
            logger.info("Stergem fisierul " + file.getName());
            return file.delete();
        }
        return false;
    }
}
