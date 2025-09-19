package org.example.CheckBackupDBMain;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
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
        //String backupDir = "C:\\Users\\Leonid Pintea\\Documents\\CheckBackupDB_TEST";
        String backupDir = "k:\\backup\\NSP\\backupsNSP"; //Nissa PRD

        if(!hasMoreThanTwoBackups(backupDir)){
            //Daca avem mai putin de doua backup-uri vom intrerupe procesul
            logger.info("Nu este necesar sa stergem backup-ul vechi. Trebuie sa existe minim doua backup-uri in sistem!");
            return;
        }else{
            File[] folderContent = new File(backupDir).listFiles();//stocam fisierele din folder
            for(File f : folderContent) {
                if(f.isFile()){
                    if(findDiffDaysFromToday(f) > 6){
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

    public static boolean hasMoreThanTwoBackups(String backupDir){
        //Functie creata pentru a ajuta la decizia stergerii de backup. Daca sunt mai putin de doua backup-uri pe storage, nu le vom sterge chiar daca sunt mai vechi
        File[] folderContent = new File(backupDir).listFiles();//stocam toate filele
        Set<String> dateFileAccumulator = new HashSet<>(); //vom stoca distinct datele din numele fiecarui fisier
        for(int i=0; i < folderContent.length; i++) {
            if(folderContent[i].isFile()){
                String[] datePart = folderContent[i].getName().split("\\."); //luam al 3-lea string din array care va fi data
                dateFileAccumulator.add(datePart[2]); //adaugam in set
            }
        }
        //Daca setul va avea doua elemente, inseamna ca avem doar doua backup-uri si va trebui sa luam o decizie in functie de asta. E important sa nu lasam programul sa stearga toate backup-urile vechi pentru ca vom ramane fara backup daca nu respectam conditia de 'minim 2'.
        return dateFileAccumulator.size() >= 2 ? true : false; //daca sunt mai mult de doua backupuri putem continua procesul de clean
    }

    //de inlocuit in cazurile repetate din cod
    private static String extractDateFromFile(File f) {
        String[] parts = f.getName().split("\\.");
        if (parts.length < 3) return null;
        return parts[2];
    }
}