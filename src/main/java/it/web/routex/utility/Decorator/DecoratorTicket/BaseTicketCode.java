package it.web.routex.utility.Decorator.DecoratorTicket;

import java.util.UUID;

public class BaseTicketCode implements Component {

    @Override
    public String genera() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}

