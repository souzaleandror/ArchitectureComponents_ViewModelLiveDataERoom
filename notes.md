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

#### 08/10/2023

@03-Integrando o ViewModel com o LiveData

@@01
Utilizando o LiveData

Como comentado anteriormente, precisamos evitar manter a referência de nossa Activity indiretamente. Em nossa implementação atual exigimos que a Activity implemente as funções da high order function, quandoSucesso e quandoFalha e, antes mesmo de entendermos como poderemos evitar isso, veremos como isso pode resultar em vazamento de memória modificando o buscaTodos(), no momento em que se faz a busca interna.
Então, em NoticiaRepository.kt, não faremos a chamada em buscaNaApi() de buscaTodos(), cuja linha comentaremos, e em buscaInterno() deixaremos alguns Logs para identificarmos quando ele se inicia e termina, incluindo um delay para simular a demora. Além disso, acrescentaremos um Log na Activity para verificar quando o comportamento da Activity é chamado.

Acrescentaremos o Log.i() chamando por meio da chave "teste" para filtrarmos facilmente. E em seguida incluiremos o Thread.sleep(), o nosso delay, no caso, de 5 segundos. E em quandoFinaliza colocaremos mais um Log.i(), e em quandoSucesso precisamos apenas nos atentar para executarmos a função também, pois quando a enviávamos diretamente, internamente, no BaseAsyncTask(), ela era executada, diferentemente do que ocorre aqui.

private fun buscaInterno(quandoSucesso: (List<Noticia>) -> Unit) {
    BaseAsyncTask(quandoExecuta = {
        Log.i(tag: "teste", msg: "buscando notícias no banco")
        Thread.sleep(millis: 5000)
        dao.buscaTodos()
    }, quandoFinaliza = {it: List<Noticia>
        Log.i(tag: "teste", msg: "finalizou busca")
        quandoSucesso(it)
    }).execute()
}COPIAR CÓDIGO
Com isso, a diferença do que tínhamos anteriormente é que desta vez temos Logs e o sleep(). E em ListaNoticiasActivity.kt, adicionaremos um Log.i() para a atualização de notícias:

private fun buscaNoticias() {
    viewModel.buscaTodos(
        quandoSucesso = {it: List<Noticia>
            Log.i(tag: "teste", msg: "atualizando noticias")
            adapter.atualiza(it)
        }, quandoFalha = {it: String?
            mostraErro(MENSAGEM_FALHA_CARREGAR_NOTICIAS)
        }
    )
}COPIAR CÓDIGO
Basicamente, este é o mapeamento que faremos para os nossos testes. Vamos executar o aplicativo e, enquanto isso, filtrar a palavra "teste" no Logcat. Começaremos a simulação rotacionando a tela do app, e assim as notícias são buscadas no banco de dados, e em seguida a lista é atualizada, um comportamento esperado.

Porém, quando fazemos a simulação duas vezes, rotacionando a tela novamente durante a primeira rotação, a busca será aberta por ser uma Async Task, em que há uma fila de execuções a ser obedecida. Reparem, porém, que ele busca, finaliza e atualiza, mesmo que a Activity não esteja sendo mais utilizada. Isto é, o procedimento ainda está sendo feito.

Ao limparmos o Logcat, voltarmos a tela ao modo retrato, e sairmos do aplicativo, teremos no Logcat que a lista de notícias foi atualizada, sendo que não há nenhuma Activity em uso. E se reabrirmos o aplicativo, teremos uma nova referência, outra Activity, e assim por diante. Precisaremos nos atentar a isto, pois é algo que não deveria acontecer, justamente pelo risco de leak de memória.

Vamos entender como evitar este problema utilizando o Live Data. O primeiro passo para isso será remover as chamadas quandoSucesso e quandoFalha, implementações de funções via high order function. Não podemos mais utilizar esta abordagem para evitar o vazamento de memória, então, para fins de didática, simplesmente comentaremos o trecho, deslocando-o para baixo do escopo de buscaTodos():

private fun buscaNoticias() {
    viewModel.buscaTodos()
//        quandoSucesso = {
//            Log.i("teste", "atualizando noticias")
//            adapter.atualiza(it)
//        }, quandoFalha = {
//            mostraErro(MENSAGEM_FALHA_CARREGAR_NOTICIAS)
//        }
}COPIAR CÓDIGO
E em buscaTodos() faremos as modificações necessárias para atender às necessidades de uso do Live Data. Iremos acessá-lo por meio de "Ctrl + B". Precisaremos fazer com que a Activity tenha acesso ao Live Data, e para isto iremos retorná-lo, porém, se fizermos apenas isto, teremos um problema de compilação. É exigido o envio de um argumento via Generics, pois o Live Data é uma classe que mantém dados internamente de forma segura.

