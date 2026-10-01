package br.gov.sp.cps.demo.util;

public class CpfValidador {

    // Recebe o CPF só com números (11 dígitos)
    public static boolean valido(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}")) {
            return false;
        }

        // 111.111.111-11 passa na conta, mas não é um CPF válido
        if (cpf.chars().distinct().count() == 1) {
            return false;
        }

        int digito1 = calcularDigito(cpf, 9);
        int digito2 = calcularDigito(cpf, 10);

        return digito1 == cpf.charAt(9) - '0' && digito2 == cpf.charAt(10) - '0';
    }

    // Multiplica cada dígito por um peso decrescente (10..2 para o 1º dígito, 11..2 para o 2º),
    // soma tudo e usa o resto da divisão por 11: resto menor que 2 vira 0, senão 11 - resto.
    private static int calcularDigito(String cpf, int quantidade) {
        int soma = 0;
        for (int i = 0; i < quantidade; i++) {
            soma += (cpf.charAt(i) - '0') * (quantidade + 1 - i);
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }
}
