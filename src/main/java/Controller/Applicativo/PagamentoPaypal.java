package Controller.Applicativo;


import java.util.List;

public class PagamentoPaypal extends RegistrazionePagamentoController
{
    @Override
    public int registra_pagamento()
    {
        System.out.println("Il pagamento l'ho fatto con Paypal");
        return 1;
    }

    @Override
    public void gestisciPagamento(double totale, List<String> codiciBig, String city) throws Exception
    {
        System.out.println("Da implementare");
    }



}
