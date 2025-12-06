package it.web.routex.utility.decorator.decoratorchange;

import java.util.List;

public abstract class Decorator implements Component
{
    protected Component component;

    protected Decorator(Component component)
    {
        this.component = component;
    }
    @Override
    public List<String> getChanges(List<String> changes)
    {
        return component.getChanges(changes);
    }
}
