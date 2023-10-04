#### 04/10/2023

Curso de Architecture Components: ViewModel, LiveData e Room

```
java -jar server.jar
```

@01-Conhecendo o projeto 

@@01
Introdução

Olá, sou Alex Felipe, instrutor da Alura, e apresentarei a vocês o curso de Arquitetura Android com ViewModel, LiveData e Room, para o qual é necessário um conhecimento prévio. Assim, conseguiremos aproveitar todo o conteúdo, e avançaremos neste mundo da arquitetura.
O que aprenderemos de novo neste curso?

Utilizaremos como exemplo um projeto que utiliza técnicas muito similares ao que vimos em outros cursos, o Tech News, um aplicativo para registrar notícias de tecnologias. Trata-se, portanto, de um CRUD, utilizando toda a estrutura aprendida em outros cursos, bem como algumas novidades em relação à linguagem Kotlin.

Aproveitaremos para avaliar tudo que fizemos no projeto, com base em tudo que aprendemos, e veremos o que podemos melhorar em relação à arquitetura.

Mas por quê precisamos levar a arquitetura em consideração quando fazemos um aplicativo?

Em muitas situações, durante o desenvolvimento de um aplicativo, utilizamos diversas bibliotecas e técnicas, e chegamos a uma estrutura com bug, por exemplo, ou que apresenta dificuldade de implementação de algo novo, sem a qualidade desejada.

Então, quando pensamos em nível de arquitetura, é realmente no momento em que já temos certa maturidade com aplicativos Android, ou então com o nosso código, e queremos melhorar cada vez mais.

Para isso, utilizaremos como base algo que a própria equipe do Android desenvolveu e nos fornece, o "Guia para a arquitetura do app", para identificarmos os pontos importantes para o qual precisamos nos atentar, seja em relação ao ciclo de vida, tomadas de decisões, e assim por diante.

E em relação a tecnologias novas, estaremos utilizando dois componentes: o ViewModel para segurar os dados da nossa Activity, e o LiveData, capaz de fazer notificações para a nossa tela. A princípio, parece abstrato, porém, durante o curso veremos o funcionamento deles com detalhes.

Tais componentes fazem parte do Android Jetpack, que vimos nos cursos de Room, conjunto de bibliotecas fornecido pela equipe de desenvolvedores do Android para que consigamos criar nossos aplicativos de maneira mais fácil e robusta, sem termos que pensar tanto na parte de configuração, e sim na parte de regras de negócio.

Ou seja, o conteúdo é de nível mais avançado, então, espero que você esteja animado com a proposta. Vamos lá?

@@02
Orientações iniciais e organização do curso
PRÓXIMA ATIVIDADE

Pré-requisitos
Neste curso, esperamos que você já seja familiarizado com os seguintes tópicos:

Fundamentos de Android;
Persistência de dados com Room;
Kotlin;
Acessando Web API;
O recomendado é concluir os cursos anteriores para melhorar a compreensão.

Como o curso está organizado
Alguns exercícios serão bem resumidos, contendo apenas informações necessárias que não foram apresentadas no vídeo.

Configurações para diferentes sistemas, links e outros detalhes não apresentados em vídeo, serão compartilhados nos exercícios.
É importante ressaltar que, qualquer dúvida, você pode entrar em contato com a gente por meio do fórum :)

https://cursos.alura.com.br/course/android-refinando-o-projeto

https://cursos.alura.com.br/course/android-kotlin-persistencia-dados-room

https://cursos.alura.com.br/course/fundamentos-android-kotlin

https://cursos.alura.com.br/course/android-api-web

@@03
Estrutura atual do projeto

