<!-- Poderíamos fazer algumas validações no próprio html, como o required que faz com que apareça aquela caixinha de "please fill out this form" 
     e o maxlenght que define uma quantidade máxima de caracteres, mas dessa forma o formulário não emitiria aviso. O usuário apenas seria impedido de submetê-lo
     então fizemos as validações no java -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CRUD Cidades</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>

    <div class="container-fluid">
        <div class="jumbotron mt-5">
            <h1>GERENCIAMENTO DE CIDADES</h1>
            <P>UM CRUD PARA CRIAR, ALTERAR, EXCLUIR E LISTAR CIDADES</P>
        </div>

        <!-- Apresentando mensagens na tela (depois de POST) -->
            <#if cidadeAtual??>
            <!-- Ajustando para a página exibir as mensagens de erro. caso existam.
                 Adicionando o atributo novalidate para evitar que o formulário use as mensagens padrão do HTML
                 E precisamos adicionar também a classe needs-validation que é do bootstrap. Ela especifica que esse formulário possui validações-->
                <form action="/alterar" method="POST" class="needs-validation" novalidate>
                <input type="hidden" name="nomeAtual" value="${(cidadeAtual.nome)!}" />
                <input type="hidden" name="estadoAtual" value="${(cidadeAtual.estado)!}" />

            <#else>
                <form action="/criar" method="POST" class="needs-validation" novalidate>

            </#if>
            <div class="form-group">
                <label for="nome">Cidade:</label>

            <!-- Recuperando valores digitados -->
            <!-- Then adiciona a classe is invalid, caso nome exista na variável memoria. Isso é usado pelo bootstrap para marcar a caixa de texto em vermelho
                 indicando que existe um erro. -->

            <!-- nomeInformado é uma expressão considional que exibe o valor digitado pela usuária e para ela, caso ele exista.-->
                <input value="${(cidadeAtual.nome)!}${nomeInformado!}" name="nome" type="text" class="form-control ${(nome??)?then('is-invalid', '')}" 
                placeholder="Informe o nome da cidade" id="nome">

            <!-- Criando elementos para exibir as mensagens de erro. Eles estão usando a formatação de erro definida pelo bootstrap (invalid-feedback).
                 Dentro do elemento apresentamos a mensagem de erro acessando o atributo definido no controller, nesse caso nome, usando a sintaxe do freemarker $...
                 + o nome do atributo que colocamos na variável memoria - nome - e oo marcador do freemarker -! -->
                <div class="invalid-feedback">
                    ${nome!}
                </div>
            </div>

            <div class="form-group">
                <label for="estado">Estado:</label>

            <!-- Recuperando valores digitados -->
                <input value="${(cidadeAtual.estado)!}${estadoInformado!}" name="estado" type="text" class="form-control ${(estado??)?then('is-invalid', '')}" 
                placeholder="Informe o estado ao qual a cidade pertence" 
                id="estado">

                <div class="invalid-feedback">
                    ${estado!}
                </div>
            </div>

        <#if cidadeAtual??>
            <button type="submit" class="btn btn-warning">CONCLUIR ALTERAÇÃO</button>
        <#else>
            <button type="submit" class="btn btn-primary">CRIAR</button>
        </#if>

        </form>

        <table class="table table-striped table-hover mt-5">
            <thead class="thead-dark">
                <tr>
                    <th>Nome</th>
                    <th>Estado</th>
                    <th>Ações</th>
                </tr>
            </thead>

            <tbody>
                <#list listaCidades as cidade>
                    <tr>
                        <td>${cidade.nome}</td>
                        <td>${cidade.estado}</td>
                        <td>
                            <div class="d-flex d-justify-content-center">
                                <a href="/preparaAlterar?nome=${cidade.nome}&estado=${cidade.estado}" class="btn btn-warning mr-3">ALTERAR</a>
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