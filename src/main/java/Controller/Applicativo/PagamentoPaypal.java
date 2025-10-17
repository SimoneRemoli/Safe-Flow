package Controller.Applicativo;



public class PagamentoPaypal extends RegistrazionePagamentoController
{
    @Override
    public int registra_pagamento()
    {
        System.out.println("Il pagamento l'ho fatto con Paypal");
        return 1;
    }

    @Override
    public void salva_pagamento()
    {
        System.out.println("Il pagamento lo devo salvare");

    }
    @Override
    public void gestisciPagamento(double totale) throws Exception
    {
        System.out.println("Da implementare");
    }



}
