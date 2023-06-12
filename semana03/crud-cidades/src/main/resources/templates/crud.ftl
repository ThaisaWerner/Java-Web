<!-- O VSCODE não reconhece a extensão .ftlh, então instalamos um plugin para o freemarker e mudamos a extensão do arquivo para .ftl
     que é o que o freemarker reconhece como padrão, mas o Spring Boot não aceita essa extensão, apenas a .ftlh, então usamos o application.properties 
     para resolver essa questão. Por meio desse arquivo que é possível fazermos ajustes conforme o que estamos precisando no momento,
     já que o spring boot é altamente flexível. O código presente lá faz com que o spring boot reconheça a extensão .ftl, casando o spring com o
     plugin do freemarker
     Para outras IDE's verificar como é o suporte -->
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

        <!-- Operação alterar -->

            <#if cidadeAtual??>
                <form action="/alterar" method="POST">
                <input type="hidden" name="nomeAtual" value="${(cidadeAtual.nome)!}" />
                <input type="hidden" name="estadoAtual" value="${(cidadeAtual.estado)!}" />

            <#else>
             <!-- Alterando a página web para que ela envie dados da cidade (Model) para para o método criar na classe CidadeController (Controller) -->
             <!-- A propriedade action indica para onde os dados devem ser enviados. A propriedade method indica o método http que deve ser usado
                para o envio dos dados -->
                <form action="/criar" method="POST">

            </#if>

        <!-- Adicionamos name nas tags de input. Elas são usadas para inserir o nome da cidade e estado que o usuário deseja.
             Essa propriedade, name, faz a relação dos valores digitados na tag input com os atributos da classe Cidade (Model)
             Para que esse mapeamento seja feito de forma automática, é importante que os valores (nomes) usados na propriedade name sejam iguais dos 
             atributos na classe Cidade. -->
        
            <div class="form-group">
                <label for="nome">Cidade:</label>
                <input value="${(cidadeAtual.nome)!}" name="nome" type="text" class="form-control" placeholder="Informe o nome da cidade" id="nome">
            </div>

            <div class="form-group">
                <label for="estado">Estado:</label>
                <input value="${(cidadeAtual.estado)!}" name="estado" type="text" class="form-control" placeholder="Informe o estado ao qual a cidade pertence" id="estado">
            </div>

        <#if cidadeAtual??>
            <button type="submit" class="btn btn-warning">CONCLUIR ALTERAÇÃO</button>
        <#else>
            <button type="submit" class="btn btn-primary">CRIAR</button>
        </#if>

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
                                <!-- Operação prepara alterar -->
                                <a href="/preparaAlterar?nome=${cidade.nome}&estado=${cidade.estado}" class="btn btn-warning mr-3">ALTERAR</a>

                                <!-- Operação excluir.
                                     Alterando o botão excluir para que ele envie uma solicitação para o controller.
                                     /excluir é a URL que receberá a solicitação.
                                     Enviamos também o nome e o estado como parâmetros na requisição.
                                     No momento, ele não faz validação, ou seja, não verifica se a cidade digitada existe para então excluí-la
                                     então vai aceitar valores vaxios e cidades repetidas, por exemplo. -->
                                <a href="/excluir?nome=${cidade.nome}&estado=${cidade.estado}" class="btn btn-danger mr-3">EXCLUIR</a>
                            </div>
                        </td>
                    </tr>
                </#list>
            </tbody>
        </table>
    </div>
    
</body>
</html>