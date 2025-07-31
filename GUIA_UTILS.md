# Guia de Utilitários - Sistema de Gestão de Padaria

Este guia explica como utilizar as classes utilitárias disponíveis no projeto.
Evite usar Scanner diretamente.

---
## CSVUtil.java
### Finalidade: Leitura e escrita de arquivos `.csv` com separador `;`.

### Leitura de arquivo CSV:
``` 
List<String[]> linhas = CSVUtil.lerArquivoCSV("produtos.csv");
for (String[] linha : linhas) {
        System.out.println(Arrays.toString(linha));
        }
```

### Escrita de arquivo CSV:
``` 
List<String[]> dados = new ArrayList<>();
dados.add(new String[]{"1", "Pão", "10", "100", "1.50", "30"});
String[] cabecalho = {"código", "descrição", "estoque mínimo", "estoque atual", "valor de custo", "percentual de lucro"};

CSVUtil.escreverArquivoCSV("produtos.csv", dados, cabecalho);
```


### IOExceptionHandler.java
Trata exceções de I/O (como arquivos inexistentes) de forma centralizada, imprimindo a mensagem exigida: "Erro de I/O."
```
try {
    // alguma operação que pode lançar IOException
} catch (IOException e) {
    IOExceptionHandler.handle(e);
}
```

### InputUtil.java
Finalidade: Padroniza leitura do console com Scanner, evitando repetição de código.

Ler inteiro:
```
int codigo = InputUtil.lerInt("Digite o código do produto");
```

Ler decimal:
```
double valor = InputUtil.lerDouble("Digite o valor de custo");
```

Ler texto:
```
String nome = InputUtil.lerString("Digite o nome do cliente");
```


### IRelatorio
Exemplo de uso 
```
List<String[]> dados = relatorio.processarDados();
CSVUtil.escreverArquivoCSV("relatorio.csv", dados, relatorio.getCabecalho());
```

### MenuUtil
Exemplo de uso
```
List<String> opcoes = List.of(
    "Cadastrar Produto",
    "Listar Produtos",
    "Voltar ao Menu Principal"
);

int opcao = MenuUtil.exibirMenu("Menu de Produtos", opcoes);

switch (opcao) {
    case 1 -> cadastrarProduto();
    case 2 -> listarProdutos();
    case 3 -> return;
    default -> System.out.println("Opção inválida");
}
```


