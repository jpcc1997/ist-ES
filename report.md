<h1>Relatório de análise dos testes JMeter</h1>

<h3>Considerações gerais</h3>

- Os testes de 30 Writes e 100 Writes foram feitos com o objetivo final de ter 100 Adventures no estado Confirmed.
- Escolhemos analisar os resultados com base nos valores de latência e do throughput por serem os parâmetros mais representativos do impacto da politica otimista da FenixFramework nas chamadas aos serviços remotos.
- Não executámos os testes com 2000 utilizadores porque a carga na memória era demasiado elevada e os resultados que obtivémos com 100 utilizadores foram suficientes para a nossa análise.

<h2>100 Reads</h2>
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

<h4>Conclusões sobre o throughput</h4>


<h3>Latência</h3>

<h4>Conclusões sobre a latência</h4>



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

<h4>Conclusões sobre o throughput</h4>


<h3>Latência</h3>

<h4>Conclusões sobre a latência</h4>



<h2>100 Writes</h2>
<h3>Sequência de operações deste teste:</h3>

1. Process Adventure
2. Process Adventure
3. Process Adventure
4. Process Adventure
5. Process Adventure

<h3>Throughput</h3>
<h4>Gráfico dos resultados</h4>
<img src="/images/100writes_throughput.png" height="300" width="500"/>

<h4>Conclusões sobre o throughput</h4>


<h3>Latência</h3>
<h4>Gráfico dos resultados</h4>
<img src="/images/100writes_latency.png" height="300" width="500"/>

<h4>Conclusões sobre a latência</h4>


