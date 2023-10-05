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

Na introdução de arquitetura de Apps para Android, mencionei que utilizaria o guia de arquitetura de Apps fornecido pelo Android Developers.
Nesta pequena apresentação, foram mencionados 2 novos componentes, o LiveData e ViewModel que fazem parte das bibliotecas do Jetpack do Android no tópico sobre arquitetura.

Fique à vontade em consultar todas as fontes compartilhadas durante o curso.

Lembrando ambas serão consideradas como referência para a maioria do conteúdo abordado no curso, portanto, se tiver algum detalhe que queira mais atenção, a leitura é mais do que recomendada.

https://developer.android.com/jetpack/docs/guide?hl=pt-br

https://developer.android.com/jetpack/docs/guide

@@07
Sobre manter o estado dentro dos componentes do Android

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

Nesta aula, aprendemos:
Os motivos para considerar o uso de arquitetura em um projeto Android;
A estrutura atual do projeto e como ela se estende com a nova proposta;
A arquitetura de Apps recomendada pela equipe de desenvolvedores do Android.

#### 05/10/2023

@02-Começando a implementação do ViewModel

@@01
Conhecendo o ViewModel e LiveData

Tivemos uma breve introdução sobre este novo modelo para uma arquitetura de apps Android sugerida pela sua equipe de desenvolvedores. Entretanto, ainda desconhecemos os componentes exibidos no diagrama, o ViewModel e o LiveData, portanto, antes de começarmos a fazer qualquer tipo de implementação, veremos melhor sobre eles.
Em relação à arquitetura de aplicativos Android, o ViewModel é um componente que irá manter todo o estado da Activity referente a dados dos nossos componentes visuais. A ListaNoticiasActivity.kt, por exemplo, tem como estado o repository, e sabemos que a Activity pode ser comprometida a qualquer momento, seja durante um processo de busca de dados, o ato de salvar, ou qualquer outro tipo de situação.

Para evitarmos este tipo de situação, enviaremos o repositório para o nosso ViewModel. Deste modo, qualquer tipo de comportamento que a nossa Activity quiser será solicitado ao ViewModel que, internamente, o acessará via objeto desejado. Neste momento, vocês podem estar pensando "mas isso nada mais é do que um encapsulamento". Ainda assim, o ViewModel será uma propriedade da nossa Activity, portanto será comprometido com o ciclo de vida.

Isto significa que o ViewModel é um componente especial já projetado para lidar com este tipo de situação, então, em situações nas quais teremos, por exemplo, uma mudança de configuração do nosso aplicativo, como quando estamos com ele aberto e rotacionamos a tela, e ocorre uma "destruição" e "reconstrução" de tudo, o ViewModel é mantido, bem como o estado do app.

A estrutura de um componente do Android, em específico a Activity, tem seu estado de criação, com onCreate, onStarte onResume, como já vimos no ciclo de vida, e ao ser rotacionada, ela lidará com vários estados: onPause, onStop, onDestroy, onCreate, onStart e onResume. Por mais que ela passe por todos eles, o ViewModel é mantido fora do ciclo de vida da Activity.

Por mais que seja uma Property, o mecanismo que cria um ViewModel Scope serve para que ele não seja comprometido neste tipo de situação em que há alguma mudança de configuração. É claro, haverá situações em que teremos que destruir nossa Activity efetivamente, e ela não será recriada. Neste momento, já não precisaremos mais do ViewModel, e quando voltarmos à Activity, será criado outro ViewModel.

O ViewModel, portanto, é uma camada capaz de proteger todo o estado nestas mudanças comuns, cujos dados não seriam destruídos, apenas a Activity. Mas como é que funciona a comunicação entre ele e o LiveData? O LiveData acaba atuando bastante para que isto seja possível, respeitando-se toda a questão do ciclo de vida.

Vamos supor que há uma Activity, que possui um ViewModel, e este será o princípio básico para lidar com este tipo de arquitetura que aplicaremos. Para a comunicação com o ViewModel, será utilizado o componente LiveData. Ou seja, a Activity pedirá os dados para o ViewModel, que fornecerá o LiveData, e com isso haverá uma conexão entre a Activity e o LiveData.

