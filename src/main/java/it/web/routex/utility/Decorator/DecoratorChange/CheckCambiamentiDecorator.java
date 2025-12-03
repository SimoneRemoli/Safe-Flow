package it.web.routex.utility.Decorator.DecoratorChange;

import java.util.ArrayList;
import java.util.List;

public class CheckCambiamentiDecorator extends Decorator
{
    public CheckCambiamentiDecorator(Component component)
    {
        super(component);
    }
    public List<String> getChanges(ArrayList<String> sequenzeChange)
    {
        List<String> array = super.getChanges(sequenzeChange);
        if(array.isEmpty())
            array.add("No");
        return array;
    }
}
