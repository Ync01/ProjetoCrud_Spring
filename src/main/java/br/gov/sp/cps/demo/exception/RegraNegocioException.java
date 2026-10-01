package br.gov.sp.cps.demo.exception;

// Lançada quando uma regra do sistema é violada (email repetido, placa já no pátio, etc.)
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