Antes de começarmos com as novidades do nosso curso, veremos um pouco sobre o projeto, pois usaremos um código pronto, disponibilizado para download, e neste momento não temos conhecimento sobre a estrutura atual, portanto aproveitaremos este momento para tirar as dúvidas e mostrar o que temos em termos de implementação.
Como primeiro passo, precisamos entender que neste projeto utilizaremos o código em Kotlin, e como segundo passo, verificaremos a sua estrutura, similar ao que foi visto em cursos anteriores. Para buscarmos alguma notícia, por exemplo, acessamos ListaNoticiasActivity.kt em "app > java > br.com.alura.technews > ui > activity", e perceberemos que, além do Adapter e de todas as configurações visuais, temos o componente repository que, como vimos em um curso anterior, faz a comunicação externa com a nossa API, e a comunicação interna com o banco de dados.

Em nosso repositório, NoticiaRepository.kt, percebemos que temos acesso ao dao e à outra referência, denominada webclient, que ainda não vimos com muitos detalhes. Basicamente, ela envolverá todo o código de responsabilidade pela comunicação externa, em que utilizamos o protocolo HTTP, por meio do próprio Retrofit, AppRetrofit.kt. Ou seja, a configuração é quase exatamente a mesma, apenas com pequenas alterações de sintaxe do Kotlin.

O que existe de novidade é que usamos um WebClient para armazenar toda esta responsabilidade, possuindo a função genérica para executar uma requisição, que pega uma Call genérica, e uma função que recebemos via parâmetros, quandoSucesso, uma High Order Function para indicar sucesso ou falha, então estamos mantendo todo tipo de responsabilidade em NoticiaWebClient.kt — questões acerca de busca, salvar, editar, remover, entre outras. E é por isso que nosso repositório tem acesso a este tipo de objeto.

Voltando, então, à parte do nosso fluxo, em nosso repositório temos a função buscaTodos(), que é chamado em ListaNoticiasActivity.kt, e se pegarmos nosso estado de resumo (onResume()), percebemos que temos a função buscaNoticias(), dentro do qual utilizamos o respository chamando o buscaTodos(), e então fazemos uma implementação das nossas funções, no caso de sucesso, atualizamos o Adapter, e quanto falha, mostramos uma mensagem de erro, que neste caso é um Toast.

O fluxo, portanto, é muito similar ao que vimos anteriormente, sem grandes novidades, a não ser pela questão do envolvimento e implementação da WebClient, pois enviamos as funções diretamente em vez de fazermos chamadas do Enqueue, por exemplo, e o uso de High Order Function. Será muito importante ter todos estes conceitos aprendidos anteriormente, assim como o BaseAsyncTask().

Nossa estrutura atual deste projeto consiste em Activities (seja de visualização, formulário ou qualquer outra), que se comunicam diretamente com Repositories, que por sua vez se comunicam com o banco de dados interno, e também com uma Web API. Com base nisso, avançaremos no curso, analisaremos o código, veremos os possíveis problemas, e como a arquitetura consegue nos ajudar a melhorar esta estrutura.

@@04
Preparando o ambiente - download do projeto Android
PRÓXIMA ATIVIDADE

Para este curso vamos utilizar o projeto de Tech News que tem o objetivo de realizar um CRUD de notícias e apresentar os seguintes comportamentos:
Listagem de notícias;
Criação de notícia;
Visualização de notícia;
Edição e remoção de notícia.
O projeto está disponível a partir deste link. Após realizar o download faça o processo de extração e importe via Android Studio.

O projeto depende da web API para funcionar conforme o esperado, portanto, antes de executar e testar as funcionalidades, configure a integração com a API.

Integrando App com a Web API
Baixe o arquivo Zip da Web API, nele temos os seguintes arquivos:

server.jar: arquivo executável da Web API;
README.md: Instruções gerais e orientações de uso.
Essa API segue o mesmo modelo que vimos no curso de persistência web no Android, mudando apenas o domínio (URL) que agora são notícias.

Após executar o servidor, pegue o endereço IP do seu computador (ifconfig para Linux ou Mac e ipconfig para Windows) e a porta que a API está rodando, então substitua o valor da constante BASE_URL que está como "http://seu_ip:porta_da_api/" com base no exemplo:

