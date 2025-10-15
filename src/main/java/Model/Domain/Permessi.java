package Model.Domain;
//
///
public enum Permessi
{
    UTENTENORMALE(1),
    UTENTEDISABILE(2),
    GENERICO(3);

    private final int id;

    private Permessi(int id)
    {
        this.id = id;
    }
    public static Permessi fromint(int id)
    {
        for(Permessi a:values())
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
