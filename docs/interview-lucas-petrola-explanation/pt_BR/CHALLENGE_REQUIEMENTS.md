# Requisitos do desafio - Desenvolvedor Backend Sênior

Estes são todos os tópicos para aprovação no desafio para Desenvovledores Backend Sêniores

O aplicativo atende atualmente a vários pontos de extremidade da API. Você deve implementar uma estratégia de
autenticação e autorização para este aplicativo.

---

## Específicos

O acesso a todos os terminais deve ser autenticado por um usuário previamente registrado.

* [x] Deve haver dois perfis de acesso possíveis (leitura e gravação).
* [x] Os terminais que fazem alterações no banco de dados devem estar disponíveis apenas para usuários com perfil de
  acesso de gravação.
* [x] Os terminais que apenas recuperam e exibem informações devem estar disponíveis apenas para usuários com perfil de
  acesso de leitura.

## Autenticação

Deve haver um ponto central de autenticação para todos os usuários e aplicativos.

* [x] Deve ser fácil adicionar novos aplicativos para o mesmo grupo de usuários.
* [x] A solução de autenticação deve suportar endereços de e-mail para identificação do usuário.
* [x] A solução de autenticação deve suportar a criação de contas self-service pelo usuário, exigindo apenas a
  verificação do e-mail.
* [x] As contas de usuário devem ter o perfil de acesso de leitura atribuído automaticamente no momento da criação da
  conta.
* [x] A solução de autenticação deve suportar centenas de milhares de usuários.
* [x] A interface de usuário de autenticação deve ser localizável e disponível pelo menos em inglês, português e
  espanhol.
* [x] A solução de autenticação deve suportar números de telefone para identificação do usuário no futuro.
* [x] A solução de autenticação deve suportar integração SSO no futuro.
* [x] A autenticação deve ser feita em um navegador atualizado fornecido pelo sistema operacional.

## OAuth2 and JWTs

* [x] A autenticação e a autorização devem ser implementadas usando OAuth2 e JWTs.
* [x] A autorização deve usar OAuth2 e tokens da web JSON (JWTs).



