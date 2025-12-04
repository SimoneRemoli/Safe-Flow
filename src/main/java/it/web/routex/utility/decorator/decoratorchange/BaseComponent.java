package it.web.routex.utility.decorator.decoratorchange;

import java.util.List;

public class BaseComponent implements Component
{
    @Override
    public List<String> getChanges(List<String> sequenzeChange)
    {
        return sequenzeChange;
    }
}
