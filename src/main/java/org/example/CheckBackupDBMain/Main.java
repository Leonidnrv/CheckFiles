package org.example.CheckBackupDBMain;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
    private static int countNumberOfFiles = 0;
    private static Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {
        try{
            FileHandler fileHandler = new FileHandler("backupCleaner_job.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        }catch(IOException ex){
            System.err.println("Error in log_job_backupCleaner.log " + ex.getMessage());
            ex.printStackTrace();
        }
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
        LocalDate todayDate = LocalDate.now(); // todayDate = data curenta in tip obiect LocalDate. Aceste doua variabile sunt create pentru a fi folosite in ChronoUnit
        String[] datePart = currentFile.getName().split("\\."); //luam al 3-lea string din array care va fi data
        LocalDate fileDate = LocalDate.parse(datePart[2], formatter);

        return ChronoUnit.DAYS.between(fileDate, todayDate);
    }


    public static boolean deleteFile(File file){
        //Stergem fisierul daca exista
        if (file.exists()) {
            logger.info("Stergem fisierul " + file.getName());
            return file.delete();
        }
        return false;
    }
}
