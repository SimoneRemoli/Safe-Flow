package Model.Domain;

public enum Ruolo
{
    WORKER(1),
    ADMIN(2),
    TRAVELER(3);

    private final int id;

    private Ruolo(int id)
    {
        this.id = id;
    }
    public static Ruolo fromint(int id)
    {
        for(Ruolo a:values())
        {
            if(a.getId()==id)
            {
                return a;
            }
        }
        return null;
    }
    public int getId() {
        return id;
    }
}