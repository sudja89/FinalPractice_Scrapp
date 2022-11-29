import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

public class Scrapp extends Main {
    //usado para obtener las url
    private static String content = null;
    //usado para palabras de busqueda
    static ArrayList<String> listSearch = new ArrayList<>();

    public static void displayMenuScrapper() {
        System.out.println("Menu Scrapper");
        System.out.println("    1. Ver en el navegador");
        System.out.println("    2. Ver estado");
        System.out.println("    3. Busqueda");
        System.out.println("    4. Scrappear");
        System.out.println("    5. Ver resumen");
        System.out.println("    0. Atrás");
    }

    public static void menuScrapper() {
        displayMenuScrapper();
        int key = InputManager.leerNumeroEnRango("Meter opción", 0, 5);
        switch (key) {
            case 0 -> menu();
            case 1 -> seeDomain();
            case 2 -> seeState();
            case 3 -> search();
            case 4 -> scrapper();
            case 5 -> seeSummary();//solo ver cuando estado analizado
        }
        menuScrapper();
    }

    private static void seeSummary() {
        ProcessBuilder pb;

        if (listWP.get(getIndex()).getState() == 2) {
            //listDomain();
            try {
                pb = new ProcessBuilder();
                pb.command("notepad.exe", "Summary.log");
                pb.start();
            } catch (Exception e) {
                System.out.println("Error => " + e.getMessage());
            }
        }
        System.out.println("Analizar primero");
    }

    /**
     * most important method download source code and search url inside
     */
    public static void scrapper() {
        //lazar con HILO y contar el tiempo transcurrido
        Summary.inicio();
        Hilo h = new Hilo();
        h.start();
        Summary.fin();
        Summary.createFile();
        System.out.println("Fin de analizar");
        menuScrapper();
    }

    /**
     * download source code for the selected domain
     */
    static void downloadSourceCode() {
        BufferedReader br = null;
        FileWriter fr = null;
        File fichero;
        URL url;
        String line;

        try {
            //***NECESITA ESTO no se pq
            System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
            //obtengo la url ***NECESITA TIPO URL
            url = new URL(listWP.get(getIndex()).getUrl());
            //creo un fichero con nombre
            fichero = new File(
                    listWP.get(getIndex()).getName() + ".dat");
            //leer codigoFuente de la url
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            //declaro donde quiero escribir
            fr = new FileWriter(
                    "D:\\aaaCole2\\ZZZIntelliJ\\FinalPractice_Scrapp\\fileDirectory\\" + fichero);

            while ((line = br.readLine()) != null) {
                fr.write(line);
            }
        } catch (Exception e) {
            System.out.println("Error => " + e.getMessage());
        } finally {
            try {
                if (br != null && fr != null) {
                    br.close();
                    fr.close();
                }
            } catch (Exception e) {
                System.out.println("Error => " + e.getMessage());
            }
        }
    }

    /**
     * method used to extract url
     * search the source code of the domain for matches to the <a href=""></a> pattern.
     */
    static String emptyFile() {
        try {
            File fichero = new File(listWP.get(getIndex()).getName());//creo un fichero con nombre
            BufferedReader lector = new BufferedReader(new FileReader(
                    "D:\\aaaCole2\\ZZZIntelliJ\\FinalPractice_Scrapp\\fileDirectory\\"
                            + fichero + ".dat"));
            StringBuilder cadena = new StringBuilder();
            String line;

            while ((line = lector.readLine()) != null) {
                cadena.append(line);
            }

            lector.close();
            content = cadena.toString();
            return content;
        } catch (Exception e) {
            System.out.println("Error => " + e.getMessage());
        }
        return null;
    }

