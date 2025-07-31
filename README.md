# Sistema de Gestão para Padaria

Sistema em Java/Maven para controle de vendas fiadas, cadastro de clientes/fornecedores, produtos e geração de relatórios financeiros mensais. Desenvolvido para substituir o controle manual e modernizar a gestão de pequenas padarias.

## Funcionalidades
- Gestão de clientes e fornecedores
- Controle de produtos e estoque
- Vendas à vista e fiadas
- Contas a receber/pagar
- Relatórios mensais


## Setup Rápido
1. Clone o repositório
2. Importe no IntelliJ como projeto Maven
3. Rode a classe App.java

## Padrões de Código
- Use as interfaces IEntity, IEntityService, ICSVReadable
- Siga o template fornecido
- Use CSVUtil para operações de arquivo
- Use InputUtil para entrada do usuário

Implemente seu módulo seguindo os exemplos em /templates/