Entretanto, somente o retorno não é o suficiente, pois precisamos retornar uma referência de Live Data válida, sendo assim é necessário criá-lo internamente, para que se mantenha o dado esperado. Se tentarmos criá-lo, o programa indica que trata-se de uma classe abstrata, então não fazemos a implementação com base no Live Data, para isto temos outras classes que herdam dele, e disponibilizam esta implementação.

Dentre elas, a mais comum de ser utilizada é MutableLiveData que, diferentemente do Live Data (uma referência imutável), permite acesso à leitura e escrita. É por isto que enviamos o Live Data diretamente, pois quando o fazemos, apenas seu criador possui a responsabilidade de fazer a modificação do dado.

Sendo assim, sempre que um Live Data for disponibilizado, é preciso enviar a sua referência.
A lista será, então, enviada para uma variável, no caso liveData, a ser retornado:

fun buscaTodos() : LiveData<List<Noticia>> {
    val liveData = MutableLiveData<List<Noticia>>()
    repository.buscaTodos(quandoSucesso, quandoFalha)
    return liveData
}COPIAR CÓDIGO
No entanto, se deixarmos desta forma, teremos um problema, uma vez que criamos um Live Data capaz de receber um tipo de lista de notícias que, em nenhum momento, tem seu valor atualizado automaticamente. Precisaremos fazer este procedimento de atualização de maneira manual quando obtemos resposta do servidor, ou do banco de dados, ou melhor, do repositório.

Então, quando fazemos a implementação do repository, realizamos a busca por meio da high order function implementando suas funções. Assim, quando tivermos sucesso, temos acesso à lista, consequentemente conseguimos alterar o valor do nosso MutableLiveData. Utilizaremos value para modificarmos o valor, faremos a atribuição, uma Property, e enviamos uma lista, denominada noticiasNovas, por exemplo.

fun buscaTodos() : LiveData<List<Noticia>> {
    val liveData = MutableLiveData<List<Noticia>>()
    repository.buscaTodos(quandoSucesso = { noticiasNovas ->
        liveData.value = noticiasNovas
    }, quandoFalha = {})
    return liveData
}COPIAR CÓDIGO
É deste modo que atualizamos o nosso Live Data, porém isso não é o suficiente para que ele seja atualizado na Activity para a conexão de comunicação com o View Model, o qual disponibilizará o Live Data. Com isso, quando houver algum dado novo, a notificação para a Activity será feita de forma automática.

Quando temos acesso ao Live Data, somos capazes de chamar suas funções, dentre elas observe(), responsável por esta conexão, e que irá sempre observar o Live Data para verificar novas informações. É necessário enviar para ela alguns argumentos, sendo o primeiro uma entidade chamada Lifecycle owner, interface que determina que quem a implementa possui o ciclo de vida do Android.

Isso é importante para o Live Data pois se usa o conceito do Lifecycle aware, comportamento em que as notificações são feitas somente quando a referência que tem ciclo de vida está ativo. Quando inativo, será feita a desconexão, removendo-se o vínculo que havia em relação à referência. É por isso que é importante enviar esta entidade, que Activities e Fragments possuem por padrão.

Neste caso, podemos nos indagar o porquê de não ser solicitada uma Activity, ou um Fragment, e o que acontece é que estes pacotes fazem parte do Lifecycle, componente do Architecture Components, e permitem que classes personalizadas funcionem de acordo com o ciclo de vida. Assim, poderíamos enviar outras classes com a implementação desta interface, e fossem responsáveis em utilizar os conceitos do Lifecycle aware.

O segundo argumento de observe() é o Observer, interface com uma função que determina se houve alguma mudança em nosso Live Data. Poderemos implementá-lo via expressão lambda, dentro da qual determinamos se houve alguma mudança, ou não. Caso haja, optaremos pela abordagem que tínhamos colocado anteriormente, o Log.i(), e a atualização do adapter.

private fun buscaNoticias() {
    viewModel.buscaTodos().observe(owner: this, Observer {it: List<Noticia>!
        Log.i(tag: "teste", msg: "atualizando noticias")
        adapter.atualiza(it)
    })
}COPIAR CÓDIGO
Deste modo, estaremos respeitando o ciclo de vida da Activity. Vamos testar o aplicativo e ver o que acontece?

Faremos as mesmas simulações de antes, rotacionando a tela duas vezes seguidas, desta vez utilizando o Live Data; de acordo com o Logcat, a primeira busca é finalizada, e é buscada novamente no banco de dados, ou seja, não há atualização após a finalização da busca. Isso quer dizer que estamos evitando o risco de vazamento de memórias porque não estamos mais presos à referência da nossa Activity, e com qualquer outro componente envolvendo esta questão de ciclo de vida, com o Lifecycle owner implementado, teríamos o mesmo comportamento.

Esta foi a primeira implementação, existem outros detalhes importantes a serem considerados, os quais veremos conforme formos avançando no curso.

