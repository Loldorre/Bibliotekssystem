package DatabasAPI;

public class Bok {
    private int ISBN;
    private String titel;
    private String forfattare;
    private int antal;
    private int utgivningsar;

    public Bok (int ISBN, String titel, String forfattare, int antal, int utgivningsar) {
        this.ISBN = ISBN;
        this.titel = titel;
        this.forfattare = forfattare;
        this.antal = antal;
        this.utgivningsar = utgivningsar;
    }

    public int getISBN() {
        return ISBN;
    }

    public void setISBN(int ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getForfattare() {
        return forfattare;
    }

    public void setForfattare(String forfattare) {
        this.forfattare = forfattare;
    }

    public int getAntal() {
        return antal;
    }

    public void setAntal(int antal) {
        this.antal = antal;
    }

    public int getUtgivningsar() {
        return utgivningsar;
    }

    public void setUtgivningsar(int utgivningsar) {
        this.utgivningsar = utgivningsar;
    }
}