    /**
     * method used to extract url
     * deletes the pattern returned by emptyFile, creates a webPage object and adds it to listWP
     */
    static void showLinks(String content) {
        try {
            Pattern pattern = Pattern.compile("<a href=\"(.+?)\"",
                    Pattern.CASE_INSENSITIVE);//***case.intensive quita <a href"">
            Matcher matcher = pattern.matcher(content);
            int cont = 2;
            while (matcher.find()) {
                //filtrar SI tiene www o http y DISTINTO al objeto que ya contiene
                if (matcher.group(1).contains("www") || matcher.group(1).contains("http") &&
                                !matcher.group(1).equals(listWP.get(getIndex()).getUrl())) {

                    webPage wP = new webPage(listWP.get(getIndex()).getName() + cont,
                            matcher.group(1),
                            0);

                    listWP.add(wP);
                    cont++;
                }
            }
        } catch (Exception e) {
            System.out.println("Error => " + e.getMessage());
        }
    }

    /**
     * add words to search and run match program
     */
    private static void search() {
        boolean valid = false;
        String confirmation;
        File fichero;
        ProcessBuilder pb;
        BufferedReader br = null;
        BufferedReader brError = null;

        try {
            if (listWP.get(getIndex()).getState() != 0) {
                do {
                    listSearch.add(InputManager.leerCadena("Meter palabra para busqueda").trim());
                    confirmation = InputManager.leerCadena("¿Quiéres añadir otra palabra? (SI/NO)");
                    if (confirmation.equalsIgnoreCase("no")) valid = true;
                } while (!valid);

                fichero = new File(listWP.get(getIndex()).getName());//creo un fichero con nombre
                pb = new ProcessBuilder();

                for (int i = 0; i < listSearch.size(); i++) {
                    //ruta del .class a ejecutar
                    pb.directory(new File(
                            "D:\\aaaCole2\\ZZZIntelliJ\\Practica2_parte1\\out\\production\\Practica2_parte1"));

                    //"programa", "clase", "args[0]=palabra a buscar", "args[1]=ruta de los archivos"
                    pb.command("java",
                            "Main",
                            listSearch.get(i),
                            "D:\\aaaCole2\\ZZZIntelliJ\\FinalPractice_Scrapp\\fileDirectory\\" + fichero + ".dat");

                    //inicia el proceso
                    Process salida = pb.start();

                    //NORMAL
                    //br recoge lo que sale en el proceso
                    br = new BufferedReader(new InputStreamReader(salida.getInputStream()));

                    String linea;
                    while ((linea = br.readLine()) != null) {
                        System.out.println(listSearch.get(i)+" => "+linea);//saca lo que lee
                    }


                    //FALLO
                    //brError recoge lo que sale en el proceso
                    brError = new BufferedReader(new InputStreamReader(salida.getErrorStream()));

                    String linea2;
                    while ((linea2 = brError.readLine()) != null) {
                        System.out.println(linea2);//saca lo que lee
                    }
                }
                menuScrapper();
            }
            System.out.println("Analizar primero.");
            menuScrapper();
        } catch (Exception e) {
            System.out.println("Error => " + e.getMessage());
        } finally {
            try {
                if (br != null && brError != null) {
                    br.close();
                    brError.close();
                }
            } catch (Exception e2) {
                System.out.println("Error => " + e2.getMessage());
            }
        }
    }

    /**
     * shows the domain status
     */
    static void seeState() {
        String cadena = null;
        for (int i = 0; i < listWP.size(); i++) {
            if (listWP.get(getIndex()).getState() == 0) cadena = "Sin analizar";
            if (listWP.get(getIndex()).getState() == 1) cadena = "En analisis";
            if (listWP.get(getIndex()).getState() == 2) cadena = "Analizado";
        }
        System.out.println(listWP.get(getIndex()).getName() + " => " + cadena);
    }

    /**
     * opens the browser and displays the domain
     */
    private static void seeDomain() {
        ProcessBuilder pb;
        try {
            pb = new ProcessBuilder();
            pb.command("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe", listWP.get(getIndex()).getUrl());
            pb.start();
        } catch (Exception e) {
            System.out.println("Error => " + e.getMessage());
        }
        menuScrapper();
    }
}