@@02
Devolvendo a lista de notícias com o LiveData

Caso você precise do projeto com todas as alterações realizadas na aula passada, você pode baixá-lo neste link.
Modifique o código para que a busca de notícias seja feita por meio do LiveData.

Para a primeira implementação, considere a busca feita apenas pelo banco de dados interno. Ao utilizar o LiveData, lembre-se que tanto a Activity como o ViewModel, não devem mais utilizar a técnica de callback (utilizar Higher-Order-Functions).

Ao finalizar teste o App e veja se a busca funciona como o esperado. Se sim, adicione um delay na AsyncTask que realiza a busca por meio do DAO e alguns logs, tanto na AsyncTask como na Activity, para verificar se o comportamento do LiveData segue o conceito de lifecycle-aware.

Portanto, no teste, confira se os logs são apresentados conforme o esperado.

Além de carregar a lista de notícias no teste deve apresentar o seguinte comportamento:
Activity ativa: aparecer todos os logs;
Activity recriada por mudança de configuração: aparecer log de inicialização e finalização da busca. Aparecer o log de atualização de lista apenas uma vez.
Activity em estado de pausa ou destruição: aparecer apenas o log da busca.
Você pode conferir o código da atividade a partir deste commit.

https://github.com/alura-cursos/android-tech-news/tree/aula-2

https://github.com/alura-cursos/android-tech-news/commit/609cdf6c7960415fffba302b017c13be840fe130

@@03
Detalhes ao utilizar o LiveData

Feita a primeira implementação com o Live Data, veremos alguns pontos importantes a serem considerados neste tipo de solução, levando em conta, também, as orientações feitas na arquitetura de apps sugerida pela equipe de desenvolvimento do Android. Em relação à rotação que fazemos em nosso aplicativo, ou seja, o processo que recria a nossa Activity, a tela fica em branco durante o período em que ocorre o delay proposital.
No entanto, ela já foi carregada uma vez, e se rotacionarmos novamente, teremos o repetimento do processo. Isto é, se já carregamos a lista uma vez, ela precisa ser disponibilizada imediatamente. Não faz sentido algum termos que buscá-la novamente para o nosso usuário, considerando a sua experiência e navegabilidade.

Considerando nossa solução atual que utiliza o Live Data, poderemos acessar buscaTodos() em ListaNoticiasViewModel.kt para verificar o que estamos implementando de fato. Ao buscarmos todas as notícias, criamos um novo Live Data (MutableLiveData), que ficará atento cada vez que houver um quandoSucesso ou quandoFalha, e só desta maneira ele irá se autoatualizar.

Porém, isso só ocorre quando buscamos as notícias, não mantendo este dado em memória como se fosse um cache disponibilizado imediatamente. Conseguimos isso fazendo com que o Live Data seja uma Property do View Model, e assim, mesmo que ele crie uma Activity, o dado estará disponível em memória, acessível sem que haja necessidade de resposta do banco de dados, por exemplo.

O código, portanto, ficará da seguinte maneira:

class ListaNoticiasViewModel(
    private val repository: NoticiaRepository
) : ViewModel() {

    private val liveData = MutableLiveData<List<Noticia>>()

    fun buscaTodos() : LiveData<List<Noticia>> {
        repository.buscaTodos(quandoSucesso = { noticiasNovas ->
            liveData.value = noticiasNovas
        }, quandoFalha = {})
        return liveData
    }
}COPIAR CÓDIGO
Assim, o valor do Live Data que for atualizado ficará disponível enquanto o View Model estiver criado, e não for destruído, isto é, enquanto não for criada outra referência. E o dado que ficar disponível em nosso Live Data sempre será notificado para quem for chamá-lo, portanto, lembrem-se de que ele sempre mantém seu último valor, disponibilizando-o imediatamente para quem o solicita, no caso, a conexão, o Observer.

Vamos executar o aplicativo no emulador para verificar seu funcionamento. Limparemos o Logcat para facilitar a visualização dos logs, e a primeira busca é realmente um pouco mais demorada por ser a primeira. Deletaremos os logs novamente, faremos a rotação da tela, e os dados são exibidos imediatamente, atualizando para a Activity antes mesmo da busca. Caso houvesse algo novo, a atualização seria feita sem nenhum problema — trata-se de um ponto muito importante quando se considera este tipo de solução, isto é, pensando na disponibilidade do dado após o primeiro carregamento.

Sobre o próximo detalhe, quando estávamos aprendendo sobre arquitetura, Live Data, View Model e afins, entendemos que eles podem se estender ao que fazemos atualmente, isto é, Activities se comunicando diretamente com o repositório, que por sua vez se comunica com o banco de dados e a API, com as informações sendo enviadas de forma encadeada.

