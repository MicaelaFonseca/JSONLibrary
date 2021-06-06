## Biblioteca de Geração de JSON

Este projeto consiste em desenvolver uma biblioteca para produzir dados no formato JSON. 

### Fase 1 - Modelo de dados

Nesta fase criou-se um conjunto de classes cujos objetos representam uma estrutura de dados JSON em memória. O JSON textual é obtido através do varrimento desta estrutura.

As classes do modelo oferecem uma forma de varrimento baseada em visitantes (padrão de desenho Visitor). Desta forma, é possível serializar a estrutura para texto e efetuar pesquisas, como por exemplo:
  Obter todas as strings;
  Obter todos os objetos que têm determinadas propriedades.
  
### Fase 2 - Inferência por reflexão

Nesta fase desenvolveu-se uma funcionalidade para instanciar automaticamente o modelo (desenvolvido na Fase 1) por reflexão de quaisquer objetos de valor (data class).

Como forma de adaptar a instanciação, foram definidas anotações para permitir excluir propriedades da instanciação e utilizar outros identificadores.

### Fase 3 - Visualizador

Desenvolveu-se um visualizador de JSON capaz de mostrar os objetos do modelo de dados (Fase 1).

O visualizador é uma pequena aplicação gráfica com as seguintes funcionalidades: 

A janela do editor é inicializada e fornece um objeto do modelo de dados JSON, sendo a árvore construída a partir desse;

Ao selecionar um elemento na árvore, o JSON serializado é mostrado (tendo o elemento selecionado como raiz);

É possível efetuar uma pesquisa por texto, sendo que todos os elementos JSON que contenham esse texto são marcados na árvore (highlight).

### Fase 4 - Plugins para visualizador
Após ter uma versão funcional do visualizador (Fase 3),  o mesmo é extendido mediante plugins para personalizar a visualização e acrescentar ações.

Atenção: A inclusão/exclusão de um plugin em caso algum exigirá alterar o código base, dado que isto será feito no ficheiro de configuração.

#### Apresentação
Um dos tipos de plugin permite personalizar a apresentação da árvore, sendo possível:

Atribuir ícones aos nós (de acordo com um critério);

Fornecer o texto dos nós (com base no conteúdo dos objetos);

Excluir a criação de alguns nós;

#### Ações
O outro tipo de plugin diz respeito a ações que podem ser executadas pelo utilizador (botões ou menus de contexto). Permite estender o editor base com ações que atuam sobre o modelo JSON que é mostrado na árvore. Desta forma, os plugins têm um acesso fácil ao JSON, incluindo o nó que está selecionado.

Exemplos de ações:

Editar as propriedades de um objeto JSON;

Escrever o JSON visível na área da direita para um ficheiro.
