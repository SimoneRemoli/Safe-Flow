package it.web.routex.utility.Decorator.DecoratorChange;

import java.util.ArrayList;
import java.util.List;

public class BaseComponent implements Component
{
    @Override
    public List<String> getChanges(List<String> sequenzeChange)
    {
        return sequenzeChange;
    }
}