Precisaremos seguir esta regra para que o Lifecycle aware seja mantido, pois da maneira como está atualmente, mantemos a referência do View Model. Caso ele seja inutilizado porque a Activity à qual estava atrelado deixa de existir, não estaremos evitando o risco de leak de memória. Então, é necessário utilizarmos a técnica do Live Data para o nosso repositório, e para isto acessaremos buscaTodos() e faremos algumas modificações, como remover a high order function.

fun buscaTodos() : LiveData<List<Noticia>> {
    val liveData = MutableLiveData<List<Noticia>>()
    buscaInterno(quandoSucesso = {it: List<Noticia>
        liveData.value = it
    })
        buscaNaApi(quandoSucesso = {it: List<Noticia>
        liveData.value = it
        }, quandoFalha = {})
        return liveData
}COPIAR CÓDIGO
A mesma regra aplicada em nosso View Model será colocada no repositório. Também atualizaremos o Live Data, usando it por ser mais objetivo, e faremos o mesmo para a API. Neste momento, não faremos a modificação necessária em quandoFalha pois teremos alguns detalhes importantes a serem vistos conforme avançamos no curso.

Como não temos mais as high order functions, não precisaremos mais implementar o nosso Live Data manualmente, bastando retornar diretamente do repositório. Porém, com isto, deixaremos de ter o Live Data que fica em memória, em nosso cache, não sendo funcional como anteriormente.

fun buscaTodos() : LiveData<List<Noticia>> {
    return repository.buscaTodos()
}COPIAR CÓDIGO
Neste caso, o que poderemos fazer é que o Live Data em memória seja do próprio repositório, como Property, e então não faremos com que ele seja criado cada vez que ocorre a busca, e o dado será mantido internamente. Assim, quando o View Model solicitar, a Property poderá ser devolvida diretamente, de forma encapsulada.

private val noticiasEncontradas = MutableLiveData<List<Noticia>>()

fun buscaTodos() : LiveData<List<Noticia>> {
    buscaInterno(quandoSucesso = {it: List<Noticia>
        liveData.value = it
    })
        buscaNaApi(quandoSucesso = {it: List<Noticia>
        liveData.value = it
        }, quandoFalha = {})
        return liveData
}COPIAR CÓDIGO
Para verificar se tudo funciona da maneira esperada, vamos executar o aplicativo novamente. Desta vez, estaremos atendendo o Lifecycle aware, evitando ao máximo a questão de manter uma referência fixa, pois fazemos uma implementação via high order function, e correr risco de vazamento de memória.

Quando se faz a busca na API, ou quando o dado retorna do banco de dados interno, é realizada uma atualização, fator importante de ser considerado para um bom funcionamento. Outro ponto ao que precisamos nos atentar é que atualmente não fazemos a notificação quando há falha, pois veremos melhor sobre isto mais adiante.

Removeremos os Logs de buscaNoticias(), que não são mais necessários:

private fun buscaNoticias() {
    viewModel.buscaTodos().observe(owner: this, Observer {it: List<Noticia>!
        adapter.atualiza(it)
    })
    // códigos relacionados a quandoSucesso e quandoFalha, omitidos
}COPIAR CÓDIGO
Em NoticiaRepository.kt, retiraremos o delay, mas caso prefiram manter por motivos de testes, não tem problema. Teremos:

private fun buscaInterno(quandoSucesso: (List<Noticia>) -> Unit) {
    BaseAsyncTask(quandoExecuta = {
        dao.buscaTodos()
    }, quandoFinaliza = {it: List<Noticia>
        quandoSucesso(it)
    }).execute()
}COPIAR CÓDIGO
Executaremos a aplicação mais uma vez, rotacionaremos a tela mais de uma vez, e nada quebra. Lidamos com as preocupações iniciais em relação à implementação do primeiro Live Data, em seguida veremos as situações de falha.

@@04
Aplicando cache e migrando LiveData para o repositório

Faça com que o repositório devolva um LiveData ao solicitar a busca de notícias.
Nesta implementação, faça com que o ViewModel apenas devolva o LiveData do repositório removendo qualquer tipo de implementação que exija implementação de funções.

Também considere a implementação de cache para a busca de notícias, ou seja, o LiveData retornado deve manter o último valor atualizado para que o disponibilize de imediato ao rotacionar a tela.

Por fim, teste o App e veja se a busca funciona como antes. Se sim, confira se ao rotacionar a lista de notícias que foi carregada uma vez é apresentada imediatamente.

Nesta etapa, o teste também foi feito com o uso da Web Api, o único detalhe é que apenas a implementação de sucesso que foi feita... Não se preocupe, a seguir veremos como podemos lidar com as situações que apresentam uma resposta de falha.

Com esta implementação a lista de notícias, ao ser carregada pela primeira vez, deve ser apresentada imediatamente ao rotacionar a tela, e quando, finalizar a busca, deve ser atualizada.
Você pode conferir o código da atividade a partir deste commit.

