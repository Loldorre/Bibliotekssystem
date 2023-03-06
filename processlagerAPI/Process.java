package processlagerAPI;
import databasAPI.Bok;
import databasAPI.Konto;

public class Process implements IProcess {
    public Process() {
    }

    @Override
    public int kollaTillgänglighetBibID(int bibID) {
        return 0;
    }

    @Override
    public Bok[] kollaTillgänglighetTitel(String titel) {
        return new Bok[0];
    }

    @Override
    public Bok[] kollaTillgänglighetISBN(int ISBN) {
        return new Bok[0];
    }

    @Override
    public Bok[] kollaTillgänglighetTitelForfattare(String titel, String forfattare) {
        return new Bok[0];
    }

    @Override
    public Bok[] kollaTillgänglighetForfattare(String forfattare) {
        return new Bok[0];
    }

    @Override
    public int kollaMedlemsstatus(int kontoId) {
        return 0;
    }

    @Override
    public int tempAvstängning(int kontoId, int antalDagar) {
        return 0;
    }

    @Override
    public int svartlistaMedlem(long personNr) {
        return 0;
    }

    @Override
    public Konto regKonto(String fnamn, String enamn, long personNr, String roll) {
        return null;
    }

    @Override
    public int avslutaKonto(int kontoId) {
        return 0;
    }

    @Override
    public int registreraLån(int kontoId, int bibID) {
        return 0;
    }

    @Override
    public int återlämnaBok(int kontoId, int bibID) {
        return 0;
    }

    @Override
    public int kollaMedlemsstatus(int kontoID, int avstängningsDagar) {
        return 0;
    }
}
