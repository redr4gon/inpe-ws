# Vivenas back

#### Instalação e acesso do vivenas-back pela primeira vez:

1. Faça uma clonagem ou checkout do vivenas-back

2. Instale um banco de dados MySQL

3. Crie um banco de dados chamado vivenas, usando a porta padrão 3306.

4. Vá até o arquivo "src/main/resources/applications.properties" e altere a linha "spring.jpa.hibernate.ddl-auto=update"  
   para "spring.jpa.hibernate.ddl-auto=create". Mude de volta para update após o banco de dados ser criado.

5. Inicie o projeto na própria IDE que estiver usando (Java application) ou user o seguinte comando: "mvn clean spring-boot:run"

6. Crie um usuário do tipo usando a seguinte linha de comando no mysql: 
  "insert into user (id, deleted, password, username) values (1, 0, '$2a$04$EqqYqs/KL44fc1evfoduJ.KVBqCfuuZnQ9LrsdgvuzRSeEdlhcVVS', 'admin')"

7. A partir desse momento é possível fazer o login no backend usando o usuário "admin" e a senha "admin"

8. Use o postman para acessar o back colocando as seguintes configurações:
   No endereço, utilize a seguinte URL: http://localhost:8081/oauth/token - método POST
   Na aba Authorization, coloque Basic Auth com username: vclient, password: vsecret
   Pule para a aba Body, coloque o tipo de body: x-www-form-urlencoded e os parametros key/value abaixo:
          username/admin
          password/admin
          grant_type/password

9. Execute a requisição. A resposta será um "access_token" que será utilizado para acessar qualquer setor do sistema que o usuário tenha acesso.

10. A requisição do postman pode ser feita a qualquer momento depois disso. A criação do banco e configurações de projeto
    só necessitam ser realizados uma única vez.
