# Kumulus Cadastro
Exemplo de CRUD com EJB 3.1 e JSF 2.2 (Primefaces 7.0, BootsFaces 1.4.1)

#### Arquitetura
Por se trartar de um exemplo que representa uma aplicação legada, optei por uma arquitetura Monolítica de 3 camadas MVC.
A injeção de dependência é feita principalmente pelo EJB.
Como servidor de aplicação utilizei o Wildfly 23.

#### Módulos
1. **kumulus-ejb** – Contém a lógica de negócio da aplicação, será utilizado pelos outros subprojetos. 
	- Dao – o pacote funciona como o pattern DAO e faz a comunicação direta com o banco de dados. Utilizando uma classe abstrata AbstractGenericDAO é possível criar de forma simples os DAOs de várias entidades.
	- Model – Onde ficam as entidades - Por ser tratar de uma implementação simples, as entidades representam diretamente as tabelas do Banco.
	- Services – Classes com transação que implementam o pattern Facade, para simplificar o tratamento das entidades em um só lugar. (EJB)
	- Producer - Classes que usam CDI para injetar dependências
	
2. **kumulus-web** - Contém os controllers e as páginas da aplicação.

#### Compilar e Executar

- Baixe o código, clonando o projeto ou baixando os fontes;
- No diretório kumuluscad, execute: mvn clean install -DskipTests (No final do desenvolvimento tive problemas na execução dos testes unitários fora do Eclpse)
- O ear será gerado na pasta target

#####WildFly 23
- Baixe o wildfly 23 em https://www.wildfly.org/downloads/
- Crie o diretório ..\wildfly-23.0.2.Final\modules\org\postgresql\main
- Copie postgresql-42.7.3.jar e module.xml para este diretório do projeto: kumuluscad\postgres-driver\jdbc\main
- Altere o arquivo ..\wildfly-23.0.2.Final\standalone\configuration\standalone.xml para conectar com o banco de dados:
	Na seção: <subsystem xmlns="urn:jboss:domain:datasources:6.0">, insira um novo datasource:
	<datasource jta="true" jndi-name="java:jboss/datasources/MyPostgresDS" pool-name="MyPostgresDS" enabled="true" use-java-context="true">
                    <connection-url>jdbc:postgresql://localhost:5432/mydatabase</connection-url>
                    <driver>postgresql</driver>
                    <transaction-isolation>TRANSACTION_READ_COMMITTED</transaction-isolation>
                    <pool>
                        <min-pool-size>3</min-pool-size>
                        <max-pool-size>20</max-pool-size>
                        <prefill>true</prefill>
                    </pool>
                    <security>
                        <user-name>postgres</user-name>
                        <password>**SENHA-DE-SUA-ESCOLHA**</password>
                    </security>
                </datasource>
				
- Subistitua **SENHA-DE-SUA-ESCOLHA**, por uma senha de sua preferência.

#####Postgres
- No Winows PowerShell ou CMD, inclua as variáveis de ambiente:
set POSTGRES_USER=postgres
set POSTGRES_PASSWORD=**SENHA-DE-SUA-ESCOLHA**
- Inicialize o banco executando: ..\kumuluscad\docker-compose up
- Crie um Database chamado **mydatabase**
- A criação das tabelas pode ser feita alterando o arquivo: C:\Users\marcel.britto\OneDrive - Zarpa\Documentos\04. Consulta\Java\Kumulus\kumuluscad\ejb\src\main\resources\META-INF\persistence.xml
	<property name="hibernate.hbm2ddl.auto" value="create" />
- Assim que a aplicação subir pela primeira vez, as tabelas serão criadas.
	
#####Subindo a aplicação
- Copie o ear gerado com mvn clean install -DskipTest para: ..\wildfly-23.0.2.Final\standalone\deployments
- Inicie o WildFly executando: ..\wildfly-23.0.2.Final\bin\standalone.bat
- Execute o script no postgres: ..kumuluscad\sql-scripts\02-insert_values.sql

Pronto, a aplicação está pronta para executar: http://localhost:8080/kumuluscad-web




#### Testes
Só é possível executar os testes unitários do Controller, via Eclipse.
Não tive tempo para implementar os testes de integração ainda.
