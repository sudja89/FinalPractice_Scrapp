import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Summary {

    private static Date date = new Date();
    static String usuario = System.getProperty("user.name");
    static String propiedades = String.valueOf(System.getProperties());
    static long inicio;
    static long fin;
    static double tiempo;

    public static void createFile() {
        File summary;
        FileWriter fr = null;

        try {
            summary = new File("Summary.log");
            fr = new FileWriter(summary, true); //NO machaca

            fr.write("\n\n___---```SUMMARY```---___");
            fr.write("\nFecha => " + date);
            fr.write("\nUsuario => " + usuario);
            fr.write("\nPropiedades del sistema => " + propiedades);
            fr.write("\nLongitud del array => " + Main.listWP.size());
            fr.write("\nDominio => " + Main.listWP.get(Main.getIndex()).toString());
            fr.write("\nTiempo de scrapper => " + time(inicio(), fin()) + " segundos");
            fr.write("\nFecha => " + date);
            fr.write("\nUsuario => " + usuario);
            fr.write("\nFin");
        } catch (IOException e) {
            System.out.println("Error => " + e.getMessage());
        } finally {
            try {
                if (fr != null) fr.close();
            } catch (Exception e2) {
                System.out.println("Error => " + e2.getMessage());
            }
        }
    }

    public static long inicio() {
        inicio = System.currentTimeMillis();
        return inicio;
    }

    public static long fin() {
        fin = System.currentTimeMillis();
        return fin;
    }

    public static double time(long inicio, long fin) {

        tiempo = (double) ((fin - inicio) /*/ 1000*/);
        return tiempo;
    }
}



