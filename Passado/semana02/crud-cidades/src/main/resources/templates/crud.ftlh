<!-- criando a página com bootstrap -->
<!-- representa o view -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CRUD Cidades</title>

    <!-- tornando o bootstrap disponível na nossa aplicação usando CDN - rede de servidores que dá acesso aos arquivos de configuração do bootstrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>

    <!-- usando componente jumbotron para criar rótulo superior que identifica a aplicação -->
    <div class="container-fluid">
        <div class="jumbotron mt-5">
            <h1>GERENCIAMENTO DE CIDADES</h1>
            <P>UM CRUD PARA CRIAR, ALTERAR, EXCLUIR E LISTAR CIDADES</P>
        </div>

        <!-- criando formulário para entrada de dados-->
        <form >
            <div class="form-group">
                <label for="nome">Cidade:</label>
                <input type="text" class="form-control" placeholder="Informe o nome da cidade" id="nome">
            </div>
            <div class="form-group">
                <label for="estado">Estado:</label>
                <input type="text" class="form-control" placeholder="Informe o estado ao qual a cidade pertence" id="estado">
            </div>
            <button type="submit" class="btn btn-primary">CRIAR</button>
        </form>

        <!-- adicionando tabela que lista cidades -->
        <table class="table table-striped table-hover mt-5">
            <thead class="thead-dark">
                <tr>
                    <th>Nome</th>
                    <th>Estado</th>
                    <th>Ações</th>
                </tr>
            </thead>

            <!-- adicionando diretivas (uma instrução do freemarker que faz algo no código) do freemarker. Funciona de forma similar ao forEach em Java -->
            <!-- Alterando a tabela para que as linhas sejam criadas dinamicamente de acordo com uma lista de cidades -->
            <tbody>
                <#list listaCidades as cidade>
                    <tr>

                        <!-- acessando a variável cidade que representa um objeto do tipo cidade -->
                        <!-- a classe cidade criada anteriormente tem os atributos cidade e estado que são acessados pelos gets que retornam o valor atual para cada cidade-->
                        <!-- todo o código dentro de list é repetido para cada elemento detro de listaCidades -->
                        <td>${cidade.nome}</td>
                        <td>${cidade.estado}</td>
                        <td>
                            <div class="d-flex d-justify-content-center">
                                <a class="btn btn-warning mr-3">ALTERNAR</a>
                                <a class="btn btn-danger mr-3">EXCLUIR</a>
                            </div>
                        </td>
                    </tr>
                </#list>
            </tbody>
        </table>
    </div>
    
</body>
</html>