# Issue tracker: GitHub

Tarefas e especificações ficam no GitHub Issues.
Repositório: Ync01/ProjetoCrud_Spring.
Usar gh CLI a partir deste checkout e conferir o remoto.

- Criar: gh issue create --title "..." --body-file <arquivo>
- Ler: gh issue view <numero> --comments
- Listar: gh issue list --state open
- Comentar: gh issue comment <numero> --body-file <arquivo>
- Etiquetar: gh issue edit <numero> --add-label "<etiqueta>"
- Remover etiqueta: gh issue edit <numero> --remove-label "<etiqueta>"
- Fechar: gh issue close <numero>

Para textos extensos, usar arquivo com quebras de linha reais.

Publicar no tracker significa criar uma issue.
Buscar um ticket significa ler a issue e seus comentários.
PRs as a request surface: no.

Em trabalhos com vários tickets, registrar relações entre eles
e usar dependências nativas do GitHub quando disponíveis.
Caso indisponíveis, registrar "Blocked by: #<numero>" no corpo.
Trabalhar primeiro nos tickets cujos bloqueadores estejam fechados.