Isso porque quando tivermos um dado novo disponível, seja do banco de dados ou de alguma fonte externa, nosso LiveData não fará nenhum tipo de notificação. Ele fará, no entanto, uma notificação para a nossa Activity, muito similar ao que fazemos em Listener — apenas quando o dado estiver disponível, ele será processado e mantido pelo LiveData, e então fará a notificação, ou seja, enviará para quem estiver conectado a ele, no caso, a Activity.

Entretanto, este tipo de notificação funciona enquanto a Activity está ativa e, ao ser destruída por mudança de configuração, a conexão é desfeita automaticamente. Isso é importante pois, desta forma, evitamos que uma Activity que deixa de ter referência para o sistema Android, e que não será mais utilizada, fique ocupando memória enquanto o nosso LiveData está processando algo demorado.

Tudo com que temos vínculo e pode demandar um tempo pode ocasionar no que chamamos de Leak de memória, um "vazamento de memória", ocupando um espaço desnecessário. Então, aquela comunicação que fazemos diretamente com a nossa Activity via repositório acarreta, sim, em um possível vazamento de memória, porque pode ser que esta atualização que estamos solicitando ao Adapter demore um minuto, e não temos este valor exato.

Por conta disto, a proposta de não termos mais vínculos quando a Activity é destruída é muito benéfica, e o LiveData o faz automaticamente, não precisamos fazer nenhum tipo de configuração. Se ele recebe o dado novamente, o ViewModel estará funcionando conforme esperado, pois ele o faz em paralelo em relação ao que ocorre com a Activity, e só quando a conexão é feita, os dados são enviados para ela.

Ou seja, enquanto a Activity é destruída e não há nenhuma referência, os dados são mantidos, não sendo enviados para ninguém. E quando a Activity é criada, ela pedirá os dados novamente ao ViewModel existente, que não é recriado, já que o que houve foi uma mudança de configuração, e não uma destruição total da Activity. O ViewModel solicitará ao LiveData, que fará a conexão e enviará os últimos dados atualizados.

A diferença entre o LiveData e o Listener é que aquele respeita o ciclo de vida dos componentes. Tudo isso é conhecido como Lifecycle-aware, ou "Ciclo de vida consciente", em português. O guia do Android Developers indica que trata-se de um comportamento que sempre estará enviando os dados apenas quando a parte visual estiver disponível, sem enviar uma parte em background, como uma Activity inutilizada, correndo risco de vazamento de memória.

Estendendo para o diagrama inicial
Com base no que temos, teríamos a Activity se comunicando com o ViewModel, que entrega um LiveData, e depois teríamos um repositório, porque o ViewModel não irá buscar, salvar, editar ou remover os dados, e sim solicitar a alguém que saiba como fazê-lo, no caso, o próprio repositório, que se comunicará com o banco de dados interno, e com a Web API.

A diferença é que o LiveData será utilizado, e receberá tudo que for enviado, os dados serão processados com base na conexão estabelecida, e então a nossa Activity estará mantendo as informações quando estiver ativa. Percebam que o fluxo ficará em torno do LiveData, que manterá um ciclo de vida mais sustentável no Android.

É dessa maneira que criaremos nossa arquitetura, e veremos o funcionamento de cada uma das implementações com maior detalhamento adiante.

@@02
Sobre o funcionamento do ViewModel e LiveData

Para a arquitetura de Apps que consideramos durante o curso, foram introduzidos 2 novos componentes, o ViewModel e o LiveData. Considerando as propostas de ambos os componentes, marque as alternativas corretas.

O ViewModel é responsável em manter os dados e regra de negócio relacionados a UI.
 
Exatamente! A proposta deste componente e garantir que o estado que costuma ficar na Activity, seja permanecido mesmo que a Activity tenha uma mudança de configuração, como é o caso de rotacionar a tela.
Alternativa correta
O LiveData é responsável em realizar as requisições com o banco de dados ou a Web API.
 
Alternativa correta
O uso do ViewModel com LiveData segue o conceito do lifecycle-aware (ciclo de vida consciente).
 
