package it.web.routex.utility.Decorator.DecoratorChange;

import java.util.ArrayList;
import java.util.List;

public abstract class Decorator implements Component
{
    protected Component component;

    public Decorator(Component component)
    {
        this.component = component;
    }
    @Override
    public List<String> getChanges(List<String> changes)
    {
        return component.getChanges(changes);
    }
}