private const val BASE_URL = "http://192.168.20.249:8080/"COPIAR CÓDIGO
Testar o endereço da API antes de executar o App via Postman ou navegador, evita falhas por digitação.
Após adicionar o endereço da Web API, execute o App e veja se todos os comportamentos de CRUD funcionam como o esperado.

Fique à vontade para testar o projeto. Caso tiver dúvidas em relação à implementação, entre em contato com gente por meio do fórum.

Neste primeiro contato com o projeto, recomendamos que não modifique o código fonte para que tenha o mesmo aspecto que será apresentado no decorrer do curso.

Porém, após conclusão, não há nenhuma ressalva em relação à edição do código.

https://github.com/alura-cursos/android-tech-news/archive/projeto-inicial.zip

https://github.com/alura-cursos/android-tech-news/archive/api.zip

https://cursos.alura.com.br/course/android-api-web

@@05
Introdução a arquitetura de apps Android

Agora que conhecemos mais sobre a estrutura do nosso projeto, bem como a implementação de código, veremos a nova arquitetura que poderemos considerar para entregarmos um aplicativo que seja mais robusto, com maior facilidade em testes e para manutenção. Trata-se de uma proposta de arquitetura que a própria equipe de desenvolvedores do Android nos disponibiliza, o Guia para a arquitetura de apps.
Isso significa que durante este curso usaremos como base todo o conteúdo deste guia, que contém os detalhes com os quais devemos nos preocupar quando vamos criar um aplicativo Android. Começaremos conhecendo os principais componentes de entrada do nosso aplicativo, pois desta maneira lidamos com as suas complexidades.

Os principais componentes são as Activities, que são o ponto de entrada que irão manter as informações a serem apresentadas na UI, isto é, na interface do usuário. Temos também os Services, componentes que executam rotinas em segundo plano, algo constante enquanto o app estiver funcionando. Podemos configurar serviços em paralelo, os quais podem levar certo tempo de execução, como por exemplo notificações, algum serviço que faça download, e assim por diante.

Há os Content Providers, componentes que fornecem dados públicos do app, possíveis de serem compartilhados entre outros apps. Por fim, temos os Broadcast Receivers, justamente um mecanismo de entrega de eventos para aplicativos. Um aplicativo de telefone, por exemplo, ficará atento a chamadas, cujo evento será enviado para todos os seus aplicativos.

Como notamos, cada componente possui seu próprio ciclo de vida e pode ser acionado a qualquer momento. Precisamos nos atentar a isso pois este ponto pode nos trazer problemas, já que, ao lidarmos com todas as possibilidades, entramos em um cenário que pode ser complexo, como a forma como o usuário final lida com nossos aplicativos, ou melhor, com o sistema Android.

Vamos supor que alguém utiliza um aplicativo de texto, um mensageiro para fins de comunicação, e neste processo de envio de mensagens, ele recebe uma ligação, situação bastante possível, pois existem entradas do Android, do sistema operacional, com outros aplicativos atuando em background, por mais que a mensagem ainda não tenha sido enviada. Outro aplicativo será acionado no momento em que ele for atender a esta chamada, após o qual ele poderá voltar ao aplicativo mensageiro, e de repente decidir que quer tirar uma foto.

Percebam que este tipo de interação será constante, e para isso teremos que lidar com ciclos de vida, o que não é nada trivial. Este, inclusive, é o primeiro tópico que consta no Guia para a arquitetura de apps — tudo que fizermos em nossos aplicativos terão que levar em consideração seu ciclo de vida.

Dentre os principais problemas que podem ocorrer, nesse sentido, está o gerenciamento da memória do sistema operacional, portanto, neste processo de mudança de uso de um aplicativo para outro, pode ser que o Android não tenha memória o suficiente para manter todos os aplicativos em funcionamento. Neste caso, o sistema operacional acabará "matando" os aplicativos, ou seja, se o usuário estiver no processo de envio da mensagem, pode acontecer dele perder todos os dados e informações que estavam sendo digitados.