Ao concluir o funcionamento do LiveData, foram removidos os comentários do código, como também, o delay. Fique à vontade para tomar a sua decisão entre manter ou remover esse trechos destinados a teste.

@@05
Utilizando o Resource com o LiveData

Vimos que a solução atual não é capaz de notificar nosso usuário quando há algum tipo de problema, isso porque utilizamos um Live Data que faz uma notificação apenas quando há alguma lista nova, ou atualização. Para modificarmos este comportamento, acessaremos a implementação mais interna, no caso, buscaTodos() do View Model, e do buscaTodos() do repositório.
Usamos o MutableLiveData como uma propriedade para mantermos a ideia de cache. Então, o valor da última atualização do Live Data é mantido, e mesmo quando há uma rotação de tela, não é feita uma nova busca. Até então, não temos nenhum problema e, além disto, as atualizações são realizadas apenas quando temos sucesso, seja na busca interna quanto na API.

Porém, deste modo, com a implementação da função quandoFalha, não temos nenhum tipo de ação. Para enviarmos a notificação, teríamos que fazer algum tipo de modificação do valor do Live Data, sendo que precisamos enviar a string que recebemos e representa o erro. Entretanto, o Live Data só recebe uma lista de notícias, e não temos a flexibilidade de enviar ambas.

Quando queremos enviar múltiplos valores dentro de um Live Data, será necessário criarmos uma classe que irá manter tanto o dado esperado, de sucesso, quanto o de erro. Este tipo de classe é muito conhecido como recurso, ou Resource na arquitetura Android. Vamos implementá-la dentro do próprio repositório, em "repository", já que ele irá lidar diretamente com ela.

Criaremos a nova classe Kotlin com "Alt + Insert", denominada "Resource" pelo padrão estabelecido na comunidade. Receberemos as duas possibilidades dentro do construtor, usando dado por ser algo genérico que poderá ser reutilizado. Para o erro, podemos deixar String para indicar que sempre teremos um erro, ou então acrescentamos ? para que o valor possa ser nulo, abordagem mais flexível.

Caso haja a possibilidade de não termos um determinado valor, faz sentido trabalharmos com valores que podem ser nulos, e isto vale inclusive para o nosso dado, pois ele pode ser nulo também, e o erro precisará ser notificado.

class Resource<T>(
    val dado: T?,
    val erro: String? = null
)COPIAR CÓDIGO
Feito isso, precisaremos modificar o repositório (NoticiaRepositorio.kt), em específico o nosso Live Data para que ele utilize o Resource em vez de uma lista de notícias. Isso porque estaremos atuando com as possibilidades que temos com base no sucesso e na falha, e já que precisamos devolver um recurso, no primeiro caso será devolvido um Resource com o dado esperado, no caso uma lista de notícias, e de erro nulo.

Por este motivo, faremos com que o erro tenha um valor default nulo, para que não tenhamos que enviá-lo. A recomendação para estas situações é manter um Named parameter para representar o dado. Caso prefiram, podem colocar nomes no lugar de it. Para o caso de erro, teremos noticiasEncontradas, e ajustaremos o valor criando um recurso.

Neste caso, enviaríamos um erro, porém, no lugar de dado = null indicando que o erro representa o it, uma vez que o Live Data sempre armazena o último valor, e supondo que enviamos um erro indicando que o erro é nulo, perderíamos a nossa lista. Portanto, esta abordagem não é recomendada.

Então, precisaremos sempre criar um recurso considerando que pegaremos o seu último valor, isso quando lidamos com um erro. Por mais que haja um erro, pode ser que ele exista por conta da busca na API, mas isto não impede que a lista seja carregada. Portanto, antes mesmo da atualização deste valor, pediremos o valor de noticiasEncontradas, já que se trata de uma propriedade.

Tendo o Resource atual, é necessário verificar se ele é um valor nulo, e a partir disto seremos capazes de tomar alguma ação. Se o recurso atual for diferente de nulo, queremos com que o recurso seja criado com base no valor existente, que seria a lista armazenada anteriormente.

Deste modo, sempre criaremos um novo recurso com base no preexistente, apenas atualizando no caso de haver um erro. E caso isso seja falso, poderemos implementar o recurso esperado (Resource(dado = null, erro = it)), ou seja, um nulo gerando um erro. Então, poderemos nomear para resourceCriado utilizando o "if (expression)".

private val noticiasEncontradas = MutableLiveData<Resource<List<Noticia>?>>()

