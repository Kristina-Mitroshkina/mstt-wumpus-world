package jade.wumpus.world;

public class Percept {
    private boolean stench;
    private boolean breeze;
    private boolean glitter;
    private boolean scream;

    public Percept() {
    }

    public void wumpusIsClose() {
        stench = true;
    }

    public void stenchIsClose() {
        stench = true;
    }

    public void heardScream() {
        scream = true;
    }

    public void breezeIsClose() {
        breeze = true;
    }

    public void pitIsClose() {
        breeze = true;
    }

    public void goldIsClose() {
        glitter = true;
    }

    public String perceptionToMessage() {
        StringBuilder builder = new StringBuilder();

        if (stench)
            builder.append("It stinks in here, you know. ");

        if (scream)
            builder.append("Seems like I hear a scream. ");

        if (breeze)
            builder.append("A cold breeze is blowing hard. ");

        if (glitter)
            builder.append("Something is shining. ");

        return builder.toString();
    }


}