Isso mesmo, ao integrar ambos os componentes somos capazes de armazenar o conteúdo que deve ser apresentado na UI respeitando o ciclo de vida, por exemplo, da Activity.
Alternativa correta
O ViewModel envia as atualizações de dados para a Activity respeitando o seu ciclo de vida.

@@03
Implementando o ViewModel

Vamos começar com a primeira implementação, cujo primeiro passo é adicionar os componentes ViewModel e LiveData ao nosso projeto, em relação à arquitetura recomendada pela equipe de desenvolvedores do Android. Iniciaremos, portanto, com a configuração de dependência, pois trata-se de bibliotecas externas que precisam ser baixadas e adicionadas ao projeto.
Tais componentes fazem parte do Jetpack, coleção de bibliotecas e técnicas comuns em vários aplicativos, com objetivo de facilitar o desenvolvimento do aplicativo Android. Na página do Android Developers, teremos que ambos se encontram na área de Arquitetura, e dentre os componentes, já trabalhamos bastante com o Room.

No fim da página, clicaremos em "ViewModel", teremos um Overview explicando o que é este componente e como poderemos utilizá-lo no framework, e outras informações, dentre as quais uma nota que indica que, para o importarmos ao projeto Android, precisamos seguir as instruções para declarar dependências sobre o Lifecycle release notes. Clicando no link, somos redirecionados à parte "Declaring dependencies".

Isso significa que tanto o ViewModel quanto o LiveData utilizam como base o componente conhecido por Lifecycle, responsável para fazer com que tudo funcione conforme desejado, no que concerne ao Lifecycle-aware components, componentes que respeitarão o ciclo de vida da Activity.

Durante o curso não veremos tantos detalhes sobre como isso funciona, mas fiquem à vontade para consultar a documentação caso sintam necessidade, inclusive se quiserem implementar componentes personalizados que não sejam o ViewModel ou LiveData.
A declaração de dependência que consta no guia é para quando se utiliza o AndroidX, como é o caso do nosso projeto. Para versões anteriores do Android, existe um tópico no índice localizado do lado direito do guia, denominado "Pre-AndroidX Dependencies". Assim, copiaremos a primeira dependência, a não ser que se queira incluir ViewModel ou LiveData sozinhos, ou ainda um Lifecycle personalizado independente.

Então, acessaremos o build.gradle do módulo app, e colaremos o código na parte inferior, logo abaixo daquele correspondente ao Room, da mesma forma como fizemos anteriormente:

kapt "androidx.room:room-compiler:$room_version"

def lifecycle_version = "2.0.0"

implementation "androidx.lifecycle:lifecycle-extensions:$lifecycle_version"COPIAR CÓDIGO
Um detalhe não visto anteriormente é que estávamos utilizando o Annotation Processor, e para o Kotlin utilizamos o kapt, componente de Annotation Processor para esta linguagem.
Feito isso, podemos sincronizar o projeto e, após a finalização, teremos tanto o ViewModel quanto o LiveData disponíveis. E então começaremos implementando o ViewModel. Tal implementação precisará de uma classe que representará um ViewModel, e como comentado, a parte visual terá um exclusivo.

Se fossemos criar um ViewModel, por exemplo, para ListaNoticiasActivity.kt, iríamos ao pacote raiz ("br.com.alura.technews"), e dentro dele indicaremos que criaremos um pacote. Porém, dado que é uma questão visual, podemos criá-lo dentro de "ui", a ser denominado "viewmodel". Assim, todos os ViewModels que criarmos para o aplicativo ficarão neste pacote.

O ViewModel que criaremos será justamente para a lista de notícias, portanto teremos um arquivo Kotlin, que será uma classe, ListaNoticiasViewModel. Sabemos que isto não é o suficiente — precisaremos configurá-la, fazendo a extensão da classe para que ela seja reconhecida como um ViewModel, utilizando a sintaxe do Kotlin.

import androidx.lifecycle.ViewModel

class ListaNoticiasViewModel : ViewModel() {

}COPIAR CÓDIGO
Simples assim. E para criarmos esta instância, temos que seguir uma regra para manter o mesmo comportamento visto, um ViewModel que não é comprometido pelo ciclo de vida da Activity. Então, abriremos ListaNoticiasActivity.kt e, no onCreate(), em que fazemos as inicializações, dado que é uma primeira implementação, não incluiremos o repositório ainda.

