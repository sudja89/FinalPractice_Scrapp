public class webPage {
    private String name;
    private String url;
    private int state;
    public webPage(String name, String url, int state) {
        this.name = name;
        this.url = url;
        this.state = state;
    }
    public String getName() {
        return name;
    }
    public String getUrl() {
        return url;
    }
    public int getState() {
        return state;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "webPage{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", state='" + stateText() + '\'' +
                '}';
    }


    public String stateText(){
        String cadena = null;
        if (getState()==0) cadena="Sin analizar";
        if (getState()==1) cadena="En analisis";
        if (getState()==2) cadena="Analizado";
        return cadena;
    }
}
