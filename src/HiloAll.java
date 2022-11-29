
public class HiloAll extends Thread {

    @Override
    public void run() {

        boolean max = false;
        for (int i = 0; i < Main.listWP.size(); i++) {
            try {
                Main.listWP.get(i).setState(1);
                ScrappAll.downloadSourceCodeAll();
                if (!max) {
                    ScrappAll.showLinksAll(ScrappAll.emptyFileAll());
                    max = true;
                }
                Main.listWP.get(i).setState(2);
            } catch (Exception e) {
            }
        }
    }

}
