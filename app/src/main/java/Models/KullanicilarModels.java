package Models;

public class KullanicilarModels {
    private String dogumtarih, egitim, hakkimda, isim, resim;
    private Boolean state;


    public KullanicilarModels() {

    }

    public KullanicilarModels(String dogumtarih, String egitim, String hakkimda, String isim, String resim, Boolean state) {
        this.dogumtarih = dogumtarih;
        this.egitim = egitim;
        this.hakkimda = hakkimda;
        this.isim = isim;
        this.resim = resim;
        this.state = state;

    }

    public String getDogumtarih() {
        return dogumtarih;
    }

    public void setDogumtarih(String dogumtarih) {
        this.dogumtarih = dogumtarih;
    }

    public String getEgitim() {
        return egitim;
    }

    public void setEgitim(String egitim) {
        this.egitim = egitim;
    }

    public String getHakkimda() {
        return hakkimda;
    }

    public void setHakkimda(String hakkimda) {
        this.hakkimda = hakkimda;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getResim() {
        return resim;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "KullanicilarModels{" +
                "dogumtarih='" + dogumtarih + '\'' +
                ", egitim='" + egitim + '\'' +
                ", hakkimda='" + hakkimda + '\'' +
                ", isim='" + isim + '\'' +
                ", resim='" + resim + '\'' +
                ", state=" + state +
                '}';
    }
}