fun buscaTodos() : LiveData<Resource<List<Noticia>?>> {
    buscaInterno(quandoSucesso = {it: List<Noticia>
        noticiasEncontradas.value = Resource(dado = it)
    })
    buscaNaApi(quandoSucesso = {it: List<Noticia>
        noticiasEncontradas.value = Resource(dado = it)
    }, quandoFalha = {it: String?
        val resourceAtual = noticiasEncontradas.value
        val resourceCriado: Resource<List<Noticia>?> =
            if(resourceAtual != null){
                Resource(dado = resourceAtual.dado, erro = it)
        } else {
            Resource(dado = null, erro = it)
        }
        noticiasEncontradas.value = resourceCriado
    })
    return noticiasEncontradas
}COPIAR CÓDIGO
Poderemos enviar resourceCriado para a obtenção das duas possibilidades que havíamos mencionado anteriormente, isto é, um recurso com um dado e um erro, e um recurso que não possui nenhum tipo de dado por não ter sido atualizado, e que estará atribuindo o erro. Para que não haja incompatibilidade por conta da opção de existir uma lista nula, o recurso com que trabalharemos terá que receber nulo em seu tipo interno, portanto acrescentaremos um ? em MutableLiveData.

E no retorno de NoticiaRepository.kt, em vez de um Live Data de lista de notícias, faz sentido que seja um Live Data de Resource com uma lista de notícias que poderá ser nula. Ou seja, em buscaTodos() teremos, assim, LiveData<Resource<List<Noticia>?>>. Este código está um pouco grande, e o refatoraremos adiante. Por enquanto, serve para entendermos o fluxo da flexibilização de possibilidades.

Agora que modificamos o repositório, precisaremos alterar o View Model, que também atua com o Live Data do repositório. O código ficará da seguinte forma:

fun buscaTodos() : LiveData<Resource<List<Noticia>?>> {
    return repository.buscaTodos()
}COPIAR CÓDIGO
Na Activity, precisaremos fazer a implementação, pegando o Resource e o dado, com o qual verificaremos se ele é nulo ou não. Caso não seja, deixaremos com o operador Safe, indicando que queremos atualizar a lista. E se for nulo, notificaremos o erro para o usuário.

private fun buscaNoticias() {
    viewModel.buscaTodos().observe(owner: this, Observer { resource ->
        resource.dado?.let { adapter.atualiza(it) }
        resource.erro?.let { it: String
            mostraErro(MENSAGEM_FALHA_CARREGAR_NOTICIAS)
        }
    })
// código omitido
}COPIAR CÓDIGO
Feita essa implementação, testaremos o aplicativo, e para recapitular, estaremos trabalhando com o resource, que por sua vez pegará o nosso dado e verificará se ele é nulo — caso não seja, será atualizado, se for, será exibida uma mensagem de erro. Assim como fizemos antes, deixaremos o Logcat aberto para acompanharmos possíveis erros na busca interna ou externa.

A lista é carregada sem nenhum problema, e para comprovarmos que tudo funciona da maneira esperada, testaremos a rotação da tela. Para simular uma situação de falha, ativaremos o modo avião no emulador, rotacionaremos a tela novamente, e teremos a mensagem de erro sendo exibida, justamente o que esperávamos.

Trata-se de um detalhe muito importante: se a lista já foi carregada uma vez, não podemos perdê-la, e é por isto que utilizamos o resource deste modo. É assim que trabalhamos com o Live Data para que tenhamos a flexibilidade de lidar tanto com a atualização de dado quando há sucesso como quando há erro.

@@06
Lidando com erros com o LiveData

Altere o código da busca de notícias para que o LiveData seja capaz de devolver a lista de notícias e um possível erro.
Para isso, considere a implementação da classe Resource que mantém o dado esperado e um erro.

Lembre-se de implementar esta solução como algo genérico que pode ser reutilizado para outros tipos.
Em seguida, modifique o LiveData para que tenha um Resource com base no tipo esperado do LiveData, então altere todo o código para que crie um Resource com base na situação que apresenta sucesso e a de falha.

Altere também as referência no ViewModel e Activity. Na Activity, em específico, identifique se o resource retorna um erro, e apresente o erro caso isso ocorra.

Ao implementar essa solução teste o App e veja se tudo funciona como antes, ou seja, a lista de notícias é carregada sem apresentar mensagem de falha.

Se sim, deixe o dispositivo em modo avião para perder a conexão com a Web API, rotacione o App ou faça com que a Activity entre no estado de resumo, e aguarde, veja se a mensagem de erro é apresentada.

O App deve funcionar como antes nos casos em que a comunicação é realizada com sucesso e, ao perder conexão com a Web Api, deve ser apresentada a mensagem de falha.
Você pode conferir o código da atividade a partir deste commit.

https://github.com/alura-cursos/android-tech-news/commit/1cb47ec8dfb97af8b3f15593783b76a0d8c9f73a

@@07
Refatorando o código

