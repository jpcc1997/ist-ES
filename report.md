<h1>Relatório de análise dos testes JMeter</h1>

<h3>Considerações gerais</h3>

- Os testes de 30 Writes e 100 Writes foram feitos com o objetivo final de ter 100 Adventures no estado Confirmed.
- Escolhemos analisar os resultados com base nos valores de latência e do throughput por serem os parâmetros mais representativos do impacto da politica otimista da FenixFramework nas chamadas aos serviços remotos.
- Não executámos os testes com 2000 utilizadores porque a carga na memória era demasiado elevada e os resultados que obtivémos com 100 utilizadores foram suficientes para a nossa análise.
- Considerámos que Sample Time = Latência + Processing Time
- Google Docs com todos os resultados da análise (gráficos e tabelas): [Link][excel_docs]

[excel_docs]: https://docs.google.com/spreadsheets/d/1Tj_yJG4dggk3l8PDhk1ShXtQ7RpXYbAV8tZyG9x9ZhA/edit?usp=sharing

<h2>100 Reads</h2>
<p>Este teste evidencia uma situação em que a politica otimista implementada pela FenixFramework produz maior aumento de desempenho, sem grandes consequências negativas ao nivel da latência.</p>
<h3>Sequência de operações deste teste:</h3>

1. Read Brokers
2. Read Adventures
3. Read Banks
4. Read Clients
5. Read Accounts
6. Read Hotels
7. Read Rooms
8. Read Providers
9. Read Activities
10. Read Offers

<h3>Throughput</h3>
<h4>Gráfico dos resultados</h4>
<img src="/images/100reads_throughput.png" height="350" width="650"/>

<h4>Conclusões sobre o throughput</h4>
<p>
Podemos concluir que aumentando o número de utilizadores conseguimos aumentar significativamente o desempenho (throughput) do sistema sem prejudicar de forma significativa a latência.
</p>

<h3>Latência</h3>
<h4>Conclusões sobre a latência</h4>
<p>
Observámos que para a maioria das operações os valores da latência não são muito influenciados pelo aumento de utilizadores, visto que não ocorrem conflictos nas operações de READ e não é necessário abortar/reiniciar transações.
</p>
<p>
Comparando o Sample Time com a Latency verificámos que estes tempos são muito semelhantes para cada operação nas mesmas condições de teste: isto indica-nos que o tempo de processamento das instruções é muito baixo (Sample Time = Latency + Processing Time), o que corresponde ao que seria de esperar numa operação de READ.
</p>

<h3>Erros</h3>
<p>Como neste teste só são executadas operações de READ, não existem conflitos nas transações de cada utilizador, logo, a taxa de erros para cada operação é de 0%.</p>


<h2>30 Writes</h2>
<h3>Sequência de operações deste teste:</h3>

1. Process Adventure
2. Read Adventures
3. Read Banks
4. Read Clients
5. Read Accounts
6. Process Adventure
7. Read Rooms
8. Read Activities
9. Read Offers
10. Process Adventure

<h3>Throughput</h3>
<h4>Gráfico dos resultados</h4>
<img src="/images/30writes_throughput.png" height="350" width="550"/>
<h4>Conclusões sobre o throughput</h4>
<p>
Observámos que o throughput diminui tendencialmente com a diminuição do número de utilizadores, visto que são executadas 70% de operações de Read e neste tipo de situações o aumento do número de utilizadores está associado a um aumento do throughput (ver conclusões do teste 100 Reads).
</p>
<p>
No entanto, a existência de operações Write provoca um aumento do throughput porque ocorrem menos conflitos e consequences restarts (ver conclusões do teste de 100 Writes).
</p>
<p>
Adicionalmente, verificámos também que com 10 e 4 utilizadores o throughput
atingiu os valores mais elevados, embora não tenha superado o desempenho com 100 utilizadores. Julgamos que isto se deva à distribuição do processamento pelos cores do processador, visto que utilizámos para os testes um computador com 4 cores.
</p>

<h3>Latência</h3>
<h4>Gráfico dos resultados</h4>
<img src="/images/30writes_latency.png" height="350" width="550"/>
<h4>Conclusões sobre a latência</h4>
<p>
De acordo com a política otimista da Fénix Framework, quando se faz uma escrita na base de dados esta é iniciada imediatamente e apenas no final é feito um teste de validação. Quando este teste falha, a operação de escrita é reiniciada. 
</p>
<p>
Consequentemente, com muitos utilizadores, e respetivas escritas concorrentes, é expectável que existam mais falhas nos testes de validação, o que obriga a vários recomeços de transações.
</p>
<p>
Isto traduz-se no aumento da latência quando aumenta o número de utilizadores como se pode observar facilmente no gráfico acima. Para piorar a situação, como são processadas operações de Read e Write concorrentes, ocorrem mais conflitos o que faz com que a latência seja ainda maior do que se as operações fossem só de Read ou de Write.
</p>

<h3>Erros</h3>
<p>
Em relação aos erros durante o teste, verifcamos que só foram encontrados erros com 100 utilizadores, o que se deve muito provavelmente às escritas concorrentes dos vários utilizadores que causam falhas nos testes de validação. Outro factor que poderá ter influenciado a taxa de erros é a população dos dados que foi feita para cada teste.
</p>

<h2>100 Writes</h2>
<h3>Sequência de operações deste teste:</h3>

1. Process Adventure
2. Process Adventure
3. Process Adventure
4. Process Adventure
5. Process Adventure

<h3>Throughput</h3>
<h4>Gráfico dos resultados</h4>
<img src="/images/100writes_throughput.png" height="350" width="550"/>

<h4>Conclusões sobre o throughput</h4>
<p>
Observámos que o throughput aumenta naturalmente com a 
diminuição do número de utilizadores, visto que se reduz o número de conflitos 
entre as operações de escrita concorrentes e consequentes restarts.
</p>

<p>
Adicionalmente, verificámos também que com 10 e 4 utilizadores o throughput
atingiu os valores mais elevados. Julgamos que isto se deva à distribuição do
processamento pelos cores do processador, visto que utilizámos para os testes um
computador com 4 cores.
</p>

<h3>Latência</h3>
<h4>Gráfico dos resultados</h4>
<img src="/images/100writes_latency.png" height="350" width="550"/>

<h4>Conclusões sobre a latência</h4>
<p>De acordo com a política otimista da Fénix Framework, quando se faz uma
escrita na base de dados esta é iniciada imediatamente e apenas no final é
feito um teste de validação. Quando este teste falha, a operação de escrita é
reiniciada.
</p>

<p>
Consequentemente, com muitos utilizadores, e respetivas escritas
concorrentes, é expectável que existam mais falhas nos testes de validação, o
que obriga a vários recomeços de transações. Isto traduz-se no aumento da
latência quando aumenta o número de utilizadores como se pode observar
facilmente no gráfico acima.
</p>

