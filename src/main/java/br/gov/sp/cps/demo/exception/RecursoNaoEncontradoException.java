package br.gov.sp.cps.demo.exception;

// Lançada quando um registro (veículo, usuário) não existe no banco
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