Precisaremos de um provedor de ViewModel, também conhecido como ViewModel Provider, com escopo de um componente visual, seja uma Activity ou uma Fragment, utilizamos a classe ViewModelProviders, à partir da qual teremos a função of(), responsável por criar um provedor para nós.

Assim, conseguimos identificar se a Activity foi recriada por meio de mudanças de configuração e, caso positivo, se ele consegue enviar a mesma instância criada da primeira vez, do ViewModel, e é por isto que precisamos do provedor, para controlar esta responsabilidade para nós. Basta enviarmos o this, e então poderemos buscar uma instância do ViewModel.

E então, com o get(), será solicitada uma referência da instância ListaNoticiasViewModel, a qual não será criada com vínculo em relação à Activity, e sim com base em sua estratégia interna por meio do Factory, padrão de projeto que cria objetos para nós.

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_lista_noticias)
    title = TITULO_APPBAR
    configuraRecyclerView()
    configuraFabAdicionaNoticia()

    val provedor = ViewModelProviders.of(activity: this)
    val viewModel = provedor.get(ListaNoticiasViewModel::class.java)
}COPIAR CÓDIGO
Assim, criamos o viewModel, que pode ser acessível, ou disponível quando se quer buscar alguma referência. E para testarmos os comportamentos de que estamos criando a mesma instância, ou estamos perdendo o viewModel e criamos uma nova, faremos algumas configurações de Log.

val provedor = ViewModelProviders.of(activity: this)
val viewModel = provedor.get(ListaNoticiasViewModel::class.java)
Log.i(tag:"viewmodel", viewModel.toString())COPIAR CÓDIGO
Dentro do ViewModelProviders verificaremos quando ele é criado, quantas vezes isto acontece, e quando ele é destruído, e para isso acrescentaremos uma instrução do Kotlin, init, para executar procedimentos durante a construção do objeto. Quando a Activity é destruída, sendo inutilizada, precisamos criar um View Model por não ser uma destruição por configuração, e o faremos por meio da função onCleared().

class ListaNoticiasViewModel : ViewModel() {

    init {
        Log.i(tag: "viewmodel", msg: "criando viewmodel")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(tag: "viewmodel", msg: "destruindo viewmodel")
    }
}COPIAR CÓDIGO
Com estes Logs, faremos uma simulação executando o aplicativo, e vendo quando eles são ativos. Acessaremos, portanto, o Logcat, que limparemos, e buscaremos por "viewmodel", enquanto fazemos as execuções para verificar o que acontece, usando o atalho "Shift + F10". Após a execução do aplicativo pelo Android Studio, analisaremos o Logcat.

O View Model foi criado com sucesso, e é exibida uma referência. O primeiro teste que faremos, então, será modificar a configuração do aplicativo, rotacionando a tela do mesmo. Foi passado pelo onCreate() da Activity, e sabemos que este é seu comportamento padrão, mantendo-se a mesma referência, sem a instrução durante a criação do nosso View Model.

Este é um fator muito importante para tomarmos consciência ao utilizarmos o View Model. Enquanto o escopo for o mesmo, isto é, a mesma Activity e o mesmo provedor, a instância será mantida para que o mesmo estado em relação aos dados da Activity permaneça inalterada.

Quando sairmos da Activity, situação na qual identificaremos que não estaremos mais utilizando-a, nosso View Model será destruído. Vamos testar o aplicativo no emulador novamente, para confirmarmos que isso realmente acontece. Em seguida, rotacionaremos a tela mais vezes, e em todas elas verificamos que a instância é mantida, como gostaríamos.

Agora que tivemos este primeiro contato, a seguir começaremos a fazer a refatoração para migrar tudo o que for relacionado a propriedades, que mantêm um estado de dados na Activity será enviado à View Model.

http://developer.android.com/jetpack

@@04
Implementando o ViewModel no projeto

Adicione o ViewModel no projeto e faça a sua implementação para a Activity de lista de notícias.
Para isso, primeiro adicione a dependência que adiciona o ViewModel junto com o LiveData:

def lifecycle_version = "2.0.0"

implementation "androidx.lifecycle:lifecycle-extensions:$lifecycle_version"COPIAR CÓDIGO
Em seguida, crie o ViewModel e faça o teste para verificar se o ViewModel apresenta o comportamento esperado em relação às mudanças de configuração.

Lembre-se que para criar o ViewModel e manter o comportamento esperado, é necessário o uso do provedor de ViewModel.

Ao inicializar deve apresentar apenas o log do construtor, ao rotacionar a tela não deve aparecer nenhum log, ao sair da Activity deve apresentar o log de destruição do ViewModel e ao entrar novamente deve apresentar o log de criação.
Você pode conferir o código da atividade a partir deste commit.

https://github.com/alura-cursos/android-tech-news/commit/f0b18626161e461f2aa87cc5484f2b3f35acfd34

@@05
Criando ViewModel com dependência

Criado o nosso primeiro View Model, faremos um processo de migração, que pegará as Properties da nossa Activity, a qual mantém o estado do dado, como é o caso do repositório, e vamos enviá-lo para o View Model. Deste modo, a Activity terá acesso direto a ele, e solicitará a busca de todas as notícias, por exemplo.
Para que o que precisamos modificar seja nítido, comentaremos o seguinte trecho de ListaNoticiasActivity.kt, nosso repositório:

private val repository by lazy {
    NoticiaRepository(AppDatabase.getInstance(context: this).noticiaDAO)
}COPIAR CÓDIGO
Assim, o repositório que está como Property na Activity deixa de existir, e o arquivo fica com alguns erros de compilação a serem resolvidos. Começaremos fazendo com que o viewModel seja uma Property, para que todos os membros da Activity consigam acessá-lo. Para isto, poderemos utilizar a técnica de inicialização Lazy, que ocorre apenas quando necessário.

Então, toda a parte do código com View Model e provedor seá recortada e adicionada a uma Property a ser inicializada via Lazy. Não precisaremos mais enviar para uma variável, e sim diretamente, já que o lazy funciona desta forma, retornando a última instrução.

private val adapter by lazy {
    ListaNoticiasAdapter(context = this)
}
private val viewModel by lazy {
    val provedor = ViewModelProviders.of(activity: this)
    provedor.get(ListaNoticiasViewModel::class.java)
}COPIAR CÓDIGO
Caso se queira manter o Log.i(tag: "viewmodel", viewModel.toString()) para fins de mapeamento, não tem problema, aqui deletaremos pois o mantivemos apenas para o verificamento inicial do nosso View Model. Agora, precisaremos utilizá-lo para buscarmos todas as notícias, portanto substituiremos repository por viewModel no seguinte trecho:

private fun buscaNoticias() {
    viewModel.buscaTodos(
        quandoSucesso = {
            adapter.atualiza(it)
        }, quandoFalha = {
            mostraErro(MENSAGEM_FALHA_CARREGAR_NOTICIAS)
        }
    )
}COPIAR CÓDIGO
No entanto, com isto perceberemos que não temos mais a função buscaTodos(), portanto precisaremos criá-la utilizando "Alt + Enter" e selecionando a opção "Create member function 'ListaNoticiasViewModel.buscaTodos'", com base na assinatura esperada, o quandoSucesso, que será uma função que retornará Unit, isto é, nada, bem como o quandoFalha.

Como em buscaNoticias() esperamos algum tipo de dado, como uma lista de notícias, não basta deixarmos as funções referentes à assinatura recebendo nada. Precisaremos atribuir algo esperado na nossa Activity, noticiasNovas:

fun buscaTodos(quandoSucesso: (noticiasNovas: List<Noticia>) -> Unit,
                            quandoFalha: (erro: String?) -> Unit) {

}COPIAR CÓDIGO
Noticia traz um erro por ser uma string que pode ser nula, e por isto utilizaremos erro: String?, por mais que na Activity não a utilizemos, porque já temos uma mensagem específica, mas estamos sempre atribuindo para que quem for acessar a mensagem consiga fazê-lo sem problemas.