Feita a implementação do Live Data, que permite tanto a notificação quando há dado quanto quando há erro, faremos uma refatoração do nosso código, para deixarmos nossa solução cada vez mais simples, enquanto código em si, e também visando melhor legibilidade do mesmo. Começaremos pela Activity, em que há um comentário desnecessário que mostra como era feito anteriormente via high order function, que poderemos deletar, uma vez que já fizemos a conversão via Live Data.
// quandoSucesso = {
//    Log.i("teste", "atualizando noticias")
//    adapter.atualiza(it)
//}, quandoFalha = {
//    mostraErro(MENSAGEM_FALHA_CARREGAR_NOTICIAS)
//}COPIAR CÓDIGO
A seguir, otimizaremos os imports utilizando o atalho "Ctrl + Alt + O". E então acessaremos o nosso View Model, buscaTodos(), que também possui imports inutilizados, a serem removidos com o mesmo atalho. Em NoticiaRepository.kt, temos bastante código, e nosso objetivo é torná-lo simples o suficiente para que seja compreendido de modo ágil.

Sabemos que os trechos que remetem a situações que dão certo durante as buscas são exatamente iguais, portanto, poderemos extraí-los para uma variável a ser reutilizada. Sim, no Kotlin é possível extrair funções para variáveis. Ao fazermos isto, o Android Studio nos alerta de que poderemos fazer essa substituição em múltiplos locais, e daremos o nome de atualizaListaNoticias.

Em relação ao quandoFalha, fazemos a busca do resourceAtual, utilizando-o como base para criar um recurso. Verificamos se ele é diferente de nulo, e isso envolve bastante código. Toda a estrutura do if serve para criar um recurso correspondente à falha, sendo assim, extrairemos o trecho para a função criaResourceDeFalha.

Agora que a função indica claramente que retornamos um recurso de uma lista de notícias que pode ser vazia, não precisaremos enviar o tipo desejado diretamente. Então, poderemos trocar resourceCriado por resourceDeFalha.

private val noticiasEncontradas = MutableLiveData<Resource<List<Noticia>?>>()

fun buscaTodos() : LiveData<List<Noticia>> {
        val atualizaListaNoticias: (List<Noticia>) -> Unit = {
            noticiasEncontradas.value = Resource(dado = it)
    }
    buscaInterno(quandoSucesso = atualizaListaNoticias)
    buscaNaApi(quandoSucesso = atualizaListaNoticias,
        quandoFalha = {it: String?
        val resourceAtual = noticiasEncontradas.value
        val resourceDeFalha = criaResourceDeFalha(resourceAtual, it)
        noticiasEncontradas.value = resourceCriado
    })
    return noticiasEncontradas
}COPIAR CÓDIGO
criaResourceDeFalha() é uma função que recebe um recurso que pode ser vazio e ter um dado nulo, e também temos o it, que não significa muito para nós. Vamos nomeá-lo com erro.

private fun criaResourceDeFalha(
    resourceAtual: Resource<List<Noticia>?>?,
    erro: String?
): Resource<List<Noticia>?> {
    // código omitido
}COPIAR CÓDIGO
Da mesma forma, poderemos modificar a função que implementamos para que ela lide com erro, e não com o it:

buscaNaApi(quandoSucesso = atualizaListaNoticias,
    quandoFalha = { erro ->
    val resourceAtual = noticiasEncontradas.value
    val resourceDeFalha = criaResourceDeFalha(resourceAtual, erro)
    noticiasEncontradas.value = resourceCriado
})COPIAR CÓDIGO
Deixar o nome do parâmetro bem semântico é uma boa abordagem ao utilizarmos expressão lambda!
Seguindo com a implementação, temos um if (expression) dentro de uma função, criaResourceDeFalha(). Já vimos isso em outros cursos de Kotlin, e sabemos que isto pode ser convertido para o Early return que, em vez de retornar um if (expression), o faremos com o valor esperado:

private fun criaResourceDeFalha(
    resourceAtual: Resource<List<Noticia>?>?,
    erro: String?
): Resource<List<Noticia>?> {
    if (resourceAtual != null) {
        return Resource(dado = resourceAtual.dado, erro = erro)
    }
    return Resource(dado = null, erro = erro)
}COPIAR CÓDIGO
As vantagens de fazermos deste modo é que estaremos utilizando uma abordagem muito comum em várias linguagens de programação, o que consistirá em uma compreensão muito mais fácil para quem não estiver familiarizado com esta linguagem.

Continuando, estamos utilizando criaResourceDeFalha() dentro de NoticiaRepository.kt, sendo que esta parte de criação poderia ficar diretamente em nossa classe de recursos, bastando então fazer o import da chamada correspondente. Vamos fazê-lo, assim, será uma função estática que poderá ser reutilizada por ser pública.

class Resource<T>(
    val dado: T?,
    val erro: String? = null
)

