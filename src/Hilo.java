public class Hilo extends Thread {

    @Override
    public void run() {

        try {
            Main.listWP.get(Main.getIndex()).setState(1);
            Scrapp.downloadSourceCode();
            Scrapp.showLinks(Scrapp.emptyFile());
            Main.listWP.get(Main.getIndex()).setState(2);
        } catch (Exception e) {
            System.out.println("Error => " + e.getMessage());
        }
    }
}