Com isso, temos uma estrutura inicial, e então precisaremos fazer a implementação com nosso repositório, o qual precisa ser acessível dentro de ListaNoticiasViewModel. Deletaremos as instruções abaixo, que estavam sendo utilizadas apenas para fins de teste:

init {
    Log.i(tag: "viewmodel", msg: "criando viewmodel")
}

override fun onCleared() {
    super.onCleared()
    Log.i(tag: "viewmodel", msg: "destruindo viewmodel")
}COPIAR CÓDIGO
Temos várias formas de incluirmos este repositório; dado que ele precisará de um DAO para ser criado, do getInstance(), que é a instância do banco de dados, podemos pensar que basta termos acesso a um contexto para criá-lo. No entanto, há um detalhe muito importante: lembrem-se de que a proposta é fazer com que evitemos o máximo possível de vínculo com o Android Framework, com componentes que tenham ciclos de vida.

Sendo assim, não podemos enviar um contexto, e sim o objeto que queremos diretamente, em vez de algo que tenha algum tipo de import do pacote do Android. Isso evitará o risco de leak de memória, pois a ideia é sempre remover o vínculo da Activity, e não manter. Portanto, neste caso poderemos apenas receber o repositório, então, no View Model, o que receberemos de fora, para quem for instanciá-lo, será o repositório.

Com este acesso, conseguimos fazer a implementação de busca de notícias sem nenhum problema, chamando-o internamente para que se faça o buscaTodos(), enviando quandoSucesso e quandoFalha, implementados pela Activity. No View Model, não será feita nenhum tipo de alteração, de modo que podemos delegar esta responsabilidade sem problemas.

class ListaNoticiasViewModel(
    private val repository: NoticiaRepository
): ViewModel() {

    fun buscaTodos(quandoSucesso: (noticiasNovas: List<Noticia>) -> Unit,
                                quandoFalha: (erro: String?) -> Unit) {
        repository.buscaTodos(quandoSucesso, quandoFalha)
    }
}COPIAR CÓDIGO
Assim, fazemos com que a Activity se comunique diretamente com o View Model, e todo o código esteja compilando corretamente. Vamos executar a aplicação no emulador para verificar seu funcionamento. Assim como feito anteriormente, manteremos o Logcat aberto para observarmos os problemas ocorrendo em tempo real.

Houve o comportamento esperado, isto é, o aplicativo quebrou. De acordo com as mensagens exibidas no Logcat, o problema se relaciona à criação do View Model, pois não foi possível criar uma instância dele, uma vez que não há nenhum tipo de construtor que não receba nenhum argumento.

Isto quer dizer que, da maneira como fazemos a criação com o provedor, temos apenas View Models sem nenhum tipo de argumento dentro do seu construtor, sendo que na verdade temos uma ListaNoticiasViewModel que realmente espera um repositório. Precisamos de uma maneira personalizada para conseguirmos fazer isso.

O Provider interno possui um Factory interno capaz de criar esta instância, sendo necessário o modificarmos para que ele saiba que, no momento em que for criar a instância de ListaNoticiasViewModel, ele envie o repositório. Para criarmos este Factory para o View Model, acessaremos o pacote "viewmodel" para criarmos um pacote específico denominado "factory".

Dentro dele, usaremos o atalho "Alt + Insert" para criarmos uma nova classe em Kotlin, chamada ListaNoticiasViewModelFactory. Nela, faremos a implementação de interface conhecida como ViewModelProvider.Factory, a partir da qual faremos a sobrescrita de uma função chamada create(), que fará o comportamento de Factory, em que faremos a instância com o nosso repositório.

Para isto, indicaremos que retornaremos uma ListaNoticiasViewModel(), a instância que queremos criar, com que teremos acesso a um repositório, referência que ainda não temos, de tipo genérico por ser uma solução genérica, e só podemos fazer deste modo, como se fosse um Cast.

Inclusive, é exibido um alarme do inspetor de código, para o qual utilizaremos o atalho "Alt + Insert" e selecionaremos "Suppress 'UNCHECKED_CAST' for fun create" para indicar que tudo bem simplesmente fazermos um Cast. Teremos certeza da compatibilidade com o View Model, mas é deste modo que a API foi construída, portanto iremos respeitá-la. Agora que indicamos que estamos enviamos um repositório para esta instância, precisamos conseguir acessá-lo, e o faremos via construtor.