fun criaResourceDeFalha(
    resourceAtual: Resource<List<Noticia>?>?,
    erro: String?
): Resource<List<Noticia>?> {
    if (resourceAtual != null) {
        return Resource(dado = resourceAtual.dado, erro = erro)
    }
    return Resource(dado = null, erro = erro)
}COPIAR CÓDIGO
E então teremos acesso a ela dentro do nosso repositório, basta realizarmos o import, o que é feito automaticamente. Na verdade, não é um import propriamente dito neste caso, por estarem no mesmo pacote. Com isso, tudo está funcionando de maneira esperada. Outro detalhe em relação ao Resource de falha é que ficamos muito atrelados a uma lista de notícias, sendo que quando queremos criar um recurso de falha, podemos trabalhar com um tipo genérico que poderá ser vazio, então, vamos fazê-lo.

Para que isto seja possível, a chamada da função terá que ser genérica também. Com isso, todas as referências com as quais estaremos trabalhando deverão ser genéricas por questões de compatibilidade do código independentemente do tipo enviado.

fun <T> criaResourceDeFalha(
    resourceAtual: Resource<List<T?>?,
    erro: String?
): Resource<T?> {
    if (resourceAtual != null) {
        return Resource(dado = resourceAtual.dado, erro = erro)
    }
    return Resource(dado = null, erro = erro)
}COPIAR CÓDIGO
Para tornar NoticiaRepository.kt mais seguro, faremos com que o Resource de falha não tenha qualquer valor — queremos que seja de uma lista de notícias, que poderá ser vazio.

buscaNaApi(quandoSucesso = atualizaListaNoticias,
    quandoFalha = { erro ->
    val resourceAtual = noticiasEncontradas.value
    val resourceDeFalha = criaResourceDeFalha<List<Noticia>?>(resourceAtual, erro)
    noticiasEncontradas.value = resourceCriado
})COPIAR CÓDIGO
Feita esta refatoração do nosso código, vamos testar nosso aplicativo e verificar se ele se comporta da maneira esperada. Testaremos se a lista de notícias é carregada, se isto ocorre mesmo quando há falha. O erro será enviado corretamente, mesmo se rotacionarmos a tela sem conexão de internet.

A mensagem de erro pode aparecer ou não, dependendo de quando a operação for finalizada, pois a última atualização é a que vale. Então, se houver o erro, logo em seguida a conexão for recuperada, e nesse meio tempo a Web API conseguir dar uma resposta, o recurso será enviado com sucesso, sem erros.

Ou seja, não temos como controlar muito estas condições em específico, mas é sempre bom saber que o último valor é enviado logo que fazemos a mudança de configuração. Precisamos nos atentar a estas questões quando trabalhamos com o Live Data. Adiante seguiremos com as implementações de outras features utilizando este modelo.

@@08
Aplicando refatoração no código

Refatore o código que foi ajustado nesta primeira adaptação do uso de ViewModel e LiveData. Nesta refatoração considere a remoção de comentários desnecessários, extrações de variáveis ou funções.
Por fim, teste o código e veja se todos os comportamentos funcionam como o esperado.

Todos os comportamentos devem funcionar como antes.
Você pode conferir o código da atividade a partir deste commit.

https://github.com/alura-cursos/android-tech-news/commit/1207ea8ea2ccdfa63871e919a51080c582b21893

@@09
Para saber mais - Evoluindo o Resource

Durante a aula criamos a classe resource para que fosse possível devolver um objeto via LiveData contendo o dado esperado e um possível erro.
A nossa abordagem inicial é bastante simples, ou seja, é muito comum o uso de variações mais completas que contenham, por exemplo, o estado do recurso.

Considerando um exemplo de código, poderíamos criar um recurso com a seguinte estrutura:

open class Resource<T>(
    val dado: T?,
    val erro: String? = null
)

class SucessoResource<T>(dado: T) : Resource<T>(dado)

class FalhaResource<T>(erro: String) : Resource<T>(dado = null, erro = erro)COPIAR CÓDIGO
Então, nas situações de sucesso criamos o SucessoResource e na falha o FalhaResource, então, na atualização do LiveData, somos capazes de identificar explicitamente as notificações de sucesso ou falha, usando o when expression:

viewModel.buscaTodos().observe(this, Observer { resource ->
    resource.dado?.let { adapter.atualiza(it) }
    when(resource) {
        is SucessoResource -> {
            //executa procedimento de sucesso
        }
        is FalhaResource -> {
            //executa procedimento de falha
            mostraErro(MENSAGEM_FALHA_CARREGAR_NOTICIAS)
        }
    }
})COPIAR CÓDIGO
É válido ressaltar que essa é uma possível versão para o Resource, ou seja, nada impede de estender mais e colocar mais status ou comportamentos esperados para essa abordagem.

@@10
O que aprendemos?

Nesta aula, aprendemos:
Implementar o LiveData integrado com o ViewModel;
Testar o comportamento do LiveData que respeita o Lifecycle-aware;
Utilizar estratégia de cache para disponibilizar os dados do LiveData imediatamente;
Verificar possíveis erros na resposta do LiveData;