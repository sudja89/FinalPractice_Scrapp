import java.io.*;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScrappAll {
    /**
     * download source code for the selected domain
     */
    static void downloadSourceCodeAll() {
        BufferedReader br = null;
        FileWriter fr = null;
        File fichero;
        URL url;
        String line;
        try {
            for (int i = 0; i < Main.listWP.size(); i++) {
                System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
                url = new URL(Main.listWP.get(i).getUrl());
                fichero = new File(Main.listWP.get(i).getName() + ".dat");
                br = new BufferedReader(new InputStreamReader(url.openStream()));
                fr = new FileWriter("D:\\aaaCole2\\ZZZIntelliJ\\FinalPractice_Scrapp\\fileDirectory\\" + fichero);
                while ((line = br.readLine()) != null) {
                    fr.write(line);
                }
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
    static String emptyFileAll() {
        try {
            int i = 0;
            while (i < Main.listWP.size()) {
                File fichero = new File(Main.listWP.get(i).getName());
                BufferedReader lector = new BufferedReader(new FileReader(
                        "D:\\aaaCole2\\ZZZIntelliJ\\FinalPractice_Scrapp\\fileDirectory\\" + fichero + ".dat"));
                StringBuilder cadena = new StringBuilder();
                String line;
                while ((line = lector.readLine()) != null) {
                    cadena.append(line);
                }
                lector.close();
                return cadena.toString();
            }
        } catch (Exception e) {
            System.out.println("Error => " + e.getMessage());
        }
        return null;
    }

    /**
     * method used to extract url
     * deletes the pattern returned by emptyFile, creates a webPage object and adds it to listWP
     */
    static void showLinksAll(String content) {
        try {
            int i = 0;
            boolean valid;
            do {
                Pattern pattern = Pattern.compile("<a href=\"(.+?)\"", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(content);
                int cont = 2;
                while (matcher.find()) {
                    //filtrar SI tiene www o http y DISTINTO al objeto que ya contiene
                    //***NO entra en bucle infinito
                    if (matcher.group(1).contains("www") || matcher.group(1).contains("http")
                            && !matcher.group(1).equals(Main.listWP.get(i).getUrl())) {
                        webPage wP = new webPage(Main.listWP.get(i).getName() + cont, matcher.group(1), 0);
                        Main.listWP.add(wP);
                        cont++;
                    }
                }
                i++;
                valid = true;
            } while (!valid);
        } catch (Exception e) {
            System.out.println("Error => " + e.getMessage());
        }
    }
}
