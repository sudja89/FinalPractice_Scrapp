import java.util.*;

/**
 * @author Daniè
 */

public class Main {
    private static webPage wP = null;
    static ArrayList<webPage> listWP = null;
    private static int index;

    public static void main(String[] args) {
        listWP = new ArrayList<>();
        menu();
    }

    /**
     * If listWP is empty, shows part of the menu
     */
    public static void displayMenu() {
        System.out.println("Menu");
        if (listWP.size() != 0) {
            System.out.println("    1. Insertar dominio");
            System.out.println("    2. Listar dominio");
            System.out.println("    3. Eliminar dominio");
            System.out.println("    4. Analizar dominio");
            System.out.println("    5. Analizar todos");
            System.out.println("    0. Salir");
        } else {
            System.out.println("    1. Insertar dominio");
            System.out.println("    0. Salir");
        }
    }

    public static void menu() {
        displayMenu();
        int key = InputManager.leerNumeroEnRango("Meter opción", 0, 5);
        switch (key) {
            case 0 -> {
                System.out.println("Fin del programa");
                System.exit(0);
            }
            case 1 -> insertDomain();
            case 2 -> listDomain();
            case 3 -> deleteDomain();
            case 4 -> analyzeDomain();
            case 5 -> analyzeAll();
        }
        menu();
    }

    /**
     * Scrapper all elements and displays the status
     */
    private static void analyzeAll() {
            for (int i = 0; i < listWP.size(); i++) {
                if (listWP.get(i).getState() != 1 && listWP.get(i).getState() != 2) {
                    Summary.inicio();
                    HiloAll hAll = new HiloAll();
                    hAll.start();
                    Summary.fin();
                    Summary.createFile();
                }
            }
        System.out.println("Fin de analizar todo");
        menu();
    }

    /**
     * pass to menuScrapper()
     */
    private static void analyzeDomain() {
        listDomain();
        setIndex((InputManager.leerNumeroEnRango(
                "Selecciona un domino para gestionar", 1, listWP.size()) - 1));
        Scrapp.menuScrapper();
    }

    /**
     * Delete a selected item
     */
    private static void deleteDomain() {
        listDomain();
        int num = InputManager.leerNumeroEnRango("Selecciona un dominio", 1, listWP.size());
        String confirmation = InputManager.leerCadena("¿Estás seguro que quieres borrar? (SI/NO)");
        if (confirmation.equalsIgnoreCase("si")) listWP.remove(num - 1);
    }

    /**
     * Print saved items
     */
    static void listDomain() {
        for (int i = 0; i < listWP.size(); i++) {
            System.out.println((i + 1) + " => " + listWP.get(i).toString());
        }
    }

    /**
     * Add an element
     */
    private static void insertDomain() {
        boolean valid = false;
        String confirmation;
        try {
            do {
                String name = InputManager.leerCadena("Nombre del dominio");
                String url = InputManager.leerCadena(
                        "Dominio web (Ejemplo => https://www.google.com/)").trim();

                if (url.contains("www") || url.contains("http")) {
                    wP = new webPage(name, url, 0);
                    listWP.add(wP);
                    System.out.println("Dominio agregado correctamente");
                } else {
                    System.out.println("\nURL no válida");
                }

                confirmation = InputManager.leerCadena("¿Quiéres añadir otro dominio? (SI/NO)");
                if (confirmation.equalsIgnoreCase("no")) valid = true;

            } while (!valid);
        } catch (Exception e) {
            System.out.println("Error => " + e.getMessage());
            menu();
        }
    }

    /**
     * get and set Index
     */
    public static int getIndex() {
        return index;
    }

    public static void setIndex(int index) {
        Main.index = index;
    }
}


