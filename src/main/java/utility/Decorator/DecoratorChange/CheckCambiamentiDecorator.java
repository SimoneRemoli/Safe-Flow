package utility.Decorator.DecoratorChange;

import java.util.ArrayList;

public class CheckCambiamentiDecorator extends Decorator
{
    public CheckCambiamentiDecorator(Component component)
    {
        super(component);
    }
    public ArrayList<String> getChanges(ArrayList<String> sequenzeChange)
    {
        ArrayList<String> array = super.getChanges(sequenzeChange);
        if(array.isEmpty())
            array.add("No");
        return array;
    }
}
