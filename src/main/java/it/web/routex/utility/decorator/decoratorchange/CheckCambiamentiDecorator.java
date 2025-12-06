package it.web.routex.utility.decorator.decoratorchange;
import java.util.List;

public class CheckCambiamentiDecorator extends Decorator
{
    public CheckCambiamentiDecorator(Component component)
    {
        super(component);
    }
    @Override
    public List<String> getChanges(List<String> sequenzeChange)
    {
        List<String> array = super.getChanges(sequenzeChange);
        if(array.isEmpty())
            array.add("No");
        return array;
    }
}
