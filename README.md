# Reactive-Ecommerce
Projeto final Let's Code - Reactive Ecommerce

Controle de autenticação feito com keycloak
https://www.keycloak.org/getting-started/getting-started-docker

Abrir Swagger
http://localhost:8080/api-docs/

clicar em authorize
user-admin -> login: alice, senha: alice
user       -> login: bob, senha: bob

Exemplo para testes de método CRUD:
Post /dados_pessoais
{  
  "nome": "Tiago Aoki",
  "dataNascimento": "2022-03-10",
  "cpf": "12345678909"
}

Post /usuario
{ 
  "username": "tiago",
  "password": "123",
  "email": "tiago@gmail.com",
  "dadosPessoais": {
    "id": 27
  }
}

Post /pedido
{
  "statusPedido": "aberto",
  "data": "2022-03-10",
  "usuario": {"id": 28}
}

put /pedido/{pedidoId}/add/{produtoId}
pedidoId 30
produtoId 5

put /pedido/{pedidoId}/add/{produtoId}
pedidoId 30
produtoId 5

put /pedido/{pedidoId}/add/{produtoId}
pedidoId 30
produtoId 4

get /pedido