Essa é uma preocupação que precisamos ter, e não é tão simples lidar com estas complexidades dos nossos aplicativos, e a ideia dessa arquitetura é justamente evitar este tipo de situação. Um destaque do Guia é a recomendação de não armazenar dados ou estados de app nos seus componentes. Assim, é melhor evitar armazenar dados nos componentes, que possuem ciclo de vida, e que a qualquer momento podem ser comprometidos.

Como comentado em cursos anteriores, é recomendado não manter códigos diretamente na Activity, e o Guia traz isso na parte dos princípios arquitetônicos comuns, que envolve o que eles chamam de "separação de conceitos", para manter os componentes do Android o mais enxutos o possível. De repente, nossa Activity terá código apenas para tela, enquanto evitaremos que os aplicativos fiquem nos componentes visuais sempre que possível.

Para que isso seja possível com maior facilidade, existe uma arquitetura recomendada, demonstrada em um diagrama exibido no Guia. No topo, está a Activity/Fragment, seguido por um intermediário, o ViewModel combinado com LiveData, antes de chegarmos ao Repository, que por sua vez desembocam para Model ou Remote Data Source. Trata-se de uma arquitetura muito similar ao que fizemos, com a diferença do intermediário, que seria responsável por evitar este tipo de situação que vimos.

Com estes componentes, seremos capazes de evitar esta complexidade e problemas comuns, que ocorreriam caso mantivéssemos o repositório diretamente na Activity, por exemplo. Isso porque quando estamos efetuando este procedimento no repositório, qualquer tipo de estado que tentamos manter dentro dele pode ser perdido a qualquer momento, e não temos controle algum disso, uma vez que Activities ou quaisquer outros componentes do Android podem ser destruídos por algum motivo.

Com base neste tipo de situação, veremos a seguir o que temos em nosso código, que atualmente pode ser comprometido, e também como contornar isso a partir do novo modelo de arquitetura.

https://developer.android.com/jetpack/docs/guide?authuser=3&hl=pt-Br

@@6
Material de apoio
PRÓXIMA ATIVIDADE

Na introdução de arquitetura de Apps para Android, mencionei que utilizaria o guia de arquitetura de Apps fornecido pelo Android Developers.
Nesta pequena apresentação, foram mencionados 2 novos componentes, o LiveData e ViewModel que fazem parte das bibliotecas do Jetpack do Android no tópico sobre arquitetura.

Fique à vontade em consultar todas as fontes compartilhadas durante o curso.

Lembrando ambas serão consideradas como referência para a maioria do conteúdo abordado no curso, portanto, se tiver algum detalhe que queira mais atenção, a leitura é mais do que recomendada.

https://developer.android.com/jetpack/docs/guide?hl=pt-br

https://developer.android.com/jetpack/docs/guide

@@07
Sobre manter o estado dentro dos componentes do Android
PRÓXIMA ATIVIDADE

Nesta introdução sobre as possibilidades de entradas de Apps por meio dos principais componentes de Apps Android, vimos que existe um risco em manter o estado direto (objetos como atributos/properties) dentro dos mesmos, pelos quais motivos?

O App quebra, pois todo o código precisa ficar dentro das funções de ciclo de vida.
 
Alternativa correta
Os componentes são controlados pelo SO e podem ser destruídos a qualquer momento.
 
Exatamente! Pelo fato de não termos controle sobre o ciclo de vida do Android, ficamos suscetíveis a qualquer decisão feita pelo sistema, principalmente em casos que exige memória que já está limitada.
Alternativa correta
Podemos perder os dados do usuário a qualquer momento.
 
Isso mesmo! Sem considerar um meio persistente que é volátil, ficamos suscetíveis a perda de dados com esse tipo de abordagem.
Alternativa correta
O App é comprometido em performance, correndo o risco de travar.

@@08
O que aprendemos?
PRÓXIMA ATIVIDADE

Nesta aula, aprendemos:
Os motivos para considerar o uso de arquitetura em um projeto Android;
A estrutura atual do projeto e como ela se estende com a nova proposta;
A arquitetura de Apps recomendada pela equipe de desenvolvedores do Android.