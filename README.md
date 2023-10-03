# Sistema-Lista-de-Tarefas

Funcionalidades:

# Lista de Tarefas
É a página principal do sistema.
Deve listar todos os registros mantidos na tabela "Tarefas" (um abaixo do outro).
Todos os campos, exceto "Ordem de apresentação", devem ser apresentados.
As tarefas devem ser apresentadas ordenadas pelo campo "Ordem de apresentação".
A tarefa que tiver o "Custo" maior ou igual a R$1.000,00 deverá ser apresentada de forma diferente (por exemplo: a linha inteira com o fundo amarelo).
Ao lado direito de cada registro devem ser apresentados dois botões (preferencialmente ícones), uma para executar a função de "Editar" e outro para a função de "Excluir" registro.
Ao final da listagem deve existir um botão para executar a função de "Incluir" registro.

# Excluir
A função deve excluir o registro da Tarefa escolhida.
É necessário apresentar uma mensagem de confirmação (Sim/Não) para a realização da exclusão.

# Editar
A função deve editar o registro da Tarefa escolhida.
Só é possível alterar o "Nome da Tarefa", o "Custo" e a "Data Limite".
É necessário verificar se o novo nome da tarefa já existe na base de dados. Se já existir, a alteração não poderá ser feita.
A implementação pode ser feita de uma das duas formas abaixo (escolha uma):
A edição é feita diretamente na tela principal (Lista de Tarefas), onde os três campos são habilitados para edição.

# Incluir
A função deve permitir a inclusão de uma nova tarefa.
Apenas os campos "Nome da Tarefa", "Custo" e "Data Limite" são informados obrigatoriamente pelo usuário.
Os demais campos são gerados automaticamente pelo sistema.
O registro recém-criado será o último na ordem de apresentação.
Não pode haver duas tarefas com o mesmo nome.

# Reordenação das tarefas
A função deve permitir que o usuário possa alterar a ordem de apresentação de uma tarefa.
Em cada linha (registro) deve ter dois botões, uma para "subir" a tarefa na ordem de apresentação e outro para "descer". Obviamente a primeira tarefa não poderá "subir" e nem a última poderá "descer".
