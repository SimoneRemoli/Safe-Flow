package utility.Decorator.DecoratorChange;

import java.util.ArrayList;

public class BaseComponent implements Component
{
    @Override
    public ArrayList<String> getChanges(ArrayList<String> sequenzeChange)
    {
        return sequenzeChange;
    }
}
