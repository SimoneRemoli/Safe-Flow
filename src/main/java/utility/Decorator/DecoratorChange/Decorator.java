package utility.Decorator.DecoratorChange;

import java.util.ArrayList;

public abstract class Decorator implements Component
{
    protected Component component;

    public Decorator(Component component)
    {
        this.component = component;
    }
    @Override
    public ArrayList<String> getChanges(ArrayList<String> changes)
    {
        return component.getChanges(changes);
    }
}