Teremos o seguinte código:

class ListaNoticiasViewModelFactory(
    private val repository: NoticiaRepository
) : ViewModelProvider.Factory {
    @Suppress(...names: "UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ListaNoticiasViewModel(repository) as T
    }
}COPIAR CÓDIGO
Feito isso, poderemos indicar que o Factory também será criado no momento em que criamos o provedor. Então, em ListaNoticiasActivity.kt, em ViewModelProviders.of(), teremos outro argumento, factory:

private val viewModel by lazy {
    val repository = NoticiaRepository(AppDatabase.getInstance(context: this).noticiaDAO)
    val factory = ListaNoticiasViewModelFactory(repository)
    val provedor = ViewModelProviders.of(activity: this, factory)
    provedor.get(ListaNoticiasViewModel::class.java)
}COPIAR CÓDIGO
No trecho acima, temos o repositório NoticiaRepository(), de que dependemos para obtermos o View Model para a lista de notícias. E para criarmos esta lista de notícias View Model sem nenhum vínculo com os pacotes do Android, contexto, ou qualquer tipo de ciclo de vida, utilizamos o Factory ListaNoticiasViewModelFactory(), que precisa do repository, dependência do View Model, e por isto criamos o factory enviando o of() do ViewModelProviders, o qual criará o provedor capaz de implementar o View Model ListaNoticiasViewModel.

Ou seja, todos estes passos são necessários quando queremos criar um View Model que necessita de alguma dependência, sobretudo quando ela tem algum vínculo com o Android Framework. Se fosse uma dependência sem nenhuma relação com o contexto, poderíamos enviar via construtor, ou mesmo fazer uma instância direta, chamando um teste, por exemplo, que recebe uma string, em ListaNoticiasViewModel. No entanto, tudo que tiver vínculo com o Android demanda a utilização do Factory.

Vamos testar nosso código para verificar se ele funciona da maneira esperada, mesmo sem o repositório dentro da Activity. A lista é carregada, e criaremos uma notícia para confirmar que isto não resulta em nenhum tipo de bug. Utilizando a estratégia View Model, teremos o mesmo comportamento de antes. Não precisamos mais do repositório, que utilizamos diretamente no View Model, portanto deletaremos o trecho a seguir, de ListaNoticiasActivity.kt:

// private val repository by lazy {
//    NoticiaRepository(AppDatabase.getInstance(this).noticiaDAO)
// }COPIAR CÓDIGO
Como dito anteriormente, usamos esta solução para evitar o vazamento de memória, no entanto, ainda utilizamos a referência, ou técnica de Listener, com que vinculamos à referência da Activity sem o comportamento proveniente do LiveData, que teria a responsabilidade de retirar este vínculo nos momentos em que a Activity é realmente destruída.

Então, mesmo que ela for destruída e este procedimento de busca ainda ocorra, estaremos vinculados a ela. A seguir, começaremos a fazer a implementação que evita este tipo de comportamento.

@@06
Migrando o repositório para o ViewModel

Faça com que o repositório seja uma property do ViewModel, e então, modifique o código para que a busca da lista de notícias seja solicitada diretamente pelo ViewModel e, internamente, o ViewModel faça a busca por meio do repositório.
Para que isso seja possível, é necessário criar um Factory capaz de criar o ViewModel que recebe um repositório.
Ao finalizar a implementação, teste o App e veja se a busca de notícias é feita da mesma maneira.

A busca não deve apresentar problemas e todas as notícias devem ser apresentadas.
Você pode conferir o código da atividade a partir deste commit.

https://github.com/alura-cursos/android-tech-news/commit/00616c9ab3395c9249883caacb7d86f002f68e03

@@07
O que aprendemos?

Nesta aula, aprendemos:
Os motivos para considerar o uso do ViewModel e LiveData na arquitetura de Apps Android;
Adicionar ao projeto e implementar o ViewModel;
Implementar o factory de ViewModel para criar ViewModels que recebem parâmetros via construtor.